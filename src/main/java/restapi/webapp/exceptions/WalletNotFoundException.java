package restapi.webapp.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(Long id){
        super("There is not Wallet corresponding to id = " + id);
        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
    public WalletNotFoundException(Object msg,String key){
        super("There is not Wallet corresponding to " + key + msg);
    }
}
