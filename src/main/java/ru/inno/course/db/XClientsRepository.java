package ru.inno.course.db;

import ru.inno.course.db.model.CompanyDB;
import ru.inno.course.db.model.EmployeeDB;

import java.sql.SQLException;

public interface XClientsRepository {

    int createCompanyDB(String name) throws SQLException;
    int createEmployeeDB(int company_id) throws SQLException;
    void deleteCompanyDBById (int id) throws SQLException;
    void deleteEmployeeDBById (int id) throws SQLException;
    void close() throws SQLException;
    void updateCompanyIsActivaFalse (int id) throws SQLException;
    void updateEmployeeIsActiveFalse(int id) throws SQLException;
    CompanyDB getCompanyDBById(int id) throws SQLException;
    EmployeeDB getEmployeeDBById(int id) throws SQLException;

}