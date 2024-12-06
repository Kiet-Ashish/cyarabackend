package com.cyara.tool.Service;

import com.cyara.tool.DTO.Report;
import com.cyara.tool.DTO.TestResult;
import com.cyara.tool.DTO.User;
import com.cyara.tool.Entity.FolderDetails;
import com.cyara.tool.Entity.TestCaseEntity;
import com.cyara.tool.Exception.CustomException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface CyaraToolService {
    public List<FolderDetails> findAllFolderList(User user) throws CustomException;
    public long findFolderId(List<FolderDetails> list, String folderName);
    public TestResult findFolderTestResult(long folderId, User user) throws CustomException;
    public void removeExtraSlash(User user);
    public Report createDetailReport(TestResult testResult);
    public byte[] createExcelReport(List<TestCaseEntity> list) throws IOException;
}
