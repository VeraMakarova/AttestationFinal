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
import ru.inno.course.db.model.CompanyDB;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.XClientsWebClient;

import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class Task4 {
    private XClientsRepository repository;
    private XClientsWebClient client;
    private String companyName;
    private String connectionString;
    private String dbUser;
    private String dbPassword;
    private int companyId;

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
        repository.deleteCompanyDBById(companyId);
        repository.close();
    }

    @Test
    @DisplayName("Удаленная компания в базе данных")
    @Description("Проверяем, что у удаленной компании проставляется в БД поле deletedAt")
    @Severity(SeverityLevel.CRITICAL)
    public void checkDeletedCompany() {
        companyId = step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));
        step("Удалить эту компанию через приложение", () -> client.deleteCompany(companyId));
        step("Подождать, пока изменения отобразятся в БД", () -> Thread.sleep(5000L));
        CompanyDB companyDB =
                step("Получить сведения о компании из БД", () -> repository.getCompanyDBById(companyId));
        step("Проверить, что поле deletedAt не пустое",
                () -> assertNotEquals(null, companyDB.deletedAt()));
    }

    @Test
    @DisplayName("Неудаленная компания в базе данных")
    @Description("Проверяем, что у неудаленной компании в БД поле deletedAt пустое")
    @Severity(SeverityLevel.CRITICAL)
    public void checkNotDeletedCompany() {
        companyId = step("Создать компанию в БД", () -> repository.createCompanyDB(companyName));
        CompanyDB companyDB =
                step("Получить сведения о компании из БД", () -> repository.getCompanyDBById(companyId));
        step("Проверить, что поле deletedAt пустое", () -> assertEquals(null, companyDB.deletedAt()));
    }
}