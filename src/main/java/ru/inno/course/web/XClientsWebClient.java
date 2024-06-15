package ru.inno.course.web;

import io.restassured.http.ContentType;
import ru.inno.course.helper.ConfigHelper;
import ru.inno.course.web.model.Company;
import ru.inno.course.web.model.CreateEmployee;
import ru.inno.course.web.model.Employee;

import java.util.List;

import static io.restassured.RestAssured.given;

public class XClientsWebClient {
    public static final String URL_AUTH = ConfigHelper.getURL_AUTH();
    public static final String URL_GET_ACTIVE = ConfigHelper.getURL_GET_ACTIVE();
    public static final String URL_CREATE_EMPLOYEE = ConfigHelper.getURL_CREATE_EMPLOYEE();
    public static final String URL_GET_EMPLOYEE_LIST = ConfigHelper.getURL_GET_EMPLOYEE_LIST();
    public static final String URL_DELETE_COMPANY = ConfigHelper.getURL_DELETE_COMPANY();
    public String employeeName = ConfigHelper.getEmployeeFirstName();
    private String employeeLastName = ConfigHelper.getEmployeeLastName();
    private String employeeEmail = ConfigHelper.getEmployeeEmail();
    private String employeePhone = ConfigHelper.getEmployeePhone();
    private String token;
    private String xClientsLogin = ConfigHelper.getXClientsLogin();
    private String xClientsPassword = ConfigHelper.getXClientsPassword();

    public String getToken(String login, String pass) {
        String creds = "{\"username\": \"" + login + "\",\"password\": \"" + pass + "\"}";
        return given().log().all()
                .body(creds)
                .contentType(ContentType.JSON)
                .when().post(URL_AUTH)
                .then().log().all()
                .extract().path("userToken");
    }

    public List<Company> getActiveCompanies(boolean active) {
        return given().log().all()
                .pathParams("is_active", String.valueOf(active))
                .get(URL_GET_ACTIVE)
                .then()
                .extract().body().jsonPath()
                .getList("", Company.class);
    }

    public List<Employee> getEmployeeList(int companyId) {
        return given().log().all()
                .pathParams("id", companyId)
                .get(URL_GET_EMPLOYEE_LIST)
                .then()
                .extract().body().jsonPath()
                .getList("", Employee.class);
    }

//    public int createEmployee2(int companyId) {
//        token = getToken(xClientsLogin, xClientsPassword);
//        CreateEmployee createEmployee = new CreateEmployee
//                (employeeName, employeeLastName, companyId, employeeEmail, employeePhone);
//        return given()
//                .log().all()
//                .body(createEmployee)
//                .header("x-client-token", token)
//                .contentType(ContentType.JSON)
//                .when()
//                .post(URL_CREATE_EMPLOYEE)
//                .then()
//                .log().all()
//                .extract().path("id");
//    }
//
//    public int createEmployee
//    (String firstName, String lastName, int companyId, String email, String phone, String token) {
//        CreateEmployee createEmployee = new CreateEmployee(firstName, lastName, companyId, email, phone);
//        return given()
//                .log().all()
//                .body(createEmployee)
//                .header("x-client-token", token)
//                .contentType(ContentType.JSON)
//                .when()
//                .post(URL_CREATE_EMPLOYEE)
//                .then()
//                .log().all()
//                .extract().path("id");
//    }

    public int createEmployeeToNotExistingCompany(int companyId) {
        token = getToken(xClientsLogin, xClientsPassword);
        CreateEmployee createEmployee = new CreateEmployee
                (employeeName, employeeLastName, companyId, employeeEmail, employeePhone);
        return given()
                .log().all()
                .body(createEmployee)
                .header("x-client-token", token)
                .contentType(ContentType.JSON)
                .when()
                .post(URL_CREATE_EMPLOYEE)
                .then()
                .log().all()
                .extract().statusCode();
    }

    public void deleteCompany(int companyId) {
        token = getToken(xClientsLogin, xClientsPassword);
        given().log().all()
                .pathParams("id", companyId)
                .header("x-client-token", token)
                .contentType(ContentType.JSON)
                .get(URL_DELETE_COMPANY)
                .then().log().all();
    }
}