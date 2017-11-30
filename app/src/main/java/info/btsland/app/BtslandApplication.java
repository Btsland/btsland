package info.btsland.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Wallet_api;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.OrderBook;
import info.btsland.app.ui.activity.WelcomeActivity;
import info.btsland.app.util.InternetUtil;
import info.btsland.app.util.NumericUtil;
import okhttp3.WebSocket;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication implements MarketStat.OnMarketStatUpdateListener{
    private static final String TAG="BtslandApplication";
    private static Context instance;

    private static BtslandApplication application;

    public static boolean isLogin=false;

    public static Handler purseHandler;

    public static account_object accountObject;
    public static List<IAsset> iAssets;
    public static boolean isWel=false;
    private static SharedPreferences sharedPreferences;

    private static MarketStat marketStat;
    public static WebSocket mWebsocket;
    private static Wallet_api walletApi;
    public static int nRet= Websocket_api.WEBSOCKET_CONNECT_INVALID;
    public static Map<String,List<MarketStat.HistoryPrice>> dataKMap=new HashMap<>();
    public static Map<String,OrderBook> orderBookMap=new HashMap<>();
    public static Map<String,List<MarketTicker>> marketMap=new HashMap<>();
    public static int _nDatabaseId = -1;
    public static int _nHistoryId = -1;
    public static int _nBroadcastId = -1;

    public static String[] bases={"CNY", "BTS", "USD", "BTC"};
    public static String[] quotes1={"BTC", "ETH", "BTS", "LTC", "OMG", "STEEM", "VEN", "HPB", "OCT", "YOYOW", "DOGE", "HASH"};
    public static String[] quotes2={"CNY","BTS", "USD", "OPEN.BTC", "OPEN.ETH", "YOYOW", "OCT", "OPEN.LTC", "OPEN.STEEM", "OPEN.DASH", "HPB", "OPEN.OMG", "IMIAO"};
    public static Map<object_id<asset_object>, asset_object> assetObjectMap=new HashMap<object_id<info.btsland.app.api.asset_object>, info.btsland.app.api.asset_object>();
    public static boolean isRefurbish=true;//是否自动刷新
    public static int fluctuationType=1;//涨跌颜色类型
    public static String strServer="wss://bitshares.dacplay.org/ws";//节点
    public static String chargeUnit="CNY";//计价单位
    public static String Language="zh";//语言
    public static int goUp=0;
    public static int goDown=0;
    public static int suspend=0;

    public static List<String> mListNode = Arrays.asList(
            "wss://bitshares.openledger.info/ws",
            "wss://eu.openledger.info/ws",
            "wss://bit.btsabc.org/ws",
            "wss://bts.transwiser.com/ws",
            "wss://bitshares.dacplay.org/ws",
            "wss://bitshares-api.wancloud.io/ws",
            "wss://openledger.hk/ws",
            "wss://secure.freedomledger.com/ws",
            "wss://dexnode.net/ws",
            "wss://altcap.io/ws",
            "wss://bitshares.crypto.fans/ws"

    );

    public static void setFluctuationType(){
        if(BtslandApplication.fluctuationType==1){
            goUp=getInstance().getResources().getColor(R.color.color_green);
            goDown=getInstance().getResources().getColor(R.color.color_font_red);
            suspend=getInstance().getResources().getColor(R.color.color_font_blue);
        }else if(BtslandApplication.fluctuationType==2){
            goUp=getInstance().getResources().getColor(R.color.color_font_red);
            goDown=getInstance().getResources().getColor(R.color.color_green);
            suspend=getInstance().getResources().getColor(R.color.color_font_blue);
        }
    }

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
        sharedPreferences=getInstance().getSharedPreferences("appConfigure", Context.MODE_PRIVATE);
        chargeUnit = readChargeUnit();
        fluctuationType = readFluctuationType();
        isRefurbish = readIsRefurbish();
        strServer = readStrServer();
        Language = readLanguage();
        while (true){
            if(InternetUtil.isConnected(this)){
                ConnectThread();
                break;
            }else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "无法连接网络", Toast.LENGTH_LONG).show();
            }
        }
        setFluctuationType();
        fiiiInMarketMap();

    }

    private static void fiiiInMarketMap() {
        for(int i=0;i<bases.length;i++){
            List<MarketTicker> tickers=new ArrayList<>();
            for(int j=0;j<quotes2.length;j++){
                if(bases[i].equals(quotes2[j])){
                    continue;
                }else {
                    tickers.add(new MarketTicker(bases[i], quotes2[j]));
                }
            }
            marketMap.put(bases[i],tickers);
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
    public static Double getAssetTotalByName(String name){
        for(int i=0;i<BtslandApplication.iAssets.size();i++){
            if(BtslandApplication.iAssets.get(i).coinName.equals(name)){
                return BtslandApplication.iAssets.get(i).total;
            }

        }
        return 0.0;
    }


    public static void ConnectThread(){
        MarketStat marketStat = getMarketStat();
        MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,getListener());
        connect.start();
    }


    /**
     * 查询账户线程外部调用方法
     *
     */
    public static void queryAccount(){
        new QueryAccountThread().start();
    }




    /**
     * 查询账户线程
     *
     */
    private static class QueryAccountThread extends Thread{
        @Override
        public void run() {

            String username=readUser();
            if (nRet!=Websocket_api.WEBSOCKET_CONNECT_SUCCESS){
                return;
            }
            if(username==null||username.equals("")){
                isLogin=false;
                return;
            }
            try {
                accountObject = getMarketStat().mWebsocketApi.get_account_by_name(username);
                queryAsset(null);
                //List<asset>  assets =getMarketStat().mWebsocketApi.list_account_balances(accountObject.id);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 查询用余额线程外部调用方法
     */
    public static void queryAsset(Handler handler){
        new AccetThread(handler).start();
    }

    /**
     * 查询分类资产线程
     *
     */
    private static class AccetThread extends Thread{
        private Handler handler;

        public AccetThread(Handler handler) {
            this.handler=handler;
        }

        @Override
        public void run() {
            try {
                if(accountObject==null||accountObject.name.equals("")){
                    return;
                }
                List<asset> assets=getMarketStat().mWebsocketApi.list_account_balances_by_name(accountObject.name);
//                    List<asset> assets=getMarketStat().mWebsocketApi.list_account_balances_by_name("tiger5422");
                accountObject.assetlist=assets;
                iAssets=new ArrayList <>();
                if(assets==null||assets.size()==0){
                    iAssets.add(new IAsset(chargeUnit));
                }else {
                    for(int i=0;i<assets.size();i++) {
                        iAssets.add(new IAsset(assets.get(i)));
                    }
                }
                if (this.handler!=null){
                    this.handler.sendEmptyMessage(1);
                }

                CuntTotalCNY();
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 外部调用查询总资产线程的方法
     */
    public static void CuntTotalCNY(){

        new CuntTotalCNYThread().start();

    }




    /**
     * 查询总资产线程
     */
    private static class CuntTotalCNYThread extends Thread{

        @Override
        public synchronized void run() {
            if (accountObject==null||accountObject.name.equals("")){
                return;
            }
            accountObject.totalCNY=0.0;
            for (int i=0; i < iAssets.size(); i++) {
                if(iAssets.get(i).coinName.equals(chargeUnit)){
                    if(iAssets.get(i)!=null){
                        accountObject.totalCNY+=iAssets.get(i).total;
                    }else {
                        accountObject.totalCNY+=0.0;
                    }
                    continue;
                }
                try {
                    MarketTicker ticker =getMarketStat().mWebsocketApi.get_ticker(chargeUnit, iAssets.get(i).coinName);
                    if(ticker==null){
                        continue;
                    }
                    Double price= NumericUtil.parseDouble(ticker.latest);
                    if (price!=null){
                        accountObject.totalCNY+=iAssets.get(i).total * price;
                    }
                } catch (NetworkStatusException e) {
                    e.printStackTrace();

                }
            }

       }




    }

    public static boolean saveIsRefurbish(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("IsRefurbish",isRefurbish);
        return editor.commit();
    }
    public static boolean saveFluctuationType(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("fluctuationType",fluctuationType);
        return editor.commit();
    }
    public static boolean saveStrServer(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("strServer",strServer);
        return editor.commit();
    }
    public static boolean saveChargeUnit(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("chargeUnit",chargeUnit);
        return editor.commit();
    }
    public static boolean saveLanguage(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Language",Language);
        return editor.commit();
    }
    public static String readLanguage(){
        return sharedPreferences.getString("Language",Language);
    }
    public static boolean readIsRefurbish(){

        return sharedPreferences.getBoolean("IsRefurbish",true);
    }
    public static int readFluctuationType(){
        return sharedPreferences.getInt("fluctuationType",1);
    }
    public static String readStrServer(){
        return sharedPreferences.getString("strServer",strServer);
    }
    public static String readChargeUnit(){
        return sharedPreferences.getString("chargeUnit",chargeUnit);
    }
    public static String readUser(){
        return sharedPreferences.getString("username","");
    }
    /**
     * 保存用户
     */
    public static boolean saveUser(){
        //借助Editor实现共享参数储存
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",BtslandApplication.accountObject.name);
        return editor.commit();
    }
    public static boolean clearUser() {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove("username");
        return editor.commit();
    }
}
