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
public class CreateSaltTest {

    public CreateSaltTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @Test
    public void createSalt() {
        GenerateSaltAndNewPasswords g = new GenerateSaltAndNewPasswords();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List completeresults = session.createSQLQuery("SELECT id,username,PASSWORD,rank,salt FROM `user`").list();

        for (int i = 0; i < completeresults.size(); i++) {
            Object[] o = (Object[]) completeresults.get(i);
            User u = new User();
            u.setId(Integer.parseInt(o[0].toString()));
            u.setUsername(o[1].toString());
            u.setPassword(o[2].toString());
            if (o[3].toString().equals("STAFF")) {
                u.setRank(Rank.STAFF);
            } else if (o[3].toString().equals("ADMIN")) {
                u.setRank(Rank.ADMIN);
            } else {
                u.setRank(Rank.USER);
            }
            u.setSalt(g.getSalt());
            System.out.println(u.getSalt() + " " + u.getSalt().length());

            session.update(u);
        }
        session.getTransaction().commit();
        session.close();
        
        assert true;
    }
}