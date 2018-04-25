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
public class ClientmeteringRepository extends AbstractRepository<Clientmetering> {
    public Clientmetering FindByClientAndByMeterSession(int clientid, int metersessionid) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Clientmetering where clientId = :clientidentifier and meterSessionId = :metersessionidentifier");
            query.setParameter("clientidentifier", clientid);
            query.setParameter("metersessionidentifier", metersessionid);
            List<Clientmetering> list = query.list();
            return (list.size() > 0) ? list.get(0) : null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }
    public List<Clientmetering> AllByClient(int clientid) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Clientmetering where clientid = :clientidentifier");
            query.setParameter("clientidentifier", clientid);
            List<Clientmetering> list = query.list();
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
