package com.cyara.tool.DTO;

import com.cyara.tool.Entity.TestCaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Report {
    public HashMap<String, List<TestCaseEntity>> folderData;
    public int success;
    public int failed;
    public int satisfactory;
    public int aborted;
    public int notRun;
}
