package info.btsland.exchange.http;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

public class UserHttp extends C2CHttp {

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
    public static void queryUser(String dealerId,Callback callback){
        String action="/user/queryUser";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询全部承兑商
     * @param stat
     * @param Callback
     */
    public static void queryAllDealer(int stat,Callback Callback){
        String action="/user/queryAllDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("stat", String.valueOf(stat));
        post(action, paramMap,Callback);
    }
}
