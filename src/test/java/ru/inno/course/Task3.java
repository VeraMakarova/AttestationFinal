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
import ru.inno.course.web.model.Employee;

import java.sql.SQLException;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class Task3 {
    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String user;
    private String password;
    private int companyId;
    private int employeeId;

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
        repository.deleteEmployeeDBById(employeeId);
        repository.deleteCompanyDBById(companyId);
        repository.close();
    }

    @Test
    @DisplayName("Неактивный сотрудник отсутствует в списке")
    @Description("Проверяем, что неактивный сотрудник не отображается в списке сотрудников компании")
    @Severity(SeverityLevel.NORMAL)
    public void checkInactiveEmployee() throws SQLException {
        companyId = step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));
        employeeId = step("Создать сотрудника в эту компанию в БД", () -> repository.createEmployeeDB(companyId));
        step("Сделать сотрудника неактивным в БД", () -> repository.updateEmployeeIsActiveFalse(employeeId));
        List<Employee> employeeList = step("Получить список сотрудников компании",
                () -> client.getEmployeeList(companyId));
        step("Проверяем, что список пустой (единственный сотрудник неактивный и не должен отображаться в списке)",
                () -> assertTrue(employeeList.isEmpty()));
    }
}