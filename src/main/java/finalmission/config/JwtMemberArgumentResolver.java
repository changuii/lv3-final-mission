package finalmission.config;

import finalmission.config.exception.TokenInvalidException;
import finalmission.member.infrastructure.JwtAuthTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String jwt = extractToken(webRequest);
        validateJwtToken(jwt);
        return jwtAuthTokenProvider.extractSubject(jwt);
    }

    private void validateJwtToken(final String jwt) {
        if (jwt == null || !jwtAuthTokenProvider.isValidJwt(jwt)) {
            throw new TokenInvalidException();
        }
    }

    private String extractToken(final NativeWebRequest webRequest) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return request.getHeader("Authorization");
    }
}
