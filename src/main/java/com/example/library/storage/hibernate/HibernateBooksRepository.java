package com.example.library.storage.hibernate;

import com.example.library.domain.Book;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateBooksRepository {

    private final SessionFactory sessionFactory;

    public HibernateBooksRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Book> findAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Book", Book.class)
                .getResultList();
    }

    public Optional<Book> findById(long id) {
        Book book = sessionFactory
                .getCurrentSession()
                .get(Book.class, id);

        return Optional.ofNullable(book);
    }

    public Book save(Book book) {
        sessionFactory
                .getCurrentSession()
                .save(book);

        return book;
    }

    public void delete(Book book) {
        sessionFactory
                .getCurrentSession()
                .delete(book);
    }
}