DROP TABLE IF EXISTS book_genres;
DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS authors;

CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL UNIQUE
);

CREATE TABLE genres (
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

CREATE TABLE book_genres (
                             book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
                             genre_id BIGINT NOT NULL REFERENCES genres(id) ON DELETE RESTRICT,
                             PRIMARY KEY (book_id, genre_id)
);

CREATE INDEX idx_book_authors_author_id ON book_authors(author_id);
CREATE INDEX idx_book_genres_genre_id ON book_genres(genre_id);