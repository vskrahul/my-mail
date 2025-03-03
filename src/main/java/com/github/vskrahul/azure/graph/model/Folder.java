package com.github.vskrahul.azure.graph.model;

import lombok.Data;

@Data
public class Folder {
    private String parentFolderId;
    private String id;
    private String displayName;
    private Integer childFolderCount;
    private Integer unreadItemCount;
    private Integer totalItemCount;
    private Integer sizeInBytes;
    private boolean isHidden;
}
