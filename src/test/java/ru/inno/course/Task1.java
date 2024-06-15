package ru.inno.course;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.course.db.XClientsRepository;
import ru.inno.course.db.XClientsRepositoryJDBC;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;
import ru.inno.course.web.model.Company;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static io.qameta.allure.Allure.step;
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
    @DisplayName("Фильтрация компаний по active")
    @Description("Проверяем, что список компаний фильтруется по параметру active")
    @Severity(SeverityLevel.NORMAL)
    void iCanFilterCompaniesByIsActive() throws SQLException {
        int[] testCompanies = new int[4];
        for (int i = 0; i < testCompanies.length; i++) {
            testCompanies[i] =  step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));
        }

        step("Деактивировать компанию 1 в БД", () -> repository.updateCompanyIsActivaFalse(testCompanies[0]));
        step("Деактивировать компанию 3 в БД", () -> repository.updateCompanyIsActivaFalse(testCompanies[2]));

        List<Company> companiesList =
                step("Получить список неактивных компаний", () -> client.getActiveCompanies(false));
        for (Company company : companiesList) {
            step("Проверить, что в списке все компании неактивные", () -> assertTrue(!company.isActive()));
        }

        List<Company> companiesList2 =
                step("Получить список активных компаний", () -> client.getActiveCompanies(true));
        for (Company company : companiesList2) {
            step("Проверить, что все компании в списке активные", () -> assertTrue(company.isActive()));
        }

        for (int companyID : testCompanies) {
             step("Удалить компанию из БД", () -> repository.deleteCompanyDBById(companyID));
        }
    }

}