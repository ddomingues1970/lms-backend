CREATE TABLE task_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO task_categories (name) VALUES ('PESQUISA');
INSERT INTO task_categories (name) VALUES ('PRATICA');
INSERT INTO task_categories (name) VALUES ('ASSISTIR_VIDEOAULA');
