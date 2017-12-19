package info.btsland.exchange.http;


import java.util.HashMap;
import java.util.Map;
import okhttp3.Callback;

public class NoteHttp extends C2CHttp {

    /**
     * 查询进行中的订单
     * @param dealerId
     * @return
     */
    public static void queryAllHavingNote(String dealerId, Callback callback){
        String action="/note/queryAllHavingNoteDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询进行中的订单
     * @param dealerId
     * @return
     */
    public static void queryAllHavingNote(String dealerId,String coin,Callback callback){
        String action="/note/queryAllHavingNoteByDealerAndCoin";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("coin",coin);
        post(action,paramMap,callback);
    }

    /**
     * 查询已完成订单
     * @param dealerId
     * @return
     */
    public static void queryAllClinchNote(String dealerId,Callback callback){
        String action="/note/queryAllClinchNoteDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询已完成订单
     * @param dealerId
     * @return
     */
    public static void queryAllClinchNote(String dealerId,String coin,Callback callback){
        String action="/note/queryAllClinchNoteByDealerAndCoin";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("coin",coin);
        post(action,paramMap,callback);
    }

    /**
     * 根据流水号查询一条订单
     * @param noteNo
     * @return
     */
    public static void queryNote(String noteNo,Callback callback){
        String action="/note/queryNote";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("noteNo",noteNo);
        post(action,paramMap,callback);
    }

    /**
     * 根据承兑商查询订单数
     * @param dealerId
     * @return
     */
    public static void queryNoteCount(String dealerId,Callback callback){
        String action="/note/queryNoteCount";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }

    /**
     * 查询用户完成的交易记录
     * @param account
     * @param coin
     * @return
     */
    public static void queryAllClinchNoteByAccount(String account,String coin,Callback callback){
        String action="/note/queryNoteClinchCountByAccountAndCoin";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("coin",coin);
        post(action,paramMap,callback);
    }

    /**
     * 查询用户进行中的的交易记录
     * @param account
     * @param coin
     * @return
     */
    public static void queryAllHavingNoteByAccount(String account,String coin,Callback callback){
        String action="/note/queryNoteHavingCountByAccountAndCoin";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("coin",coin);
        post(action,paramMap,callback);
    }
}
