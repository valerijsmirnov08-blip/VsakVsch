CREATE TABLE review_complaint_categories (
    review_id BIGINT NOT NULL,
    complaint_category_id BIGINT NOT NULL,
    PRIMARY KEY (review_id, complaint_category_id)
);