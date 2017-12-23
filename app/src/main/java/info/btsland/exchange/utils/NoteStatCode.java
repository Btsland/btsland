package info.btsland.exchange.utils;

/**
 * Created by Administrator on 2017/12/12.
 */
public class NoteStatCode {
    public static final int ACCOUNT_FILLING=0;
    public static final int ACCOUNT_TRANSFERRING=1;
    public static final int DEALER_CONFIRMING=2;
    public static final int DEALER_TRANSFERRING=3;
    public static final int HELP_CONFIRMING=4;
    public static final int ACCOUNT_CONFIRMED=7;
    public static final int HELP_CONFIRMED=6;
    public static final int ADMIN_CONFIRMED=8;
    public static final int ADMIN_TRANSFERRING=5;
    public static final int CANCELLED=-2;
    public static final int TIMEOUT=-1;

    public static final String CANCELED="已取消";
    public static final String OVERTIME="已超时";
    public static final String INITIATE="待发起";
    public static final String TRANSFER="待转账";
    public static final String TRADING="交易中";
    public static final String OK="已完成";


    public static String getTabAccount(int stat){
        switch (stat){
            case -2:
                return CANCELED;
            case -1:
                return OVERTIME;
            case 0:
                return INITIATE;
            case 1:
                return TRANSFER;
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
                return TRADING;
            case 6:
            case 8:
                return OK;
        }
        return "";
    }
    public static String getTabDealer(int stat){
        switch (stat){
            case -2:
                return "已取消";
            case -1:
                return "已超时";
            case 0:
                return "待发起";
            case 1:
                return "用户转账中";
            case 2:
                return "待确认收款";
            case 3:
                return "待发起提议";
            case 4:
                return "客服确认中";
            case 5:
                return "平台转账中";
            case 7:
                return "用户已确认转账";
            case 6:
                return "已完成";
            case 8:
                return "已完成";
        }
        return "";
    }
}
