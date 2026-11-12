-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               PostgreSQL 18.6 on x86_64-windows, compiled by msvc-19.44.35229, 64-bit
-- Операционная система:         
-- HeidiSQL Версия:              12.14.0.7165
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Дамп структуры для функция public.gin_extract_query_trgm
DELIMITER //
CREATE FUNCTION "gin_extract_query_trgm"() RETURNS UNKNOWN AS $$ gin_extract_query_trgm $$//
DELIMITER ;

-- Дамп структуры для функция public.gin_extract_value_trgm
DELIMITER //
CREATE FUNCTION "gin_extract_value_trgm"() RETURNS UNKNOWN AS $$ gin_extract_value_trgm $$//
DELIMITER ;

-- Дамп структуры для функция public.gin_trgm_consistent
DELIMITER //
CREATE FUNCTION "gin_trgm_consistent"() RETURNS BOOLEAN AS $$ gin_trgm_consistent $$//
DELIMITER ;

-- Дамп структуры для функция public.gin_trgm_triconsistent
DELIMITER //
CREATE FUNCTION "gin_trgm_triconsistent"() RETURNS CHAR AS $$ gin_trgm_triconsistent $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_compress
DELIMITER //
CREATE FUNCTION "gtrgm_compress"() RETURNS UNKNOWN AS $$ gtrgm_compress $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_consistent
DELIMITER //
CREATE FUNCTION "gtrgm_consistent"() RETURNS BOOLEAN AS $$ gtrgm_consistent $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_decompress
DELIMITER //
CREATE FUNCTION "gtrgm_decompress"() RETURNS UNKNOWN AS $$ gtrgm_decompress $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_distance
DELIMITER //
CREATE FUNCTION "gtrgm_distance"() RETURNS DOUBLE PRECISION AS $$ gtrgm_distance $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_in
DELIMITER //
CREATE FUNCTION "gtrgm_in"() RETURNS UNKNOWN AS $$ gtrgm_in $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_options
DELIMITER //
CREATE FUNCTION "gtrgm_options"() RETURNS UNKNOWN AS $$ gtrgm_options $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_out
DELIMITER //
CREATE FUNCTION "gtrgm_out"() RETURNS UNKNOWN AS $$ gtrgm_out $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_penalty
DELIMITER //
CREATE FUNCTION "gtrgm_penalty"() RETURNS UNKNOWN AS $$ gtrgm_penalty $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_picksplit
DELIMITER //
CREATE FUNCTION "gtrgm_picksplit"() RETURNS UNKNOWN AS $$ gtrgm_picksplit $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_same
DELIMITER //
CREATE FUNCTION "gtrgm_same"() RETURNS UNKNOWN AS $$ gtrgm_same $$//
DELIMITER ;

-- Дамп структуры для функция public.gtrgm_union
DELIMITER //
CREATE FUNCTION "gtrgm_union"() RETURNS UNKNOWN AS $$ gtrgm_union $$//
DELIMITER ;

-- Дамп структуры для функция public.set_limit
DELIMITER //
CREATE FUNCTION "set_limit"() RETURNS REAL AS $$ set_limit $$//
DELIMITER ;

-- Дамп структуры для функция public.show_limit
DELIMITER //
CREATE FUNCTION "show_limit"() RETURNS REAL AS $$ show_limit $$//
DELIMITER ;

-- Дамп структуры для функция public.show_trgm
DELIMITER //
CREATE FUNCTION "show_trgm"() RETURNS UNKNOWN AS $$ show_trgm $$//
DELIMITER ;

-- Дамп структуры для функция public.similarity
DELIMITER //
CREATE FUNCTION "similarity"() RETURNS REAL AS $$ similarity $$//
DELIMITER ;

-- Дамп структуры для функция public.similarity_dist
DELIMITER //
CREATE FUNCTION "similarity_dist"() RETURNS REAL AS $$ similarity_dist $$//
DELIMITER ;

-- Дамп структуры для функция public.similarity_op
DELIMITER //
CREATE FUNCTION "similarity_op"() RETURNS BOOLEAN AS $$ similarity_op $$//
DELIMITER ;

