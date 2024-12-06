package com.cyara.tool.Service;

import com.cyara.tool.DTO.TestResult;
import com.cyara.tool.DTO.User;
import com.cyara.tool.Entity.FolderDetails;
import com.cyara.tool.Exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CyaraToolServiceImplTest {

    @Autowired
    public CyaraToolServiceImpl cyaraToolService;


    @Test
    public void findAllFolderListTest(){
        User user = User
                .builder()
                .accountId(404)
                .baseUrl("https://cat.cyara.com")
                .apiToken("ApiKey 2bcf96609aad46e289be4189729da526:dHeqzrjKOWNFf1dEwkyM7zSkYzgBKxbtF3jQ2E82ySI=")
                .folderName("Aafreen")
                .build();
        try{
            List<FolderDetails> list = cyaraToolService.findAllFolderList(user);
            System.out.println(list.size());
            Assertions.assertNotNull(list);
        }
        catch (CustomException exception){
            System.out.println(exception.getMessage());
            System.out.println(exception);
            Assertions.fail();
        }
    }

    @Test
    public void findFolderIdTest(){
        User user = User
                .builder()
                .accountId(404)
                .baseUrl("https://cat.cyara.com")
                .apiToken("ApiKey 2bcf96609aad46e289be4189729da526:dHeqzrjKOWNFf1dEwkyM7zSkYzgBKxbtF3jQ2E82ySI=")
                .folderName("Aafreen")
                .build();
        try{
            List<FolderDetails> list = cyaraToolService.findAllFolderList(user);
            System.out.println(list.size());
            Assertions.assertEquals(99901, cyaraToolService.findFolderId(list, "Karan (training)"));
        }
        catch (CustomException exception){
            System.out.println(exception.getMessage());
            System.out.println(exception);
            Assertions.fail();
        }
    }

    @Test
    public void findFolderTestResultTest(){
        User user = User
                .builder()
                .accountId(404)
                .baseUrl("https://cat.cyara.com")
                .apiToken("ApiKey 2bcf96609aad46e289be4189729da526:dHeqzrjKOWNFf1dEwkyM7zSkYzgBKxbtF3jQ2E82ySI=")
                .folderName("Aafreen")
                .build();
        try{
            TestResult result = cyaraToolService.findFolderTestResult(116732, user);
            System.out.println(result.getResults().size());
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getResults());
            Assertions.assertNotEquals(0, result.getResults().size());
        }catch (CustomException exception){
            System.out.println(exception.getMessage());
            System.out.println(exception);
            Assertions.fail();
        }
    }
}
