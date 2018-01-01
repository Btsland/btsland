package info.btsland.exchange.http;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import info.btsland.exchange.entity.RealAsset;
import okhttp3.Callback;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RealAssetHttp extends C2CHttp {
    private static final String TAG="RealAssetHttp";
    public static void updateRealAsset(String dealerId,
                                String account, RealAsset realAsset1, Callback callback){
        String action="/realAsset/updateRealAsset";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("account",account);
        Gson gson=new Gson();
        String realAsset=gson.toJson(realAsset1);
        paramMap.put("realAsset",realAsset);
        Log.e(TAG, "updateRealAsset: "+realAsset );
        post(action,paramMap,callback);
    }
    public static void saveRealAsset(String dealerId,String password,
                              String account,RealAsset realAsset1, Callback callback){
        String action="/realAsset/saveRealAsset";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("password",password);
        paramMap.put("account",account);
        Gson gson=new Gson();
        String realAsset=gson.toJson(realAsset1);
        paramMap.put("realAsset",realAsset);
        post(action,paramMap,callback);
    }
    public static void removeRealAsset(String dealerId,String password,
                                String account,RealAsset realAsset1, Callback callback){
        String action="/realAsset/removeRealAsset";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        paramMap.put("password",password);
        paramMap.put("account",account);
        Gson gson=new Gson();
        String realAsset=gson.toJson(realAsset1);
        paramMap.put("realAsset",realAsset);
        post(action,paramMap,callback);
    }

    public static void queryRealAsset(String dealerId,Callback callback){
        String action="/realAsset/queryRealAsset";
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("dealerId",dealerId);
        post(action,paramMap,callback);
    }
}
