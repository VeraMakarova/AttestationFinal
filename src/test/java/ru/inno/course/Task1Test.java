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

import java.sql.SQLException;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Task1Test {

    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String user;
    private String password;

    @BeforeEach
    public void setUp() throws SQLException {
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
    void iCanFilterCompaniesByIsActive() {
        int[] testCompanies = new int[4];

        step("Создать 4 компании в БД", () -> {
            for (int i = 0; i < testCompanies.length; i++) {
                testCompanies[i] = repository.createCompanyDB(companyName);
            }
        });

        step("Деактивировать компанию 1 в БД", () -> repository.updateCompanyIsActivaFalse(testCompanies[0]));
        step("Деактивировать компанию 3 в БД", () -> repository.updateCompanyIsActivaFalse(testCompanies[2]));

        List<Company> companiesList =
                step("Получить список неактивных компаний", () -> client.getActiveCompanies(false));

        step("Проверить, что все компании в списке неактивные", () -> {
            for (Company company : companiesList) {
                assertTrue(!company.isActive());
            }
        });

        List<Company> companiesList2 =
                step("Получить список активных компаний", () -> client.getActiveCompanies(true));

        step("Проверить, что все компании в списке активные", () -> {
            for (Company company : companiesList2) {
                assertTrue(company.isActive());
            }
        });


        step("Удалить все созданные в тесте компании из БД", () -> {
            for (int companyID : testCompanies) {
                repository.deleteCompanyDBById(companyID);
            }
        });

    }
}