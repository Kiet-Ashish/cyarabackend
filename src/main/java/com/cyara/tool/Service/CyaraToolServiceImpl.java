package com.cyara.tool.Service;

import com.cyara.tool.DTO.Report;
import com.cyara.tool.DTO.TestResult;
import com.cyara.tool.DTO.User;
import com.cyara.tool.Entity.FolderDetails;
import com.cyara.tool.Entity.TestCaseEntity;
import com.cyara.tool.Exception.CustomException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CyaraToolServiceImpl implements CyaraToolService{

    private final RestTemplate restTemplate;

    @Autowired
    public CyaraToolServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    @Override
    public List<FolderDetails> findAllFolderList(User user) throws CustomException {
        // let first create url to find data...
        // need to remove last char if present '/'
        try{

            String str = user.getBaseUrl()+"/CyaraWebApi/v3.0/accounts/"+user.getAccountId()+"/directories/TestCase/folders";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", user.getApiToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            System.out.println(str);
            ParameterizedTypeReference<List<FolderDetails>> typeReference = new ParameterizedTypeReference<List<FolderDetails>>() {};
            ResponseEntity<List<FolderDetails>> response = restTemplate
                    .exchange(str, HttpMethod.GET, httpEntity, typeReference);

            // use RestTemplate here
            if(response.hasBody()){
                return Objects.requireNonNull(response.getBody());
            }
            throw  new CustomException("No result found please check your request", HttpStatus.NOT_FOUND);
        }catch (RuntimeException e){
            System.out.println(e);
            throw new CustomException(e.getMessage(), e.getCause(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public long findFolderId(List<FolderDetails> list, String folderName) {
        // need to find folderId from list....
        Optional<FolderDetails> result =
                list.stream().filter(a -> a.getName() != null)
                        .filter(a -> a.getName().equals(folderName))
                        .findFirst();
        return result
                .map(FolderDetails::getFolderId)
                .orElse(-1L);
    }

    @Override
    public TestResult findFolderTestResult(long folderId, User user) throws CustomException {
        String url = user.getBaseUrl() + "/CyaraWebApi/v3.0/accounts/"+user.getAccountId()+"/directories/TestCase/folders/"+folderId+"?pageSize=1000";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", user.getApiToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<TestResult> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, TestResult.class);
            if(!response.hasBody()){
                throw  new CustomException("No result found please check your request", HttpStatus.NOT_FOUND);
            }
            return response.getBody();
        }
        catch (RuntimeException e){
            throw new CustomException(e.getMessage(), e.getCause(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void removeExtraSlash(User user) {
        int n = user.getBaseUrl().length();
        if(user.getBaseUrl().charAt(n - 1) == '/'){
            user.setBaseUrl(user.getBaseUrl().substring(0, n - 1));
        }
    }

    @Override
    public Report createDetailReport(TestResult testResult) {
        HashMap<String, List<TestCaseEntity>> map = new HashMap<>();
        map.put("Success", new ArrayList<>());
        map.put("Failed", new ArrayList<>());
        map.put("Satisfactory", new ArrayList<>());
        map.put("Aborted", new ArrayList<>());
        map.put("Not Run", new ArrayList<>());

        List<TestCaseEntity> list = testResult.getResults();
        for(TestCaseEntity x : list){
            String result = x.getLastResult().get("result");
            if(result == null){
                map.get("Not Run").add(x);
            }else{
                map.get(result).add(x);
            }
        }
        // now all the map will be filled with appropriate result value
        Report report = Report
                .builder()
                .folderData(map)
                .aborted(map.get("Aborted").size())
                .failed(map.get("Failed").size())
                .success(map.get("Success").size())
                .notRun(map.get("Not Run").size())
                .satisfactory(map.get("Satisfactory").size())
                .build();
        return report;
    }

    @Override
    public byte[] createExcelReport(List<TestCaseEntity> list) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        Row headerRow = sheet.createRow(0);
        String[] header = new String[]{
                "TestCaseId",
                "Name",
                "Description",
                "FolderPath",
                "Direction",
                "Destination",
                "Result",
                "TestResultUrl",
                "TestCaseUrl"
        };
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
            // Apply style to header
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
        int rowIndex = 1;
        for(TestCaseEntity x : list){
            Row row = sheet.createRow(rowIndex++);
            for(int i = 0; i < header.length; i++){
                Cell cell = row.createCell(i);
                switch (i){
                    case 0 -> cell.setCellValue(x.getTestCaseId());
                    case 1 -> cell.setCellValue(x.getName());
                    case 2 -> cell.setCellValue(x.getDescription());
                    case 3 -> cell.setCellValue(x.getFolderPath());
                    case 4 -> cell.setCellValue(x.getDirection());
                    case 5 -> cell.setCellValue(x.getDestination());
                    case 6 -> cell.setCellValue(x.getLastResult().get("result"));
                    case 7 -> cell.setCellValue(x.getLastResult().get("url"));
                    case 8 -> cell.setCellValue(x.getUrl());
                }
            }
        }
        // Autosize columns for better visibility
        for (int i = 0; i < header.length; i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        return byteArrayOutputStream.toByteArray();
    }
}
