package finalmission.restaurant.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;

import finalmission.fixture.ApiTestFixture;
import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.NicknameResult;
import finalmission.restaurant.dto.RestaurantRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:init.sql")
public class RestaurantApiTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private NameGenerator nameGenerator;

    @BeforeEach
    void setUp() {
        NicknameResult mockNickname = new NicknameResult("MOCK", false);
        given(nameGenerator.generateName()).willReturn(mockNickname);
    }

    @Nested
    @DisplayName("식당 생성")
    class Create {

        @DisplayName("정상 식당 생성")
        @Test
        void create1() {
            final String email = "asd123@naver.com";
            final String password = "pass";

            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final RestaurantRequest restaurantRequest = new RestaurantRequest(restaurantName);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .header(authHeader)
                    .contentType(ContentType.JSON)
                    .body(restaurantRequest)
                    .when().post("/restaurant")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo(restaurantName));
        }
    }

    @Nested
    @DisplayName("전체 식당 조회")
    class FindAll {

        @DisplayName("전체 식당 조회")
        @Test
        void findAll() {
            final String email = "asd@naver.com";
            final String password = "1234";

            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            ApiTestFixture.createRestaurant(authHeader, "restaurant1", port);
            ApiTestFixture.createRestaurant(authHeader, "restaurant2", port);
            ApiTestFixture.createRestaurant(authHeader, "restaurant3", port);

            RestAssured.given()
                    .port(port)
                    .when()
                    .get("/restaurant")
                    .then()
                    .body("size()", equalTo(3));
        }
    }

}
