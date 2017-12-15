package info.btsland.exchange.http;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

public class AllegeHttp extends C2CHttp {

    /**
     * 创建申述单
     * @param account
     * @param noteNo
     * @param callback
     */
    public void createAllege(String account, String noteNo, Callback callback){
        String action="/allege/createAllege";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("noteNo",noteNo);
        post(action,paramMap,callback);
    }

    /**
     * 更新申述单状态
     * @param noteNo
     * @param stat
     * @param callback
     */
    public void updateAllege(String noteNo,int stat,Callback callback){
        String action="/allege/updateAllege";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("noteNo",noteNo);
        paramMap.put("stat", String.valueOf(stat));
        post(action,paramMap,callback);
    }

    /**
     * 根据用户查询申述单
     * @param account
     * @param callback
     */
    public void queryAllAllege(String account,Callback callback){
        String action="/allege/queryAllAllegeByAccount";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        post(action,paramMap,callback);
    }


    /**
     * 根据承兑商查询申述单
     * @param dealerId
     * @param callback
     */
    public void queryAllAllegeByDealerId(String dealerId,Callback callback){
        String action="/allege/queryAllAllegeByDealerId";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询全部
     * @param callback
     */
    public void queryAllAllege(Callback callback){
        String action="/allege/queryAllAllege";
        get(action,callback);
    }

    /**
     * 根据状态查询申述单
     * @param stat
     * @param callback
     */
    public void queryAllAllege(int stat,Callback callback){
        String action="/allege/queryAllAllegeByStat";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("stat", String.valueOf(stat));
        post(action,paramMap,callback);
    }
}
