package com.github.vskrahul.azure.graph.model;

import lombok.Data;

import java.util.List;

@Data
public class MailFolders {
    private List<Folder> value;
}
