package com.github.vskrahul.azure.graph;

import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.github.vskrahul.azure.graph.api.MailFolderApi;
import com.github.vskrahul.azure.graph.model.Folder;
import com.github.vskrahul.azure.ui.swing.LoginPage;
import com.microsoft.graph.models.MessageCollectionResponse;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

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

import static com.github.vskrahul.azure.graph.Creds.*;

@Slf4j
public class OnBehalfOfFlowAzureAuthentication {

    private static String ACCESS_TOKEN = "";

    public static void main(String[] args) throws Exception {
        startLocalServer(null);
        JFrame frame = new JFrame("Outlook Authentication");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
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
    public static void startLocalServer(LoginPage loginPage) throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/auth", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("code=")) {
                    String code = query.split("code=")[1].split("&")[0];
                    System.out.println("Authorization Code: " + code);
                    String middleTierAccessToken = middleTierAccessToken(code);
                    String downstreamAccessToken = downstreamAccessToken(middleTierAccessToken);
                    ACCESS_TOKEN = downstreamAccessToken;
                    //server.stop(1);
                    if (middleTierAccessToken != null) {
                        java.util.List<Folder> folders = MailFolderApi.mailFolders(downstreamAccessToken);
                        folders.forEach(v -> {
                            log.info("[name={}] [unread count={}] [total count={}]", v.getDisplayName(),
                                    v.getUnreadItemCount(), v.getTotalItemCount());
                        });
                        //TODO: Write your logic here to navigate to seconds page
                        // and render your List<Folder>
                        if(loginPage != null) {
                            loginPage.showMailFolders(folders);
                        }
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

    private static String middleTierAccessToken(String code) {
        String params = "grant_type=authorization_code" +
                "&client_id=" + CLIENT_ID +
                "&code=" + code +
                "&scope=api://" + CLIENT_ID + "/.default" +
                "&redirect_uri=http://localhost:8080/auth";

        return oauth2TokenCall(params);
    }

    private static String downstreamAccessToken(String accessToken) {
        String params = "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer" +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&assertion=" + accessToken +
                "&scope=https://graph.microsoft.com/.default" +
                "&requested_token_use=on_behalf_of";

        return oauth2TokenCall(params);
    }

    private static String oauth2TokenCall(String params) {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

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
            log.debug("AccessToken Body " + accessToken);
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This is working using HTTP endpoints
     */
    private static void fetchInboxMessageInfo() {
        try {
            URL url = new URL(GRAPH_API_URL + "/mailFolders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("User Profile: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void fetchByGraph() {
        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .clientId(CLIENT_ID).tenantId("common").clientSecret(CLIENT_SECRET)
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
