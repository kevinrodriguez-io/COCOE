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
public class SettingsRepository extends AbstractRepository<Settings> {
    public Settings Get(String settingKey) {
        Session session = null;
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Settings where settingKey = :skey");
            query.setParameter("skey", settingKey);
            List<Settings> list = query.list();
            return (list.size() > 0) ? list.get(0) : null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }
}