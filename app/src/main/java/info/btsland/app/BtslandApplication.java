package info.btsland.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import info.btsland.app.ui.activity.AccountC2CTypesActivity;
import info.btsland.app.ui.activity.MainActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.activity.WelcomeActivity;
import info.btsland.app.ui.fragment.DealerListFragment;
import info.btsland.app.ui.fragment.DealerManageFragment;
import info.btsland.app.ui.fragment.DealerNoteListFragment;
import info.btsland.app.ui.fragment.PurseFragment;
import info.btsland.app.ui.fragment.UserManageFragment;
import info.btsland.app.util.BaseThread;
import info.btsland.app.util.InternetUtil;
import info.btsland.app.util.NumericUtil;
import info.btsland.exchange.entity.Chat;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.ChatHttp;
import info.btsland.exchange.http.HelpHttp;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.http.UserHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import info.btsland.exchange.utils.UserTypeCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication implements MarketStat.OnMarketStatUpdateListener{
    private static final String TAG="BtslandApplication";
    private static Context instance;

    private static BtslandApplication application;

    public static boolean isLogin=false;

    public static account_object accountObject;
    public static String account;
    public static User dealer;

    public static List<Note> dealerHavingNotes=new ArrayList<>();
    public static List<Note> dealerClinchNotes=new ArrayList<>();
    public static List<Note> userHavingInNotes=new ArrayList<>();
    public static List<Note> userHavingOutNotes=new ArrayList<>();
    public static List<User> dealers;

    public static Map<String,User> helpUserMap=new LinkedHashMap<>();
    public static Map<String,HelpQueryDealer> helpQueryThreadMap=new LinkedHashMap<>();
    public static Map<String,HelpQueryChatDealer> helpQueryChatDealerThreadMap=new LinkedHashMap<>();
    public static List<IAsset> iAssets=new ArrayList<>();
    private static SharedPreferences sharedPreferences;

    private static MarketStat marketStat;
    public static WebSocket mWebsocket;
    private static Wallet_api walletApi;
    public static int nRet= Websocket_api.WEBSOCKET_CONNECT_INVALID;
    public static Map<String,List<MarketStat.HistoryPrice>> dataKMap=new LinkedHashMap<>();
    public static Map<String,OrderBook> orderBookMap=new LinkedHashMap<>();

    public static Map<String,Map<String,MarketTicker>> marketMap=new LinkedHashMap<>();


    public static List<asset_object> allAsset=new ArrayList<>();
    public static boolean isQueryALlAsset=false;
    public static int _nDatabaseId = 2;
    public static int _nHistoryId = 3;
    public static int _nBroadcastId = 4;
    public static Map<object_id<asset_object>, asset_object> assetObjectMap=new LinkedHashMap<>();
    public static boolean isRefurbish=true;//是否自动刷新
    public static int fluctuationType=1;//涨跌颜色类型
    public static String strServer="wss://www.btsland.info/ws";//节点
    public static String chargeUnit="CNY";//计价单位
    public static String Language="zh";//语言
    public static int goUp=0;
    public static int goDown=0;
    public static int suspend=0;

    public static List<String> mListNode = Arrays.asList(
            "wss://www.btsland.info/ws",
            "wss://bit.btsabc.org/ws",
            "wss://bitshares-api.wancloud.io/ws",
            "wss://bitshares.openledger.info/ws",
            "wss://openledger.hk/ws"
    );

    public static List<String> baseList = Arrays.asList("CNY", "BTS", "USD", "BTC");

    public static Map<String,List<String>> listMap=new LinkedHashMap<>();

    private static String strListMap="{\"CNY\":[\"BTS\",\"OPEN.EOS\",\"IPFS\",\"USD\",\"OPEN.BTC\",\"OPEN.ETH\",\"YOYOW\",\"OCT\",\"OPEN.LTC\",\"OPEN.STEEM\",\"OPEN.DASH\",\"HPB\",\"OPEN.OMG\",\"IMIAO\"]" +
            ",\"BTS\":[\"CNY\",\"OPEN.EOS\",\"IPFS\",\"USD\",\"OPEN.BTC\",\"OPEN.ETH\",\"YOYOW\",\"OCT\",\"OPEN.LTC\",\"OPEN.STEEM\",\"OPEN.DASH\",\"HPB\",\"OPEN.OMG\",\"IMIAO\"]" +
            ",\"USD\":[\"CNY\",\"BTS\",\"OPEN.EOS\",\"IPFS\",\"OPEN.BTC\",\"OPEN.ETH\",\"YOYOW\",\"OCT\",\"OPEN.LTC\",\"OPEN.STEEM\",\"OPEN.DASH\",\"HPB\",\"OPEN.OMG\",\"IMIAO\"]" +
            ",\"BTC\":[\"CNY\",\"BTS\",\"OPEN.EOS\",\"IPFS\",\"USD\",\"OPEN.BTC\",\"OPEN.ETH\",\"YOYOW\",\"OCT\",\"OPEN.LTC\",\"OPEN.STEEM\",\"OPEN.DASH\",\"HPB\",\"OPEN.OMG\",\"IMIAO\"]}";
    public static List<MarketTicker> tickerList=new ArrayList<>();

    //public static List<String> quoteList=Arrays.asList("CNY","BTS","OPEN.EOS","IPFS", "USD", "OPEN.BTC", "OPEN.ETH", "YOYOW", "OCT", "OPEN.LTC", "OPEN.STEEM", "OPEN.DASH", "HPB", "OPEN.OMG", "IMIAO");
    private QueryReceiver queryReceiver ;
    private DialogReceiver dialogReceiver ;

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

    public static BtslandApplication getApplication(){
        return application;
    }

    public static BtslandApplication getListener() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        init();
        IntentFilter intentFilter = new IntentFilter(QueryReceiver.EVENT);
        queryReceiver=new QueryReceiver();
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(queryReceiver,intentFilter);
        IntentFilter dialogFilter = new IntentFilter(DialogReceiver.EVENT);
        dialogReceiver=new DialogReceiver();
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(dialogReceiver,dialogFilter);
        ConnectThread();
    }
    private BaseThread queryAllHaving;
    private BaseThread queryAllClinch;
    private BaseThread queryAllDealer;
    private void init(){
        instance=getApplicationContext();
        application=this;
        sharedPreferences=getInstance().getSharedPreferences("appConfigure", Context.MODE_PRIVATE);
        chargeUnit = readChargeUnit();
        fluctuationType = readFluctuationType();
        isRefurbish = readIsRefurbish();
        strServer = readStrServer();
        Language = readLanguage();
        strListMap = readListMap();
        account = readUser();
        setFluctuationType();
        fillInListMap();
        fillInMarketMap();
        queryAllClinch=new QueryAllClinch();
        queryAllHaving=new QueryAllHaving();
        queryAllDealer=new QueryAllDealer();
        Log.e(TAG, "init: " );
        queryAllHaving.start();
        queryAllClinch.start();
        queryAllDealer.setTime(10);
        queryAllDealer.start();

    }
    private Gson gson;
    class QueryAllHaving extends BaseThread{
        @Override
        public void execute() {
            Log.e("QueryAllHaving", "execute: " );
            if(dealer!=null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, new GsonDateAdapter());
                gson = gsonBuilder.create();
                if (dealer != null && dealer.getDealerId() != null) {
                    if(dealer.getType()==UserTypeCode.DEALER){
                        NoteHttp.queryAllHavingNote(dealer.getDealerId(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                if (json.indexOf("error") != -1) {
                                    BtslandApplication.sendBroadcastDialog(BtslandApplication.this, json);
                                } else {
                                    dealerHavingNotes = gson.fromJson(json, new TypeToken<List<Note>>() {
                                    }.getType());
                                    if (dealerHavingNotes != null) {
                                        MainActivity.sendBroadcast(getInstance(),dealerHavingNotes.size()+userHavingOutNotes.size()+userHavingInNotes.size());
                                        DealerManageFragment.sendBroadcastPoint(getInstance(),dealerHavingNotes.size());
                                        DealerNoteListFragment.sendBroadcast(getInstance(), 1);
                                    }
                                }
                            }
                        });
                    }
                    if(dealer.getType()==UserTypeCode.HELP){
                        int a=0;
                        for(String name : helpUserMap.keySet()){
                            a = helpUserMap.get(name).havingNotes.size();
                        }
                        MainActivity.sendBroadcast(BtslandApplication.getInstance(),a+userHavingOutNotes.size()+userHavingInNotes.size());
                    }
                    NoteHttp.queryAllHavingNoteByAccount(dealer.getDealerId(),"CNY", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if (json.indexOf("error") != -1) {
                                BtslandApplication.sendBroadcastDialog(BtslandApplication.this, json);
                            } else {
                                userHavingInNotes = gson.fromJson(json, new TypeToken<List<Note>>() {
                                }.getType());
                                if (userHavingInNotes != null) {
                                    UserManageFragment.sendBroadcastPoint(BtslandApplication.getInstance(),userHavingInNotes.size()+userHavingOutNotes.size());
                                    MainActivity.sendBroadcast(getInstance(),dealerHavingNotes.size()+userHavingOutNotes.size()+userHavingInNotes.size());
                                    DealerNoteListFragment.sendBroadcast(getInstance(), 1);
                                }
                            }
                        }
                    });
                    NoteHttp.queryAllHavingNoteByAccount(dealer.getDealerId(),"RMB", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if (json.indexOf("error") != -1) {
                                BtslandApplication.sendBroadcastDialog(BtslandApplication.this, json);
                            } else {
                                userHavingOutNotes = gson.fromJson(json, new TypeToken<List<Note>>() {
                                }.getType());
                                if (userHavingOutNotes != null) {
                                    UserManageFragment.sendBroadcastPoint(BtslandApplication.getInstance(),userHavingOutNotes.size()+userHavingInNotes.size());
                                    MainActivity.sendBroadcast(getInstance(),dealerHavingNotes.size()+userHavingOutNotes.size()+userHavingInNotes.size());
                                    DealerNoteListFragment.sendBroadcast(getInstance(), 1);
                                }
                            }
                        }
                    });

                }
            }
        }


    }
    class QueryAllClinch extends BaseThread{
        @Override
        public void execute() {
            Log.e("QueryAllClinch", "execute: " );
            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
            gson=gsonBuilder.create();
            if(dealer!=null&&dealer.getDealerId()!=null) {
                NoteHttp.queryAllClinchNote(dealer.getDealerId(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        if(json.indexOf("error")!=-1){
                            BtslandApplication.sendBroadcastDialog(BtslandApplication.this,json);
                        }else {
                            dealerClinchNotes = gson.fromJson(json, new TypeToken<List<Note>>() {
                            }.getType());
                            if (dealerClinchNotes != null && dealerClinchNotes.size() > 0) {
                                DealerNoteListFragment.sendBroadcast(getInstance(), 2);
                            }
                        }
                    }
                });
            }
        }

    }


    /**
     * 查询所有货币
     */
    public static void queryAllAsset(Handler handler){
        try {
            while (true){
                if(allAsset.size()>0) {
                    asset_object assetObject=allAsset.get(allAsset.size() - 1);
                    if (assetObject != null) {
                        List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.list_assets(assetObject.symbol,100);
                        synchronized (allAsset){
                            allAsset.addAll(objects);
                        }
                        if(objects.size()<100){
                            isQueryALlAsset=true;
                            break;
                        }
                    }
                }else {
                    allAsset = BtslandApplication.getMarketStat().mWebsocketApi.list_assets("", 100);
                }
            }
            if(handler!=null) {
                handler.sendEmptyMessage(1);
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
    }
    public static List<asset_object> getAssetByName(String n){
        List<asset_object> all=new ArrayList<>();
        all.addAll(allAsset);
        String name=n.toUpperCase();
        List<asset_object> newAsset=new ArrayList<>();
        for(int i=0;i<all.size();i++){
            asset_object object=all.get(i);
            String assetName=object.symbol;
            if(assetName.indexOf(name)!=-1){
                newAsset.add(object);
                continue;
            }
        }
        return newAsset;
    }

    /**
     * 默认的市场
     */
    private static void fillInListMap(){
        Gson gson=new Gson();
        listMap=gson.fromJson(strListMap,listMap.getClass());
    }

    /**
     * 初始化交易对
     */
    private static void fillInMarketMap() {

        for(String base : listMap.keySet()){
            Map<String,MarketTicker> tickers=new HashMap<>();
            for(int i=0;i<listMap.get(base).size();i++){
                String quote=listMap.get(base).get(i);
                if(quote.equals(base)){
                    continue;
                }
                MarketTicker ticker=new MarketTicker(base,quote);
                tickerList.add(ticker);
                tickers.put(quote,ticker);
            }
            marketMap.put(base,tickers);
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
        queryAccount(account,handler);
    }
    public static Double getAssetTotalByName(String name){
        synchronized (BtslandApplication.iAssets) {
            if (BtslandApplication.iAssets != null) {
                synchronized (BtslandApplication.iAssets) {
                    for (int i = 0; i < BtslandApplication.iAssets.size(); i++) {
                        if (BtslandApplication.iAssets.get(i).coinName != null && BtslandApplication.iAssets.get(i).coinName.equals(name)) {
                            return BtslandApplication.iAssets.get(i).total;
                        }
                    }
                }
            }
        }
        return 0.0;
    }
    public static void ConnectThread(){

        if(InternetUtil.isConnected(BtslandApplication.getInstance())){
            MarketStat marketStat = getMarketStat();
            MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,getListener());
            connect.start();
        }else {
            Toast.makeText(BtslandApplication.getInstance(), "无法连接网络", Toast.LENGTH_LONG).show();
            WelcomeActivity.sendBroadcast(getInstance(),Websocket_api.WEBSOCKET_CONNECT_NO_NETWORK);
        }


    }
    public static void sendBroadcast(Context context,String name){
        Intent intent=new Intent(QueryReceiver.EVENT);
        intent.putExtra("name",name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class QueryReceiver extends BroadcastReceiver {
        public static final String EVENT = "QueryReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            String name=intent.getStringExtra("name");
            queryAccount(name,handler);
        }
    }


    public static void sendBroadcastDialog(Context context,String str){
        Intent intent=new Intent(DialogReceiver.EVENT);
        intent.putExtra("str",str);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class DialogReceiver extends BroadcastReceiver {
        public static final String EVENT = "DialogReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(context!=null) {
                String str = intent.getStringExtra("str");
                dialogContext=context;
                Bundle bundle=new Bundle();
                bundle.putString("str",str);
                Message message=Message.obtain();
                message.setData(bundle);
                dialogHandler.sendMessage(message);
                Log.e(TAG, "DialogOnReceive: " + str);

            }
        }
    }
    private Context dialogContext;
    private Handler dialogHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            String str=bundle.getString("str");
            Toast.makeText(dialogContext, str,Toast.LENGTH_LONG).show();
        }
    };
    /**
     * 查询账户线程外部调用方法
     *
     */
    public static void queryAccount(String name,Handler handler ){
        new QueryAccountThread(name,handler).start();
    }
    private static BaseThread queryDealer=new QueryDealer();
    private static class QueryDealer extends BaseThread{
        @Override
        public void execute() {
            UserHttp.queryDealer(dealer.getDealerId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                    Gson gson = gsonBuilder.create();
                    String json = response.body().string();
                    if(json.indexOf("error")!=-1){
                        sendBroadcastDialog(BtslandApplication.getInstance(),json);
                    }else {
                        dealer = gson.fromJson(json, User.class);
                        if (dealer != null) {
                            DealerManageFragment.sendBroadcast(getInstance());
                            AccountC2CTypesActivity.sendBroadcast(getInstance());
                        }
                    }
                }
            });
        }
    }
    /**
     * 查询账户线程
     *
     */
    private static class QueryAccountThread extends Thread{
        private String name;
        private Handler handler;

        public QueryAccountThread(String name,Handler handler) {
            this.name=name;
            this.handler=handler;
        }
        @Override
        public void run() {
            if (nRet!=Websocket_api.WEBSOCKET_CONNECT_SUCCESS){
                return;
            }
            if(name==null||name.equals("")){
                isLogin=false;
                return;
            }
            try {
                accountObject = getMarketStat().mWebsocketApi.get_account_by_name(name);
                if(accountObject!=null){
                    account=accountObject.name;
                    UserManageFragment.sendBroadcast(getInstance(),2);
                    if(handler!=null){

                        handler.sendEmptyMessage(1);
                    }
                }
                UserHttp.queryAccount(name, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final Gson gson=new Gson();
                        String json=response.body().string();
                        if(json.indexOf("error")!=-1){
                            sendBroadcastDialog(BtslandApplication.getInstance(),json);
                        }else {
                            dealer=gson.fromJson(json,User.class);
                            PurseFragment.sendBroadcast(getInstance());
                            if(dealer!=null) {
                                if(!queryDealer.isDead()) {
                                    if (!queryDealer.isStart()) {
                                        queryDealer.start();
                                    } else if (queryDealer.isRun()) {
                                        queryDealer.reStart();
                                    }
                                }
                                switch (dealer.getType()) {
                                    case UserTypeCode.ACCOUNT:
                                        break;
                                    case UserTypeCode.DEALER:
                                        break;
                                    case UserTypeCode.HELP:
                                        HelpHttp.queryDealer(dealer.getDealerId(), new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String json = response.body().string();
                                                if (json.indexOf("error") != -1) {
                                                    sendBroadcastDialog(BtslandApplication.getInstance(),json);
                                                } else {
                                                    Log.e(TAG, "onResponse: " + json);
                                                    List<User> users = gson.fromJson(json, new TypeToken<List<User>>() {
                                                    }.getType());
                                                    if (users != null && users.size() > 0) {
                                                        for (int i = 0; i < users.size(); i++) {
                                                            helpUserMap.put(users.get(i).getDealerId(), users.get(i));
                                                            HelpQueryDealer helpQueryDealer = new HelpQueryDealer(users.get(i).getDealerId());
                                                            helpQueryDealer.setTime(6);
                                                            helpQueryDealer.start();
                                                            helpQueryThreadMap.put(users.get(i).getDealerId(), helpQueryDealer);
                                                            HelpQueryChatDealer helpQueryChatDealer = new HelpQueryChatDealer(users.get(i).getDealerId());
                                                            helpQueryChatDealer.setTime(3);
                                                            helpQueryChatDealer.start();
                                                            helpQueryChatDealerThreadMap.put(users.get(i).getDealerId(), helpQueryChatDealer);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                        break;
                                    case UserTypeCode.ADMIN:
                                        break;
                                }
                            }
                        }
                    }
                });
                //List<asset>  assets =getMarketStat().mWebsocketApi.list_account_balances(accountObject.id);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }
    }
    static class HelpQueryDealer extends BaseThread{
        private String name;

        public HelpQueryDealer(String name) {
            super(name);
            this.name = name;

        }

        @Override
        public void execute() {
            if (helpUserMap.get(name)!=null){
                NoteHttp.queryAllHavingNote(helpUserMap.get(name).getDealerId(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        GsonBuilder gsonBuilder=new GsonBuilder() ;
                        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                        Gson gson = gsonBuilder.create();
                        String json=response.body().string();
                        if (json.indexOf("error") != -1) {
                            sendBroadcastDialog(BtslandApplication.getInstance(),json);
                        } else {
                            List<Note> notes = gson.fromJson(json, new TypeToken<List<Note>>() {}.getType());
                            if (notes != null && notes.size() > 0) {
                                helpUserMap.get(name).havingNotes = notes;
                            }
                        }
                    }
                });
            }
        }
    }

    static class HelpQueryChatDealer extends BaseThread{
        private String to;

        public HelpQueryChatDealer(String to) {
            this.to = to;
        }

        @Override
        public void execute() {
            if (helpUserMap.get(to)!=null){
                ChatHttp.queryChat(account,helpUserMap.get(to).getAccount(),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        GsonBuilder gsonBuilder=new GsonBuilder() ;
                        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                        Gson gson = gsonBuilder.create();
                        if(helpUserMap.get(to).helpNewChatList==null){
                            helpUserMap.get(to).helpNewChatList=new ArrayList<>();
                        }
                        String json = response.body().string();
                        if (json.indexOf("error") != -1) {
                            sendBroadcastDialog(BtslandApplication.getInstance(),json);
                        } else {
                            List<Chat> chats = gson.fromJson(json, new TypeToken<List<Chat>>() {}.getType());
                            if(chats!=null){
                                helpUserMap.get(to).helpNewChatList.addAll(chats);
                            }

                        }
                    }
                });
            }
        }
    }
    private static   AssetThread assetThread;
    /**
     * 查询用余额线程外部调用方法
     */
    public static void queryAsset(String account){
        if(assetThread!=null){
            assetThread.kill();
        }
        assetThread  = new AssetThread(account);
        assetThread.setTime(6).start();
    }

    /**
     * 查询分类资产线程
     *
     */
    private static class AssetThread extends BaseThread{
        private String account;

        public AssetThread(String account) {
            this.account=account;
        }

        @Override
        public void execute() {
            if (account!= null && !account.equals("")) {
                Double totalCNY=0.0;
                Double totalBTS=0.0;
                try {
                    List<asset> assets = getMarketStat().mWebsocketApi.list_account_balances_by_name(account);
                    synchronized (iAssets) {
                        iAssets.clear();
                        if (assets == null || assets.size() == 0) {
                            iAssets.add(new IAsset(chargeUnit));
                        } else {
                            for (int i = 0; i < assets.size(); i++) {
                                IAsset asset = new IAsset(assets.get(i));
                                if (asset != null) {
                                    if (asset.coinName != null && asset.coinName.equals("CNY")) {
                                        if (asset != null) {
                                            asset.totalCNY = asset.total;
                                        } else {
                                            asset.totalCNY = 0.0;
                                        }
                                    } else {
                                        try {
                                            MarketTicker ticker = getMarketStat().mWebsocketApi.get_ticker("CNY", asset.coinName);
                                            if (ticker == null) {
                                                continue;
                                            }
                                            Double price = NumericUtil.parseDouble(ticker.latest);
                                            if (price != null) {
                                                asset.totalCNY = asset.total * price;
                                                totalCNY += asset.totalCNY;

                                            }
                                        } catch (NetworkStatusException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (asset.coinName != null && asset.coinName.equals("BTS")) {
                                        if (asset != null) {
                                            asset.totalBTS = asset.total;
                                        } else {
                                            asset.totalBTS = 0.0;
                                        }
                                    } else {
                                        try {
                                            MarketTicker ticker = getMarketStat().mWebsocketApi.get_ticker("BTS", asset.coinName);
                                            if (ticker == null) {
                                                continue;
                                            }
                                            Double price = NumericUtil.parseDouble(ticker.latest);
                                            if (price != null) {
                                                asset.totalBTS = asset.total * price;
                                                totalBTS += asset.totalBTS;
                                            }
                                        } catch (NetworkStatusException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    iAssets.add(asset);
                                }
                            }
                            TransferActivity.sendBroadcast(getInstance());
                            PurseAssetActivity.sendBroadcast(getInstance());
                        }
                    }
                } catch (NetworkStatusException e) {
                    e.printStackTrace();
                }
                UserManageFragment.sendBroadcast(getInstance(),totalCNY,"CNY");
                UserManageFragment.sendBroadcast(getInstance(),totalBTS,"BTS");
            }
        }
    }


    class QueryAllDealer extends BaseThread{
        @Override
        public void execute() {
            Log.e(TAG, "QueryAllDealer execute: " );
            UserHttp.queryAllDealer(0, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Log.e(TAG, "onResponse: "+json );
                    if(json.indexOf("error")!=-1){
                        sendBroadcastDialog(null,json);
                    }else {
                        if (json != null && !json.equals("")) {
                            Gson gson=new Gson();
                            dealers = gson.fromJson(json,new TypeToken<List<User>>() {}.getType());
                            DealerListFragment.sendBroadcast(getInstance());
                        }
                    }
                }
            });
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(account!=null) {
                queryAsset(account);
            }
        }
    };


    public static boolean saveListMap(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String strListMap=gson.toJson(listMap);
        editor.putString("listMap",strListMap);
        return editor.commit();
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
    public static String readListMap(){
        return sharedPreferences.getString("listMap",strListMap);
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
