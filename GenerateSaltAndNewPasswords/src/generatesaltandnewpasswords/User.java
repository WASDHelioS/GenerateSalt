/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generatesaltandnewpasswords;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private Rank rank;
    private String salt;
    
    public User() {
        rank = Rank.USER;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password, password is MD5 coded
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the value of password, password is MD5 coded
     *
     * @param password new value of password
     */
    public void setPassword(String password, boolean setHash) {
        if (setHash) {
            //this.salt = getSalt();
            //this.password = this.hash(password, salt); 
            
            //Ook nog een instance variable 'private String salt' maken om in
            //de database op te kunnen slaan.
            this.password = this.hash(password);
        } else {
            this.password = password;
        }
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    /**
     * Creates a new hash with salt. RECOMMENDED.
     *
     * @param password Password typed by user
     * @param salt Generated salt.
     * @return SHA-512 salt encryption.
     */
    private String hashSHA512(String password, String salt) {
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
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return genPw;
    }

    /**
     * Generates a new salt (random set of characters to add to the password before encrypting.).
     * @return Salt.
     */

    
    private String hash(String password) {
        String result;

        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(password.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            result = hashtext;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }

        return result;
    }

    @Override
    public String toString() {
        return username;
    }
}
