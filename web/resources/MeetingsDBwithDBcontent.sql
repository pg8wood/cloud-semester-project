-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: MeetingsDB
-- ------------------------------------------------------
-- Server version	5.7.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Meeting`
--

SET FOREIGN_KEY_CHECKS=0; 
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Meeting`;
DROP TABLE IF EXISTS `Meeting_Users`;
DROP TABLE IF EXISTS `MeetingFile`;
SET FOREIGN_KEY_CHECKS=1;

--
-- Table structure for table `User`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `middle_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) NOT NULL,
  `address1` varchar(128) NOT NULL,
  `address2` varchar(128) DEFAULT NULL,
  `city` varchar(64) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `security_question` int(11) NOT NULL,
  `security_answer` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `userPhoto` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'alexmartin','password1','Alex','James','Martin','220 Edge Way',NULL,'Blacksburg','VA','24060',1,'answer','alexm118@vt.edu','1.jpeg'),(2,'johndoe','password1','Agent','','Smith','100 Main Street','','Blacksburg','VA','24060',1,'answer','johndoe@jd.com','2.png'),(3,'p','p','Jennifer','','Lawrence','100 Main Street','','Blacksburg','VA','24060',1,'answer','pg8wood@vt.edu','3.jpeg'),(4,'fakedude','p','Jimmy','','Russell','100 Main Street','','Blacksburg','VA','24060',1,'answer','johndoe@jd.com','4.png'),(5,'fakerdude','p','Thor','','Odinson','100 Main Street','','Blacksburg','VA','24060',1,'answer','johndoe@jd.com','5.png'),(6,'somefakedude','p','Mr.','','Krabs','100 Main Street','','Blacksburg','VA','24060',1,'answer','johndoe@jd.com','6.jpeg'),(7,'tester','P@55word!','test','tes','tes','sdf','sdf','sdf','AK','23433',0,'sdf','tes@test.com','7.jpeg'),(8,'tester2','P@55word!','te','sdf','sdf','sdf','sdf','sdfg','AK','24060',0,'sdf','dsf@test.com','8.jpeg'),(9,'jshih11','MijimarU!12','Jeffrey','','Shih','826 Cascade Ct.','','Blacksburg','VA','24060',1,'Wu','j.shih11@gmail.com','9.jpeg'),(10,'Osmanbalci','P@55word!','Osman','','Balci','100 Main St','','Blacksburg','VA','24060',0,'answer','test@test.com','10.jpeg'),(11,'erinkocis','Password123*','Erin','','Kocis','8859 Glenridge Ct.','','Vienna','VA','22182',0,'Houston','erink13@vt.edu','11.png'),(12,'JohnDoes','Cloud(SD)s17','John','','Doe','MCB','','Blacksburg','VA','24060',0,'blacksburg','john@gmail.com','defaultUserPhoto.png'),(13,'silven','Qwerty!2','Jason','','You','Test Address1','Test Address2','Test City','VA','24060',0,'Test Security Answer','Test@Test.com','13.png'),(14,'tester3','P@55word!','psdf','psdf','psdf','sdfp','psdf','psdf','PA','23432',0,'answer','test@tser.com','defaultUserPhoto.png'),(15,'Gatewood','Cloud(SD)s17','Patrick','Eugene ','Gatewood','310 Webb St','','Blacksburg','VA','24060',4,'Dale','pg8wood@vt.edu','15.png'),(16,'KocisE','Cloud(SD)s17','Erin','','Kocis','310 Webb St','','Blacksburg','AK','24060',0,'Houston','erink13@vt.edu','16.jpeg');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;



/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Meeting` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `address1` varchar(128) NOT NULL,
  `address2` varchar(128) DEFAULT NULL,
  `city` varchar(64) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zipcode` smallint(5) NOT NULL,
  `owner_id` int(10) unsigned DEFAULT NULL,
  `topic` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `timeslots` varchar(4000) DEFAULT NULL,
  `invitees` varchar(256) DEFAULT NULL,
  `finaltime` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_id` (`owner_id`),
  CONSTRAINT `Meeting_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Meeting`
--

