package info.btsland.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/10/11.
 */

public class PreferenceUtil {
    private static SharedPreferences mSharedPreferences = null;
    private static SharedPreferences.Editor mEditor = null;

    public static void init(Context context) {
        if (null == mSharedPreferences) {
            mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void removeKey(String key) {
        mEditor = mSharedPreferences.edit();
        mEditor.remove(key);
        mEditor.commit();
    }

    public static void removeAll() {
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }

    public static void commitString(String key, String value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static String getString(String key, String faillValue) {
        return mSharedPreferences.getString(key, faillValue);
    }

    public static void commitInt(String key, int value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public static int getInt(String key, int failValue) {
        return mSharedPreferences.getInt(key, failValue);
    }

    public static void commitLong(String key, long value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public static long getLong(String key, long failValue) {
        return mSharedPreferences.getLong(key, failValue);
    }

    public static void commitBoolean(String key, boolean value) {
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static Boolean getBoolean(String key, boolean failValue) {
        return mSharedPreferences.getBoolean(key, failValue);
    }


//    /**
//     * 设置主题
//     *
//     * @param context
//     * @return true 表示明亮主题 false 表示暗主题
//     */
//    public static void setTheme(Context context, boolean is) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(null, Context.MODE_PRIVATE);
//        mEditor.putBoolean("theme", is).commit();
//    }
//
//    /**
//     * 得到主题
//     *
//     * @param context
//     * @return
//     */
//    public static boolean getTheme(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(null, Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean("theme", false);
//    }

//    public static void commitTheme(Context context,boolean value){
//        SharedPreferences sf = context.getSharedPreferences("cons",context.MODE_PRIVATE);
//        mEditor.putBoolean("theme",value);
//        mEditor.commit();
//    }




}
