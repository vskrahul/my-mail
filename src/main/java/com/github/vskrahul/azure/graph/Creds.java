package com.github.vskrahul.azure.graph;

public class Creds {
    public static final String CLIENT_ID = "";
    public static final String CLIENT_SECRET = "";
    public static final String TENANT_ID = "";

    public static final String REDIRECT_URI = "http://localhost:8080/auth";
    public static final String TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
    public static final String GRAPH_API_URL = "https://graph.microsoft.com/v1.0/me";
    public static final String AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize" +
            "?client_id=" + CLIENT_ID +
            "&response_type=code" +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_mode=query" +
            "&scope=https://graph.microsoft.com/.default" +
            "&state=12345";
}
