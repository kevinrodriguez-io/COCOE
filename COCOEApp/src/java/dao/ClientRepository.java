/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

/**
 *
 * @author Kevin Rodriguez
 */
public class ClientRepository extends AbstractRepository<Client> {
    public List<Client> AllByArea(int areaid) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Client where areaid = :identifier and active = 1");
            query.setParameter("identifier", areaid);
            List<Client> list = query.list();
            session.getTransaction().commit();
            return list;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }
}
