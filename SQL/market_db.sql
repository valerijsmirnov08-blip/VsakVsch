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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.chat_messeges: ~6 rows (приблизительно)
INSERT INTO `chat_messeges` (`id`, `created_at`, `sender`, `text`, `user_id`) VALUES
	(1, '2026-10-28 12:36:49.819142', 'user', 'Как зарегистрироваться?', '14'),
	(2, '2026-10-28 12:36:50.860563', 'bot', 'Для регистрации перейдите на страницу входа и нажмите вкладку \'Регистрация\'. Вам понадобится только email и придумать свой надёжный пароль.', '14'),
	(3, '2026-10-28 12:51:10.593420', 'user', 'Как стать продавцом?', '14'),
	(4, '2026-10-28 12:51:11.597894', 'bot', 'В вашем профиле есть кнопка \'Стать продавцом\'. Нажмите её, заполните реквизиты магазина, и мы проверим вашу заявку за 1 день.', '14'),
	(5, '2026-11-04 20:18:56.413950', 'user', 'Позвать оператора', '14'),
	(6, '2026-11-04 20:18:57.428522', 'bot', 'Перевожу диалог на свободного специалиста. Пожалуйста, подождите... (в разработке)', '14');

-- Дамп структуры для таблица market_db.complaint_category
CREATE TABLE IF NOT EXISTS `complaint_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn5i601t3g49xwedkv2gmmfiyo` (`parent_id`),
  CONSTRAINT `FK4xnhjcduttk0yan50ebmevvvp` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKn5i601t3g49xwedkv2gmmfiyo` FOREIGN KEY (`parent_id`) REFERENCES `complaint_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.complaint_category: ~17 rows (приблизительно)
INSERT INTO `complaint_category` (`id`, `name`, `parent_id`) VALUES
	(1, 'Проблема Товара', 1),
	(2, 'Пересорт', 1),
	(3, 'Не тот цвет или размер', 1),
	(5, 'Неполная комплектация ', 1),
	(6, 'Истекший срок годности', 1),
	(7, 'Проблемы с упаковкой', 1),
	(8, 'Подделка', 1),
	(9, 'Брак товара', 1),
	(10, 'Проблема с доставкой', 2),
	(11, 'Не соответсвие карточки товара', 3),
	(12, 'Доставка опаздала', 2),
	(13, 'Повреждение товара', 2),
	(14, 'Не соответсвие фото товара', 3),
	(15, 'Не соответсвие описанию товара', 3),
	(16, 'Другое', 1),
	(18, 'Другое', 2),
	(19, 'Другое', 3);

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.favorite: ~2 rows (приблизительно)
INSERT INTO `favorite` (`id`, `product_id`, `user_id`) VALUES
	(15, 2, 14),
	(16, 5, 2);

