package info.btsland.exchange.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ChatHttp extends C2CHttp {
    public static void queryChat(String from, String to, Callback callback){
        String action="/chat/queryChat";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("from",from);
        paramMap.put("to",to);
        post(action,paramMap,callback);
    }

    public static void sendChat(String from,String context,String to,Callback callback){
        String action="/chat/sendChat";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("from",from);
        paramMap.put("context",context);
        paramMap.put("to",to);
        post(action,paramMap,callback);
    }
    public static void deleteChar(String from,String to,String time,Callback callback) {
        String action="/chat/deleteChar";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("from",from);
        paramMap.put("time",time);
        paramMap.put("to",to);
        post(action,paramMap,callback);
    }

}
