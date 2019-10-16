# ************************************************************
# Sequel Pro SQL dump
# Version 4541
# 感谢issues中网友提供的sql文件
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 39.108.135.185 (MySQL 5.7.23)
# Database: weibo
# Generation Time: 2019-08-01 03:04:52 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table atme
# ------------------------------------------------------------

DROP TABLE IF EXISTS `atme`;

CREATE TABLE `atme` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(500) DEFAULT NULL,
  `time` date DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `wid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table feed
# ------------------------------------------------------------

DROP TABLE IF EXISTS `feed`;

CREATE TABLE `feed` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `data` varchar(4000) DEFAULT NULL,
  `event_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table focus_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `focus_group`;

CREATE TABLE `focus_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table keep
# ------------------------------------------------------------

DROP TABLE IF EXISTS `keep`;

CREATE TABLE `keep` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `time` date DEFAULT NULL,
  `wid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table login_ticket
# ------------------------------------------------------------

DROP TABLE IF EXISTS `login_ticket`;

CREATE TABLE `login_ticket` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `expired` date DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ticket` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table picture
# ------------------------------------------------------------

DROP TABLE IF EXISTS `picture`;

CREATE TABLE `picture` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mini` varchar(500) DEFAULT NULL,
  `medium` varchar(500) DEFAULT NULL,
  `max` varchar(500) DEFAULT NULL,
  `wid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `salt` varchar(50) DEFAULT NULL,
  `registime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table userinfo
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userinfo`;

CREATE TABLE `userinfo` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nickname` varchar(200) DEFAULT NULL,
  `truename` varchar(200) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `constellation` varchar(200) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `face50` varchar(200) DEFAULT NULL,
  `face80` varchar(200) DEFAULT NULL,
  `face180` varchar(200) DEFAULT NULL,
  `style` varchar(200) DEFAULT NULL,
  `followee` bigint(20) DEFAULT NULL,
  `follower` bigint(20) DEFAULT NULL,
  `weibo` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table weibo
# ------------------------------------------------------------

DROP TABLE IF EXISTS `weibo`;

CREATE TABLE `weibo` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) DEFAULT NULL,
  `isturn` int(11) DEFAULT NULL,
  `time` date DEFAULT NULL,
  `turn` int(11) DEFAULT NULL,
  `keep` int(11) DEFAULT NULL,
  `comment` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
