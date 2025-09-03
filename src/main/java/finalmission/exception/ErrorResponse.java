package finalmission.exception;

public record ErrorResponse(
        String message,
        int httpStatusCode
) {
}
