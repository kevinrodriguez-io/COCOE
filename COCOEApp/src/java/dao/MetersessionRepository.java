/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

/**
 *
 * @author darkn
 */
public class MetersessionRepository extends AbstractRepository<Metersession> {
    public List<Metersession> GeByUserId(int userId) { 
        MetersessionuserRepository meterSessionUserRepository = new MetersessionuserRepository();
        List<Metersession> all = super.All("Metersession");
        List<Metersession> filtered = new ArrayList<Metersession>();
        for (Metersession metersession : all) {
            List<Metersessionuser> allMeterSessionUsers = meterSessionUserRepository.GetByMeterSessionId(metersession.getId());
            for (Metersessionuser meterSessionUser : allMeterSessionUsers) {
                if (meterSessionUser.getUserid() == userId) {
                    filtered.add(metersession);
                }
            }
        }
        return filtered;
    }
}
