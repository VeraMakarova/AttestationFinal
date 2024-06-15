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
import ru.inno.course.db.model.EmployeeDB;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;

import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("Создание сотрудника в несуществующую компанию")
    @Description("Проверяем, что невозможно создать сотрудника в несуществующей компании")
    @Severity(SeverityLevel.CRITICAL)
    public void iCannotCreateEmployeeToNotExistingCompany() {
        int companyId = step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));
        step("Удалить эту компанию из БД", () -> repository.deleteCompanyDBById(companyId));
        int statusCode = step("Попытаться создать сотрудника в удаленную компанию",
                () -> client.createEmployeeToNotExistingCompany(companyId));
        step("Проверить, что сотрудника создать не получилось",
                () -> assertThat(statusCode, greaterThan(201)));
    }

    @Test
    @DisplayName("Создание сотрудника в существующую компанию")
    @Description("Проверяем, что возможно создать сотрудника в существующню компанию")
    @Severity(SeverityLevel.CRITICAL)
    public void iCanCreateEmployeeToExistingCompany() throws SQLException {
        int companyId = step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));

        int employeeId = step("Создать сотрудника в компанию",
                () -> client.createEmployee(companyId));
        EmployeeDB employeeDB =
                step("Получить информацию о сотруднике из БД", () -> repository.getEmployeeDBById(employeeId));
        step("Проверить, что у пользователя правильный ID", () -> assertEquals(employeeId, employeeDB.id()));
        step("Проверить, что имя пользователя сохраняется правильно",
                () -> assertEquals(ConfigHelper.getEmployeeFirstName(), employeeDB.firstName()));
    }
}