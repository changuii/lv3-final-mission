package finalmission.fixture;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import finalmission.member.dto.AuthRequest;
import finalmission.member.dto.AuthResponse;
import finalmission.member.dto.MemberRequest;
import finalmission.reservation.dto.ReservationDetailResponse;
import finalmission.reservation.dto.ReservationRequest;
import finalmission.reservationtime.dto.ReservationTimeRequest;
import finalmission.reservationtime.dto.ReservationTimeResponse;
import finalmission.restaurant.dto.RestaurantRequest;
import finalmission.restaurant.dto.RestaurantResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.http.HttpStatus;

public class ApiTestFixture {

    public static void createMember(final String email, final String password, int port) {
        final MemberRequest request = new MemberRequest(email, password);
        RestAssured
                .given()
                .log().all()
                .body(request)
                .port(port)
                .contentType(ContentType.JSON)
                .post("/member")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("email", equalTo(email))
                .body("nickname", notNullValue());
    }

    public static Header createAuthHeader(final String email, final String password, int port) {
        final AuthRequest authRequest = new AuthRequest(email, password);
        final String token = RestAssured.given()
                .log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(authRequest)
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue())
                .extract().as(AuthResponse.class)
                .token();

        return new Header("Authorization", token);
    }

    public static Long createRestaurant(final Header authHeader, final String name, final int port) {
        final RestaurantRequest restaurantRequest = new RestaurantRequest(name);
        return RestAssured.given()
                .log().all()
                .port(port)
                .header(authHeader)
                .contentType(ContentType.JSON)
                .body(restaurantRequest)
                .post("/restaurant")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(RestaurantResponse.class)
                .id();
    }

    public static Long createReservationTime(
            final Header authHeader,
            final Long restaurantId,
            final LocalTime time,
            final int port
    ) {
        final ReservationTimeRequest request = new ReservationTimeRequest(restaurantId, time);
        return RestAssured
                .given()
                .log().all()
                .header(authHeader)
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .post("/reservation-time")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(ReservationTimeResponse.class)
                .id();
    }

    public static Long createReservation(
            final Header authHeader,
            final LocalDate date,
            final Long restaurantId,
            final Long reservationTimeId,
            final int port
    ) {
        final ReservationRequest request = new ReservationRequest(date, reservationTimeId, restaurantId);
        return RestAssured.given()
                .log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .header(authHeader)
                .body(request)
                .post("/reservation")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(ReservationDetailResponse.class)
                .id();
    }

    public static void acceptReservation(final Header authHeader, final Long reservationId, final int port){
        RestAssured.given()
                .port(port)
                .header(authHeader)
                .when()
                .patch("/reservation/accept/{id}", reservationId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
