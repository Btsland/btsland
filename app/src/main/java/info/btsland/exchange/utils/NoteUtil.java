package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/13.
 */

public class NoteUtil {
    public static int isOk(int stat){
        switch (stat){
            case -2:
            case -1:
                return -1;
            case 6:
            case 8:
            case 13:
            case 14:
                return 1;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 11:
            case 12:
            case 0:
                return 0;
        }
        return 0;
    }
}
