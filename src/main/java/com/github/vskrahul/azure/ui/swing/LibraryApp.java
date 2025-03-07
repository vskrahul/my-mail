package com.github.vskrahul.azure.ui.swing;

import com.github.vskrahul.azure.graph.OnBehalfOfFlowAzureAuthentication;

public class LibraryApp {
    public static void main(String[] args) throws Exception {
        LoginPage loginPage = new LoginPage();
        OnBehalfOfFlowAzureAuthentication.startLocalServer(loginPage);
        //SwingUtilities.invokeLater(LoginPage::new);
    }
}
