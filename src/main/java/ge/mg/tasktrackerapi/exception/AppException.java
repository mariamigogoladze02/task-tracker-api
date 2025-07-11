package ge.mg.tasktrackerapi.exception;

public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getSeries());
        this.errorCode = errorCode;
        this.message = errorCode.getSeries();
    }

    public AppException(ErrorCode errorCode, String message) {
        super(errorCode.getSeries());
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}