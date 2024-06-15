package ru.inno.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.course.db.XClientsRepository;
import ru.inno.course.db.XClientsRepositoryJDBC;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;
import ru.inno.course.web.model.Company;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Task1 {

    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String user;
    private String password;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        connectionString = ConfigHelper.getConnectionString();
        user = ConfigHelper.getUser();
        password = ConfigHelper.getPassword();
        repository = new XClientsRepositoryJDBC(connectionString, user, password);
        companyName = ConfigHelper.getcCompanyName();
        client = new XClientsWebClient();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        repository.close();
    }

    @Test
    void iCanFilterCompaniesByIsActive() throws SQLException {
        int[] testCompanies = new int[4];
        for (int i = 0; i < testCompanies.length; i++) {
            testCompanies[i] = repository.createCompanyDB(companyName);
        }

        repository.updateCompanyIsActivaFalse(testCompanies[0]);
        repository.updateCompanyIsActivaFalse(testCompanies[2]);

        List<Company> companiesList = client.getActiveCompanies(false);
        for (Company company : companiesList) {
            assertTrue(!company.isActive(), "Expected company to be inactive");
        }

        List<Company> companiesList2 = client.getActiveCompanies(true);
        for (Company company : companiesList2) {
            assertTrue(company.isActive(), "Expected company to be active");
        }

        for (int companyID : testCompanies) {
            repository.deleteCompanyDBById(companyID);
        }
    }

}