LOCK TABLES `Meeting` WRITE;
/*!40000 ALTER TABLE `Meeting` DISABLE KEYS */;
INSERT INTO `Meeting` VALUES (15,'8859 Glenridge Ct.','','Vienna','VA',22182,11,'Bake Cookies!','Lets Bake!','','p',''),(16,'Torgerson Hall','','Blacksburg','VA',24060,3,'Semester Project Presentation!!','We will be giving our semester project presentation to Dr. Balci, Dr. Laurian Vega, and the rest of the Cloud Software Development class! ','Thu May 04 02:00:00 EDT 2017','alexmartin, Osmanbalci, erinkocis, tester, tester2','Fri May 04 02:00:00 EDT 3917'),(17,'13','4','44','45',4561,9,'asdfa','asdf','Wed May 17 02:11:00 EDT 2017','jshih11','Wed May 17 02:11:00 EDT 2017'),(18,'225 Stanger St','','Blacksburg','VA',24060,10,'Semester Project Delivery ','We will deploy our semester project on Dr. Balci\'s iMac in order to be graded on our design. ','Mon May 08 02:00:00 EDT 2017','p, alexmartin,erinkocis,jshih11,fakedude,somefakedude',NULL),(22,'310 Webb St','Apartment 208','Blacksburg','VA',24060,3,'Presentatation Test','description ','Thu May 04 02:00:00 EDT 2017','erinkocis,Osmanbalci',NULL),(25,'Burress Hall','','Blacksburg','VA',24060,3,'moo','cow','Wed May 17 03:10:00 EDT 2017, Fri May 12 09:15:00 EDT 2017','erinkocis, fakedude','Fri May 12 09:15:00 EDT 2017'),(26,'Burruss Hall','','Blacksburg','VA',24060,9,'meow','cat','','fakedude, erinkocis',''),(27,'225 Stanger St','','Blacksburg','VA',24060,3,'Test Create','Test','Wed May 10 12:00:00 EDT 2017, Wed May 10 12:30:00 EDT 2017, Thu May 11 12:00:00 EDT 2017, Thu May 11 12:30:00 EDT 2017','erinkocis',NULL),(28,'100 Drillfield Dr','','Blacksburg','VA',24060,3,'Finish Semester Project Paper','We will finish composing the semester project paper and prepare our project for delivery to Dr. Balci\'s office. ','Fri May 05 12:30:00 EDT 2017, Fri May 05 01:00:00 EDT 2017','Osmanbalci',NULL),(30,'225 Stanger St','','Blacksburg','VA',24060,15,'Deliver Semester Project to Dr. Balci','We will deliver our semester project to Dr. Balci\'s office! ','Mon May 08 11:00:00 EDT 2017, Mon May 08 11:30:00 EDT 2017, Tue May 09 11:00:00 EDT 2017, Tue May 09 02:00:00 EDT 2017, Tue May 09 03:00:00 EDT 2017','Osmanbalci, alexmartin, fakedude, fakerdude','Wed May 09 02:00:00 EDT 3917'),(31,'310 Webb St','Apartment 208','Blacksburg','VA',24060,4,'End of Semester Party','The semester is over! Come out to The Edge and hang out to celebrate!','Thu May 11 02:00:00 EDT 2017, Thu May 11 03:00:00 EDT 2017, Thu May 11 03:30:00 EDT 2017, Fri May 12 06:00:00 EDT 2017, Fri May 12 06:30:00 EDT 2017','Gatewood, fakerdude, erinkocis, somefakedude',NULL),(32,'200 Park Avenue','','New York','NY',10166,5,'Prospective Avengers Recruiting Event','If you are interested in joining the Avengers, please come to Stark Tower! ','Tue May 09 08:00:00 EDT 2017, Tue May 09 01:00:00 EDT 2017, Tue May 09 02:00:00 EDT 2017, Sat May 13 04:00:00 EDT 2017','Gatewood, KocisE',''),(33,'6834 Hollywood Blvd','','Los Angeles','CA',0,6,'Jennifer Lawrence Fan Club Meeting','Join us as we watch Jennifer\'s latest movies and talk all things Jennifer! ','Fri May 12 08:00:00 EDT 2017, Fri May 12 09:00:00 EDT 2017, Sun May 14 10:00:00 EDT 2017, Sun May 14 10:30:00 EDT 2017','Gatewood, Kocis, Osmanbalci',NULL),(34,'8859 Glenridge Ct.','','Vienna','VA',22182,16,'House Party!','Come spend some time in the Kocis household! :)','Thu May 18 02:00:00 EDT 2017, Thu May 18 12:00:00 EDT 2017, Sat May 27 12:00:00 EDT 2017, Sat May 27 01:00:00 EDT 2017','Gatewood','Fri May 18 02:00:00 EDT 3917'),(35,'225 Stanger Street','','Blacksburg','VA',24060,3,'SQL DUMP!','Export (dump) our populated MySQL database content into a SQL script file','Tue May 09 02:00:00 EDT 2017, Tue May 09 03:00:00 EDT 2017, Tue May 09 01:00:00 EDT 2017','KocisE, Gatewood','');
/*!40000 ALTER TABLE `Meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Meeting_Users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Meeting_Users` (
  `user_id` int(10) unsigned NOT NULL,
  `meeting_id` int(10) unsigned NOT NULL,
  `response` tinyint(1) DEFAULT NULL,
  `available_times` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`meeting_id`),
  UNIQUE KEY `user_id` (`user_id`,`meeting_id`),
  KEY `meeting_id` (`meeting_id`),
  CONSTRAINT `Meeting_Users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Meeting_Users_ibfk_2` FOREIGN KEY (`meeting_id`) REFERENCES `Meeting` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `Meeting_Users`
--

LOCK TABLES `Meeting_Users` WRITE;
/*!40000 ALTER TABLE `Meeting_Users` DISABLE KEYS */;
INSERT INTO `Meeting_Users` VALUES (1,16,1,'Fri May 04 02:00:00 EDT 3917'),(1,18,0,''),(1,26,0,''),(1,27,0,''),(1,30,1,'Wed May 09 03:00:00 EDT 3917, Tue May 08 11:30:00 EDT 3917'),(1,35,0,''),(3,15,1,''),(3,16,1,'Thu May 04 02:00:00 EDT 2017'),(3,18,1,'Tue May 08 02:00:00 EDT 3917'),(3,22,1,'Thu May 04 02:00:00 EDT 2017'),(3,25,1,'Wed May 17 03:10:00 EDT 2017, Fri May 12 09:15:00 EDT 2017'),(3,26,1,'Thu May 04 02:00:00 EDT 2017'),(3,27,1,'Thu May 04 02:00:00 EDT 2017'),(3,28,1,'Fri May 05 12:30:00 EDT 2017, Fri May 05 01:00:00 EDT 2017'),(3,30,1,'Wed May 17 03:10:00 EDT 2017, Fri May 12 09:15:00 EDT 2017'),(3,35,1,'Tue May 09 02:00:00 EDT 2017, Tue May 09 03:00:00 EDT 2017, Tue May 09 01:00:00 EDT 2017'),(4,18,0,''),(4,25,1,'Sat May 12 09:15:00 EDT 3917'),(4,26,0,''),(4,30,1,'Wed May 09 02:00:00 EDT 3917'),(4,31,1,'Thu May 11 02:00:00 EDT 2017, Thu May 11 03:00:00 EDT 2017, Thu May 11 03:30:00 EDT 2017, Fri May 12 06:00:00 EDT 2017, Fri May 12 06:30:00 EDT 2017'),(4,35,0,''),(5,30,1,'Wed May 09 02:00:00 EDT 3917, Wed May 09 11:00:00 EDT 3917'),(5,31,1,'Sat May 12 06:00:00 EDT 3917, Fri May 11 02:00:00 EDT 3917'),(5,32,1,'Tue May 09 08:00:00 EDT 2017, Tue May 09 01:00:00 EDT 2017, Tue May 09 02:00:00 EDT 2017, Sat May 13 04:00:00 EDT 2017'),(5,35,1,'Tue May 09 08:00:00 EDT 2017, Tue May 09 01:00:00 EDT 2017, Tue May 09 02:00:00 EDT 2017, Sat May 13 04:00:00 EDT 2017'),(6,18,1,'Tue May 08 02:00:00 EDT 3917'),(6,31,1,'Sat May 12 06:00:00 EDT 3917, Sat May 12 06:30:00 EDT 3917'),(6,33,1,'Fri May 12 08:00:00 EDT 2017, Fri May 12 09:00:00 EDT 2017, Sun May 14 10:00:00 EDT 2017, Sun May 14 10:30:00 EDT 2017'),(7,16,0,''),(7,26,0,''),(7,27,0,''),(8,16,0,''),(8,26,0,''),(8,27,0,''),(9,17,1,'Wed May 17 02:11:00 EDT 2017'),(9,18,0,''),(9,22,1,'Wed May 17 02:11:00 EDT 2017'),(9,26,1,''),(10,16,1,'Fri May 04 02:00:00 EDT 3917'),(10,18,1,'Mon May 08 02:00:00 EDT 2017'),(10,22,1,''),(10,26,1,''),(10,27,1,''),(10,28,1,'Sat May 05 01:00:00 EDT 3917'),(10,30,1,'Wed May 09 02:00:00 EDT 3917, Wed May 09 03:00:00 EDT 3917'),(10,33,1,'Sat May 12 08:00:00 EDT 3917, Sat May 12 09:00:00 EDT 3917, Mon May 14 10:00:00 EDT 3917, Mon May 14 10:30:00 EDT 3917'),(10,35,0,''),(11,15,1,''),(11,16,1,'Fri May 04 02:00:00 EDT 3917'),(11,18,0,''),(11,22,1,'Fri May 04 02:00:00 EDT 3917'),(11,25,1,'Thu May 17 03:10:00 EDT 3917, Sat May 12 09:15:00 EDT 3917'),(11,26,0,''),(11,27,0,''),(11,30,0,''),(11,31,1,'Sat May 12 06:00:00 EDT 3917, Fri May 11 02:00:00 EDT 3917, Fri May 11 03:30:00 EDT 3917'),(15,30,1,'Mon May 08 11:00:00 EDT 2017, Mon May 08 11:30:00 EDT 2017, Tue May 09 11:00:00 EDT 2017, Tue May 09 02:00:00 EDT 2017, Tue May 09 03:00:00 EDT 2017'),(15,31,1,'Sat May 12 06:00:00 EDT 3917'),(15,32,0,''),(15,33,0,''),(15,34,1,'Sun May 27 01:00:00 EDT 3917, Fri May 18 02:00:00 EDT 3917'),(15,35,0,''),(16,32,1,'Wed May 09 08:00:00 EDT 3917, Wed May 09 02:00:00 EDT 3917'),(16,34,1,'Thu May 18 02:00:00 EDT 2017, Thu May 18 12:00:00 EDT 2017, Sat May 27 12:00:00 EDT 2017, Sat May 27 01:00:00 EDT 2017'),(16,35,0,'');
/*!40000 ALTER TABLE `Meeting_Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MeetingFile`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MeetingFile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filename` varchar(256) NOT NULL,
  `meeting_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `meeting_id` (`meeting_id`),
  CONSTRAINT `MeetingFile_ibfk_1` FOREIGN KEY (`meeting_id`) REFERENCES `Meeting` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MeetingFile`
--

LOCK TABLES `MeetingFile` WRITE;
/*!40000 ALTER TABLE `MeetingFile` DISABLE KEYS */;
INSERT INTO `MeetingFile` VALUES (4,'16_Team 4 Semester Project Report.docx',16),(5,'16_SoftwareLifeCycle.png',16),(6,'16_CS3984 Java EE Overview.pdf',16),(7,'16_HardAtWork.jpeg',16),(8,'18_Team 4 Semester Project Report.docx',18),(9,'18_meetingMinutes.tar',18),(10,'18_SoftwareLifeCycle.png',18),(11,'34_image.jpg',34),(12,'34_IMG_0008.JPG',34),(13,'32_Avengers.mp4',32),(14,'32_thor.jpg',32),(15,'35_oldSqlScript.sql',35),(16,'30_Team 4 Semester Project Report.docx',30);
/*!40000 ALTER TABLE `MeetingFile` ENABLE KEYS */;
UNLOCK TABLES;




/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-09  6:48:34
