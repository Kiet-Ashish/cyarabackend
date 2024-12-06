package com.cyara.tool.DTO;

import com.cyara.tool.Entity.TestCaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestResult {
    public List<TestCaseEntity> results;
    public int totalResults;
}
