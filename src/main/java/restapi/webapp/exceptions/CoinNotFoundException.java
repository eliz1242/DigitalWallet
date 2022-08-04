package restapi.webapp.exceptions;

import java.util.function.Supplier;

public class CoinNotFoundException extends RuntimeException{
    public CoinNotFoundException(Long id){
        super("There is not Coin corresponding to id = " + id);
        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
    public CoinNotFoundException(Object name,String key){
        super("There is not Coin corresponding with that"+key + name);
    }

}
