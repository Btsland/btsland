package info.btsland.exchange.http;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/15.
 */

public class C2CHttp {
    protected static String url="http://172.25.234.1:8080/";
    protected static OkHttpClient client = new OkHttpClient();

    protected static void get(String action,Callback callback){
        Request request = new Request.Builder().url(url+action).build();
        Call call=client.newCall(request);
        if(callback!=null) {
            call.enqueue(callback);
        }
    }

    protected static void post(String action, Map<String,String> paramMap, Callback callback){
        FormBody.Builder builder=new FormBody.Builder();
        for(String name : paramMap.keySet()){
            builder.add(name,paramMap.get(name));
        }
        Request request = new Request.Builder().url(url+action).post(builder.build()).build();
        Call call=client.newCall(request);
        if(callback!=null) {
            call.enqueue(callback);
        }
    }


    public interface CallBacKListener{
       void onResponse(Call call, Response response);
    }
}
