package info.btsland.app.exception;

/**
 * Created by lorne on 20/09/2017.
 */

public class CreateAccountException extends Exception {
    public CreateAccountException(String strMessage) {
        super(strMessage);
    }

    public CreateAccountException(Throwable throwable) {
        super(throwable);
    }
}
