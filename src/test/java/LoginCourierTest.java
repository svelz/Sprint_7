import client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    private Courier courier;
    private ScooterServiceClient client;

    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
        String uniqueLogin = "ninka_" + UUID.randomUUID().toString().substring(0, 8);
        courier = new Courier(uniqueLogin, "password52", "boyarski");

        createCourier(courier);
    }

    @Step("Создание курьера с логином и паролем")
    public void createCourier(Courier courier) {
        ValidatableResponse response = client.createCourier(courier);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void login_ok() {
        Credentials credentials = Credentials.fromCourier(courier);

        ValidatableResponse response = client.login(credentials);
        response
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при отсутствии логина или пароля")
    public void login_missingCredentials() {
        Credentials invalidCredentials = new Credentials("", "");

        ValidatableResponse response = client.login(invalidCredentials);
        response
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при неправильных логине или пароле")
    public void login_invalidCredentials() {
        Credentials invalidCredentials = new Credentials("wrongLogin", "wrongPassword");

        ValidatableResponse response = client.login(invalidCredentials);
        response
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при авторизации под несуществующим пользователем")
    public void login_nonExistentCourier() {
        Credentials nonexistentCourier = new Credentials("nonExistentUser", "nonExistentPassword");

        ValidatableResponse response = client.login(nonexistentCourier);
        response
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @Step("Удаление курьера с ID")
    public void after() {
        String courierId = String.valueOf(courier.getId());
        if (!courierId.isEmpty()) {
            client.deleteCourier(courierId);
        }
    }
}
