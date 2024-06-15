package ru.inno.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.course.db.XClientsRepository;
import ru.inno.course.db.XClientsRepositoryJDBC;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;
import ru.inno.course.web.model.Employee;

import java.sql.SQLException;
import java.util.List;

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
    public void checkInactiveEmployee2() throws SQLException, InterruptedException {
        companyId = repository.createCompanyDB(companyName);
        System.out.println(companyId);
        employeeId = repository.createEmployeeDB(companyId);
        repository.updateEmployeeIsActiveFalse(employeeId);
        List<Employee> employeeList = client.getEmployeeList(companyId);
        System.out.println(employeeList);
        assertTrue(employeeList.isEmpty());
    }
}