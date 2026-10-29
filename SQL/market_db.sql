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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.cart_items: ~0 rows (приблизительно)

-- Дамп структуры для таблица market_db.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2y94svpmqttx80mshyny85wqr` (`parent_id`),
  CONSTRAINT `FK2y94svpmqttx80mshyny85wqr` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.category: ~11 rows (приблизительно)
INSERT INTO `category` (`id`, `name`, `parent_id`) VALUES
	(1, 'Электроника', NULL),
	(2, 'Одежда', NULL),
	(3, 'Смартфоны', 1),
	(4, 'Куртки', 2),
	(5, 'Компьютеры и комплектующие', NULL),
	(6, 'Смартфоны и гаджеты', NULL),
	(7, 'Бытовая техника', NULL),
	(8, 'Спорт и отдых', NULL),
	(9, 'Автотовары', NULL),
	(10, 'Красота и здоровье', NULL),
	(11, 'Товары для дома', NULL);

-- Дамп структуры для таблица market_db.chat_messeges
CREATE TABLE IF NOT EXISTS `chat_messeges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `sender` varchar(255) NOT NULL,
  `text` varchar(1000) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_chat_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.chat_messeges: ~4 rows (приблизительно)
INSERT INTO `chat_messeges` (`id`, `created_at`, `sender`, `text`, `user_id`) VALUES
	(1, '2026-10-28 12:36:49.819142', 'user', 'Как зарегистрироваться?', '14'),
	(2, '2026-10-28 12:36:50.860563', 'bot', 'Для регистрации перейдите на страницу входа и нажмите вкладку \'Регистрация\'. Вам понадобится только email и придумать свой надёжный пароль.', '14'),
	(3, '2026-10-28 12:51:10.593420', 'user', 'Как стать продавцом?', '14'),
	(4, '2026-10-28 12:51:11.597894', 'bot', 'В вашем профиле есть кнопка \'Стать продавцом\'. Нажмите её, заполните реквизиты магазина, и мы проверим вашу заявку за 1 день.', '14');

-- Дамп структуры для таблица market_db.favorite
CREATE TABLE IF NOT EXISTS `favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrd09pqm0vasylahjcnagd0c7o` (`product_id`),
  KEY `FKa2lwa7bjrnbti5v12mga2et1y` (`user_id`),
  CONSTRAINT `FKa2lwa7bjrnbti5v12mga2et1y` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrd09pqm0vasylahjcnagd0c7o` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.favorite: ~1 rows (приблизительно)
INSERT INTO `favorite` (`id`, `product_id`, `user_id`) VALUES
	(15, 2, 14);

-- Дамп структуры для таблица market_db.month_stat
CREATE TABLE IF NOT EXISTS `month_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `month_label` varchar(255) NOT NULL,
  `sales_сount` int(255) NOT NULL,
  `reviews_сount` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.month_stat: ~3 rows (приблизительно)
INSERT INTO `month_stat` (`id`, `product_id`, `month_label`, `sales_сount`, `reviews_сount`) VALUES
	(1, 1, '2026-10', 2, 0),
	(2, 4, '2026-10', 6, 0),
	(3, 2, '2026-10', 1, 0);

