package com.github.vskrahul.azure.graph.model;

import lombok.Data;

@Data
public class MyProfile {
    private String id;
    private String userPrincipalName;
    private String displayName;
    private String surname;
    private String givenName;
    private String preferredLanguage;
    private String mail;
    private String mobilePhone;
    private String jobTitle;
    private String officeLocation;
    private String[] businessPhones;
}
