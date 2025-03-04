package com.github.vskrahul.azure.graph.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MailFolders {
    private List<Folder> value;
}
