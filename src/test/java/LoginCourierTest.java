import org.example.CreatingCourier;
import org.example.LoginCourier;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.CourierController.*;

public class LoginCourierTest {

    private StepTest stepTestCourierLogin = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void successCourierLoginTest() {
        CreatingCourier creatingCourier = stepTestCourierLogin.createCourierStep("Gagarin001", "12345", "Gagarin");
        LoginCourier loginCourier = stepTestCourierLogin.loginCourierStep("Gagarin001", "12345");
        stepTestCourierLogin.checkResponseCourierAutoStep(loginCourier);
    }

    @Test
    public void failLoginNonExistCourierTest() {
        LoginCourier loginCourier = stepTestCourierLogin.failInputCourierStep("Gagarin888888", "12345!!!!");
        stepTestCourierLogin.failResponseCourierStep(loginCourier);
    }

    @Test
    public void failLoginWithWrongLoginTest() {
        LoginCourier loginCourier = stepTestCourierLogin.failInputLoginCourierStep("Gagarin0000000", "12345");
        stepTestCourierLogin.failResponseCourierStep(loginCourier);
    }

    @Test
    public void failLoginWithWrongPasswordTest() {
        LoginCourier loginCourier = stepTestCourierLogin.failInputPasswordCourierStep("Gagarin001", "54321");
        stepTestCourierLogin.failResponseCourierStep(loginCourier);
    }

    @After
    public void deleteChanges() {
        LoginCourier loginCourier = new LoginCourier("Gagarin001", "12345");
        executeDelete(loginCourier);
    }
}