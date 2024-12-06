package com.cyara.tool.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @NotNull
    public String baseUrl;
    @NotNull
    public String apiToken;
    @NotNull
    public String folderName;
    @NotNull
    public int accountId;
}
