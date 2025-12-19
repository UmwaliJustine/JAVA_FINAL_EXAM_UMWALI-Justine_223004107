package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
 public static Connection getConnection() throws Exception{
	 Class.forName("com.mysql.cj.jdbc.Driver");
	 String url ="jdbc:mysql://localhost:3306/sports_monitoring_system";
	 String user ="root";
	 String password ="";
	 return DriverManager.getConnection(url, user, password);
 }
}