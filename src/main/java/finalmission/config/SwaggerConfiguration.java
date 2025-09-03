package finalmission.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "Authorization",
        description = "토큰을 입력하세요",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .security(getSecurityRequirement());
    }

    public Info getInfo() {
        Info info = new Info();
        info.setTitle("맛집 예약 서비스");
        return info;
    }

    private List<SecurityRequirement> getSecurityRequirement() {
        return List.of(new SecurityRequirement().addList("Authorization"));
    }
}
