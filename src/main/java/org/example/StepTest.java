package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.example.CourierController.*;
import static org.example.CourierController.executeCreate;
import static org.example.OrderController.executeOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StepTest {

    @Step("Регистрация нового пользователя")
    public CreatingCourier createCourierStep(String login, String password, String firstName) {
        CreatingCourier createCourier = new CreatingCourier(login, password, firstName);
        executeCreate(createCourier);
        return createCourier;
    }

    @Step("Авторизация созданного пользователя")
    public LoginCourier loginCourierStep(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        executeLogin(loginCourier);
        return loginCourier;
    }

    @Step("Проверка полученного ответа после регистрации")
    public void checkResponseCourierStep(CreatingCourier creatingCourier, LoginCourier loginCourier) {
        if (executeLogin(loginCourier).getStatusCode() == SC_OK) {
            executeDelete(loginCourier);
        } else {
            Response response = executeCreate(creatingCourier);
            response.then().assertThat().body("ok", equalTo(true))
                    .and()
                    .statusCode(SC_CREATED);
        }
    }

    @Step("Регистрация существующего пользователя")
    public void checkDoubleCourierStep(CreatingCourier creatingCourier) {
        executeCreate(creatingCourier);
        Response response = executeCreate(creatingCourier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

    @Step("Проверка наличия ошибки при регистрации и отправлении неполных учётных данных (отсутствие логина или пароля)")
    public void checkFailCreateNoDataStep(CreatingCourier creatingCourier) {
        Response response = executeCreate(creatingCourier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step("Проверка полученного ответа (id) после авторизации")
    public void checkResponseCourierAutoStep(LoginCourier loginCourier) {
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Step("Авторизация несуществующего пользователя")
    public LoginCourier failInputCourierStep(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        executeLogin(loginCourier);
        return loginCourier;
    }

    @Step("Авторизация пользователя c некорректным логином")
    public LoginCourier failInputLoginCourierStep(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        executeLogin(loginCourier);
        return loginCourier;
    }

    @Step("Авторизация пользователя c некорректным паролем")
    public LoginCourier failInputPasswordCourierStep(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        executeLogin(loginCourier);
        return loginCourier;
    }

    @Step("Проверка ответа при авторизации несуществующего пользователя либо некорректном логине и пароле")
    public void failResponseCourierStep(LoginCourier loginCourier) {
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Step("Проверка ответа при отправке неполных данных авторазиции")
    public void failAutoWithoutRequiredFields(LoginCourier loginCourier) {
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
               .and()
               .statusCode(SC_BAD_REQUEST);
    }

    @Step("Проверка тела ответа заказа на содержание track")
    public void checkBodyCreateOrderTest(CreateOrder createOrder ){
        Response response = executeOrder(createOrder);
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);
    }

}
