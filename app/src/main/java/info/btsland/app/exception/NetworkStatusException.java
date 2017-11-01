package info.btsland.app.exception;


import android.util.Log;

public class NetworkStatusException extends Exception {
    private String TAG ="NetworkStatusException";
    public NetworkStatusException(String strMessage) {
        super(strMessage);
    }

    public NetworkStatusException(Throwable throwable) {
        super(throwable);
    }
}
