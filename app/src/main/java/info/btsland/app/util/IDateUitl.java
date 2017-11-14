package info.btsland.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/14.
 */

public class IDateUitl {
    /**
     * 转化为国际标准时间
     * @param date
     * @return
     */
    public static Date toUniversalTime(Date date){
        long time=date.getTime();
        SimpleDateFormat df = new SimpleDateFormat("zzz");//设置日期格式
        String str=df.format(date);
        int num = Integer.parseInt(str.substring(4,6));
        if(str.substring(3,4).equals("+")){
            time = time - TimeUnit.HOURS.toMillis(num);
        }else if(str.substring(3,4).equals("-")){
            time = time + TimeUnit.HOURS.toMillis(num);
        }
        return new Date(time);
    }
    public static Date toLocalTime(Date date){
        long time=date.getTime();
        SimpleDateFormat df = new SimpleDateFormat("zzz");
        String str=df.format(new Date());//获得时区
        int num = Integer.parseInt(str.substring(4,6));
        if(str.substring(3,4).equals("+")){
            time = time + TimeUnit.HOURS.toMillis(num);
        }else if(str.substring(3,4).equals("-")){
            time = time - TimeUnit.HOURS.toMillis(num);
        }
        return new Date(time);
    }
}
