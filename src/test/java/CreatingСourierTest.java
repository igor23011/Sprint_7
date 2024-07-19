import org.example.CreatingCourier;
import org.example.LoginCourier;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.CourierController.*;

public class CreatingСourierTest {

    private StepTest stepTestCourier = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierAndCheckResponse() {
        CreatingCourier creatingCourier = stepTestCourier.createCourierStep("Gagarin001", "12345", "Gagarin");
        LoginCourier loginCourier = stepTestCourier.loginCourierStep("Gagarin001", "12345");
        stepTestCourier.checkResponseCourierStep(creatingCourier, loginCourier);
    }

    @Test
    public void failDoubleTest() {
        CreatingCourier creatingCourier = stepTestCourier.createCourierStep("Gagarin001", "12345","Gagarin" );
        stepTestCourier.checkDoubleCourierStep(creatingCourier);

    }

    @After
    public void deleteChanges() {
        LoginCourier loginCourier = new LoginCourier("Gagarin001", "12345");
        executeDelete(loginCourier);
    }

}

