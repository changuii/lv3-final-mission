package finalmission.reservation.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;

import finalmission.fixture.ApiTestFixture;
import finalmission.reservation.domain.DateGenerator;
import finalmission.reservation.dto.ReservationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import java.time.LocalDate;
import java.time.LocalTime;
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
public class ReservationApiTest {

    @MockitoBean
    private DateGenerator dateGenerator;

    @LocalServerPort
    private int port;


    @Nested
    @DisplayName("예약 생성")
    class Create {

        @DisplayName("정상 예약 생성 테스트")
        @Test
        void create1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().post("/reservation")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("date", equalTo(reservationDate.toString()))
                    .body("member", notNullValue())
                    .body("restaurant", notNullValue());
        }

        @DisplayName("지난 날짜 예약이라면 400 응답을 반환한다.")
        @Test
        void create2() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 05, 20);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().post("/reservation")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("당일 예약이라면 400 응답을 반환한다.")
        @Test
        void create3() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 01);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().post("/reservation")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("다른 식당의 예약 시간 사용시 400을 응답한다.")
        @Test
        void create4() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);
            final Long anotherRestaurantId = ApiTestFixture.createRestaurant(authHeader, "다른 식당", port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long anotherReservationTimeId = ApiTestFixture.createReservationTime(authHeader, anotherRestaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, anotherReservationTimeId,
                    restaurantId);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().post("/reservation")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("id 기준 예약 상세 조회")
    class FindById{

        @DisplayName("정상 조회")
        @Test
        void findById1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().get("/reservation/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("자신의 소유가 아니라면 401을 응답한다.")
        @Test
        void findById2() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            final String anotherEmail = "asd1234@naver.com";
            ApiTestFixture.createMember(anotherEmail, password, port);
            final Header anotherAuthHeader = ApiTestFixture.createAuthHeader(anotherEmail, password, port);
            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(anotherAuthHeader)
                    .body(request)
                    .when().get("/reservation/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

    }

    @Nested
    @DisplayName("member 기준 전체 조회")
    class FindAllByEmail {

        @DisplayName("정상 조회")
        @Test
        void findAllByEmail1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().get("/reservation/member")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }
    }

    @Nested
    @DisplayName("식당, 날짜 기준 전체 조회")
    class FindAllByRestaurantAndDate {

        @DisplayName("식당 날짜 기준으로 예약을 전체 조회한다.")
        @Test
        void findAllByRestaurantAndDate1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final String anotherRestaurantName = "다른 식당";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);
            final Long anotherRestaurantId = ApiTestFixture.createRestaurant(authHeader, anotherRestaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);
            final Long anotherReservationTimeId = ApiTestFixture.createReservationTime(authHeader, anotherRestaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final LocalDate anotherReservationDate = LocalDate.of(2025, 06, 13);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            ApiTestFixture.createReservation(authHeader, reservationDate, anotherRestaurantId, anotherReservationTimeId, port);
            ApiTestFixture.createReservation(authHeader, anotherReservationDate, restaurantId, reservationTimeId, port);


            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().get("/reservation/restaurant/{id}?date={date}", restaurantId, reservationDate.toString())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(1));
        }

    }

    @Nested
    @DisplayName("예약 승인")
    class Accept {

        @DisplayName("예약 정상 승인")
        @Test
        void accept1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            RestAssured.given()
                    .port(port)
                    .header(authHeader)
                    .when()
                    .patch("/reservation/accept/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("식당 주인이 아니라면, 승인처리가 불가능하고 401 응답을 반환한다.")
        @Test
        void accept2() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String anotherEmail = "asd1234@naver.com";
            ApiTestFixture.createMember(anotherEmail, password, port);
            final Header anotherAuthHeader = ApiTestFixture.createAuthHeader(anotherEmail, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(anotherAuthHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            RestAssured.given()
                    .port(port)
                    .header(anotherAuthHeader)
                    .when()
                    .patch("/reservation/accept/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @DisplayName("대기 상태의 예약이 아니라면 400을 응답한다.")
        @Test
        void accept3() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            ApiTestFixture.acceptReservation(authHeader, reservationId, port);

            RestAssured.given()
                    .port(port)
                    .header(authHeader)
                    .when()
                    .patch("/reservation/accept/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

    }

    @Nested
    @DisplayName("예약 거부")
    class Reject {

        @DisplayName("예약 정상 거부")
        @Test
        void reject1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            RestAssured.given()
                    .port(port)
                    .header(authHeader)
                    .when()
                    .patch("/reservation/reject/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("식당 주인이 아니라면, 거부처리가 불가능하고 401 응답을 반환한다.")
        @Test
        void rejct2() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String anotherEmail = "asd1234@naver.com";
            ApiTestFixture.createMember(anotherEmail, password, port);
            final Header anotherAuthHeader = ApiTestFixture.createAuthHeader(anotherEmail, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(anotherAuthHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            RestAssured.given()
                    .port(port)
                    .header(anotherAuthHeader)
                    .when()
                    .patch("/reservation/reject/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @DisplayName("대기 상태의 예약이 아니라면 400을 응답한다.")
        @Test
        void reject3() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            ApiTestFixture.acceptReservation(authHeader, reservationId, port);

            RestAssured.given()
                    .port(port)
                    .header(authHeader)
                    .when()
                    .patch("/reservation/reject/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

    }

    @Nested
    @DisplayName("예약 삭제")
    class Delete {

        @DisplayName("정상 삭제")
        @Test
        void delete1() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(authHeader)
                    .body(request)
                    .when().delete("/reservation/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("자신이 소유한 예약만 삭제할 수 있다.")
        @Test
        void delete2() {
            final LocalDate today = LocalDate.of(2025, 06, 01);
            given(dateGenerator.today()).willReturn(today);

            final String email = "asd@naver.com";
            final String password = "1234";
            ApiTestFixture.createMember(email, password, port);
            final Header authHeader = ApiTestFixture.createAuthHeader(email, password, port);

            final String restaurantName = "식당이름";
            final Long restaurantId = ApiTestFixture.createRestaurant(authHeader, restaurantName, port);

            final LocalTime time = LocalTime.of(12, 30);
            final Long reservationTimeId = ApiTestFixture.createReservationTime(authHeader, restaurantId, time, port);

            final LocalDate reservationDate = LocalDate.of(2025, 06, 12);
            final ReservationRequest request = new ReservationRequest(reservationDate, reservationTimeId,
                    restaurantId);

            final Long reservationId = ApiTestFixture.createReservation(authHeader, reservationDate, restaurantId,
                    reservationTimeId, port);
            final String anotherEmail = "asd1234@naver.com";
            ApiTestFixture.createMember(anotherEmail, password, port);
            final Header anotherAuthHeader = ApiTestFixture.createAuthHeader(anotherEmail, password, port);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .header(anotherAuthHeader)
                    .body(request)
                    .when().delete("/reservation/{id}", reservationId)
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
