package com.example.library.repository.hibernate;

import com.example.library.domain.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateAuthorsRepository {

    private final SessionFactory sessionFactory;

    public HibernateAuthorsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Author> findAll() {
        return currentSession()
                .createQuery("select distinct a from Author a", Author.class)
                .getResultList();
    }

    public Optional<Author> findById(long id) {
        return Optional.ofNullable(currentSession().get(Author.class, id));
    }


    public void delete(Author author) {
        currentSession().remove(author);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}