-- Дамп структуры для функция public.strict_word_similarity
DELIMITER //
CREATE FUNCTION "strict_word_similarity"() RETURNS REAL AS $$ strict_word_similarity $$//
DELIMITER ;

-- Дамп структуры для функция public.strict_word_similarity_commutator_op
DELIMITER //
CREATE FUNCTION "strict_word_similarity_commutator_op"() RETURNS BOOLEAN AS $$ strict_word_similarity_commutator_op $$//
DELIMITER ;

-- Дамп структуры для функция public.strict_word_similarity_dist_commutator_op
DELIMITER //
CREATE FUNCTION "strict_word_similarity_dist_commutator_op"() RETURNS REAL AS $$ strict_word_similarity_dist_commutator_op $$//
DELIMITER ;

-- Дамп структуры для функция public.strict_word_similarity_dist_op
DELIMITER //
CREATE FUNCTION "strict_word_similarity_dist_op"() RETURNS REAL AS $$ strict_word_similarity_dist_op $$//
DELIMITER ;

-- Дамп структуры для функция public.strict_word_similarity_op
DELIMITER //
CREATE FUNCTION "strict_word_similarity_op"() RETURNS BOOLEAN AS $$ strict_word_similarity_op $$//
DELIMITER ;

-- Дамп структуры для функция public.word_similarity
DELIMITER //
CREATE FUNCTION "word_similarity"() RETURNS REAL AS $$ word_similarity $$//
DELIMITER ;

-- Дамп структуры для функция public.word_similarity_commutator_op
DELIMITER //
CREATE FUNCTION "word_similarity_commutator_op"() RETURNS BOOLEAN AS $$ word_similarity_commutator_op $$//
DELIMITER ;

-- Дамп структуры для функция public.word_similarity_dist_commutator_op
DELIMITER //
CREATE FUNCTION "word_similarity_dist_commutator_op"() RETURNS REAL AS $$ word_similarity_dist_commutator_op $$//
DELIMITER ;

-- Дамп структуры для функция public.word_similarity_dist_op
DELIMITER //
CREATE FUNCTION "word_similarity_dist_op"() RETURNS REAL AS $$ word_similarity_dist_op $$//
DELIMITER ;

-- Дамп структуры для функция public.word_similarity_op
DELIMITER //
CREATE FUNCTION "word_similarity_op"() RETURNS BOOLEAN AS $$ word_similarity_op $$//
DELIMITER ;

