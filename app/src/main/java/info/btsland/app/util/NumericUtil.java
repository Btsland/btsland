package info.btsland.app.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * <pre>
 * 数值相关工具类
 * </pre>
 *
 * @author: piorpua
 * @since: 2017/9/21
 */
public final class NumericUtil {

    private NumericUtil() {}

    public static long parseLong(@Nullable String value) {
        return parseLong(value, 0L);
    }

    public static long parseLong(@Nullable String value, long defaultVal) {
        if (TextUtils.isEmpty(value)) {
            return defaultVal;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }

    public static double parseDouble(@Nullable String value) {
        return parseDouble(value, 0.0D);
    }

    public static double parseDouble(@Nullable String value, double defaultVal) {
        if (TextUtils.isEmpty(value)) {
            return defaultVal;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }

    public static String doubleToString(double n){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double b=0.0;
        String str="";
        if(n < 1000) {
            str = decimalFormat.format(n);
        } else if(n >=1000 && n < 1000000){
            b = n/1000;
            str=decimalFormat.format(b)+"K";
        }else if(n >=1000000){
            b = n/1000000;
            str = decimalFormat.format(b)+"M";
        }
        return str;
    }
}
