package com.example.library.repository.hibernate;

import com.example.library.domain.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
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
        return currentSession()
                .createQuery(
                        "select distinct b from Book b left join fetch b.authors",
                        Book.class
                )
                .getResultList();
    }

    public Optional<Book> findById(long id) {
        CriteriaBuilder cb = currentSession().getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);

        root.fetch("authors");
        cq.select(root)
                .distinct(true)
                .where(cb.equal(root.get("id"), id));

        List<Book> result = currentSession()
                .createQuery(cq)
                .getResultList();

        return result.stream().findFirst();
    }

    public Book save(Book book) {
        currentSession().saveOrUpdate(book);
        return book;
    }

    public void delete(Book book) {
        currentSession().delete(book);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}