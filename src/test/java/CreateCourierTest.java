import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.UUID;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_ENDPOINT = "/api/v1/courier";
    private Courier testCourier;
    private int courierId;

    @Before
    public void setUp() {
        String uniqueLogin = "ninjadik_" + UUID.randomUUID().toString().substring(0, 8);
        testCourier = new Courier(uniqueLogin, "1234", "saske");
    }

    @Test
    @DisplayName("Создание курьера")
    public void a_createCourier_ok() {
        ValidatableResponse response = createCourier(testCourier);

        response.assertThat().statusCode(201).body("ok", equalTo(true));

        courierId = extractCourierId();
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void b_createDuplicateCourier_fail() {
        createCourier(testCourier);

        ValidatableResponse response = createCourier(testCourier);

        response.assertThat().statusCode(409).body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin_fail() {
        Courier courier = new Courier("", "password123", "Sasuke");
        ValidatableResponse response = createCourier(courier);

        response.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void d_createCourierWithoutPassword_fail() {
        Courier courier = new Courier("newUser", "", "Sasuke");
        ValidatableResponse response = createCourier(courier);

        response.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без firstName")
    public void f_createCourierWithoutFirstName_fail() {
        Courier courier = new Courier("ninjagainagain", "password123", "");
        ValidatableResponse response = createCourier(courier);

        response.assertThat().statusCode(400).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    @Step("Удаление курьера по ID")
    public void tearDown() {
        if (courierId > 0) {
            try {
                given()
                        .baseUri(BASE_URI)
                        .header("Content-Type", "application/json")
                        .when()
                        .delete(COURIER_ENDPOINT + "/" + courierId)
                        .then()
                        .log().all()
                        .assertThat().statusCode(200);
            } catch (Exception e) {
                System.err.println("Ошибка при удалении курьера: " + e.getMessage());
            }
        }
    }

    @Step("Создание курьера с данными")
    private ValidatableResponse createCourier(Courier courier) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post(COURIER_ENDPOINT)
                .then()
                .log().all();
    }
    @Step("Полученние данных курьера ID")
    private int extractCourierId() {
        return given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body("{\"login\": \"" + testCourier.getLogin() + "\", \"password\": \"" + testCourier.getPassword() + "\"}")
                .post(COURIER_ENDPOINT + "/login")
                .then()
                .extract()
                .path("id");
    }
}
