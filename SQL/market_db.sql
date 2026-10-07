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

-- Дамп структуры для таблица market_db.cart_items
CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.cart_items: ~1 rows (приблизительно)
INSERT INTO `cart_items` (`id`, `user_id`, `product_id`, `quantity`) VALUES
	(19, 14, 4, 1);

-- Дамп структуры для таблица market_db.products
CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `estimated_delivery` varchar(255) DEFAULT NULL,
  `main_image` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `vendor_id` bigint(20) NOT NULL,
  `vendor_sku` varchar(255) NOT NULL,
  `warranty` varchar(255) DEFAULT NULL,
  `gallery` text DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.products: ~4 rows (приблизительно)
INSERT INTO `products` (`id`, `brand`, `description`, `estimated_delivery`, `main_image`, `model`, `price`, `rating`, `vendor_id`, `vendor_sku`, `warranty`, `gallery`, `status`, `quantity`) VALUES
	(1, 'Nivea men', 'Отличный мужской шампунь для глубокого очищения и свежести на весь день.', '9 августа', '/шампунь.jpg', 'Шампунь 500мл', 450.00, 4.1, 10, '45782', '1 год', NULL, 'Не оформлен', 0),
	(2, 'Garnier', 'Крепкие и здоровые волосы от самых корней. Эффективно устраняет повреждения.', 'Завтра', '/шампунь_2.jpg', 'Fructis SOS Восстановление', 380.00, 4.7, 12, '33910', 'Нет', NULL, 'Не оформлен', 0),
	(4, 'PUMA', 'Хорошая худи сам бы носил', '9 августа', '/images.jfif', 'Пума Худи XL', 1500.00, 0, 14, '14', '1 год', '/images_(1).jfif', 'Не оформлен', 0),
	(5, 'Машинка', 'Круто', '9 августа', '/images_(2).jfif', 'Машинка Синяя', 900.00, 0, 14, '2352315', '1 год', '/images_(2).jfif', 'Не оформлен', 0);

-- Дамп структуры для таблица market_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_description` varchar(255) DEFAULT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `is_vendor` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKk8d0f2n7n88w1a16yhua64onx` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.users: ~3 rows (приблизительно)
INSERT INTO `users` (`id`, `shop_description`, `shop_name`, `email`, `is_vendor`, `password`, `user_name`) VALUES
	(1, NULL, NULL, '', b'0', '', ''),
	(2, NULL, 'wqegwqeg', 'valerijsmirnov08@gmail.com', b'1', '123', 'цапйу'),
	(12, NULL, 'wegtwqeg', 'testemail@gmail.ru', b'1', '1234', 'уко4уоу'),
	(13, NULL, 'sgwegwqg', 'test2email@gmail.ru', b'1', '12345', 'fher'),
	(14, NULL, 'МироходецМаркет', 'ght@gmail.com', b'1', '123456', 'JDH');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
