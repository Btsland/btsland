package info.btsland.exchange.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class HelpHttp extends C2CHttp {
    public static void queryDealer(String helpId, Callback callback){
        String action="/help/queryDealer";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("helpId",helpId);
        post(action,paramMap,callback);
    }
}
