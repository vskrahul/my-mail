package com.github.vskrahul.azure.ui.swing;

import com.github.vskrahul.azure.graph.model.Folder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TablePage {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Folder> folderData; // Dynamic ArrayList

    public TablePage(List<Folder> folders) {
        frame = new JFrame("Folder Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        // Table Column Names
        String[] columnNames = {"Table Of Folder", "Root Folder Name", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Load Sample Data (This can be replaced with real data)
        folderData = folders;

        updateTable(); // Load data into table

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 🔹 Buttons
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // 🔹 Button Actions
        backButton.addActionListener(e -> {
            frame.dispose(); // Close Table Page
            new LoginPage(); // Go Back to Login
        });

        nextButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Next Page Clicked!"));

        // 🔹 Simulating Dynamic Data Update (This can be replaced with live data updates)
        //Timer timer = new Timer(5000, e -> addRandomFolder());
        //timer.start(); // Every 5 seconds, new data will be added
    }

    // 🔹 Function to Update Table with Dynamic Data
    private void updateTable() {
        tableModel.setRowCount(0); // Clear existing table
        int idx = 0;
        for (Folder folder : folderData) {
            idx++;
            String[] row = new String[]{
                    idx + "", folder.getDisplayName(), folder.getTotalItemCount() + ""
            };
            tableModel.addRow(row);
        }
    }

    // 🔹 Simulate Adding New Folder Dynamically
    private void addRandomFolder() {
        //int newId = folderData.size() + 1;
        //folderData.add(new String[]{String.valueOf(newId), "Folder " + newId, String.valueOf((int) (Math.random() * 100))});
        //updateTable();
    }
}
