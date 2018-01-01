package info.btsland.app.util;

import info.btsland.app.model.News;
import okhttp3.Callback;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class NewsHttp extends NewsConnectionUtil{


    /**
     *查询Btsland
     * @param callback
     */
    public static void queryBtsland(Callback callback){
        String action="/TypeQuery/Btsland";
        get(action,callback);
    }
    /**
     * 查询IFAST
     * @param callback
     */
    public static void queryIFAST(Callback callback){
        String action="/TypeQuery/IFAST";
        get(action,callback);
    }

    /**
     * 查询最新资讯
     * @param callback
     */
    public static void queryLatest( Callback callback){
        String action="/news/TypeQuery/L";
        get(action,callback);
    }
    /**
     *查询热点资讯
     * @param callback
     */
    public static void queryHot(Callback callback){
        String action="/news/TypeQuery/H";
        get(action,callback);
    }
    /**
     *根据newsId查询本条新闻
     * @param callback
     */
    public static void queryById(int newsId, Callback callback){
        String action="/news/TypeQuery/"+newsId;
        get(action,callback);
    }



}
