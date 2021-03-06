package info.btsland.exchange.http;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

public class TradeHttp extends C2CHttp {

    /**
     * 查询承兑商的总订单数
     * @param dealerId
     * @param callback
     */
    public static void queryNoteCountByDealerId(String dealerId,Callback callback){
        String action="/trade/queryNoteCountByDealerId";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 创建订单
     * @param account
     * @param coin
     * @param dealerId
     * @param callback
     */
    public static void createNote(String account,String coin,String dealerId,Callback callback){
        String action="/trade/createNote";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("coin",coin);
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 保存订单
     * @param account
     * @param noteJson
     * @param callback
     */
    public static void saveNote(String account,String noteJson,Callback callback){
        String action="/trade/saveNote";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("noteJson",noteJson);
        post(action,paramMap,callback);
    }

    /**
     * 更新订单
     * @param noteNo
     * @param realNo
     * @param realType
     * @param realDepict
     * @param callback
     */
    public static void updateNoteReal(String noteNo,String realNo,String realType,String realDepict,Callback callback) {
        String action="/trade/updateNoteReal";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("noteNo",noteNo);
        paramMap.put("realNo",realNo);
        paramMap.put("realType",realType);
        paramMap.put("realDepict",realDepict);
        post(action,paramMap,callback);
    }
    /**
     * 更新订单状态
     * @param noteNo
     * @param stat
     * @param callback
     */
    public static void updateNoteStat(String noteNo,int stat,Callback callback){
        String action="/trade/updateNoteStat";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("noteNo",noteNo);
        paramMap.put("stat", String.valueOf(stat));
        post(action,paramMap,callback);
    }

    /**
     * 更新备注
     * @param noteNo
     * @param depict
     * @param callback
     */
    public static void updateNoteDepict(String noteNo,String depict,Callback callback){
        String action="/trade/updateNoteDepict";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("noteNo",noteNo);
        paramMap.put("depict",depict);
        post(action,paramMap,callback);
    }
}
