package com.sparkfinance.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by conner.tolley on 3/23/16.
 */
public class PasswordUtil {

    public static String hashPassword(String pwd) {
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());

        return hashed;
    }

    public static boolean verifyPassword(String pwd, String hash) {
        boolean b = BCrypt.checkpw(pwd, hash);

        return b;
    }
}
