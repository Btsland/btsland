package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/12.
 */
public class UserTypeCode {
    public static final int ACCOUNT=0;
    public static final int AUDITING=2;
    public static final int DEALER=3;
    public static final int HELP=4;
    public static final int ADMIN=5;

    public static boolean isInclude(int tpye){
        if(tpye==ACCOUNT){
            return true;
        }
        if(tpye==AUDITING){
            return true;
        }
        if(tpye==DEALER){
            return true;
        }
        if(tpye==HELP){
            return true;
        }
        if(tpye==ADMIN){
            return true;
        }
        return false;
    }

}