package com.example.library.repository.hibernate;

import com.example.library.domain.Author;
import com.example.library.repository.AuthorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HibernateAuthorsRepository implements AuthorRepository {
    private final SessionFactory sessionFactory;

    public HibernateAuthorsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(currentSession().get(Author.class, id));
    }

    @Override
    public void delete(Author author) {
        Session session = currentSession();
        session.remove(session.contains(author) ? author : session.merge(author));
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}