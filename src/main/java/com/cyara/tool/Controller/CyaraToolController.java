package com.cyara.tool.Controller;


import com.cyara.tool.DTO.Report;
import com.cyara.tool.DTO.TestResult;
import com.cyara.tool.DTO.User;
import com.cyara.tool.Entity.FolderDetails;
import com.cyara.tool.Exception.CustomException;
import com.cyara.tool.Service.CyaraToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cyara")
public class CyaraToolController {

    @Autowired
    public CyaraToolService cyaraToolService;

    @PostMapping("/folder/result")
    public ResponseEntity<Report> findFolderReport(@RequestBody User user) throws CustomException {
        // Need to do things steps by steps

        // firstly need to remove / if present more..
        cyaraToolService.removeExtraSlash(user);
        // find all folders present in cyara
        List<FolderDetails> folders = cyaraToolService.findAllFolderList(user);
        // find folder which we are searching for...
        long folderId = cyaraToolService.findFolderId(folders, user.getFolderName());
        if(folderId == -1L){
            throw new CustomException("No folder found. please check it again", HttpStatus.NOT_FOUND);
        }
        // find all content which is present in folder
        TestResult testResult = cyaraToolService.findFolderTestResult(folderId, user);
        // find all success/failed/satisfactory/not run result
        Report folderResult = cyaraToolService.createDetailReport(testResult);

        return new ResponseEntity<>(folderResult, HttpStatus.OK);
    }

    @PostMapping("/generate-report")
    public ResponseEntity<byte[]> generateExcelReport(@RequestBody User user) throws IOException, CustomException {
        cyaraToolService.removeExtraSlash(user);
        // find all folders present in cyara
        List<FolderDetails> folders = cyaraToolService.findAllFolderList(user);
        // find folder which we are searching for...
        long folderId = cyaraToolService.findFolderId(folders, user.getFolderName());
        if(folderId == -1L){
            throw new CustomException("No folder found. please check it again", HttpStatus.NOT_FOUND);
        }
        // find all content which is present in folder
        TestResult testResult = cyaraToolService.findFolderTestResult(folderId, user);

        byte[] excelData = cyaraToolService.createExcelReport(testResult.getResults());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xlsx");

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}
