package info.btsland.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.github.mikephil.charting.data.CandleEntry;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Websocket_api;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication{
    private static Context instance;

    public static boolean isWel=false;

    public static MarketStat marketStat;
    public static int nRet= Websocket_api.WEBSOCKET_CONNECT_INVALID;
    public static List<CandleEntry> candleEntries=new ArrayList<>();//烛形图数据

    public static List<MarketStat.HistoryPrice> prices;
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

    @Override
    public void onCreate() {
        super.onCreate();
        instance=getApplicationContext();

    }

}
