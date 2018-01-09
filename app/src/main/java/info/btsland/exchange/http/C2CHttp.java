package info.btsland.exchange.http;

import java.util.Map;

import info.btsland.app.BtslandApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/12/15.
 */

public class C2CHttp {
    protected static OkHttpClient client = new OkHttpClient();

    private static String TAG="C2CHttp";

    protected static void get(String action,Callback callback){
        String url="http://"+ BtslandApplication.ipServer+":8080/";
        Request request = new Request.Builder().url(url+action).build();
        Call call=client.newCall(request);
        if(callback!=null) {
            call.enqueue(callback);
        }
    }

    protected static void post(String action, Map<String,String> paramMap, Callback callback){
        String url="http://"+ BtslandApplication.ipServer+":8080/";
        FormBody.Builder builder=new FormBody.Builder();
        for(String name : paramMap.keySet()){
            String value=paramMap.get(name);
            if(value==null){
                value="";
            }
            builder.add(name,value);
        }
        Request request = new Request.Builder().url(url+action).post(builder.build()).build();
        Call call=client.newCall(request);
        if(callback!=null) {
            call.enqueue(callback);
        }
    }

}
