package ru.inno.course.db;

import ru.inno.course.db.model.CompanyDB;
import ru.inno.course.helper.ConfigHelper;

import java.sql.*;
import java.sql.SQLException;

public class XClientsRepositoryJDBC implements XClientsRepository {

    private final Connection connection;
    private static final String SQL_DELETE_EMP_BY_ID = "DELETE FROM employee WHERE id = ?";
    private static final String SQL_DELETE_COMPANY_BY_ID = "DELETE FROM company WHERE id = ?";
    private static final String SQL_INSERT_COMPANY = "INSERT INTO company(\"name\") values (?)";
    private static final String SQL_INSERT_EMPLOYEE =
            "INSERT INTO employee(\"first_name\",\"last_name\",\"phone\",\"company_id\") values (?, ?, ?, ?)";
    private static final String SQL_UPDATE_COMPANY_IS_ACTIVE_FALSE =
            "UPDATE company SET is_active = false WHERE id = ?";
    private static final String SQL_UPDATE_EMPLOYEE_IS_ACTIVE_FALSE =
            "UPDATE employee SET is_active = false WHERE id = ?";
    private static final String SQL_SELECT_COMPANY_BY_ID = "SELECT * FROM company WHERE id = ?";

    private String employeeName = ConfigHelper.getEmployeeFirstName();
    private String employeeLastName = ConfigHelper.getEmployeeLastName();
    private String employeePhone = ConfigHelper.getEmployeePhone();

    public XClientsRepositoryJDBC(String connectionString, String user, String pass) throws SQLException {
        this.connection = DriverManager.getConnection(connectionString, user, pass);
    }

    @Override
    public int createCompanyDB(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_COMPANY, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);

        statement.executeUpdate();

        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    @Override
    public int createEmployeeDB(int company_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, employeeName);
        statement.setString(2, employeeLastName);
        statement.setString(3, employeePhone);
        statement.setInt(4, company_id);
        statement.executeUpdate();

        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    @Override
    public void deleteCompanyDBById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_COMPANY_BY_ID);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public void deleteEmployeeDBById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_DELETE_EMP_BY_ID);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public void updateCompanyIsActivaFalse(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_COMPANY_IS_ACTIVE_FALSE);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public void updateEmployeeIsActiveFalse(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_EMPLOYEE_IS_ACTIVE_FALSE);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public CompanyDB getCompanyDBById(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_COMPANY_BY_ID);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        CompanyDB companyDB = new CompanyDB(
                resultSet.getInt("id"),
                resultSet.getBoolean("is_active"),
                resultSet.getTimestamp("create_timestamp"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("deleted_at"));
        return companyDB;
    }

}