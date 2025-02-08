import client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private ScooterServiceClient client;

    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] colors() {
        return new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        };
    }

    @Before
    public void setUp() {
        client = new ScooterServiceClient(BASE_URI);
        prepareTestEnvironment();
    }

    @Step("Подготовка тестового окружения")
    public void prepareTestEnvironment() {
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrder_withDifferentColors() {
        Order order = new Order(
                "Данила", "Очаровательный", "ул. Ленина, д. 7", "4",
                "+79777777777", 4, "2025-5-10", "Я звонил ровно 5 минут назад", color);

        ValidatableResponse response = createOrder(order);
        response.statusCode(201).body("track", notNullValue());
    }

    @Step("Создание заказа с деталями: {0}")
    public ValidatableResponse createOrder(Order order) {
        return client.createOrder(order);
    }
}
