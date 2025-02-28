package org.example.test;

import com.azure.identity.InteractiveBrowserCredential;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.microsoft.graph.models.MessageCollectionResponse;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;

public class InteractiveBrowserCredentialBuilderTest {
    private static final String CLIENT_ID = Creds.CLIENT_ID;
    private static final String CLIENT_SECRET = Creds.CLIENT_SECRET;
    private static final String TENANT_ID = Creds.TENANT_ID;
    private static final String REDIRECT_URI = "http://localhost:8080/auth";
    private static final String TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
    private static final String GRAPH_API_URL = "https://graph.microsoft.com/v1.0/me";
    private static final String AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize" +
            "?client_id=" + CLIENT_ID +
            "&response_type=code" +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_mode=query" +
            "&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default" +
            "&state=12345";

    private static String ACCESS_TOKEN = "";
    final static String[] scopes = new String[] {"User.Read"};

    public static void main(String[] args) throws Exception {
        InteractiveBrowserCredential credential = new InteractiveBrowserCredentialBuilder()
                .clientId(CLIENT_ID)
                .tenantId(TENANT_ID)
                .redirectUrl(REDIRECT_URI)
                .build();

        if (null == credential) {
            throw new Exception("Unexpected error");
        }

        GraphServiceClient graphServiceClient = new GraphServiceClient(credential);
        MessageCollectionResponse result = graphServiceClient.me().messages().get(requestConfiguration -> {
            requestConfiguration.queryParameters.select = new String []{"sender", "subject"};
        });

        result.getValue()
                .stream()
                .forEach(m -> {
                    System.out.println(m.getSubject());
                });
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Authenticate with Outlook:");
        userLabel.setBounds(10, 20, 250, 25);
        panel.add(userLabel);

        JButton loginButton = new JButton("Login with Outlook");
        loginButton.setBounds(100, 80, 200, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(AUTH_URL));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    static HttpServer server = null;
    private static void startLocalServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/auth", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("code=")) {
                    String code = query.split("code=")[1].split("&")[0];
                    System.out.println("Authorization Code: " + code);
                    String accessToken = exchangeAuthorizationCodeForToken(code);
                    server.stop(1);
                    if (accessToken != null) {
                        //fetchUserProfile(accessToken);
                        fetchByGraph();
                    }
                }
                String response = "Authentication successful. You can close this window.";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.setExecutor(null);
        server.start();
        System.out.println("Local server started on http://localhost:8080");
    }

    private static String exchangeAuthorizationCodeForToken(String code) {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String params = "client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET +
                    "&code=" + code +
                    "&scope=https://graph.microsoft.com/.default" +
                    //"&grant_type=authorization_code";
                    "&grant_type=client_credentials";

            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String accessToken = response.toString().split("\"access_token\":\"")[1].split("\"")[0];
            System.out.println("AccessToken Body " + accessToken);
            ACCESS_TOKEN = accessToken;
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void fetchUserProfile(String accessToken) {
        try {
            URL url = new URL(GRAPH_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("User Profile: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void fetchByGraph() {

//        GraphServiceClient graphServiceClient = GraphServiceClient
//                .builder()
//                .authenticationProvider(request -> {
//                    CompletableFuture<String> completableFuture = new CompletableFuture<>();
//                    completableFuture.complete(ACCESS_TOKEN);
//                    return completableFuture;
//                })
//                .buildClient();
//        graphServiceClient.me()
//                .messages()
//                .getOptions()
//
        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .clientId(CLIENT_ID).tenantId(null).clientSecret(CLIENT_SECRET)
                .userAssertion(ACCESS_TOKEN).build();

        final GraphServiceClient graphClient = new GraphServiceClient(credential, new String[] {"https://graph.microsoft.com/.default"});

        MessageCollectionResponse result = graphClient.me().messages().get(requestConfiguration -> {
            requestConfiguration.queryParameters.select = new String []{"sender", "subject"};
        });

        result.getValue()
                .stream()
                .forEach(m -> {
                    System.out.println(m.getSubject());
                });

    }
}
