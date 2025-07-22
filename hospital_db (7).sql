-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 29, 2014 at 08:53 AM
-- Server version: 5.5.27
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hospital_db`
--
CREATE DATABASE IF NOT EXISTS `hospital_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `hospital_db`;

-- --------------------------------------------------------

--
-- Table structure for table `amountreceipt`
--

CREATE TABLE IF NOT EXISTS `amountreceipt` (
  `receipt_id` int(200) NOT NULL AUTO_INCREMENT,
  `receipt_type` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_amount` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_username` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_ref_doctor` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_patientname` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt__date` date NOT NULL DEFAULT '0000-00-00',
  `receipt_time` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `receipt_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;


--
-- Table structure for table `exam_amountreceipt`
--

CREATE TABLE IF NOT EXISTS `exam_amountreceipt` (
  `exam_receiptid` int(200) NOT NULL AUTO_INCREMENT,
  `exam_namecat` varchar(250) NOT NULL,
  `exam_doctor_ref` varchar(250) NOT NULL,
  `exam_patientname` varchar(250) NOT NULL,
  `exam_amount` varchar(250) NOT NULL,
  `exam_username` varchar(250) NOT NULL,
  `exam_date` date NOT NULL DEFAULT '0000-00-00',
  `exam_time` varchar(250) NOT NULL,
  `exam_text1` varchar(250) NOT NULL DEFAULT 'n/a',
  `exam_text2` varchar(250) NOT NULL DEFAULT 'n/a',
  `exan_text3` varchar(250) NOT NULL DEFAULT 'n/a',
  `exam_text4` varchar(250) NOT NULL DEFAULT 'n/a',
  PRIMARY KEY (`exam_receiptid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;


--
-- Table structure for table `ipd_bill`
--

CREATE TABLE IF NOT EXISTS `ipd_bill` (
  `bill_id` int(200) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(250) NOT NULL,
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `ipd_bill`
--

INSERT INTO `ipd_bill` (`bill_id`, `bill_no`) VALUES
(1, '33200');

-- --------------------------------------------------------

--
-- table structure for table `ipd_doctor_incharge`
--

create table if not exists `ipd_doctor_incharge` (
  `handover_charge_id` int(255) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(255) NOT NULL,
  `doctor_name` varchar(255) NOT NULL,
  `doctor_id` varchar(255) NOT NULL,
  `p_id` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_name` varchar(255) NOT NULL DEFAULT 'N/A',
  `start_date` date NOT NULL,
  `start_time` varchar(255) NOT NULL,
  `end_date` date NOT NULL DEFAULT '0000-00-00',
  `end_time` varchar(255) NOT NULL DEFAULT 'n/a',
  `note1` varchar(255) NOT NULL DEFAULT 'N/A',
  `note2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`handover_charge_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='doctor incharge in ipd entry' AUTO_INCREMENT=12 ;


--
-- Table structure for table `ipd_entery`
--

CREATE TABLE IF NOT EXISTS `ipd_entery` (
  `ipd_id` int(255) NOT NULL AUTO_INCREMENT,
  `ipd_id2` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_id` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `ipd_doctor_refference` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_building` varchar(255) NOT NULL,
  `ipd_ward` varchar(255) NOT NULL,
  `ipd_room` varchar(255) NOT NULL,
  `ipd_bed_no` varchar(255) NOT NULL,
  `ipd_ward_code` varchar(255) NOT NULL,
  `ipd_entry_date` date NOT NULL,
  `ipd_enter_time` varchar(255) NOT NULL,
  `ipd_advanced_payment` varchar(255) NOT NULL DEFAULT '00',
  `ipd_total_charges` varchar(255) NOT NULL DEFAULT '00',
  `ipd_balance` varchar(255) NOT NULL DEFAULT '00',
  `ipd_recieved_amount` varchar(255) NOT NULL DEFAULT '00',
  `ipd_payment_recieved` varchar(255) NOT NULL DEFAULT 'No',
  `ipd_discharge_date` date NOT NULL DEFAULT '0000-00-00',
  `ipd_discharge_time` varchar(255) DEFAULT 'N/A',
  `ipd_discharged` varchar(255) NOT NULL DEFAULT 'No',
  `ipd-entry_user` varchar(250) NOT NULL DEFAULT 'N/A',
  `ipd_text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `ipd_text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`ipd_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='IPD PATIENT ENTRY' AUTO_INCREMENT=14 ;

--
-- Table structure for table `ipd_expenses`
--

CREATE TABLE IF NOT EXISTS `ipd_expenses` (
  `expense_id` int(250) NOT NULL AUTO_INCREMENT,
  `ipd_id` varchar(250) NOT NULL,
  `expense_name` varchar(250) NOT NULL,
  `expense_desc` varchar(250) NOT NULL,
  `expense_amount` varchar(250) NOT NULL,
  `charges_id` varchar(250) NOT NULL DEFAULT '0',
  `expense_date` date NOT NULL DEFAULT '0000-00-00',
  `expense_time` varchar(250) NOT NULL,
  `expense_text1` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text2` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text3` varchar(250) NOT NULL DEFAULT 'N/A',
  `expense_text4` varchar(250) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`expense_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=31 ;

--
-- Table structure for table `ward_management`
--

CREATE TABLE IF NOT EXISTS `ward_management` (
  `ward_id` int(255) NOT NULL AUTO_INCREMENT,
  `building_name` varchar(255) NOT NULL,
  `ward_name` varchar(255) NOT NULL,
  `ward_room_no` varchar(255) NOT NULL,
  `bed_no` varchar(255) NOT NULL,
  `ward_category` varchar(255) NOT NULL DEFAULT 'WARD',
  `p_id` varchar(255) NOT NULL DEFAULT 'N/A',
  `p_name` varchar(255) NOT NULL DEFAULT 'N/A',
  `bed_filled` varchar(255) NOT NULL DEFAULT '0',
  `ward_entry_date` date NOT NULL DEFAULT '0000-00-00',
  `ward_incharge` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text1` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text2` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text3` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text4` varchar(255) NOT NULL DEFAULT 'N/A',
  `ward_text5` varchar(255) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`ward_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='WARD MANAGEMENT' AUTO_INCREMENT=12 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
