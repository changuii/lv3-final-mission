package finalmission.member.api;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.*;

import finalmission.fixture.ApiTestFixture;
import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.AuthRequest;
import finalmission.member.dto.NicknameResult;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
public class AuthApiTest {

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
    @DisplayName("로그인")
    class Login {

        @DisplayName("정상 로그인 테스트")
        @Test
        void login1() {
            final String email = "asd@naver.com";
            final String password = "pass";
            final AuthRequest authRequest = new AuthRequest(email, password);

            ApiTestFixture.createMember(email, password, port);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(authRequest)
                    .when().post("/auth/login")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("token", notNullValue());
        }

        @DisplayName("존재하지 않는 이메일이라면 404를 응답한다.")
        @Test
        void login2() {
            final String email = "asd@naver.com";
            final String password = "pass";
            final AuthRequest authRequest = new AuthRequest(email, password);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(authRequest)
                    .when().post("/auth/login")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("잘못된 비밀번호라면 400을 응답한다.")
        @Test
        void login3() {
            final String email = "asd@naver.com";
            final String password = "pass";
            final AuthRequest authRequest = new AuthRequest(email, password);

            ApiTestFixture.createMember(email, "pass1", port);

            RestAssured.given()
                    .log().all()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(authRequest)
                    .when().post("/auth/login")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
