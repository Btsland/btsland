package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UserStatUtil {
    public static String getUserStat(int stat) {
        String s="";
        switch (stat){
            case 1:
                s="在线中";
                break;
            case 2:
                s="离开";
                break;
            case -1:
                s="离线";
                break;
        }
        return s;
    }
}

