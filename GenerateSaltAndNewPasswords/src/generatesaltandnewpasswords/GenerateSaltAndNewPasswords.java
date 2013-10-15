/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatesaltandnewpasswords;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Nick
 */
public class GenerateSaltAndNewPasswords {

    private Session session;

    public GenerateSaltAndNewPasswords() {
    }

    /**
     * @param args the command line arguments
     */
    private void createUser(Object o) {
        User user = new User();

    }

    private void initSession() {
        if (session == null || HibernateUtil.getSessionFactory().isClosed()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
    }

    public boolean update(Object obj) {
        try {
            initSession();
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getSecurePassword(String password, String salt) {
        String genPw = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPw = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return genPw;
    }

    public String getSalt() {
        SecureRandom sr = new SecureRandom();
        sr.setSeed(sr.generateSeed(20));
        byte[] salt = new byte[8];
        sr.nextBytes(salt);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < salt.length; i++) {
            sb.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
