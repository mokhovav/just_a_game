package com.mokhovav.justAGame.authentication.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DAOService<T> {

    final SessionFactory sessionFactory;

    public DAOService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean save(T object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(object);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean update(T object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(object);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean delete(T object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(object);
        transaction.commit();
        session.close();
        return true;
    }

    public T getById(Long id) {
        Session session = sessionFactory.openSession();
        T result = (T) session.get(getClass(), id);
        session.close();
        return result;
    }

    public List<T> findList(String text) {
        Session session = sessionFactory.openSession();
        List<T> list = session.createQuery(text).list();
        session.close();
        return list;
    }

    public T findObject(String text) {
        Session session = sessionFactory.openSession();
        List<T> list = session.createQuery(text).list();
        session.close();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<T> findAll(Class c) {
        Session session = sessionFactory.openSession();
        List<T> list = (List<T>) session.createQuery("From " + c.getName()).list();
        session.close();
        return list;
    }
}

