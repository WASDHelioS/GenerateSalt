/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import generatesaltandnewpasswords.GenerateSaltAndNewPasswords;
import generatesaltandnewpasswords.HibernateUtil;
import generatesaltandnewpasswords.Rank;
import generatesaltandnewpasswords.User;
import java.util.List;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nick
 */
public class CreateNewPasswords {
    
    public CreateNewPasswords() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void createPasswords() {
        GenerateSaltAndNewPasswords g = new GenerateSaltAndNewPasswords();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String commonPasswordHash = "fly2mars";
        List completeList = session.createSQLQuery("SELECT id, username, password, rank, salt FROM `user` where password=md5('" + commonPasswordHash + "')").list();
        
        
        for(int i = 0; i < completeList.size(); i++) {
            Object[] o = (Object[]) completeList.get(i);
            User u = new User();
            u.setId(Integer.parseInt(o[0].toString()));
            u.setUsername(o[1].toString());
            if (o[3].toString().equals("STAFF")) {
                u.setRank(Rank.STAFF);
            } else if (o[3].toString().equals("ADMIN")) {
                u.setRank(Rank.ADMIN);
            } else {
                u.setRank(Rank.USER);
            }
            u.setSalt(o[4].toString());
            u.setPassword(g.getSecurePassword("fly2mars", u.getSalt()));
            System.out.println(g.getSecurePassword("fly2mars", u.getSalt()).length() + " " );
            System.out.println(u.getSalt());
            session.update(u);
        }
        session.getTransaction().commit();
        session.close();
        
        assert true;
    }
}