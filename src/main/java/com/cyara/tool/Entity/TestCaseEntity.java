package com.cyara.tool.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseEntity {
    public long testCaseId;
    public String name;
    public String description;
    public String folderPath;
    public String direction;
    public String destination;
    HashMap<String, String> lastResult;
    public String url;
}
