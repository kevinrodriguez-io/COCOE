/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

/**
 *
 * @author COCOE
 */
public abstract class AbstractRepository<T> {
    
    public void Create(T item) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
    }
    public void Update(T item) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
    }
    public void Delete(T item) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
    }
    public List<T> All(String tableName) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from " + tableName);
            List<T> list = query.list();
            session.getTransaction().commit();
            return list;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.flush();
            session.close();
        }
    }
    public T Get(String tableName, int id) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from " + tableName + " where id = :identifier");
            query.setParameter("identifier", id);
            List<T> list = query.list();
            return (list.size() > 0) ? list.get(0) : null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.flush();
            session.close();
        }
    }
    
}
