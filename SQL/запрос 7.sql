SELECT setval(pg_get_serial_sequence('category', 'id'), COALESCE((SELECT MAX(id) FROM category), 1));
INSERT INTO category (name, parent_id) VALUES
('Дом и сад', NULL), ('Красота и здоровье', NULL),
('Спорт и отдых', NULL), ('Автотовары', NULL),
('Книги и канцелярия', NULL), ('Другое', NULL);