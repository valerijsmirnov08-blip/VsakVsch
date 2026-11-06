-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               11.8.6-MariaDB - MariaDB Server
-- Операционная система:         Win64
-- HeidiSQL Версия:              12.14.0.7165
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Дамп структуры базы данных market_db
CREATE DATABASE IF NOT EXISTS `market_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `market_db`;

-- Дамп структуры для таблица market_db.review_complaint_categories
CREATE TABLE IF NOT EXISTS `review_complaint_categories` (
  `review_id` bigint(20) NOT NULL,
  `complaint_category_id` bigint(20) NOT NULL,
  KEY `FKr9eectr982d2ay1k9af5lxohd` (`complaint_category_id`),
  KEY `FKkasvvmvbvk318awpde8y584x3` (`review_id`),
  CONSTRAINT `FKkasvvmvbvk318awpde8y584x3` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`),
  CONSTRAINT `FKr9eectr982d2ay1k9af5lxohd` FOREIGN KEY (`complaint_category_id`) REFERENCES `complaint_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.review_complaint_categories: ~8 rows (приблизительно)
INSERT INTO `review_complaint_categories` (`review_id`, `complaint_category_id`) VALUES
	(64, 3),
	(65, 5),
	(66, 2),
	(66, 3),
	(66, 5),
	(67, 3),
	(67, 5),
	(67, 6);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
