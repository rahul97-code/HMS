-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 23, 2013 at 08:23 AM
-- Server version: 5.6.12-log
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
CREATE DATABASE IF NOT EXISTS `hospital_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `hospital_db`;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_detail`
--

CREATE TABLE IF NOT EXISTS `doctor_detail` (
  `doc_id` int(255) NOT NULL AUTO_INCREMENT,
  `doc_name` varchar(255) NOT NULL,
  `doc_username` varchar(255) NOT NULL,
  `doc_password` varchar(255) NOT NULL,
  `doc_specialization` varchar(255) NOT NULL,
  `doc_qualification` varchar(255) DEFAULT NULL,
  `doc_opdroom` varchar(255) NOT NULL,
  `doc_telephone` varchar(255) DEFAULT NULL,
  `doc_address` varchar(255) DEFAULT NULL,
  `doc_hindiname` varchar(255) DEFAULT NULL,
  `doc_lastlogin` date DEFAULT NULL,
  `doc_active` varchar(255) NOT NULL DEFAULT '1',
  `doc_text1` varchar(255) DEFAULT NULL,
  `doc_text2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`doc_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `doctor_detail`
--

INSERT INTO `doctor_detail` (`doc_id`, `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_qualification`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_hindiname`, `doc_lastlogin`, `doc_active`, `doc_text1`, `doc_text2`) VALUES
(1, 'sukhpal saini', 'sukhpal', 'saini', 'software Engg.', NULL, '1234', '3543656', 'sdfdsgs343dfd ', 'sdsdfdsf', NULL, '1', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `exam_entery`
--

CREATE TABLE IF NOT EXISTS `exam_entery` (
  `exam_id` int(10) NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(255) NOT NULL,
  `exam_nameid` varchar(255) NOT NULL,
  `exam_pid` varchar(255) NOT NULL,
  `exam_pname` varchar(255) NOT NULL,
  `exam_doctorreff` varchar(255) NOT NULL,
  `exam_date` date NOT NULL,
  `exam_charges` varchar(255) NOT NULL,
  `exam_chargespaid` varchar(255) DEFAULT 'Yes',
  `exam_performed` varchar(255) NOT NULL DEFAULT 'No',
  `exam_operator` varchar(255) DEFAULT NULL,
  `exam_reportno` varchar(255) DEFAULT NULL,
  `exam_reportname1` varchar(255) DEFAULT NULL,
  `exam_reportname2` varchar(255) DEFAULT NULL,
  `exam_reportname3` varchar(255) DEFAULT NULL,
  `exam_reportname4` varchar(255) DEFAULT NULL,
  `exam_result1` varchar(255) DEFAULT NULL,
  `exam_result2` varchar(255) DEFAULT NULL,
  `exam_result3` varchar(255) DEFAULT NULL,
  `exam_result4` varchar(255) DEFAULT NULL,
  `exam_result5` varchar(255) DEFAULT NULL,
  `exam_sample1` varchar(255) DEFAULT NULL,
  `exam_sample2` varchar(255) DEFAULT NULL,
  `exam_sample3` varchar(255) DEFAULT NULL,
  `exam_sample4` varchar(255) DEFAULT NULL,
  `exam_sample5` varchar(255) DEFAULT NULL,
  `exam_note1` varchar(255) DEFAULT NULL,
  `exam_note2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`exam_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `exam_entery`
--

INSERT INTO `exam_entery` (`exam_id`, `exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_chargespaid`, `exam_performed`, `exam_operator`, `exam_reportno`, `exam_reportname1`, `exam_reportname2`, `exam_reportname3`, `exam_reportname4`, `exam_result1`, `exam_result2`, `exam_result3`, `exam_result4`, `exam_result5`, `exam_sample1`, `exam_sample2`, `exam_sample3`, `exam_sample4`, `exam_sample5`, `exam_note1`, `exam_note2`) VALUES
(1, 'x-Ray', '1', '1002334', 'abc', 'sukhpal saini', '2013-11-22', '200', 'Yes', 'No', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(2, 'x-Ray', '1', '0001130000006', 'ramesh chand', 'sukhpal saini', '2013-11-22', '200', 'Yes', 'No', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(3, 'Ultrasount', '2', '0001130000006', 'ramesh chand', 'sukhpal saini', '2013-11-22', '400', 'Yes', 'No', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(4, 'x-Ray', '1', '0001130000006', 'ramesh chand', 'sukhpal saini', '2013-11-22', '200', 'Yes', 'No', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `exam_master`
--

CREATE TABLE IF NOT EXISTS `exam_master` (
  `exam_code` int(10) NOT NULL,
  `exam_desc` varchar(255) NOT NULL,
  `exam_lab` varchar(255) DEFAULT NULL,
  `exam_room` varchar(255) DEFAULT NULL,
  `exam_operator` varchar(255) DEFAULT NULL,
  `exam_text1` varchar(255) DEFAULT NULL,
  `exam_text2` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `exam_master`
--

INSERT INTO `exam_master` (`exam_code`, `exam_desc`, `exam_lab`, `exam_room`, `exam_operator`, `exam_text1`, `exam_text2`) VALUES
(1, 'X-Ray', 'X-Ray Lab', '23', 'Ram Singh', NULL, NULL),
(2, 'ultrasound', 'ultrasound lab', '68', 'roshal lal', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `insurance_detail`
--

CREATE TABLE IF NOT EXISTS `insurance_detail` (
  `ins_id` int(10) NOT NULL AUTO_INCREMENT,
  `ins_name` varchar(255) NOT NULL,
  `ins_detail` varchar(255) NOT NULL,
  `ins_persentage` varchar(255) DEFAULT NULL,
  `ins_test1` varchar(255) DEFAULT NULL,
  `ins_test2` varchar(255) DEFAULT NULL,
  `ins_test3` varchar(255) DEFAULT NULL,
  `ins_test4` varchar(255) DEFAULT NULL,
  `ins_test5` varchar(255) DEFAULT NULL,
  `ins_test6` varchar(255) DEFAULT NULL,
  `ins_test7` varchar(255) DEFAULT NULL,
  `ins_test8` varchar(255) DEFAULT NULL,
  `ins_test9` varchar(255) DEFAULT NULL,
  `ins_test10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ins_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `insurance_detail`
--

INSERT INTO `insurance_detail` (`ins_id`, `ins_name`, `ins_detail`, `ins_persentage`, `ins_test1`, `ins_test2`, `ins_test3`, `ins_test4`, `ins_test5`, `ins_test6`, `ins_test7`, `ins_test8`, `ins_test9`, `ins_test10`) VALUES
(1, 'BPL', 'BPL Card', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `opd_entery`
--

CREATE TABLE IF NOT EXISTS `opd_entery` (
  `opd_id` int(255) NOT NULL AUTO_INCREMENT,
  `p_id` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `opd_doctor` varchar(255) NOT NULL,
  `opd_doctorid` varchar(255) NOT NULL,
  `opd_date` date NOT NULL,
  `opd_symptom` varchar(255) DEFAULT NULL,
  `opd_prescription` varchar(255) DEFAULT NULL,
  `opd_token` varchar(255) NOT NULL,
  `opd_diseasestype` varchar(255) NOT NULL,
  `opd_type` varchar(255) DEFAULT NULL,
  `opd_refferal` varchar(255) DEFAULT NULL,
  `opd_charge` varchar(255) DEFAULT NULL,
  `opd_text1` varchar(255) DEFAULT NULL,
  `opd_text2` varchar(255) DEFAULT NULL,
  `opd_text3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`opd_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `opd_entery`
--

INSERT INTO `opd_entery` (`opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_doctorid`, `opd_date`, `opd_symptom`, `opd_prescription`, `opd_token`, `opd_diseasestype`, `opd_type`, `opd_refferal`, `opd_charge`, `opd_text1`, `opd_text2`, `opd_text3`) VALUES
(1, '1241', 'sdfs', 'sdfdsf', 'sdfds', '2013-11-13', 'dsfdsf', 'dsfdsf', 'sdfds', 'sdfdfs', 'dsfdsf', 'sdfdsf', 'sdfds', 'dsfds', 'sdfds', 'sdfds'),
(2, '1002334', 'abc', 'sukhpal saini', '1', '2013-11-15', '', NULL, '1', 'ALL TYPE', '', '', '', NULL, NULL, NULL),
(3, '1002334', 'abc', 'sukhpal saini', '1', '2013-11-15', '', NULL, '1', 'ALL TYPE', '', '', '', NULL, NULL, NULL),
(4, '1245454', 'xyz', 'sukhpal saini', '1', '2013-11-15', '', NULL, '3', 'ALL TYPE', '', '', '', NULL, NULL, NULL),
(5, '1002334', 'abc', 'sukhpal saini', '1', '2013-11-15', '', NULL, '4', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(6, '1245454', 'xyz', 'sukhpal saini', '1', '2013-11-18', '', NULL, '1', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(7, '0001130000007', 'reena devi', 'sukhpal saini', '1', '2013-11-20', '', NULL, '1', 'ALL TYPE', 'New Attendance', '', '', NULL, NULL, NULL),
(8, '1002334', 'abc', 'sukhpal saini', '1', '2013-11-21', '', NULL, '1', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(9, '0001130000006', 'ramesh chand', 'sukhpal saini', '1', '2013-11-21', '', NULL, '2', 'ALL TYPE', 'New Attendance', '', '', NULL, NULL, NULL),
(10, '0000130000010', 'zxczxc', 'sukhpal saini', '1', '2013-11-21', '', NULL, '3', 'ALL TYPE', 'New Attendance', '', '', NULL, NULL, NULL),
(11, '0001130000006', 'ramesh chand', 'sukhpal saini', '1', '2013-11-23', '', NULL, '1', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(12, '0001130000006', 'ramesh chand', 'sukhpal saini', '1', '2013-11-23', '', NULL, '2', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(13, '0001130000006', 'ramesh chand', 'sukhpal saini', '1', '2013-11-23', '', NULL, '3', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL),
(14, '1245454', 'xyz', 'sukhpal saini', '1', '2013-11-23', '', NULL, '4', 'ALL TYPE', 'Re-Attendance', '', '', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `patient_detail`
--

CREATE TABLE IF NOT EXISTS `patient_detail` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT 'running number',
  `pid1` varchar(255) NOT NULL,
  `pid2` varchar(255) NOT NULL,
  `p_name` varchar(255) NOT NULL,
  `p_agetype` varchar(10) NOT NULL,
  `p_age` varchar(255) NOT NULL,
  `p_birthdate` date DEFAULT NULL,
  `p_sex` varchar(255) NOT NULL,
  `p_addresss` text NOT NULL,
  `p_city` varchar(255) NOT NULL,
  `p_telephone` varchar(255) NOT NULL,
  `p_bloodtype` varchar(255) NOT NULL,
  `p_guardiantype` varchar(10) NOT NULL,
  `p_father_husband` varchar(255) NOT NULL,
  `p_insurancetype` varchar(255) NOT NULL,
  `p_lastopd` date DEFAULT '0000-00-00',
  `p_text1` varchar(255) DEFAULT NULL,
  `p_text2` varchar(255) DEFAULT NULL,
  `p_text3` varchar(255) DEFAULT NULL,
  `p_text4` varchar(255) DEFAULT NULL,
  `p_text5` varchar(255) DEFAULT NULL,
  `p_text6` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `patient_detail`
--

INSERT INTO `patient_detail` (`id`, `pid1`, `pid2`, `p_name`, `p_agetype`, `p_age`, `p_birthdate`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_bloodtype`, `p_guardiantype`, `p_father_husband`, `p_insurancetype`, `p_lastopd`, `p_text1`, `p_text2`, `p_text3`, `p_text4`, `p_text5`, `p_text6`) VALUES
(1, '1002334', '1002334', 'abc', 'age', '2-0-0', '1111-11-11', 'Male', 'asdasd', 'asdas', '21343544', 'Unknown', 'F', 'asasd', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(2, '1245454', '1245454', 'xyz', 'age', '0-3-0', '1111-11-11', 'Male', 'dfdf', 'dfdf', '1213234', 'B+', 'F', 'wrwer', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(3, '132144', '132144', 'asdad', 'age', '3-4-6', '1111-11-11', 'Male', 'asdasd', 'asdasd', '132123213', 'A+', 'F', 'adsasd', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(4, '4512345678906', '4512345678906', 'abcsd', 'age', '3-0-0', '1111-11-11', 'Male', 'rfefdf', 'ddffd', '35465776', 'AB+', 'F', 'sdafssf', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(5, '0001130000005', '0001130000005', 'adfgg', 'age', '3-0-0', '1111-11-11', 'Male', 'dfdf', 'dsff', 'dfdfff', 'A+', 'F', 'ddfdff', 'BPL', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(6, '0001130000006', '0001130000006', 'ramesh chand', 'age', '3-0-0', '1111-11-11', 'Male', '34,main street', 'ambala', '346576897', 'Unknown', 'F', 'vikrant kumar', 'BPL', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(7, '0001130000007', '0001130000007', 'reena devi', 'age', '3-0-0', '1111-11-11', 'Female', '34, amrgarh', 'ambala cantt.', '235354465467', 'Unknown', 'F', 'suresh kumar', 'BPL', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(8, '0000130000008', '0000130000008', 'afsaasd', 'age', '3-0-0', '1111-11-11', 'Male', 'asdsa', 'asdas', 'asd', 'Unknown', 'F', 'asdasd', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(9, '0000130000009', '0000130000009', 'sadsd', 'age', '3-0-0', '1111-11-11', 'Male', 'ddsf', 'sdsd', 'dsdsff', 'A+', 'F', 'dsfsdfs', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL),
(10, '0000130000010', '0000130000010', 'zxczxc', 'age', '0-2-0', '1111-11-11', 'Male', 'xzcxzc', 'xzcxzc', '', 'Unknown', 'F', 'xzczxc', 'Unknown', '0000-00-00', '', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `price_master`
--

CREATE TABLE IF NOT EXISTS `price_master` (
  `item_code` int(10) NOT NULL AUTO_INCREMENT,
  `item_desc` varchar(255) NOT NULL,
  `item-charges` varchar(255) NOT NULL,
  `item_category` varchar(255) NOT NULL,
  `item_text1` varchar(255) DEFAULT NULL,
  `item_text2` varchar(255) DEFAULT NULL,
  `item-text3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_code`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `price_master`
--

INSERT INTO `price_master` (`item_code`, `item_desc`, `item-charges`, `item_category`, `item_text1`, `item_text2`, `item-text3`) VALUES
(1, 'x-Ray', '200', '01', NULL, NULL, NULL),
(2, 'Ultrasount', '400', '01', NULL, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
