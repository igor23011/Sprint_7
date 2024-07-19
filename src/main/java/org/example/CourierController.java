package org.example;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierController {
    private static final String apiCourier = "/api/v1/courier";
    public static Response executeCreate (CreatingCourier creatingCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(creatingCourier)
                .when()
                .post(apiCourier);
        return response;
    }

    private static Response executeDelete(int courierId) {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format(apiCourier + "%s", courierId));
        return response;
    }

    public static Response executeLogin(LoginCourier loginCourier) {
        Response response =given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(apiCourier + "/login");
        return response;
    }

    public static int getCourierId(LoginCourier loginCourier) {
        Response response = executeLogin(loginCourier);
        int id = response.jsonPath().get("id");
        return id;
    }

    public static Response executeDelete(LoginCourier loginCourier) {
        return executeDelete(getCourierId(loginCourier));
    }

}
