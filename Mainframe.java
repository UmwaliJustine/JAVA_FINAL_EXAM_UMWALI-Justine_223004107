package com.form;

import com.panel.AdminDashboard;
import com.panel.CoachDashboard;
import com.panel.PlayerDashboard;
import javax.swing.*;
import java.awt.*;

public class Mainframe {
    private static Mainframe instance;
    private JFrame currentFrame;
    private String currentUser;
    private String currentRole;
    private Integer currentUserId;
    private Integer currentProfileId;

    Mainframe() {
        showLoginForm();
    }

    public static Mainframe getInstance() {
        if (instance == null) {
            instance = new Mainframe();
        }
        return instance;
    }

    public void showLoginForm() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = new LoginForm(this);
        currentFrame.setVisible(true);
        clearSession();
    }

    public void loginUser(String username, String role, int userId, Integer profileId) throws Exception {
        this.currentUser = username;
        this.currentRole = role;
        this.currentUserId = userId;
        this.currentProfileId = profileId;
        
        redirectToDashboard();
    }

    private void redirectToDashboard() throws Exception {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        switch (currentRole.toLowerCase()) {
            case "admin":
                currentFrame = createDashboardFrame("Admin Dashboard", new AdminDashboard(), new Color(30, 60, 120));
                break;
            case "coach":
                currentFrame = createDashboardFrame("Coach Dashboard", new CoachDashboard(currentProfileId), new Color(40, 100, 140));
                break;
            case "player":
                currentFrame = createDashboardFrame("Player Dashboard", new PlayerDashboard(currentProfileId), new Color(50, 120, 80));
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role: " + currentRole);
                showLoginForm();
                return;
        }
        
        currentFrame.setVisible(true);
    }

    private JFrame createDashboardFrame(String title, JPanel dashboardPanel, Color headerColor) {
        JFrame frame = new JFrame("Sports Monitoring System - " + title);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Create header with user info and logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel(title.toUpperCase(), JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(headerColor);
        
        String userInfoText;
        if ("admin".equals(currentRole)) {
            userInfoText = "Welcome, " + currentUser + " (" + currentRole + ")";
        } else {
            userInfoText = "Welcome, " + currentUser + " (" + currentRole + " ID: " + currentProfileId + ")";
        }
        
        JLabel userInfo = new JLabel(userInfoText);
        userInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfo.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userInfo);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(dashboardPanel, BorderLayout.CENTER);

        return frame;
    }

    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            currentFrame,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            clearSession();
            showLoginForm();
        }
    }

    private void clearSession() {
        currentUser = null;
        currentRole = null;
        currentUserId = null;
        currentProfileId = null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public Integer getCurrentProfileId() {
        return currentProfileId;
    }

    
}