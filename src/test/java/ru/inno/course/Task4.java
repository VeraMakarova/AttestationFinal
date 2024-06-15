package ru.inno.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.course.db.XClientsRepository;
import ru.inno.course.db.XClientsRepositoryJDBC;
import ru.inno.course.db.model.CompanyDB;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class Task4 {
    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String dbUser;
    private String dbPassword;
    private int companyId;
    private String token;
    private String xClientsLogin;
    private String xClientsPassword;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionString = ConfigHelper.getConnectionString();
        dbUser = ConfigHelper.getUser();
        dbPassword = ConfigHelper.getPassword();
        repository = new XClientsRepositoryJDBC(connectionString, dbUser, dbPassword);
        companyName = ConfigHelper.getcCompanyName();
        client = new XClientsWebClient();
        xClientsLogin = ConfigHelper.getXClientsLogin();
        xClientsPassword = ConfigHelper.getXClientsPassword();
        token = client.getToken(xClientsLogin, xClientsPassword);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        repository.deleteCompanyDBById(companyId);
        repository.close();
    }

    @Test
    public void checkDeletedCompany() throws SQLException, InterruptedException {
        companyId = repository.createCompanyDB(companyName);
        client.deleteCompany(companyId);
        Thread.sleep(5000L);
        CompanyDB companyDB = repository.getCompanyDBById(companyId);
        assertNotEquals(null, companyDB.deletedAt());
    }

}