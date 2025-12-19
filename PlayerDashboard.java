package com.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.util.DBConnection;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class PlayerDashboard extends JPanel {
    private int playerId;
    private JTabbedPane tabbedPane;

    public PlayerDashboard(int playerId) throws Exception {
        this.playerId = playerId;
        setLayout(new BorderLayout());
        initializeComponents();
        try {
            loadPlayerData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void initializeComponents() throws Exception {
        tabbedPane = new JTabbedPane();

        // Profile Tab
        JPanel profilePanel = createProfilePanel();
        
        // Performance Tab
        JPanel performancePanel = createPerformancePanel();
        
        // Attendance Tab
        JPanel attendancePanel = createAttendancePanel();
        
        // Injuries Tab
        JPanel injuriesPanel = createInjuriesPanel();

        tabbedPane.addTab("My Profile", profilePanel);
        tabbedPane.addTab("My Performance", performancePanel);
        tabbedPane.addTab("My Attendance", attendancePanel);
        tabbedPane.addTab("Injury Status", injuriesPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() throws Exception {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        String query = "SELECT p.full_name, p.age, p.gender, p.position, p.height_cm, p.weight_kg, " +
                      "p.join_date, t.team_name, c.full_name as coach_name " +
                      "FROM players p " +
                      "LEFT JOIN teams t ON p.team_id = t.team_id " +
                      "LEFT JOIN coaches c ON t.coach_id = c.coach_id " +
                      "WHERE p.player_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, playerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                int row = 0;
                addProfileField(panel, gbc, "Full Name:", rs.getString("full_name"), row++);
                addProfileField(panel, gbc, "Age:", rs.getInt("age") + " years", row++);
                addProfileField(panel, gbc, "Gender:", rs.getString("gender"), row++);
                addProfileField(panel, gbc, "Position:", rs.getString("position"), row++);
                addProfileField(panel, gbc, "Height:", rs.getFloat("height_cm") + " cm", row++);
                addProfileField(panel, gbc, "Weight:", rs.getFloat("weight_kg") + " kg", row++);
                addProfileField(panel, gbc, "Team:", rs.getString("team_name"), row++);
                addProfileField(panel, gbc, "Coach:", rs.getString("coach_name"), row++);
                addProfileField(panel, gbc, "Join Date:", rs.getDate("join_date").toString(), row++);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage());
        }

        return panel;
    }

    private void addProfileField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label, JLabel.RIGHT), gbc);
        
        gbc.gridx = 1;
        JTextField field = new JTextField(value != null ? value : "N/A", 20);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setBackground(panel.getBackground());
        panel.add(field, gbc);
    }

    private JPanel createPerformancePanel() throws Exception {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "Match vs", "Date", "Goals", "Assists", "Minutes", "Rating", "Remarks"
        }, 0);
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load performance data
        String query = "SELECT m.opponent, m.match_date, pd.goals, pd.assists, " +
                      "pd.minutes_played, pd.rating, pd.remarks " +
                      "FROM performance_data pd " +
                      "JOIN matches m ON pd.match_id = m.match_id " +
                      "WHERE pd.player_id = ? ORDER BY m.match_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, playerId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("opponent"));
                row.add(rs.getDate("match_date"));
                row.add(rs.getInt("goals"));
                row.add(rs.getInt("assists"));
                row.add(rs.getInt("minutes_played"));
                row.add(rs.getBigDecimal("rating"));
                row.add(rs.getString("remarks"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading performance: " + e.getMessage());
        }

        return panel;
    }

    private JPanel createAttendancePanel() throws Exception {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "Session Date", "Start Time", "Location", "Status", "Remarks"
        }, 0);
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT ts.session_date, ts.start_time, ts.location, a.status, a.remarks " +
                      "FROM attendance a " +
                      "JOIN training_sessions ts ON a.session_id = ts.session_id " +
                      "WHERE a.player_id = ? ORDER BY ts.session_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, playerId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getDate("session_date"));
                row.add(rs.getTime("start_time"));
                row.add(rs.getString("location"));
                row.add(rs.getString("status"));
                row.add(rs.getString("remarks"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance: " + e.getMessage());
        }

        return panel;
    }

    private JPanel createInjuriesPanel() throws Exception {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "Injury Type", "Injury Date", "Recovery Date", "Status", "Remarks"
        }, 0);
        
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT injury_type, injury_date, recovery_date, status, remarks " +
                      "FROM injuries WHERE player_id = ? ORDER BY injury_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, playerId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("injury_type"));
                row.add(rs.getDate("injury_date"));
                row.add(rs.getDate("recovery_date"));
                row.add(rs.getString("status"));
                row.add(rs.getString("remarks"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading injuries: " + e.getMessage());
        }

        return panel;
    }

    private void loadPlayerData() throws Exception {
        // Data is loaded in each tab's creation method
    }
}