-- Дамп структуры для таблица market_db.month_stat
CREATE TABLE IF NOT EXISTS `month_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `stat_date` date NOT NULL,
  `sales_сount` int(255) NOT NULL,
  `reviews_сount` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.month_stat: ~9 rows (приблизительно)
INSERT INTO `month_stat` (`id`, `product_id`, `stat_date`, `sales_сount`, `reviews_сount`) VALUES
	(4, 4, '2026-11-03', 1, 1),
	(5, 4, '2026-11-04', 2, 3),
	(6, 2, '2026-11-04', 6, 3),
	(7, 5, '2026-11-04', 3, 3),
	(8, 1, '2026-11-04', 3, 3),
	(9, 2, '2026-11-05', 0, 1),
	(10, 4, '2026-11-05', 1, 1),
	(11, 5, '2026-11-05', 2, 2),
	(12, 4, '2026-11-06', 2, 2);

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
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.order_items: ~5 rows (приблизительно)
INSERT INTO `order_items` (`id`, `price`, `quantity`, `status`, `vendor_id`, `order_id`, `product_id`, `is_reviewed`) VALUES
	(27, 1600.00, 1, 'Доставлен', 14, 23, 4, 1),
	(28, 900.00, 1, 'Доставлен', 14, 24, 5, 1),
	(29, 900.00, 1, 'Доставлен', 14, 25, 5, 1),
	(30, 1600.00, 1, 'Доставлен', 14, 26, 4, 1),
	(31, 1600.00, 1, 'Доставлен', 14, 27, 4, 1);

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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.orders: ~5 rows (приблизительно)
INSERT INTO `orders` (`id`, `created_at`, `status`, `total_price`, `user_id`) VALUES
	(23, '2026-11-05 20:31:38.888904', 'Доставлен', 1600.00, 14),
	(24, '2026-11-05 21:09:13.192243', 'Доставлен', 900.00, 14),
	(25, '2026-11-05 23:16:38.239248', 'Доставлен', 900.00, 14),
	(26, '2026-11-06 00:44:38.537547', 'Доставлен', 1600.00, 14),
	(27, '2026-11-06 00:56:29.524189', 'В процессе', 1600.00, 14);

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
  `category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1cf90etcu98x1e6n9aks3tel3` (`category_id`),
  FULLTEXT KEY `ft_search_idx` (`model`,`vendor_sku`),
  CONSTRAINT `FK1cf90etcu98x1e6n9aks3tel3` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.products: ~4 rows (приблизительно)
INSERT INTO `products` (`id`, `brand`, `description`, `estimated_delivery`, `main_image`, `model`, `price`, `rating`, `vendor_id`, `vendor_sku`, `warranty`, `gallery`, `quantity`, `status`, `category_id`) VALUES
	(1, 'Nivea men', 'Отличный мужской шампунь для глубокого очищения и свежести на весь день.', '9 августа', '/шампунь.jpg', 'Шампунь 500мл', 450.00, 1.3, 10, '45782', '1 год', NULL, 8, NULL, NULL),
	(2, 'Garnier', 'Крепкие и здоровые волосы от самых корней. Эффективно устраняет повреждения.', 'Завтра', '/шампунь_2.jpg', 'Fructis SOS Восстановление', 380.00, 3.5, 12, '33910', 'Нет', NULL, 3, NULL, NULL),
	(4, 'PUMA', 'Хорошая худи сам бы носил', '10 дней', '/images.jfif', 'Пума Худи XL', 1600.00, 2.3, 14, '14', '2 года', '/images_(1).jfif', 99, NULL, NULL),
	(5, 'Машинка', 'Круто', '9 августа', '/images_(2).jfif', 'Машинка Синяя', 900.00, 1.5, 14, '2352315', '1 год', '/images_(2).jfif', 700, NULL, NULL);

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

-- Дамп структуры для таблица market_db.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `order_item_id` bigint(20) DEFAULT NULL,
  `is_complaint` bit(1) DEFAULT NULL,
  `complaint_status` varchar(250) DEFAULT 'UNPROCESSED',
  `complaint_text` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2x2x74lnliqmt91bc1w95ll8n` (`order_item_id`),
  CONSTRAINT `FK2x2x74lnliqmt91bc1w95ll8n` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.reviews: ~5 rows (приблизительно)
INSERT INTO `reviews` (`id`, `comment`, `created_at`, `product_id`, `rating`, `user_id`, `is_read`, `order_item_id`, `is_complaint`, `complaint_status`, `complaint_text`) VALUES
	(63, 'Всё хорошо', '2026-11-05 20:32:09.220014', 4, 4, 14, b'1', 27, b'0', 'UNPROCESSED', NULL),
	(64, '23еа23к213к', '2026-11-05 21:09:57.443435', 5, 3, 14, b'0', 28, b'1', 'IN_PROGRESS', NULL),
	(65, 'Всё...', '2026-11-05 23:21:30.541641', 5, 0, 14, b'0', 29, b'1', 'IN_PROGRESS', ''),
	(66, 'Не то не это всё не так', '2026-11-06 00:45:34.245525', 4, 0, 14, b'0', 30, b'1', 'UNPROCESSED', ''),
	(67, 'Всё короче я спать', '2026-11-06 00:57:05.575361', 4, 3, 14, b'0', 31, b'1', 'UNPROCESSED', '');

-- Дамп структуры для таблица market_db.reviews_photo
CREATE TABLE IF NOT EXISTS `reviews_photo` (
  `reviews_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKsf31h1yvq1t2euaurucfj7fyc` (`reviews_id`),
  CONSTRAINT `FKsf31h1yvq1t2euaurucfj7fyc` FOREIGN KEY (`reviews_id`) REFERENCES `reviews` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Дамп данных таблицы market_db.reviews_photo: ~4 rows (приблизительно)
INSERT INTO `reviews_photo` (`reviews_id`, `id`, `url`) VALUES
	(63, 42, 'Снимок экрана 2026-11-05 203114.png'),
	(65, 43, 'Снимок экрана 2026-11-05 231745.png'),
	(66, 44, 'Снимок экрана 2026-11-05 231745.png'),
	(67, 45, 'Снимок экрана 2026-11-06 005237.png');

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