-- Дамп структуры для таблица market_db.order_items
CREATE TABLE IF NOT EXISTS `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `status` varchar(255) NOT NULL,
  `vendor_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `is_reviewed` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  KEY `idx_orderItem_vendor_id` (`vendor_id`),
  KEY `idx_orderItem_order_id` (`order_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.order_items: ~3 rows (приблизительно)
INSERT INTO `order_items` (`id`, `price`, `quantity`, `status`, `vendor_id`, `order_id`, `product_id`, `is_reviewed`) VALUES
	(12, 380.00, 1, 'В процессе', 12, 10, 2, 0),
	(13, 1600.00, 1, 'Выполнен', 14, 11, 4, 0),
	(14, 1600.00, 1, 'Выполнен', 14, 12, 4, 0);

-- Дамп структуры для таблица market_db.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_price` decimal(38,2) NOT NULL,
  `user_id` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`) USING BTREE,
  KEY `idx_orders_user_id` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.orders: ~12 rows (приблизительно)
INSERT INTO `orders` (`id`, `created_at`, `status`, `total_price`, `user_id`) VALUES
	(1, '2026-10-15 18:33:52.575286', 'Выполнен', 1600.00, 14),
	(2, '2026-10-16 21:43:05.460838', 'Выполнен', 1600.00, 2),
	(3, '2026-10-16 21:53:26.163205', 'Выполнен', 1600.00, 2),
	(4, '2026-10-16 23:19:21.354213', 'В процессе', 2360.00, 2),
	(5, '2026-10-20 23:32:04.967280', 'В процессе', 2050.00, 14),
	(6, '2026-10-20 23:33:28.188437', 'В процессе', 450.00, 14),
	(7, '2026-10-25 16:51:50.424406', 'В процессе', 1600.00, 14),
	(8, '2026-10-25 17:21:43.380479', 'В процессе', 1600.00, 13),
	(9, '2026-10-25 17:26:25.035396', 'В процессе', 1600.00, 14),
	(10, '2026-10-25 17:27:08.162755', 'В процессе', 380.00, 14),
	(11, '2026-10-28 22:43:25.800023', 'В процессе', 1600.00, 14),
	(12, '2026-10-29 11:30:24.310737', 'В процессе', 1600.00, 14);

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
  `quantity` int(11) DEFAULT 0,
  `status` varchar(255) DEFAULT NULL,
  `category` bigint(20) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1cf90etcu98x1e6n9aks3tel3` (`category_id`),
  FULLTEXT KEY `ft_search_idx` (`model`,`vendor_sku`),
  CONSTRAINT `FK1cf90etcu98x1e6n9aks3tel3` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.products: ~4 rows (приблизительно)
INSERT INTO `products` (`id`, `brand`, `description`, `estimated_delivery`, `main_image`, `model`, `price`, `rating`, `vendor_id`, `vendor_sku`, `warranty`, `gallery`, `quantity`, `status`, `category`, `category_id`) VALUES
	(1, 'Nivea men', 'Отличный мужской шампунь для глубокого очищения и свежести на весь день.', '9 августа', '/шампунь.jpg', 'Шампунь 500мл', 450.00, 4.1, 10, '45782', '1 год', NULL, 11, NULL, 0, NULL),
	(2, 'Garnier', 'Крепкие и здоровые волосы от самых корней. Эффективно устраняет повреждения.', 'Завтра', '/шампунь_2.jpg', 'Fructis SOS Восстановление', 380.00, 5, 12, '33910', 'Нет', NULL, 9, NULL, 0, NULL),
	(4, 'PUMA', 'Хорошая худи сам бы носил', '10 дней', '/images.jfif', 'Пума Худи XL', 1600.00, 1.6, 14, '14', '2 года', '/images_(1).jfif', 5, NULL, 0, NULL),
	(5, 'Машинка', 'Круто', '9 августа', '/images_(2).jfif', 'Машинка Синяя', 900.00, 0, 14, '2352315', '1 год', '/images_(2).jfif', 11, NULL, 0, NULL);

-- Дамп структуры для таблица market_db.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.reviews: ~13 rows (приблизительно)
INSERT INTO `reviews` (`id`, `comment`, `created_at`, `product_id`, `rating`, `user_id`) VALUES
	(1, 'Всё хорошо', '2026-10-16 20:48:02.747779', 4, 4, 14),
	(16, '\nНорм', '2026-10-16 22:50:11.388119', 4, 0, 2),
	(17, 'норм 2', '2026-10-16 22:50:18.430478', 4, 0, 2),
	(18, '23t23t32t13', '2026-10-16 23:18:24.561021', 4, 0, 2),
	(19, '23t23t32t13', '2026-10-16 23:18:25.493269', 4, 0, 2),
	(20, '23t23t32t13', '2026-10-16 23:18:33.158005', 4, 0, 2),
	(21, 'wegwehgwqeh2wh', '2026-10-16 23:19:40.909205', 2, 5, 2),
	(22, 'wegwehgwqeh2wh', '2026-10-16 23:19:41.644651', 2, 5, 2),
	(23, 'wegwehgwqeh2wh', '2026-10-16 23:19:41.812194', 2, 5, 2),
	(24, 'цун34цн34н24', '2026-10-18 21:18:16.669386', 4, 0, 2),
	(25, 'eagqgeq3g', '2026-10-18 21:20:12.287060', 4, 4, 2),
	(26, 'eagqgeq3g', '2026-10-18 21:20:43.174865', 4, 4, 2),
	(27, 'пйпйфпйцп3йц3п', '2026-10-18 21:23:09.055322', 4, 4, 2);

-- Дамп структуры для таблица market_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_description` varchar(255) DEFAULT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `is_vendor` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `brand_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKk8d0f2n7n88w1a16yhua64onx` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.users: ~5 rows (приблизительно)
INSERT INTO `users` (`id`, `shop_description`, `shop_name`, `email`, `is_vendor`, `password`, `user_name`, `brand_name`) VALUES
	(1, NULL, NULL, '', b'0', '', '', NULL),
	(2, NULL, 'wqegwqeg', 'valerijsmirnov08@gmail.com', b'1', '123', 'цапйу', NULL),
	(12, NULL, 'wegtwqeg', 'testemail@gmail.ru', b'1', '1234', 'уко4уоу', NULL),
	(13, NULL, NULL, 'test2email@gmail.ru', b'0', '12345', 'fher', NULL),
	(14, 'Лучший магазин во всей мульти-вселенной', 'Мироходец-Маркет', 'ght@gmail.com', b'1', '123456', 'JDH', 'Mirohodec');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
