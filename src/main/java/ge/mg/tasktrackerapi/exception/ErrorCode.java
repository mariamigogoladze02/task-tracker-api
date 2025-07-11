package ge.mg.tasktrackerapi.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND("NOT_FOUND", 404),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401),
    USER_NOT_FOUND("USER_NOT_FOUND", 404),
    INVALID_TOKEN("INVALID_TOKEN", 401),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", 401),
    EXPIRED_TOKEN("EXPIRED_TOKEN", 401),
    USER_ALREADY_EXIST("USER_ALREADY_EXIST", 400),
    FORBIDDEN("FORBIDDEN", 403),
    SERVER_ERROR("SERVER_ERROR", 500),
    BAD_REQUEST_ID_MUST_NOT_BE_NULL("BAD_REQUEST_ID_MUST_NOT_BE_NULL", 400),
    PROJECT_NOT_FOUND("PROJECT_NOT_FOUND", 404);


    private final String series;
    private final Integer value;

    ErrorCode(String series, Integer value) {
        this.series = series;
        this.value = value;
    }

}