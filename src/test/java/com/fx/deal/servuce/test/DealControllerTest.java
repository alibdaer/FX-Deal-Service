package com.fx.deal.servuce.test;

import com.fx.deal.servuce.test.dto.DealRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DealControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateDealSuccessfully() {
        DealRequest deal = new DealRequest();
        deal.setDealId("D-TEST-001");
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(1000.0);

        given()
                .contentType(ContentType.JSON)
                .body(deal)
                .when()
                .post("/deals")
                .then()
                .statusCode(HttpStatus.OK.value()) // 200
                .body(equalTo("Deal saved successfully"));
    }

    @Test
    void shouldReturn400ForInvalidDeal_MissingDealId() {
        DealRequest invalidDeal = new DealRequest();
        invalidDeal.setFromCurrency("EUR");
        invalidDeal.setToCurrency("USD");
        invalidDeal.setTimestamp(LocalDateTime.now());
        invalidDeal.setAmount(1500.0);

        given()
                .contentType(ContentType.JSON)
                .body(invalidDeal)
                .when()
                .post("/deals")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value()) // 400
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("dealId"))
                .body("errors[0].message", containsString("not be blank"));
    }

    @Test
    void shouldReturn400ForInvalidDeal_MissingAmount() {
        DealRequest invalidDeal = new DealRequest();
        invalidDeal.setDealId("D-TEST-002");
        invalidDeal.setFromCurrency("GBP");
        invalidDeal.setToCurrency("CAD");
        invalidDeal.setTimestamp(LocalDateTime.now());

        given()
                .contentType(ContentType.JSON)
                .body(invalidDeal)
                .when()
                .post("/deals")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value()) // 400
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].message", containsString("must not be null"));
    }

    @Test
    void shouldReturn409ForDuplicateDeal() {
        DealRequest deal = new DealRequest();
        deal.setDealId("D-TEST-003");
        deal.setFromCurrency("AUD");
        deal.setToCurrency("NZD");
        deal.setTimestamp(LocalDateTime.now());
        deal.setAmount(3000.0);

        given()
                .contentType(ContentType.JSON)
                .body(deal)
                .when()
                .post("/deals")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .body(deal)
                .when()
                .post("/deals")
                .then()
                .statusCode(HttpStatus.CONFLICT.value()) // 409
                .body(containsString("Duplicate dealId"));
    }

}