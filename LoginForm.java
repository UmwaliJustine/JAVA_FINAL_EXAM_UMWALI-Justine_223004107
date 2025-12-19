package com.form;

import javax.swing.*;
import com.util.DBConnection;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox robotCheck;
    private JButton loginButton, exitButton, registerButton;
    private Mainframe mainframe;

    public LoginForm(Mainframe mainframe) {
        this.mainframe = mainframe;
        initializeUI();
        setupEventListeners();
    }

    private void initializeUI() {
        setTitle("Sports Monitoring System - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 245, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 60, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));

        JLabel title = new JLabel("🏆 SPORTS MONITORING SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Role-Based Access System", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 220, 255));

        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));
        formPanel.setBackground(new Color(240, 245, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 35));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 35));
        formPanel.add(passwordField, gbc);

        // Robot Checkbox
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        robotCheck = new JCheckBox(" I'm not a robot 🤖");
        robotCheck.setFont(new Font("Arial", Font.PLAIN, 12));
        robotCheck.setBackground(new Color(240, 245, 250));
        robotCheck.setFocusPainted(false);
        formPanel.add(robotCheck, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        loginButton = createStyledButton("Login", new Color(40, 167, 69));
        registerButton = createStyledButton("Register", new Color(23, 162, 184));
        exitButton = createStyledButton("Exit", new Color(220, 53, 69));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set Enter key as default for login
        getRootPane().setDefaultButton(loginButton);
    }

    private void setupEventListeners() {
        loginButton.addActionListener(e -> loginUser());
        
        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "User registration is currently managed by administrators.\nPlease contact your system administrator for account creation.",
                "Registration Information",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Add key listener for Enter key on password field
        passwordField.addActionListener(e -> loginUser());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate inputs
        if (!validateInputs(username, password)) {
            return;
        }

        // Show loading state
        setLoadingState(true);

        // Perform login in background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String message;
            private int userId;
            private String role;
            private Integer profileId;

            @Override
            protected Boolean doInBackground() {
                try {
                    Connection con = DBConnection.getConnection();
                    if (con == null) {
                        message = "Database connection failed. Please check your connection.";
                        return false;
                    }

                    String query = "SELECT user_id, role FROM users WHERE username=? AND password=? AND status='Active'";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, username);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                        role = rs.getString("role");

                        // Get profile ID based on role
                        profileId = getProfileId(userId, role);
                        if (("coach".equals(role) || "player".equals(role)) && profileId == null) {
                            message = "No " + role + " profile linked to this account.";
                            return false;
                        }

                        return true;
                    } else {
                        message = "Invalid username or password, or account is inactive!";
                        return false;
                    }
                } catch (SQLException ex) {
                    message = "Database error: " + ex.getMessage();
                    return false;
                } catch (Exception ex) {
                    message = "System error: " + ex.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                setLoadingState(false);
                try {
                    boolean success = get();
                    if (success) {
                        // Successful login
                        String welcomeMessage = getRoleBasedWelcome(username, role);
                        JOptionPane.showMessageDialog(LoginForm.this, 
                            welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Login through mainframe
                        mainframe.loginUser(username, role, userId, profileId);
                    } else {
                        // Failed login
                        JOptionPane.showMessageDialog(LoginForm.this, 
                            message, "Login Failed", JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginForm.this, 
                        "Unexpected error during login: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }

        if (!robotCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Please verify that you're not a robot!", "Verification", JOptionPane.WARNING_MESSAGE);
            robotCheck.requestFocus();
            return false;
        }

        return true;
    }

    private Integer getProfileId(int userId, String role) throws Exception {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            String query = "";
            
            if ("coach".equals(role)) {
                query = "SELECT coach_id FROM coaches WHERE user_id=?";
            } else if ("player".equals(role)) {
                query = "SELECT player_id FROM players WHERE user_id=?";
            } else {
                return null; // Admin doesn't need profile ID
            }

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return null;

        } catch (SQLException ex) {
            return null;
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException ex) { }
            }
        }
    }

    private void setLoadingState(boolean loading) {
        loginButton.setEnabled(!loading);
        registerButton.setEnabled(!loading);
        exitButton.setEnabled(!loading);
        
        if (loading) {
            loginButton.setText("Logging in...");
            loginButton.setBackground(new Color(108, 117, 125));
        } else {
            loginButton.setText("Login");
            loginButton.setBackground(new Color(40, 167, 69));
        }
    }

    private String getRoleBasedWelcome(String username, String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return "Welcome " + username + "! 🎯\nYou have full system administrator access.";
            case "coach":
                return "Welcome Coach " + username + "! 📋\nAccess your team management tools.";
            case "player":
                return "Welcome " + username + "! ⚽\nView your performance and training data.";
            default:
                return "Welcome " + username + "!";
        }
    }

    // Method to clear form (useful for logout scenarios)
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        robotCheck.setSelected(false);
        usernameField.requestFocus();
    }

    // Test method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Mainframe mainframe = new Mainframe();
            LoginForm loginForm = new LoginForm(mainframe);
            loginForm.setVisible(true);
        });
    }
}