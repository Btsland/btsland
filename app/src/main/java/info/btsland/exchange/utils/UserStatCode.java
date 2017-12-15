package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/12.
 */
public class UserStatCode {
    public static final int ONLINE=1;
    public static final int OFFLINE=2;
    public static final int BEOUT=-1;

    public static boolean isInclude(int stat){
        if(stat==ONLINE){
            return true;
        }
        if(stat==OFFLINE){
            return true;
        }
        if(stat==BEOUT){
            return true;
        }
        return false;
    }
}