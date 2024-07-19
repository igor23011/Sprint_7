import org.example.LoginCourier;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.example.CourierController.executeDelete;

@RunWith(Parameterized.class)
public class LoginCourierParameterizedTest {
    private final LoginCourier pathToLogin; // pathToLogin - путь к JSON с учётными данными для логина курьера

    public LoginCourierParameterizedTest(LoginCourier pathToLogin) {
        this.pathToLogin = pathToLogin;
    }

    private StepTest stepTestCourier = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters
    public static Object[][] getJsonLoginVariable() {
        return new Object[][] {
                {new LoginCourier("","12345")},
                {new LoginCourier("Gagarin001","")}
        };
    }
    @Test
    public void failLoginTestWithoutRequiredFields() {
        stepTestCourier.createCourierStep("Gagarin001","12345", "Gagarin" );
        stepTestCourier.failAutoWithoutRequiredFields(pathToLogin);
    }

    @After
    public void deleteChanges() {
        LoginCourier loginCourier = new LoginCourier("Gagarin001", "12345");
        executeDelete(loginCourier);
    }
}