import io.restassured.RestAssured;
import org.example.CreatingCourier;
import org.example.StepTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTest {
    private final CreatingCourier credential; // credential - путь к JSON с учётными данными для создания курьера

    public CreateCourierParameterizedTest(CreatingCourier credential) {
        this.credential = credential;
    }

    private StepTest stepTestCourier = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters
    public static Object[][] getJsonLoginVariable() {
        return new Object[][] {
                {new CreatingCourier("","12345","Gagarin")},
                {new CreatingCourier("Gagarin001","","Gagarin")}
        };
    }
    @Test
    public void failCreateTest() {
    stepTestCourier.checkFailCreateNoDataStep(credential);
    }
}