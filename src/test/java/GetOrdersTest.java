import io.qameta.allure.junit4.DisplayName;
import org.example.CreateOrder;
import org.example.CreatingCourier;
import org.example.LoginCourier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

import static org.example.CourierController.*;
import static org.example.OrderController.*;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

public class GetOrdersTest {

    LoginCourier loginCourier = new LoginCourier("Gagarin001","12345");
    CreateOrder createOrder = new CreateOrder("Pushkin","Alex","Moscow, 123 str.",4,"+7-800-888-88-77", 5,"2024-07-07","No comments", new String[]{"BLACK"});
    private int courierId;
    private int orderId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        courierId = getCourierId(loginCourier);
        orderId = parseOrderIdFromResponse(executeOrder(createOrder));

    }

    @Test
    @DisplayName("Получение списка всех заказов")
    public void successGetAllOrdersTest() {
        executeOrder(createOrder);
        Response response = executeListOrder(new HashMap<String,String>());
        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("orders", hasSize(greaterThan(0)));
    }

    @Test
    @DisplayName("Получение списка заказов конкретного курьера")
    public void successGetOrdersWithCourierTest() {
        CreatingCourier createCourier = new CreatingCourier("Gagarin001","12345","Gagarin");
        executeCreate(createCourier);
        Response response = executeAcceptOrder(courierId, orderId);
        response.then()
                .assertThat()
                .body("ok", equalTo(true))
                .and()
                .statusCode(SC_OK);
        response = executeListOrder(courierId);
        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("orders", hasItem(hasEntry("id",orderId)));
    }

    @Test
    @DisplayName("Получение списка заказов для несуществующего курьера")
    public void failGetOrdersTest() {
        CreatingCourier createCourier = new CreatingCourier("Gagarin001","12345","Gagarin");
        LoginCourier loginCourier = new LoginCourier("Gagarin001","12345");
        if (executeLogin(loginCourier).getStatusCode() == SC_OK) {
            executeDelete(loginCourier);
        } else {
            executeCreate(createCourier);
            executeDelete(loginCourier);
            Response response = executeListOrder(courierId);
            response.then().assertThat().body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"))
                    .and()
                    .statusCode(SC_NOT_FOUND);
        }
    }

    @After
    public void deleteChanges() {
        executeDelete(loginCourier);
        executeDeleteOrder(orderId);
    }
}