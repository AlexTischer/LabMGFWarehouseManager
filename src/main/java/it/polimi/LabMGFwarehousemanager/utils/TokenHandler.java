package it.polimi.LabMGFwarehousemanager.utils;

import java.util.UUID;

public class TokenHandler {

    /**Generates a random token*/
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }

    /**Checks if the provided tokens are equal*/
    public static boolean checkToken(String token, String tokenToCheck) {
        return token.equals(tokenToCheck);
    }

}
