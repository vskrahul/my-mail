package com.github.vskrahul.azure.ui.swing;

import com.github.vskrahul.azure.graph.model.Folder;

import javax.swing.*;
import java.awt.*;

import java.net.URI;
import java.util.List;

import static com.github.vskrahul.azure.graph.Creds.AUTH_URL;

public class LoginPage {
    private JFrame frame;

    public LoginPage() {
        frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel instructionLabel = new JLabel("Please enter mail (Office 365 or Hotmail)");
        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField(20);
        JButton signInButton = new JButton("Sign In");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(instructionLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(signInButton, gbc);

        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // On "Sign In" click → Open Second Page (TablePage)
        signInButton.addActionListener(e -> {
            //open the browser for authentication
            try {
                Desktop.getDesktop().browse(new URI(AUTH_URL));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void showMailFolders(List<Folder> folders) {
        frame.dispose();
        new TablePage(folders);
    }
}

