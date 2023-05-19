package it.polimi.geodesicwarehousemanager.utils;


import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordHandler {

        public static String encryptPassword(String password){
            return BCrypt.withDefaults().hashToString(12, password.toCharArray());
        }

        public static boolean checkPassword(String password, String hashedPassword){
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
            return result.verified;
        }

    public static String getNewPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
