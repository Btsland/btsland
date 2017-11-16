package info.btsland.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Wallet_api;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.ui.activity.WelcomeActivity;
import info.btsland.app.util.InternetUtil;
import okhttp3.WebSocket;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication implements MarketStat.OnMarketStatUpdateListener{
    private static final String TAG="BtslandApplication";
    private static Context instance;

    private static BtslandApplication application;

    public static boolean isLogin=false;

    public static account_object accountObject=new account_object();
    public static List<IAsset> iAssets;
    public static boolean isWel=false;
    private static SharedPreferences sharedPreferences;

    private static MarketStat marketStat;
    public static WebSocket mWebsocket;
    private static Wallet_api walletApi;
    public static int nRet= Websocket_api.WEBSOCKET_CONNECT_INVALID;
    public static Map<String,List<MarketStat.HistoryPrice>> dataKMap=new HashMap<>();
    public static int _nDatabaseId = -1;
    public static int _nHistoryId = -1;
    public static int _nBroadcastId = -1;

    public static String[] bases={"CNY", "BTS", "USD", "BTC"};
    public static String[] quotes1={"BTC", "ETH", "BTS", "LTC", "OMG", "STEEM", "VEN", "HPB", "OCT", "YOYOW", "DOGE", "HASH"};
    public static String[] quotes2={"CNY","BTS", "USD", "OPEN.BTC", "OPEN.ETH", "YOYOW", "OCT", "OPEN.LTC", "OPEN.STEEM", "OPEN.DASH", "HPB", "OPEN.OMG", "IMIAO"};


    public static Wallet_api getWalletApi() {
        if(walletApi==null){
            walletApi=new Wallet_api();
        }
        return walletApi;
    }
    public static MarketStat getMarketStat() {
        if(marketStat==null){
            marketStat=new MarketStat();
        }
        return marketStat;
    }
    public BtslandApplication() {

    }

    public static Context getInstance() {
        return instance;
    }

    public static BtslandApplication getListener() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        instance=getApplicationContext();
        application=this;

        if(InternetUtil.isConnected(this)){
                ConnectThread();
        }else {
            Toast.makeText(this, "无法连接网络", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if(stat!=null){
            this.nRet=stat.nRet;
        }else {
            this.nRet=Websocket_api.WEBSOCKET_CONNECT_INVALID;
        }
        WelcomeActivity.sendBroadcast(getInstance(),this.nRet);
        queryAccount();

    }
    public static void ConnectThread(){
        MarketStat marketStat = getMarketStat();
        MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,getListener());
        connect.start();
    }


    /**
     * 登陆线程外部调用方法
     *
     */
    public static void queryAccount(){
        new QueryAccountThread().start();
    }


    /**
     * 登录线程
     *
     */
    private static class QueryAccountThread extends Thread{
        @Override
        public void run() {
            sharedPreferences=getInstance().getSharedPreferences("Login", Context.MODE_PRIVATE);
            String username=sharedPreferences.getString("username","");
            if (nRet!=Websocket_api.WEBSOCKET_CONNECT_SUCCESS){
                return;
            }
            if(username==null||username.equals("")){
                isLogin=false;
                return;
            }
            try {
                accountObject = getMarketStat().mWebsocketApi.get_account_by_name(username);
                queryAsset();
                //List<asset>  assets =getMarketStat().mWebsocketApi.list_account_balances(accountObject.id);
                if(accountObject!=null){
                    isLogin=true;
                }
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 查询用余额线程外部调用方法
     */
    public static void queryAsset(){
        new AccetThread().start();
    }

    /**
     * 查询用余额线程
     *
     */
    private static class AccetThread extends Thread{
        @Override
        public void run() {
            super.run();
            synchronized (accountObject.assetlist) {
                try {
                    List<asset> assets=getMarketStat().mWebsocketApi.list_account_balances_by_name(accountObject.name);
//                    List<asset> assets=getMarketStat().mWebsocketApi.list_account_balances_by_name("tiger5422");
                    accountObject.assetlist=assets;
                    iAssets=new ArrayList <>();
                    if(assets==null||assets.size()==0){
                        iAssets.add(new IAsset("CNY"));
                    }else {
                        for(int i=0;i<assets.size();i++) {
                            iAssets.add(new IAsset(assets.get(i)));
                        }
                    }
                } catch (NetworkStatusException e) {
                    e.printStackTrace();
                }

            }
        }
    }




}
