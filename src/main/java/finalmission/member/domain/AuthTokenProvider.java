package finalmission.member.domain;

public interface AuthTokenProvider {
    String generateToken(String email);
}
