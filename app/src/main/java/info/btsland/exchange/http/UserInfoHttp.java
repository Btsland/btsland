package info.btsland.exchange.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;


public class UserInfoHttp extends C2CHttp {
    /**
     * 查询承兑商信息
     * @param dealerId
     * @param password
     * @param callback
     */
    public void queryUserInfoByPwd( String dealerId, String password, Callback callback){
        String action="/userInfo/queryUserInfoByPwd";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("password",password);
        post(action,paramMap,callback);
    }

    /**
     * 查询承兑商信息
     * @param dealerId
     * @param callback
     */
    public void queryUserInfo(String dealerId, Callback callback){
        String action="/userInfo/queryUserInfo";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }
}
