package com.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.util.DBConnection;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class CoachDashboard extends JPanel {
    private int coachId;
    private JTabbedPane tabbedPane;

    public CoachDashboard(int coachId) {
        this.coachId = coachId;
        setLayout(new BorderLayout());
        initializeComponents();
        try {
            loadCoachData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Training Sessions Tab
        JPanel sessionsPanel = new JPanel(new BorderLayout());
        DefaultTableModel sessionsModel = new DefaultTableModel(new String[]{
            "Session ID", "Date", "Start Time", "End Time", "Location", "Team", "Description"
        }, 0);
        JTable sessionsTable = new JTable(sessionsModel);
        JScrollPane sessionsScroll = new JScrollPane(sessionsTable);
        
        JPanel sessionsButtonPanel = new JPanel();
        JButton refreshSessionsBtn = new JButton("Refresh");
        sessionsButtonPanel.add(refreshSessionsBtn);
        
        sessionsPanel.add(sessionsScroll, BorderLayout.CENTER);
        sessionsPanel.add(sessionsButtonPanel, BorderLayout.SOUTH);

        // Players Tab
        JPanel playersPanel = new JPanel(new BorderLayout());
        DefaultTableModel playersModel = new DefaultTableModel(new String[]{
            "Player ID", "Full Name", "Team", "Age", "Gender", "Position", "Join Date"
        }, 0);
        JTable playersTable = new JTable(playersModel);
        JScrollPane playersScroll = new JScrollPane(playersTable);
        playersPanel.add(playersScroll, BorderLayout.CENTER);

        // Attendance Tab
        JPanel attendancePanel = new JPanel(new BorderLayout());
        DefaultTableModel attendanceModel = new DefaultTableModel(new String[]{
            "Attendance ID", "Session Date", "Player Name", "Status", "Remarks"
        }, 0);
        JTable attendanceTable = new JTable(attendanceModel);
        JScrollPane attendanceScroll = new JScrollPane(attendanceTable);
        attendancePanel.add(attendanceScroll, BorderLayout.CENTER);

        // Performance Tab
        JPanel performancePanel = new JPanel(new BorderLayout());
        DefaultTableModel performanceModel = new DefaultTableModel(new String[]{
            "Player Name", "Match vs", "Goals", "Assists", "Minutes", "Rating", "Remarks"
        }, 0);
        JTable performanceTable = new JTable(performanceModel);
        JScrollPane performanceScroll = new JScrollPane(performanceTable);
        performancePanel.add(performanceScroll, BorderLayout.CENTER);

        tabbedPane.addTab("Training Sessions", sessionsPanel);
        tabbedPane.addTab("My Players", playersPanel);
        tabbedPane.addTab("Attendance", attendancePanel);
        tabbedPane.addTab("Performance", performancePanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Button actions
        refreshSessionsBtn.addActionListener(e -> {
            try {
                loadTrainingSessions(sessionsModel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }

    private void loadCoachData() throws Exception {
        // Data will be loaded when tabs are created
    }

    private void loadTrainingSessions(DefaultTableModel model) throws Exception {
        model.setRowCount(0);
        String query = "SELECT ts.session_id, ts.session_date, ts.start_time, ts.end_time, " +
                      "ts.location, t.team_name, ts.description " +
                      "FROM training_sessions ts " +
                      "JOIN teams t ON ts.team_id = t.team_id " +
                      "WHERE ts.coach_id = ? ORDER BY ts.session_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, coachId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("session_id"));
                row.add(rs.getDate("session_date"));
                row.add(rs.getTime("start_time"));
                row.add(rs.getTime("end_time"));
                row.add(rs.getString("location"));
                row.add(rs.getString("team_name"));
                row.add(rs.getString("description"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading sessions: " + e.getMessage());
        }
    }

    private void loadMyPlayers(DefaultTableModel model) throws Exception {
        model.setRowCount(0);
        String query = "SELECT p.player_id, p.full_name, t.team_name, p.age, p.gender, p.position, p.join_date " +
                      "FROM players p " +
                      "JOIN teams t ON p.team_id = t.team_id " +
                      "WHERE t.coach_id = ? ORDER BY p.full_name";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, coachId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("player_id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("team_name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("position"));
                row.add(rs.getDate("join_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading players: " + e.getMessage());
        }
    }

    private void loadAttendance(DefaultTableModel model) throws Exception {
        model.setRowCount(0);
        String query = "SELECT a.attendance_id, ts.session_date, p.full_name, a.status, a.remarks " +
                      "FROM attendance a " +
                      "JOIN training_sessions ts ON a.session_id = ts.session_id " +
                      "JOIN players p ON a.player_id = p.player_id " +
                      "WHERE ts.coach_id = ? ORDER BY ts.session_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, coachId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("attendance_id"));
                row.add(rs.getDate("session_date"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("status"));
                row.add(rs.getString("remarks"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading attendance: " + e.getMessage());
        }
    }

    private void loadPerformance(DefaultTableModel model) throws Exception {
        model.setRowCount(0);
        String query = "SELECT p.full_name, m.opponent, pd.goals, pd.assists, " +
                      "pd.minutes_played, pd.rating, pd.remarks " +
                      "FROM performance_data pd " +
                      "JOIN players p ON pd.player_id = p.player_id " +
                      "JOIN matches m ON pd.match_id = m.match_id " +
                      "JOIN teams t ON p.team_id = t.team_id " +
                      "WHERE t.coach_id = ? ORDER BY m.match_date DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, coachId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("full_name"));
                row.add(rs.getString("opponent"));
                row.add(rs.getInt("goals"));
                row.add(rs.getInt("assists"));
                row.add(rs.getInt("minutes_played"));
                row.add(rs.getBigDecimal("rating"));
                row.add(rs.getString("remarks"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            throw new Exception("Error loading performance: " + e.getMessage());
        }
    }
}