package service.Exception;

public class UserCrudException extends RuntimeException {
    public UserCrudException(String message) {
        super(message);
    }
}
