package info.btsland.exchange.http;


import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import info.btsland.exchange.entity.User;
import okhttp3.Callback;

public class UserHttp extends C2CHttp {
    private static String TAG="UserHttp";

    /**
     * 承兑商登录
     * @param dealerId
     * @param password
     * @param callback
     */
    public static void loginDealer(String dealerId, String password, Callback callback){
        String action="/user/loginDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("password",password);
        post(action,paramMap,callback);
    }

    /**
     * 用户登录
     * @param account
     * @param callback
     */
    public static void loginAccount(String account,Callback callback){
        String action="/user/loginAccount";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        post(action,paramMap,callback);
    }

    /**
     * 管理员登录
     * @param account
     * @param password
     * @param callback
     */
    public static void loginAdmin(String account, String password,Callback callback){
        String action="/user/loginAdmin";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("password",password);
        post(action,paramMap,callback);
    }

    /**
     * 查询承兑商信息
     * @param dealerId
     * @param callback
     */
    public static void queryDealer(String dealerId,Callback callback){
        String action="/user/queryDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询用户信息
     * @param account
     * @param callback
     */
    public static void queryAccount(String account,Callback callback){
        String action="/user/queryAccount";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        post(action,paramMap,callback);
    }

    /**
     * 查询全部承兑商
     * @param stat
     * @param callback
     */
    public static void queryAllDealer(int stat,Callback callback){
        String action="/user/queryAllDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("stat", String.valueOf(stat));
        post(action, paramMap,callback);
    }

    /**
     * 更新状态
     * @param dealerId
     * @param stat
     * @return
     */
    public static void updateStat(String dealerId,String password,int stat,Callback callback){
        String action="/user/updateStat";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId", dealerId);
        paramMap.put("password", password);
        paramMap.put("stat", String.valueOf(stat));
        post(action, paramMap,callback);
    }

    /**
     * 注册用户
     * @param accountName
     * @param password
     * @param callback
     */
    public static void registerAccount(String accountName,String password,Callback callback){
        String action="/user/registerAccount";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("accountName", accountName);
        paramMap.put("password", password);
        post(action, paramMap,callback);
    }

    /**
     * 更新用户
     * @param dealerId
     * @param user
     * @param callback
     */
    public static void updateUser(String dealerId, User user, Callback callback){
        String action="/user/updateUser";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId", dealerId);
        Gson gson=new Gson();
        Log.e(TAG, "updateUser: "+user );
        String user1=gson.toJson(user);
        paramMap.put("user", user1);
        post(action, paramMap,callback);
    }
}
