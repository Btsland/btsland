package info.btsland.app.util;

/**
 * Created by Administrator on 2017/11/23.
 */

public class  KeyUtil {
    public static String constructingDateKKey(String base,String quote,long bucket,long ago){
        return ""+base+"/"+quote+":"+bucket+","+ago;
    }
    public static String constructingOrderBooksKey(String base,String quote){
        return "";
    }
}
