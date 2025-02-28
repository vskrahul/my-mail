package com.github.vskrahul.azure.graph;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class GetAuthCodeOnly {
    private static final String CLIENT_ID = Creds.CLIENT_ID;
    private static final String REDIRECT_URI = "http://localhost:8080/auth";
    private static final String AUTH_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize" +
            "?client_id=" + CLIENT_ID +
            "&response_type=code" +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_mode=query" +
            "&scope=https://graph.microsoft.com/.default" +
            "&state=12345";

    public static void main(String[] args) throws Exception {
        startLocalServer();
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
    private static void startLocalServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/auth", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.contains("code=")) {
                    String code = query.split("code=")[1].split("&")[0];
                    System.out.println("Authorization Code: " + code);
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
}
