package restapi.webapp.exceptions;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("There is not User corresponding to id = " + id);
        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
    public UserNotFoundException(String name,String key){
        super("There is not User corresponding with that"+key + name);
    }

}
