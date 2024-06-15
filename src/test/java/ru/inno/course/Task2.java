package ru.inno.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.course.db.XClientsRepository;
import ru.inno.course.db.XClientsRepositoryJDBC;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class Task2 {
    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String dbUser;
    private String dbPassword;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionString = ConfigHelper.getConnectionString();
        dbUser = ConfigHelper.getUser();
        dbPassword = ConfigHelper.getPassword();
        repository = new XClientsRepositoryJDBC(connectionString, dbUser, dbPassword);
        companyName = ConfigHelper.getcCompanyName();
        client = new XClientsWebClient();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        repository.close();
    }

    @Test
    public void iCannotCreateEmployeeToNotExistingCompany2() throws SQLException {
        int companyId = repository.createCompanyDB(companyName);
        repository.deleteCompanyDBById(companyId);
        int statusCode = client.createEmployeeToNotExistingCompany(companyId);
        assertThat(statusCode, greaterThan(201));
    }
}