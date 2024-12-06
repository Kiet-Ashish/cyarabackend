package com.cyara.tool.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FolderDetails {
    public String type;
    public long folderId;
    public long parentFolderId;
    public String name;
}
