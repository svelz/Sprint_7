package client;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import model.Order;

public class ScooterServiceClient {

    private String baseURI;

    public ScooterServiceClient(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Клиент – получение списка заказов с параметрами")
    public ValidatableResponse getOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", nearestStation)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .get("/api/v1/orders")
                .then()
                .log().all();
    }

    @Step("Клиент – создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log().all();
    }

    @Step("Клиент – логин курьера")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log().all();
    }

    @Step("Клиент – удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + id)
                .then()
                .log().all();
    }

    @Step("Клиент – создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .log().all();
    }
}
