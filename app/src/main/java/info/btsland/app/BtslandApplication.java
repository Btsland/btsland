package info.btsland.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.TimeUtils;
import android.widget.Toast;

import com.github.mikephil.charting.data.CandleEntry;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.model.DataK;
import info.btsland.app.ui.activity.MainActivity;
import info.btsland.app.ui.activity.WelcomeActivity;
import info.btsland.app.util.InternetUtil;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication implements MarketStat.OnMarketStatUpdateListener{
    private static Context instance;

    private static BtslandApplication application;

    public static boolean isWel=false;

    public static MarketStat marketStat;
    public static int nRet= Websocket_api.WEBSOCKET_CONNECT_INVALID;
    public static Map<String,DataK> dataKMap=new HashMap<>();
    public static int _nDatabaseId = -1;
    public static int _nHistoryId = -1;
    public static int _nBroadcastId = -1;

    public static String[] bases={"CNY", "BTS", "USD", "BTC"};
    public static String[] quotes1={"BTC", "ETH", "BTS", "LTC", "OMG", "STEEM", "VEN", "HPB", "OCT", "YOYOW", "DOGE", "HASH"};
    public static String[] quotes2={"BTS", "USD", "OPEN.BTC", "OPEN.ETH", "YOYOW", "OCT", "OPEN.LTC", "OPEN.STEEM", "OPEN.DASH", "HPB", "OPEN.OMG", "IMIAO"};


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
        instance=getApplicationContext();
        application=this;
        if(InternetUtil.isConnected(this)){
            ConnectThread thread=new ConnectThread();
            thread.start();
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
    }
    public static class ConnectThread extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            MarketStat marketStat = getMarketStat();
            if(marketStat.connectHashMap!=null){
                MarketStat.Connect connect = marketStat.connectHashMap.get("connect");
                if(connect!=null){
                    connect.updateConnect();
                }
                else{
                    marketStat.connect(MarketStat.STAT_COUNECT,getListener());
                }
            }
        }
    }

}
