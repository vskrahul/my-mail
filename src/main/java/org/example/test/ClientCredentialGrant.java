package org.example.test;

import com.microsoft.aad.msal4j.*;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

public class ClientCredentialGrant {
    // It is important to reuse this object, as it will cache tokens.
    private static IConfidentialClientApplication app;

    public static void main(String args[]) throws Exception{

        String authority = "https://login.microsoftonline.com/common/";
        String clientId = Creds.CLIENT_ID;
        String secret = Creds.CLIENT_SECRET;
        String scope = "https://graph.microsoft.com/.default";

        try {

            // Ensure the app object is not re-created on each request, as it holds a token cache
            // If you are getting tokens for many tenants (millions), see the msal-client-credential-secret-high-availability sample
            // which shows how to use an in-memory token cache with eviction based on a size limit
            GetOrCreateApp(clientId, secret, authority);

            ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                            Collections.singleton(scope))
                    .build();

            // The first time this is called, the app will make an HTTP request to the token issuer, so this is slow. Latency can be >1s
            IAuthenticationResult result = app.acquireToken(clientCredentialParam).get();

            // On subsequent calls, the app will return a token from its cache. It is important to reuse the app object

            // Now that we have a Bearer token, call the protected API
            String usersListFromGraph = getUsersListFromGraph(result.accessToken());

            System.out.println("Users in the Tenant = " + usersListFromGraph);
            System.out.println("Press any key to exit ...");
            System.in.read();

        } catch(Exception ex){
            System.out.println("Oops! We have an exception of type - " + ex.getClass());
            System.out.println("Exception message - " + ex.getMessage());
            throw ex;
        }
    }

    private static void GetOrCreateApp(String clientId, String secret, String authority) throws MalformedURLException {

        if (app == null) {
            app = ConfidentialClientApplication.builder(
                            clientId,
                            ClientCredentialFactory.createFromSecret(secret))
                    .authority(authority)
                    .build();
        }
    }

    private static String getUsersListFromGraph(String accessToken) throws IOException {
        System.out.println("start: \n"+accessToken+ "\n");
        URL url = new URL("https://graph.microsoft.com/v1.0/users");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept","application/json");

        int httpResponseCode = conn.getResponseCode();
        if(httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))){

                String inputLine;
                response = new StringBuilder();
                while (( inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }
    }

    /**
     * Helper function unique to this sample setting. In a real application these wouldn't be so hardcoded, for example
     * different users may need different authority endpoints or scopes
     */
    private static void setUpSampleData() throws IOException {

    }
}
