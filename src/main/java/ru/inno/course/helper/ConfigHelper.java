package ru.inno.course.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUrl() {
        String url = properties.getProperty("URL");
        return url;
    }
    public static String getURL_AUTH() {
        String url = properties.getProperty("URL_AUTH");
        return url;
    }
    public static String getURL_GET_ACTIVE() {
        String url = properties.getProperty("URL_GET_ACTIVE");
        return url;
    }
    public static String getURL_CREATE_EMPLOYEE() {
        String url = properties.getProperty("URL_CREATE_EMPLOYEE");
        return url;
    }
    public static String getURL_GET_EMPLOYEE_LIST() {
        String url = properties.getProperty("URL_GET_EMPLOYEE_LIST");
        return url;
    }
    public static String getURL_DELETE_COMPANY() {
        String url = properties.getProperty("URL_DELETE_COMPANY");
        return url;
    }

    public static String getConnectionString() {
        String connectionString = properties.getProperty("connectionString");
        return connectionString;
    }

    public static String getUser() {
        String user = properties.getProperty("db.user");
        return user;
    }

    public static String getPassword() {
        String password = properties.getProperty("db.pass");
        return password;
    }

    public static String getcCompanyName() {
        String companyName = properties.getProperty("companyName");
        return companyName;
    }

    public static String getEmployeeFirstName() {
        String employeeFirstName = properties.getProperty("employeeFirstName");
        return employeeFirstName;
    }

    public static String getEmployeeLastName() {
        String employeeLastName = properties.getProperty("employeeLastName");
        return employeeLastName;
    }

    public static String getEmployeeEmail() {
        String employeeEmail = properties.getProperty("employeeEmail");
        return employeeEmail;
    }

    public static String getEmployeePhone() {
        String employeePhone = properties.getProperty("employeePhone");
        return employeePhone;
    }

    public static String getXClientsLogin() {
        String XClientsLogin = properties.getProperty("xClients.Login");
        return XClientsLogin;
    }
    public static String getXClientsPassword() {
        String xClientsPassword = properties.getProperty("xClients.Password");
        return xClientsPassword;
    }

}
