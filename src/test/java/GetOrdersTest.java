import client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private ScooterServiceClient client;

    @Before
    public void setUp() {
        client = new ScooterServiceClient(BASE_URI);
    }

    @Test
    @DisplayName("Получение списка заказов без параметров")
    public void getOrders_noParams() {
        ValidatableResponse response = client.getOrders(null, null, null, null);
        response.statusCode(200).body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов по courierId")
    public void getOrders_byCourierId() {
        int courierId = 466191;
        ValidatableResponse response = client.getOrders(courierId, null, null, null);
        response.statusCode(200).body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов с фильтрацией по станции метро")
    public void getOrders_byNearestStation() {
        String nearestStations = "[\"2\", \"4\"]";
        ValidatableResponse response = client.getOrders(null, nearestStations, null, null);
        response.statusCode(200).body("orders", notNullValue());
    }
}
