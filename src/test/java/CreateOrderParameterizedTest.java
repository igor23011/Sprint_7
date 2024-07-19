import org.example.CreateOrder;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.example.OrderController.*;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {

    private final CreateOrder pathToOrderRequestBody;
    public CreateOrderParameterizedTest(CreateOrder pathToOrderRequestBody) {
        this.pathToOrderRequestBody = pathToOrderRequestBody;
    }
    private StepTest stepTestOrder = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters
    public static Object[][] colorForOrder() {
        return new Object[][] {
                {new CreateOrder("Pushkin","Alex","Moscow, 123 str.",4,"+7-800-888-88-77", 5,"2024-07-07","No comments", new String[]{"BLACK"})},
                {new CreateOrder("Fet","Afan","Moscow, 12 str.",3,"+7-800-888-88-77", 3,"2024-07-07","No comments", new String[]{"GREY"})},
                {new CreateOrder("Esenin","Serg","Moscow, 53 str.",2,"+7-800-888-88-77", 4,"2024-07-07","No comments", new String[]{"BLACK", "GREY"})},
                {new CreateOrder("Tolstoy","Lev","Moscow, 36 str.",1,"+7-800-888-88-77", 2,"2024-07-07","No comments", new String[]{""})}
        };
    }

    @Test
    public void successCreateOrderTest() {
       stepTestOrder.checkBodyCreateOrderTest(pathToOrderRequestBody);
    }

    @After
    public void deleteChanges() {
        int orderId = parseOrderIdFromResponse(executeOrder(pathToOrderRequestBody));
        executeDeleteOrder(orderId);
    }
}