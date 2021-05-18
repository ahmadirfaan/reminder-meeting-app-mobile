package application.remindermeeting.exceptions;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException{
    //Membuat Exception sendiri agak yang ditampilkan dapat diketahui oleh Front-End
    private HttpStatus status;

    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
