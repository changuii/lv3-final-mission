package finalmission.member.infrastructure;

import finalmission.member.domain.NameGenerator;
import finalmission.member.dto.NicknameResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RandomNameGenerator implements NameGenerator {

    private final RestClient restClient;

    public RandomNameGenerator(
            final RestClient.Builder restClientBuilder,
            @Value("${api.random}") final String apiKey
    ) {
        restClient = restClientBuilder
                .baseUrl("https://randommer.io/api/Name?nameType=fullname&quantity=1")
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    @Override
    public NicknameResult generateName() {
        final ResponseEntity<List<String>> response = restClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<String>>() {
                });

        if (response.getStatusCode().isError()) {
            return new NicknameResult(
                    "error",
                    true
            );
        }
        return new NicknameResult(
                response.getBody().getFirst(),
                false
        );
    }
}
