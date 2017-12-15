package info.btsland.exchange;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import info.btsland.exchange.utils.UserStatCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/12.
 */

public class C2C {
    private final String TAG="C2C";

    private String url="http://172.25.234.1:8080/";
    private OkHttpClient client = new OkHttpClient();

    private C2CResponseListener listener;

    public static C2C newInstance() {
        C2C c2C = new C2C();
        return c2C;
    }

    public C2C setListener(C2CResponseListener listener) {
        this.listener = listener;
        return this;
    }

    public void queryAllDealer() throws IOException {
        Request request = new Request.Builder().url(url+"/user/queryAllDealer/"+ UserStatCode.BEOUT).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });

    }
    public void post(String action, List<String> keys,List<String> values) throws IOException {


    }

    public void queryHavingNote(String dealerId)throws IOException {
        Log.e(TAG, "run: ");
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("dealerId",dealerId);
        Request request = new Request.Builder().url(url+"/note/queryAllNote/").post(builder.build()).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });
    }

    public interface C2CResponseListener {
        void onResponse(Call call, Response response);
    }

}
