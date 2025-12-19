-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Nov 25, 2025 at 08:05 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sports_monitoring_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance` (
  `attendance_id` int NOT NULL AUTO_INCREMENT,
  `session_id` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  `status` enum('Present','Absent','Late') DEFAULT 'Absent',
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`attendance_id`),
  KEY `session_id` (`session_id`),
  KEY `player_id` (`player_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`attendance_id`, `session_id`, `player_id`, `status`, `remarks`) VALUES
(1, 1, 1, 'Present', 'On time'),
(2, 1, 2, 'Late', 'Arrived 10 min late'),
(3, 2, 3, 'Absent', 'Sick'),
(4, 3, 4, 'Present', ''),
(5, 3, 5, 'Present', ''),
(6, 4, 4, 'Late', ''),
(7, 5, 6, 'Present', ''),
(8, 6, 6, 'Present', ''),
(9, 7, 1, 'Present', ''),
(10, 8, 2, 'Absent', 'Injury');

-- --------------------------------------------------------

--
-- Table structure for table `coaches`
--

DROP TABLE IF EXISTS `coaches`;
CREATE TABLE IF NOT EXISTS `coaches` (
  `coach_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`coach_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `coaches`
--

INSERT INTO `coaches` (`coach_id`, `user_id`, `full_name`, `specialization`, `experience_years`, `email`, `phone_number`) VALUES
(1, 2, 'Mike Johnson', 'Fitness', 5, 'mike@club.com', '0789001111'),
(2, 3, 'Sandra Miller', 'Goalkeeping', 7, 'sandra@club.com', '0789002222'),
(3, 4, 'John Peter', 'Defensive Training', 10, 'john@club.com', '0789003333'),
(4, 14, 'GIANT OF AFRICA', 'General', 1, 'giant@gmail.com', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `injuries`
--

DROP TABLE IF EXISTS `injuries`;
CREATE TABLE IF NOT EXISTS `injuries` (
  `injury_id` int NOT NULL AUTO_INCREMENT,
  `player_id` int DEFAULT NULL,
  `injury_type` varchar(100) DEFAULT NULL,
  `injury_date` date DEFAULT NULL,
  `recovery_date` date DEFAULT NULL,
  `status` enum('Recovering','Recovered','Severe') DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`injury_id`),
  KEY `player_id` (`player_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `injuries`
--

INSERT INTO `injuries` (`injury_id`, `player_id`, `injury_type`, `injury_date`, `recovery_date`, `status`, `remarks`) VALUES
(1, 1, 'Knee Sprain', '2025-08-10', '2025-09-01', 'Recovered', ''),
(2, 2, 'Ankle Twist', '2025-08-20', '2025-09-10', 'Recovered', ''),
(3, 3, 'Shoulder Injury', '2025-09-01', NULL, 'Recovering', 'Still in therapy'),
(4, 4, 'Hamstring', '2025-09-15', '2025-09-30', 'Recovered', ''),
(5, 5, 'Back Pain', '2025-10-01', NULL, 'Recovering', ''),
(6, 6, 'Wrist Injury', '2025-09-05', '2025-09-20', 'Recovered', ''),
(7, 2, 'Knee Pain', '2025-10-10', NULL, 'Severe', 'Needs rest'),
(8, 3, 'Muscle Tear', '2025-10-15', NULL, 'Recovering', ''),
(9, 5, 'Ankle Sprain', '2025-10-16', NULL, 'Recovering', ''),
(10, 1, 'Leg Cramp', '2025-10-18', NULL, 'Recovering', '');

-- --------------------------------------------------------

--
-- Table structure for table `matches`
--

DROP TABLE IF EXISTS `matches`;
CREATE TABLE IF NOT EXISTS `matches` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `team_id` int DEFAULT NULL,
  `opponent` varchar(100) DEFAULT NULL,
  `match_date` date DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `result` enum('Win','Lose','Draw') DEFAULT NULL,
  `score` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`match_id`),
  KEY `team_id` (`team_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `matches`
--

INSERT INTO `matches` (`match_id`, `team_id`, `opponent`, `match_date`, `location`, `result`, `score`) VALUES
(1, 1, 'Panthers FC', '2025-09-20', 'Stadium A', 'Win', '3-1'),
(2, 1, 'Warriors FC', '2025-09-25', 'Stadium B', 'Lose', '0-1'),
(3, 2, 'Rockets FC', '2025-09-22', 'Stadium C', 'Draw', '2-2'),
(4, 3, 'Queens FC', '2025-09-23', 'Arena A', 'Win', '1-0'),
(5, 1, 'Lions B', '2025-10-02', 'Main Field', 'Win', '2-0'),
(6, 2, 'Stars FC', '2025-10-03', 'Field B', 'Lose', '1-3'),
(7, 3, 'Angels FC', '2025-10-05', 'Arena A', 'Draw', '0-0'),
(8, 1, 'Eagles', '2025-10-06', 'Stadium A', 'Lose', '1-2'),
(9, 2, 'Dragons', '2025-10-07', 'Stadium B', 'Win', '4-2'),
(10, 3, 'Tigers', '2025-10-08', 'Arena A', 'Lose', '0-2');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE IF NOT EXISTS `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `message` text,
  `sent_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Unread','Read') DEFAULT 'Unread',
  PRIMARY KEY (`notification_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`notification_id`, `user_id`, `title`, `message`, `sent_at`, `status`) VALUES
(1, 1, 'Welcome', 'Welcome Justine! You are now the admin of Sports Monitoring System.', '2025-11-05 09:57:52', 'Unread'),
(2, 2, 'Training Reminder', 'You have a session scheduled tomorrow at 09:00.', '2025-11-05 09:57:52', 'Unread'),
(3, 3, 'Match Report', 'Your team report for last match is ready.', '2025-11-05 09:57:52', 'Unread'),
(4, 4, 'Injury Update', 'Player Emma is recovering well.', '2025-11-05 09:57:52', 'Unread'),
(5, 5, 'Next Match', 'Lions FC plays on Saturday.', '2025-11-05 09:57:52', 'Unread'),
(6, 6, 'Training Update', 'You have new drills assigned by Coach Mike.', '2025-11-05 09:57:52', 'Unread'),
(7, 7, 'Performance Review', 'Check your stats after last match.', '2025-11-05 09:57:52', 'Unread'),
(8, 8, 'Session Cancelled', 'Training on Friday has been postponed.', '2025-11-05 09:57:52', 'Unread'),
(9, 9, 'Team Meeting', 'Meeting today at 5 PM in the club office.', '2025-11-05 09:57:52', 'Unread'),
(10, 10, 'Account Notice', 'Your profile has been updated successfully.', '2025-11-05 09:57:52', 'Unread');

-- --------------------------------------------------------

--
-- Table structure for table `performance_data`
--

DROP TABLE IF EXISTS `performance_data`;
CREATE TABLE IF NOT EXISTS `performance_data` (
  `performance_id` int NOT NULL AUTO_INCREMENT,
  `player_id` int DEFAULT NULL,
  `match_id` int DEFAULT NULL,
  `goals` int DEFAULT '0',
  `assists` int DEFAULT '0',
  `minutes_played` int DEFAULT NULL,
  `rating` decimal(3,1) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`performance_id`),
  KEY `player_id` (`player_id`),
  KEY `match_id` (`match_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `performance_data`
--

INSERT INTO `performance_data` (`performance_id`, `player_id`, `match_id`, `goals`, `assists`, `minutes_played`, `rating`, `remarks`) VALUES
(1, 1, 1, 2, 1, 90, 8.5, 'Excellent performance'),
(2, 2, 1, 1, 0, 88, 7.2, 'Solid game'),
(3, 3, 2, 0, 0, 90, 6.0, 'Needs improvement'),
(4, 4, 3, 0, 2, 85, 7.8, 'Good assists'),
(5, 5, 4, 1, 0, 90, 8.0, 'Decisive goal'),
(6, 6, 5, 0, 0, 70, 6.5, 'Average'),
(7, 1, 5, 1, 1, 90, 8.0, 'Consistent'),
(8, 2, 8, 0, 0, 85, 7.0, 'Defensive contribution'),
(9, 3, 9, 2, 1, 90, 8.7, 'Great finishing'),
(10, 6, 10, 0, 0, 90, 6.8, 'Solid defense');

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
CREATE TABLE IF NOT EXISTS `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `team_id` int DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `age` int DEFAULT NULL,
  `gender` enum('Male','Female') DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `height_cm` float DEFAULT NULL,
  `weight_kg` float DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  PRIMARY KEY (`player_id`),
  KEY `team_id` (`team_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`player_id`, `user_id`, `team_id`, `full_name`, `age`, `gender`, `position`, `height_cm`, `weight_kg`, `join_date`) VALUES
(1, 5, 1, 'Anne Smith', 22, 'Female', 'Forward', 165, 55, '2023-05-10'),
(2, 6, 1, 'Ronald Karemera', 24, 'Male', 'Midfielder', 178, 70, '2023-04-01'),
(3, 7, 1, 'Tony Mugabo', 21, 'Male', 'Defender', 182, 72, '2023-03-15'),
(4, 8, 2, 'Emma Uwase', 20, 'Female', 'Goalkeeper', 170, 60, '2023-02-12'),
(5, 9, 2, 'James Habimana', 23, 'Male', 'Striker', 176, 68, '2023-01-08'),
(6, 10, 3, 'Lucas Iradukunda', 29, 'Male', 'Midfielder', 180, 73, '2022-12-02');

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
CREATE TABLE IF NOT EXISTS `teams` (
  `team_id` int NOT NULL AUTO_INCREMENT,
  `team_name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `coach_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`team_id`),
  KEY `coach_id` (`coach_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `teams`
--

INSERT INTO `teams` (`team_id`, `team_name`, `category`, `coach_id`, `created_at`) VALUES
(1, 'Lions FC', 'Senior', 1, '2025-11-05 09:53:46'),
(2, 'Eagles FC', 'Junior', 2, '2025-11-05 09:53:46'),
(3, 'Titans FC', 'Women', 3, '2025-11-05 09:53:46');

-- --------------------------------------------------------

--
-- Table structure for table `training_sessions`
--

DROP TABLE IF EXISTS `training_sessions`;
CREATE TABLE IF NOT EXISTS `training_sessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `team_id` int DEFAULT NULL,
  `coach_id` int DEFAULT NULL,
  `session_date` date NOT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`session_id`),
  KEY `team_id` (`team_id`),
  KEY `coach_id` (`coach_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `training_sessions`
--

INSERT INTO `training_sessions` (`session_id`, `team_id`, `coach_id`, `session_date`, `start_time`, `end_time`, `location`, `description`) VALUES
(1, 1, 1, '2025-10-01', '09:00:00', '11:00:00', 'Main Field', 'Endurance Training'),
(2, 1, 1, '2025-10-05', '10:00:00', '12:00:00', 'Gym Hall', 'Strength Session'),
(3, 2, 2, '2025-10-03', '08:30:00', '10:30:00', 'Field B', 'Goalkeeping Drills'),
(4, 2, 2, '2025-10-07', '09:00:00', '11:00:00', 'Training Ground', 'Team Coordination'),
(5, 3, 3, '2025-10-02', '14:00:00', '16:00:00', 'Arena A', 'Defensive Tactics'),
(6, 3, 3, '2025-10-06', '15:00:00', '17:00:00', 'Arena A', 'Passing Drills'),
(7, 1, 1, '2025-10-10', '08:00:00', '10:00:00', 'Main Field', 'Speed Training'),
(8, 1, 1, '2025-10-15', '09:00:00', '11:00:00', 'Gym Hall', 'Agility Test'),
(9, 2, 2, '2025-10-12', '08:30:00', '10:30:00', 'Field B', 'Shooting Practice'),
(10, 3, 3, '2025-10-18', '15:00:00', '17:00:00', 'Arena A', 'Ball Control');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','coach','player') NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `status` enum('Active','Inactive') DEFAULT 'Active',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `email`, `status`, `created_at`) VALUES
(1, 'Justine', '@2003', 'admin', 'justine@gmail.com', 'Active', '2025-11-05 09:53:02'),
(2, 'coach_mike', 'pass123', 'coach', 'mike@club.com', 'Active', '2025-11-05 09:53:02'),
(3, 'coach_sandra', 'pass234', 'coach', 'sandra@club.com', 'Active', '2025-11-05 09:53:02'),
(4, 'coach_john', 'pass345', 'coach', 'john@club.com', 'Active', '2025-11-05 09:53:02'),
(5, 'player_anne', 'p1', 'player', 'anne@team.com', 'Active', '2025-11-05 09:53:02'),
(6, 'player_ronald', 'p2', 'player', 'ronald@team.com', 'Active', '2025-11-05 09:53:02'),
(7, 'player_tony', 'p3', 'player', 'tony@team.com', 'Active', '2025-11-05 09:53:02'),
(8, 'player_emma', 'p4', 'player', 'emma@team.com', 'Active', '2025-11-05 09:53:02'),
(9, 'player_james', 'p5', 'player', 'james@team.com', 'Active', '2025-11-05 09:53:02'),
(10, 'player_lucas', 'p6', 'player', 'lucas@team.com', 'Active', '2025-11-05 09:53:02'),
(15, 'GIANT', '1', 'coach', 'g@gmail.com', 'Active', '2025-11-20 11:28:15');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
