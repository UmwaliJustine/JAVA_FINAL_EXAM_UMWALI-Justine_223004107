package com.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.util.DBConnection;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminDashboard extends JPanel {
    private JTabbedPane tabbedPane;
    private JTable usersTable, coachesTable, playersTable, teamsTable;
    private DefaultTableModel usersModel, coachesModel, playersModel, teamsModel;

    public AdminDashboard() {
        setLayout(new BorderLayout());
        initializeComponents();
        try {
            loadAllData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Users Tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersModel = new DefaultTableModel(new String[]{"User ID", "Username", "Role", "Email", "Status", "Created At"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTable = new JTable(usersModel);
        JScrollPane usersScroll = new JScrollPane(usersTable);
        
        JPanel usersButtonPanel = new JPanel();
        JButton refreshUsersBtn = new JButton("Refresh");
        JButton addUserBtn = new JButton("Add User");
        JButton toggleUserStatusBtn = new JButton("Toggle Status");
        
        usersButtonPanel.add(refreshUsersBtn);
        usersButtonPanel.add(addUserBtn);
        usersButtonPanel.add(toggleUserStatusBtn);
        
        usersPanel.add(usersScroll, BorderLayout.CENTER);
        usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);

        // Coaches Tab
        JPanel coachesPanel = new JPanel(new BorderLayout());
        coachesModel = new DefaultTableModel(new String[]{"Coach ID", "User ID", "Full Name", "Specialization", "Experience", "Email", "Phone"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coachesTable = new JTable(coachesModel);
        JScrollPane coachesScroll = new JScrollPane(coachesTable);
        coachesPanel.add(coachesScroll, BorderLayout.CENTER);

        // Players Tab
        JPanel playersPanel = new JPanel(new BorderLayout());
        playersModel = new DefaultTableModel(new String[]{"Player ID", "User ID", "Full Name", "Team", "Age", "Gender", "Position", "Join Date"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playersTable = new JTable(playersModel);
        JScrollPane playersScroll = new JScrollPane(playersTable);
        playersPanel.add(playersScroll, BorderLayout.CENTER);

        // Teams Tab
        JPanel teamsPanel = new JPanel(new BorderLayout());
        teamsModel = new DefaultTableModel(new String[]{"Team ID", "Team Name", "Category", "Coach", "Created At"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamsTable = new JTable(teamsModel);
        JScrollPane teamsScroll = new JScrollPane(teamsTable);
        teamsPanel.add(teamsScroll, BorderLayout.CENTER);

        // Add tabs
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Coaches", coachesPanel);
        tabbedPane.addTab("Players", playersPanel);
        tabbedPane.addTab("Teams", teamsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Button actions
        refreshUsersBtn.addActionListener(e -> {
            try {
                loadUsers();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Error: " + e1.getMessage());
            }
        });
        
        addUserBtn.addActionListener(e -> {
            try {
                showAddUserDialog();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Error: " + e1.getMessage());
            }
        });
        
        toggleUserStatusBtn.addActionListener(e -> {
            try {
                toggleUserStatus();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Error: " + e1.getMessage());
            }
        });
    }

    private void loadAllData() throws Exception {
        loadUsers();
        loadCoaches();
        loadPlayers();
        loadTeams();
    }

    private void loadUsers() throws Exception {
        usersModel.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY user_id")) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("role"));
                row.add(rs.getString("email"));
                row.add(rs.getString("status"));
                row.add(rs.getTimestamp("created_at"));
                usersModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading users: " + e.getMessage());
        }
    }

    private void loadCoaches() throws Exception {
        coachesModel.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM coaches ORDER BY coach_id")) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("coach_id"));
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("specialization"));
                row.add(rs.getInt("experience_years"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone_number"));
                coachesModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading coaches: " + e.getMessage());
        }
    }

    private void loadPlayers() throws Exception {
        playersModel.setRowCount(0);
        String query = "SELECT p.player_id, p.user_id, p.full_name, t.team_name, p.age, p.gender, p.position, p.join_date " +
                      "FROM players p LEFT JOIN teams t ON p.team_id = t.team_id ORDER BY p.player_id";
        
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("player_id"));
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("team_name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("position"));
                row.add(rs.getDate("join_date"));
                playersModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading players: " + e.getMessage());
        }
    }

    private void loadTeams() throws Exception {
        teamsModel.setRowCount(0);
        String query = "SELECT t.team_id, t.team_name, t.category, c.full_name as coach_name, t.created_at " +
                      "FROM teams t LEFT JOIN coaches c ON t.coach_id = c.coach_id ORDER BY t.team_id";
        
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("team_id"));
                row.add(rs.getString("team_name"));
                row.add(rs.getString("category"));
                row.add(rs.getString("coach_name"));
                row.add(rs.getTimestamp("created_at"));
                teamsModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading teams: " + e.getMessage());
        }
    }

    private void showAddUserDialog() throws Exception {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"admin", "coach", "player"});
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New User", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            addUser(usernameField.getText(), new String(passwordField.getPassword()), 
                   (String) roleCombo.getSelectedItem(), emailField.getText());
        }
    }

    private void addUser(String username, String password, String role, String email) throws Exception {
        String query = "INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            pst.setString(4, email);
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "User added successfully!");
            loadUsers();
        } catch (SQLException e) {
            throw new Exception("Error adding user: " + e.getMessage());
        }
    }

    private void toggleUserStatus() throws Exception {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first!");
            return;
        }

        int userId = (int) usersModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) usersModel.getValueAt(selectedRow, 4);
        String newStatus = "Active".equals(currentStatus) ? "Inactive" : "Active";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("UPDATE users SET status = ? WHERE user_id = ?")) {
            
            pst.setString(1, newStatus);
            pst.setInt(2, userId);
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "User status updated!");
            loadUsers();
        } catch (SQLException e) {
            throw new Exception("Error updating user status: " + e.getMessage());
        }
    }
}