DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL UNIQUE
);

CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title TEXT NOT NULL,
                       description TEXT NULL
);

CREATE UNIQUE INDEX uq_books_title_description
    ON books (title, COALESCE(description, ''));

CREATE TABLE book_authors (
                              book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
                              author_id BIGINT NOT NULL REFERENCES authors(id) ON DELETE RESTRICT,
                              PRIMARY KEY (book_id, author_id)
);