-- Дамп структуры для таблица public.cart_items
CREATE TABLE IF NOT EXISTS "cart_items" (
	"id" BIGINT NOT NULL,
	"quantity" INTEGER NOT NULL,
	"user_id" BIGINT NOT NULL,
	"product_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk1re40cjegsfvw58xrkdp6bac6" FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.cart_items: 0 rows
/*!40000 ALTER TABLE "cart_items" DISABLE KEYS */;
/*!40000 ALTER TABLE "cart_items" ENABLE KEYS */;

-- Дамп структуры для таблица public.category
CREATE TABLE IF NOT EXISTS "category" (
	"id" BIGINT NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"parent_id" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk2y94svpmqttx80mshyny85wqr" FOREIGN KEY ("parent_id") REFERENCES "category" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.category: 2 rows
/*!40000 ALTER TABLE "category" DISABLE KEYS */;
INSERT INTO "category" ("id", "name", "parent_id") VALUES
	(1, 'Электроника', NULL),
	(2, 'Одежда', NULL);
/*!40000 ALTER TABLE "category" ENABLE KEYS */;

-- Дамп структуры для таблица public.chat_messeges
CREATE TABLE IF NOT EXISTS "chat_messeges" (
	"id" BIGINT NOT NULL,
	"created_at" TIMESTAMP NULL DEFAULT NULL,
	"sender" VARCHAR(255) NOT NULL,
	"text" VARCHAR(1000) NOT NULL,
	"user_id" VARCHAR(255) NOT NULL,
	PRIMARY KEY ("id")
)
CREATE INDEX "idx_chat_user_id" ON "" ("user_id");;

-- Дамп данных таблицы public.chat_messeges: 0 rows
/*!40000 ALTER TABLE "chat_messeges" DISABLE KEYS */;
/*!40000 ALTER TABLE "chat_messeges" ENABLE KEYS */;

-- Дамп структуры для таблица public.complaint_category
CREATE TABLE IF NOT EXISTS "complaint_category" (
	"id" BIGINT NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"parent_id" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkn5i601t3g49xwedkv2gmmfiyo" FOREIGN KEY ("parent_id") REFERENCES "complaint_category" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.complaint_category: 0 rows
/*!40000 ALTER TABLE "complaint_category" DISABLE KEYS */;
/*!40000 ALTER TABLE "complaint_category" ENABLE KEYS */;

-- Дамп структуры для таблица public.favorite
CREATE TABLE IF NOT EXISTS "favorite" (
	"id" BIGINT NOT NULL,
	"product_id" BIGINT NOT NULL,
	"user_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fka2lwa7bjrnbti5v12mga2et1y" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkrd09pqm0vasylahjcnagd0c7o" FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.favorite: 1 rows
/*!40000 ALTER TABLE "favorite" DISABLE KEYS */;
INSERT INTO "favorite" ("id", "product_id", "user_id") VALUES
	(2, 12, 1);
/*!40000 ALTER TABLE "favorite" ENABLE KEYS */;

-- Дамп структуры для таблица public.month_stat
CREATE TABLE IF NOT EXISTS "month_stat" (
	"id" BIGINT NOT NULL,
	"product_id" BIGINT NOT NULL,
	"reviews_сount" INTEGER NOT NULL,
	"sales_сount" INTEGER NOT NULL,
	"stat_date" DATE NOT NULL,
	PRIMARY KEY ("id")
);

-- Дамп данных таблицы public.month_stat: 1 rows
/*!40000 ALTER TABLE "month_stat" DISABLE KEYS */;
INSERT INTO "month_stat" ("id", "product_id", "reviews_сount", "sales_сount", "stat_date") VALUES
	(1, 12, 1, 0, '2026-11-12');
/*!40000 ALTER TABLE "month_stat" ENABLE KEYS */;

-- Дамп структуры для таблица public.order_items
CREATE TABLE IF NOT EXISTS "order_items" (
	"id" BIGINT NOT NULL,
	"is_reviewed" BOOLEAN NULL DEFAULT NULL,
	"price" NUMERIC(38,2) NOT NULL,
	"quantity" INTEGER NOT NULL,
	"status" VARCHAR(255) NOT NULL,
	"vendor_id" BIGINT NOT NULL,
	"order_id" BIGINT NOT NULL,
	"product_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkbioxgbv59vetrxe0ejfubep1w" FOREIGN KEY ("order_id") REFERENCES "orders" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkocimc7dtr037rh4ls4l95nlfi" FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
)
CREATE INDEX "idx_orderitem_vendor_id" ON "" ("vendor_id");
CREATE INDEX "idx_orderitem_order_id" ON "" ("order_id");;

-- Дамп данных таблицы public.order_items: 2 rows
/*!40000 ALTER TABLE "order_items" DISABLE KEYS */;
INSERT INTO "order_items" ("id", "is_reviewed", "price", "quantity", "status", "vendor_id", "order_id", "product_id") VALUES
	(4, 'true', 32541235.00, 1, 'Доставлен', 1, 4, 12),
	(5, 'false', 32541235.00, 1, 'В процессе', 1, 5, 12);
/*!40000 ALTER TABLE "order_items" ENABLE KEYS */;

-- Дамп структуры для таблица public.orders
CREATE TABLE IF NOT EXISTS "orders" (
	"id" BIGINT NOT NULL,
	"created_at" TIMESTAMP NOT NULL,
	"status" VARCHAR(255) NOT NULL,
	"total_price" NUMERIC(38,2) NOT NULL,
	"user_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk32ql8ubntj5uh44ph9659tiih" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
)
CREATE INDEX "idx_orders_user_id" ON "" ("user_id");;

-- Дамп данных таблицы public.orders: 2 rows
/*!40000 ALTER TABLE "orders" DISABLE KEYS */;
INSERT INTO "orders" ("id", "created_at", "status", "total_price", "user_id") VALUES
	(4, '2026-11-12 22:26:56.113295', 'Доставлен', 32541235.00, 1),
	(5, '2026-11-12 23:04:36.365635', 'В процессе', 32541235.00, 1);
/*!40000 ALTER TABLE "orders" ENABLE KEYS */;

-- Дамп структуры для таблица public.products
CREATE TABLE IF NOT EXISTS "products" (
	"id" BIGINT NOT NULL,
	"brand" VARCHAR(255) NULL DEFAULT NULL,
	"price" NUMERIC(38,2) NULL DEFAULT NULL,
	"quantity" INTEGER NOT NULL,
	"vendor_id" BIGINT NOT NULL,
	"warranty" VARCHAR(255) NULL DEFAULT NULL,
	"category_id" BIGINT NULL DEFAULT NULL,
	"details" JSONB NULL DEFAULT NULL,
	"model" VARCHAR(255) NULL DEFAULT NULL,
	"description" VARCHAR(255) NULL DEFAULT NULL,
	"rating" DOUBLE PRECISION NULL DEFAULT NULL,
	"vendor_sku" VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	UNIQUE ("vendor_id", "vendor_sku"),
	CONSTRAINT "fk1cf90etcu98x1e6n9aks3tel3" FOREIGN KEY ("category_id") REFERENCES "category" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fklag4lc2yiaaehog28yd6lsoao" FOREIGN KEY ("vendor_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
)
CREATE INDEX "idx_products_brand_trgm" ON "" ("brand");
CREATE INDEX "idx_products_model_trgm" ON "" ("model");
CREATE INDEX "idx_products_vendor_sku_trgm" ON "" ("vendor_sku");;

-- Дамп данных таблицы public.products: 1 rows
/*!40000 ALTER TABLE "products" DISABLE KEYS */;
INSERT INTO "products" ("id", "brand", "price", "quantity", "vendor_id", "warranty", "category_id", "details", "model", "description", "rating", "vendor_sku") VALUES
	(12, 'agweg', 32541235.00, 8, 1, '1 месяц', 1, '{"os": "wefgtweqgtfwaegt", "color": "Красный", "memory": "64 ГБ", "colorHex": "#dc2626", "warranty": "1 Год", "mainImage": "Пума продукт 1.1.jpg", "colorCustomName": "", "estimatedDelivery": "1 день"}', 'sageg', 'пывпывп', 0, '121');
/*!40000 ALTER TABLE "products" ENABLE KEYS */;

-- Дамп структуры для таблица public.review_complaint_categories
CREATE TABLE IF NOT EXISTS "review_complaint_categories" (
	"review_id" BIGINT NOT NULL,
	"complaint_category_id" BIGINT NOT NULL,
	CONSTRAINT "fkkasvvmvbvk318awpde8y584x3" FOREIGN KEY ("review_id") REFERENCES "reviews" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkr9eectr982d2ay1k9af5lxohd" FOREIGN KEY ("complaint_category_id") REFERENCES "complaint_category" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.review_complaint_categories: 0 rows
/*!40000 ALTER TABLE "review_complaint_categories" DISABLE KEYS */;
/*!40000 ALTER TABLE "review_complaint_categories" ENABLE KEYS */;

-- Дамп структуры для таблица public.reviews
CREATE TABLE IF NOT EXISTS "reviews" (
	"id" BIGINT NOT NULL,
	"comment" VARCHAR(255) NULL DEFAULT NULL,
	"complaint_status" VARCHAR(255) NULL DEFAULT NULL,
	"complaint_text" TEXT NULL DEFAULT NULL,
	"created_at" TIMESTAMP NOT NULL,
	"is_complaint" BOOLEAN NOT NULL,
	"is_read" BOOLEAN NULL DEFAULT NULL,
	"product_id" BIGINT NOT NULL,
	"rating" INTEGER NOT NULL,
	"user_id" BIGINT NOT NULL,
	"order_item_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk2x2x74lnliqmt91bc1w95ll8n" FOREIGN KEY ("order_item_id") REFERENCES "order_items" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "reviews_complaint_status_check" CHECK (((complaint_status)::text = ANY ((ARRAY['UNPROCESSED'::character varying, 'IN_PROGRESS'::character varying, 'RESOLVED_VENDOR'::character varying, 'RESOLVED_CUSTOMER'::character varying, 'WITHDRAWN_BY_CUSTOMER'::character varying, 'ESCALATED'::character varying])::text[])))
);

-- Дамп данных таблицы public.reviews: 1 rows
/*!40000 ALTER TABLE "reviews" DISABLE KEYS */;
INSERT INTO "reviews" ("id", "comment", "complaint_status", "complaint_text", "created_at", "is_complaint", "is_read", "product_id", "rating", "user_id", "order_item_id") VALUES
	(1, 'Всё хорошо', NULL, NULL, '2026-11-12 22:38:19.797375', 'false', 'false', 12, 4, 1, 4);
/*!40000 ALTER TABLE "reviews" ENABLE KEYS */;

-- Дамп структуры для таблица public.reviews_photo
CREATE TABLE IF NOT EXISTS "reviews_photo" (
	"id" BIGINT NOT NULL,
	"url" VARCHAR(255) NULL DEFAULT NULL,
	"reviews_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fksf31h1yvq1t2euaurucfj7fyc" FOREIGN KEY ("reviews_id") REFERENCES "reviews" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.reviews_photo: 0 rows
/*!40000 ALTER TABLE "reviews_photo" DISABLE KEYS */;
/*!40000 ALTER TABLE "reviews_photo" ENABLE KEYS */;

-- Дамп структуры для таблица public.users
CREATE TABLE IF NOT EXISTS "users" (
	"id" SERIAL NOT NULL,
	"user_name" VARCHAR(255) NOT NULL,
	"email" VARCHAR(255) NOT NULL,
	"password" VARCHAR(255) NOT NULL,
	"role" VARCHAR(255) NOT NULL,
	"phone" VARCHAR(255) NULL DEFAULT NULL,
	"hired" BIGINT NULL DEFAULT NULL,
	"is_banned" BOOLEAN NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE ("email")
);

-- Дамп данных таблицы public.users: 1 rows
/*!40000 ALTER TABLE "users" DISABLE KEYS */;
INSERT INTO "users" ("id", "user_name", "email", "password", "role", "phone", "hired", "is_banned") VALUES
	(1, 'JDH', 'ght@gmail.com', '123456', 'CUSTOMER', '+79991230011', 1794260219733, 'false');
/*!40000 ALTER TABLE "users" ENABLE KEYS */;

-- Дамп структуры для таблица public.vendor_profile
CREATE TABLE IF NOT EXISTS "vendor_profile" (
	"id" BIGINT NOT NULL,
	"brand_name" VARCHAR(255) NULL DEFAULT NULL,
	"inn" VARCHAR(255) NOT NULL,
	"legal_address" VARCHAR(255) NOT NULL,
	"logo_url" VARCHAR(255) NULL DEFAULT NULL,
	"rating_vendor" DOUBLE PRECISION NOT NULL,
	"shop_description" VARCHAR(255) NULL DEFAULT NULL,
	"shop_name" VARCHAR(255) NOT NULL,
	"vendor_id" BIGINT NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE ("vendor_id"),
	CONSTRAINT "fk1ywb839mm56ey8gnpu04i6egi" FOREIGN KEY ("vendor_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Дамп данных таблицы public.vendor_profile: 0 rows
/*!40000 ALTER TABLE "vendor_profile" DISABLE KEYS */;
/*!40000 ALTER TABLE "vendor_profile" ENABLE KEYS */;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
