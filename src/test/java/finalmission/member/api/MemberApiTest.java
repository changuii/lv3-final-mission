package finalmission.member.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

import finalmission.fixture.ApiTestFixture;
import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.MemberRequest;
import finalmission.member.dto.NicknameResult;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
public class MemberApiTest {

    @MockitoBean
    private NameGenerator nameGenerator;

    @LocalServerPort
    private int port;

    @Nested
    @DisplayName("멤버 생성")
    class CreateMember {

        @DisplayName("멤버 정상 생성")
        @Test
        void create1() {
            final String email = "asd@naver.com";
            final String password = "pass";
            final MemberRequest request = new MemberRequest(email, password);

            final String nickname = "mock";
            given(nameGenerator.generateName())
                    .willReturn(new NicknameResult(nickname, false));

            RestAssured
                    .given()
                    .log().all()
                    .body(request)
                    .port(port)
                    .contentType(ContentType.JSON)
                    .when().post("/member")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("email", equalTo(email))
                    .body("nickname", equalTo(nickname));
        }

        @DisplayName("이미 존재하는 이메일인 경우 409을 응답한다.")
        @Test
        void create2() {
            final String email = "asd@naver.com";
            final String password = "pass";
            final MemberRequest request = new MemberRequest(email, password);

            final String nickname = "mock";
            given(nameGenerator.generateName())
                    .willReturn(new NicknameResult(nickname, false));

            ApiTestFixture.createMember(email, password, port);

            RestAssured
                    .given()
                    .log().all()
                    .body(request)
                    .port(port)
                    .contentType(ContentType.JSON)
                    .when().post("/member")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }

    }

}
