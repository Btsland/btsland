package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/12.
 */
public class AllegeStatCode {
    public static final int ACCOUNT_APPEALING = 11;
    public static final int ADMIN_CONFIRMED = 13;
    public static final int ACCOUNT_CONFIRMED = 14;
    public static final int PROCESSING = 12;
    public static boolean isInclude(int stat){
        if(stat==ACCOUNT_APPEALING){
            return true;
        }
        if(stat==ADMIN_CONFIRMED){
            return true;
        }
        if(stat==ACCOUNT_CONFIRMED){
            return true;
        }
        if(stat==PROCESSING){
            return true;
        }
        return false;
    }

    public static String getTabDealer(int stat){
        switch (stat){
            case 11:
                return "用户发起申述";
            case 12:
                return "待处理";
            case 13:
                return "平台已处理";
            case 14:
                return "用户已接受处理结果";
        }
        return "";
    }
    public static String getTabAccount(int stat){
        switch (stat){
            case 11:
                return "用户发起申述";
            case 12:
                return "正在处理中";
            case 13:
            case 14:
                return "已处理";
        }
        return "";
    }
}
