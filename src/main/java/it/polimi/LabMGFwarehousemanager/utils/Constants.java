package it.polimi.LabMGFwarehousemanager.utils;

import java.io.File;
import java.util.Objects;

public class Constants {

    public static final String MAIL_CONFIG_FILE_PATH = "Config" + File.separator + "mail.json";
    public static final String DB_CONFIG_FILE_PATH = "Config" + File.separator + "db.json";
    public static final String SERVER_CONFIG_FILE_PATH = "Config" + File.separator + "server.json";
    public static String SERVER_URL = FileHandler.getConfig(SERVER_CONFIG_FILE_PATH) != null ? FileHandler.getConfig(SERVER_CONFIG_FILE_PATH).get("serverURL") : "http://localhost:8080/LabMGFWarehouseManager";
    public static final String INDEX_PATH = "/public/index/";
    public static final String LOGIN_PATH = "/public/login/";
    public static final String REGISTER_PATH = "/public/register/";
    public static final String HOMEPAGE_PATH = "/users/homePage/";
    public static final String CONFIRM_PATH = "/users/confirmRegistration/";
    public static final String CHANGE_PASSWORD_PATH = "/users/changePassword/";
    public static final String WAIT_FOR_ROLE_PATH = "/users/waitForRole/";

}
