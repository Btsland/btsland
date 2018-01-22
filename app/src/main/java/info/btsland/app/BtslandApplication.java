package info.btsland.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import info.btsland.app.Adapter.DealerListAdapter;
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
import info.btsland.app.ui.activity.ChatAccountListActivity;
import info.btsland.app.ui.activity.ChatActivity;
import info.btsland.app.ui.activity.MainActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.RingActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.activity.WelcomeActivity;
import info.btsland.app.ui.fragment.DealerListFragment;
import info.btsland.app.ui.fragment.DealerManageFragment;
import info.btsland.app.ui.fragment.DealerNoteListFragment;
import info.btsland.app.ui.fragment.DetailedBuyAndSellFragment;
import info.btsland.app.ui.fragment.HelpManageFragment;
import info.btsland.app.ui.fragment.PurseFragment;
import info.btsland.app.ui.fragment.UserManageFragment;
import info.btsland.app.util.BaseThread;
import info.btsland.app.util.InternetUtil;
import info.btsland.app.util.NumericUtil;
import info.btsland.exchange.entity.Chat;
import info.btsland.exchange.entity.Help;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.ChatHttp;
import info.btsland.exchange.http.HelpHttp;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.http.UserHttp;
import info.btsland.exchange.scoket.ChatWebScoket;
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
    public static Map<String,List<Chat>> chatListMap=new LinkedHashMap<>();
    public static List<User> dealers=new ArrayList<>();
    public static Map<String,User> stringDealers=new HashMap<>();
    public static List<DealerListAdapter.DealerData> inDataList=new ArrayList<>();
    public static List<DealerListAdapter.DealerData> outDataList=new ArrayList<>();
    public static List<String> chatAccounts=new LinkedList<>();
    public static Map<String,User> chatUsers=new LinkedHashMap<>();
    public static Map<String,String> dealerHelpMap=new LinkedHashMap<>();

    public static Map<String,User> helpUserMap=new LinkedHashMap<>();
    public static Map<String,HelpQueryDealer> helpQueryThreadMap=new LinkedHashMap<>();
    public static List<IAsset> iAssets=new ArrayList<>();
    public static List<asset> assets=new ArrayList<>();
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static MarketStat marketStat;
    public static WebSocket mWebsocket;
    private static Wallet_api walletApi;
    public static ChatWebScoket chatWebScoket;
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
    public static String ipServer="123.1.154.214";
//    public static String ipServer="192.168.1.110";
    public static String chargeUnit="CNY";//计价单位
    public static String Language="zh";//语言
    public static int chatRing=-1;//聊天铃声
    public static int noteRing=-1;//订单铃声
    public static int goUp=0;
    public static int goDown=0;
    public static int suspend=0;

    public static String chatAccount="";

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
        initChat();
        IntentFilter intentFilter = new IntentFilter(QueryReceiver.EVENT);
        queryReceiver=new QueryReceiver();
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(queryReceiver,intentFilter);
        IntentFilter dialogFilter = new IntentFilter(DialogReceiver.EVENT);
        dialogReceiver=new DialogReceiver();
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(dialogReceiver,dialogFilter);
        ConnectThread();

    }
    public static void playChatRing(String account){
        if(account.equals(chatAccount)){
            return;
        }
        Log.e(TAG, "playRing: " );
        if (chatRing==-1) {
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
                    getInstance(), RingtoneManager.TYPE_NOTIFICATION);
            RingtoneManager.getRingtone(getInstance(), uri).play();
        }else {
                /*判断位置不为0则播放的条目为position-1*/
            try {

                RingtoneManager rm = new RingtoneManager(getInstance());
                rm.setType(RingtoneManager.TYPE_NOTIFICATION);
                rm.getCursor();
                rm.getRingtone(chatRing).play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void playNoteRing(){
        if (noteRing==-1) {
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
                    getInstance(), RingtoneManager.TYPE_NOTIFICATION);
            RingtoneManager.getRingtone(getInstance(), uri).play();
        }else {
                /*判断位置不为0则播放的条目为position-1*/
            try {

                RingtoneManager rm = new RingtoneManager(getInstance());
                rm.setType(RingtoneManager.TYPE_NOTIFICATION);
                rm.getCursor();
                rm.getRingtone(noteRing).play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void initChat(){
        if(chatAccounts!=null) {
            chatAccounts.clear();
        }
        if(chatUsers!=null){
            chatUsers.clear();
        }
        if(chatListMap!=null){
            chatListMap.clear();
        }
        readChatAccounts();
        //获取服务器的聊天列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatHttp.queryDealer(account, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        if(json.indexOf("error")!=-1){

                        }else {
                            Gson gson=new Gson();
                            List<String> strings=gson.fromJson(json,new TypeToken<List<String>>(){}.getType());
                            if(strings!=null&&strings.size()>0){
                                if(chatAccounts==null){
                                    chatAccounts=new LinkedList<>();
                                }
                                Log.e(TAG, "onResponse: strings.size():"+strings.size() );
                                chatAccounts.addAll(strings);
                                chatAccounts=new ArrayList<>(new LinkedHashSet<>(chatAccounts));
                                Log.e(TAG, "onResponse: chatAccounts.size():"+chatAccounts.size() );
                                saveChatAccounts();
                            }
                            if(chatAccounts!=null) {
                                for (int i = 0; i < chatAccounts.size(); i++) {
                                    Log.e(TAG, "onResponse: " + chatAccounts.get(i));
                                    String account = chatAccounts.get(i);
                                    queryAccountChat(account);
                                }
                            }
                        }
                    }
                });
            }
        }).start();

    }
    public static int totalChatNum=0;

    private static void queryAccountChat(String account){
        new QueryAccountChatThread(account).start();
    }
    static class QueryAccountChatThread extends Thread{
        private String user;

        public QueryAccountChatThread(String account) {
            this.user = account;
        }

        @Override
        public void run() {
            ChatHttp.queryChat(account, user, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json=response.body().string();
                    if(json.indexOf("error")!=-1){

                    }else {
                        GsonBuilder gsonBuilder=new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                        final Gson gson = gsonBuilder.create();
                        final List<Chat> chats=gson.fromJson(json,new TypeToken<List<Chat>>(){}.getType());
                        if(chats==null||chats.size()==0){
                            return;
                        }
                        if(chatListMap==null){
                            chatListMap=new LinkedHashMap<>();
                        }
                        if(chatListMap.get(user)==null){
                            chatListMap.put(user,chats);
                        }else {
                            chatListMap.get(user).addAll(chats);
                        }
                        final Chat chat=chats.get(chats.size()-1);
                        if(BtslandApplication.chatUsers.get(chat.getFromUser())==null&&BtslandApplication.chatUsers.get(chat.getToUser())==null){
                            String newAccount="";
                            if(!chat.getFromUser().equals(account)) {
                                newAccount=chat.getFromUser();
                            }else {
                                newAccount=chat.getToUser();
                            }
                            final String finalNewAccount = newAccount;
                            UserHttp.queryAccount(newAccount, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String json=response.body().string();
                                    if(json.indexOf("error")!=-1){

                                    }else {
                                        User user=gson.fromJson(json,User.class);
                                        if(user==null){
                                            return;
                                        }
                                        user.chatPoint=chats.size();
                                        user.chatDate=chat.getTime();
                                        user.chat=chat;
                                        BtslandApplication.chatUsers.put(finalNewAccount,user);
                                        ChatActivity.sendBroadcast(getInstance());
                                        ChatAccountListActivity.sendBroadcast(getInstance());
                                    }
                                }
                            });

                        }else {
                            if(!chat.getFromUser().equals(account)) {
                                BtslandApplication.chatUsers.get(chat.getFromUser()).chatPoint += chats.size();
                                BtslandApplication.chatUsers.get(chat.getFromUser()).chatDate = chat.getTime();
                                BtslandApplication.chatUsers.get(chat.getFromUser()).chat = chat;
                            }else {
                                BtslandApplication.chatUsers.get(chat.getToUser()).chatPoint += chats.size();
                                BtslandApplication.chatUsers.get(chat.getToUser()).chatDate = chat.getTime();
                                BtslandApplication.chatUsers.get(chat.getToUser()).chat = chat;
                            }

                            ChatActivity.sendBroadcast(getInstance());
                            ChatAccountListActivity.sendBroadcast(getInstance());
                        }


                    }
                }
            });
        }
    }
    private void totalChatNum(){

    }

    public static class ReceiveChatThread extends Thread{
        private String message;

        public ReceiveChatThread(String message) {
            this.message = message;
        }

        @Override
        public synchronized void run() {
            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
            final Gson gson=gsonBuilder.create();
            final Chat chat=gson.fromJson(message,Chat.class);
            if (chat.getTime()==null){
                chat.setTime(new Date());
            }
            if(BtslandApplication.chatListMap.get(chat.getFromUser())==null&&BtslandApplication.chatListMap.get(chat.getToUser())==null) {
                List<Chat> chats = new ArrayList<>();
                chats.add(chat);
                if(!chat.getFromUser().equals(account)){
                    BtslandApplication.chatListMap.put(chat.getFromUser(), chats);
                }else {
                    BtslandApplication.chatListMap.put(chat.getToUser(), chats);
                }

            }else {
                if(!chat.getFromUser().equals(account)) {
                    if(BtslandApplication.chatListMap.get(chat.getFromUser())==null){
                        List<Chat> chats=new ArrayList<>();
                        chats.add(chat);
                    }
                    BtslandApplication.chatListMap.get(chat.getFromUser()).add(chat);

                }else {
                    if(BtslandApplication.chatListMap.get(chat.getToUser())==null){
                        List<Chat> chats=new ArrayList<>();
                        BtslandApplication.chatListMap.put(chat.getToUser(),chats);
                    }
                    BtslandApplication.chatListMap.get(chat.getToUser()).add(chat);

                }
            }
            if(!isIn(chat.getFromUser())&&!isIn(chat.getToUser())){
                if(BtslandApplication.chatAccounts==null) {
                    BtslandApplication.chatAccounts=new ArrayList<>();
                }
                if(!chat.getFromUser().equals(account)) {
                    BtslandApplication.chatAccounts.add(chat.getFromUser());
                }else {
                    BtslandApplication.chatAccounts.add(chat.getToUser());
                }
                BtslandApplication.saveChatAccounts();
            }
            if(BtslandApplication.chatUsers.get(chat.getFromUser())==null&&BtslandApplication.chatUsers.get(chat.getToUser())==null){
                String newAccount="";
                if(!chat.getFromUser().equals(account)) {
                    newAccount=chat.getFromUser();
                }else {
                    newAccount=chat.getToUser();
                }
                final String finalNewAccount = newAccount;
                UserHttp.queryAccount(newAccount, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json=response.body().string();
                        if(json.indexOf("error")!=-1){

                        }else {
                            User user=gson.fromJson(json,User.class);
                            if (user==null){
                                return;
                            }
                            user.chatPoint=1;
                            totalChatNum+=1;
                            user.chatDate=new Date();
                            user.chat=chat;
                            BtslandApplication.chatUsers.put(finalNewAccount,user);
                            UserManageFragment.sendBroadcastChatPoint(getInstance(),BtslandApplication.totalChatNum);
                            ChatActivity.sendBroadcast(getInstance());
                            ChatAccountListActivity.sendBroadcast(getInstance());
                        }
                    }
                });

            }else {
                if(!chat.getFromUser().equals(account)) {
                    BtslandApplication.chatUsers.get(chat.getFromUser()).chatPoint += 1;
                    BtslandApplication.chatUsers.get(chat.getFromUser()).chatDate = chat.getTime();
                    BtslandApplication.chatUsers.get(chat.getFromUser()).chat = chat;
                }else {
                    BtslandApplication.chatUsers.get(chat.getToUser()).chatPoint += 1;
                    BtslandApplication.chatUsers.get(chat.getToUser()).chatDate = chat.getTime();
                    BtslandApplication.chatUsers.get(chat.getToUser()).chat = chat;
                }
                totalChatNum+=1;
                UserManageFragment.sendBroadcastChatPoint(getInstance(),BtslandApplication.totalChatNum);
                ChatActivity.sendBroadcast(getInstance());
                ChatAccountListActivity.sendBroadcast(getInstance());
            }

        }
        private boolean isIn(String name){
            if(BtslandApplication.chatAccounts!=null) {
                for (int i = 0; i < BtslandApplication.chatAccounts.size(); i++) {
                    if (BtslandApplication.chatAccounts.get(i).equals(name)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private BaseThread queryAllHaving;
    private BaseThread queryAllClinch;
    private BaseThread queryAllDealer;
    private Thread queryAllHelp;
    private void init(){
        instance=getApplicationContext();
        application=this;
        this.sharedPreferences=getInstance().getSharedPreferences("appConfigure", Context.MODE_PRIVATE);
        this.editor=this.sharedPreferences.edit();
        chargeUnit = readChargeUnit();
        fluctuationType = readFluctuationType();
        isRefurbish = readIsRefurbish();
        strServer = readStrServer();
        Language = readLanguage();
        strListMap = readListMap();
        account = readUser();
        chatRing = readChatRing();
        noteRing = readNoteRing();
        setFluctuationType();
        fillInListMap();
        fillInMarketMap();
        queryAllClinch=new QueryAllClinch();
        queryAllHaving=new QueryAllHaving();
        queryAllDealer=new QueryAllDealer();
        queryAllHelp=new QueryAllHelp();
        Log.e(TAG, "init: " );
        queryAllHaving.start();
        queryAllClinch.start();
        queryAllDealer.setTime(10);
        queryAllDealer.start();
        queryAllHelp.start();

    }
    private Gson gson;
    public static int pointNum=0;
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
                                        DealerManageFragment.sendBroadcastPoint(getInstance(),dealerHavingNotes.size());
                                        DealerNoteListFragment.sendBroadcast(getInstance(), 1);
                                    }
                                }
                            }
                        });
                    }
                    int a=0;
                    if(dealer.getType()==UserTypeCode.HELP){
                        for(String name : helpUserMap.keySet()){
                            a+= helpUserMap.get(name).havingNotes.size();
                        }
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
                                    DealerNoteListFragment.sendBroadcast(getInstance(), 1);
                                }
                            }
                        }
                    });
                    int newPointNum=a+userHavingOutNotes.size()+ userHavingInNotes.size()+ dealerHavingNotes.size();
                    if(newPointNum>pointNum){
                        Log.e(TAG, "queryAllHaving: " );
                        playNoteRing();
                    }
                    pointNum=newPointNum;
                    MainActivity.sendBroadcast(getInstance(),pointNum);

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
    private boolean isQueryAount=false;
    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if(stat!=null){
            this.nRet=stat.nRet;
        }else {
            this.nRet=Websocket_api.WEBSOCKET_CONNECT_INVALID;
        }

        WelcomeActivity.sendBroadcast(getInstance(),this.nRet);
        Log.e(TAG, "onMarketStatUpdate: " );
        if(!isQueryAount){
            queryAccount(account,handler);
            isQueryAount=true;
        }

    }
    public static Double getAssetTotalByName(String name){
        if (iAssets != null) {
            synchronized (iAssets) {
                for (int i = 0; i < iAssets.size(); i++) {
                    if (iAssets.get(i).coinName.equals(name)) {
                        return iAssets.get(i).total;
                    }
                }
            }
        }
        return 0.0;
    }
    public static void ConnectThread(){

        if(InternetUtil.isConnected(BtslandApplication.getInstance())){
            MarketStat marketStat = getMarketStat();
            marketStat.connect(MarketStat.STAT_COUNECT,BtslandApplication.getListener());
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
            if (dealer != null) {
                queryDealer();
            }
        }
    }

    public static void queryDealer(){
        UserHttp.queryDealer(dealer.getDealerId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, new GsonDateAdapter());
                Gson gson = gsonBuilder.create();
                String json = response.body().string();
                if (json.indexOf("error") != -1) {
                    sendBroadcastDialog(BtslandApplication.getInstance(), json);
                } else {
                    dealer = gson.fromJson(json, User.class);
                    if (dealer != null) {
                        DealerManageFragment.sendBroadcast(getInstance());
                        AccountC2CTypesActivity.sendBroadcast(getInstance());
                    }
                }
            }
        });
    }
    /**
     * 查询账户线程
     *
     */
    private static class QueryAccountThread extends Thread{
        private String name="";
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
                    queryC2CAccount(account);
                    UserManageFragment.sendBroadcast(getInstance(),2);
                    if(handler!=null){

                        handler.sendEmptyMessage(1);
                    }
                }

                //List<asset>  assets =getMarketStat().mWebsocketApi.list_account_balances(accountObject.id);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }
    }
    public static void queryC2CAccount(String name){
            QueryC2CAccountThread queryC2CAccountThread = new QueryC2CAccountThread(name);
            queryC2CAccountThread.start();
    }
    static class QueryC2CAccountThread extends Thread{
        private String name;

        public QueryC2CAccountThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
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
                            chatWebScoket = ChatWebScoket.createWebScoket(dealer.getAccount());
                            chatWebScoket.setChatOnMessageListener(new ChatWebScoket.ChatOnMessageListener() {
                                @Override
                                public void onMessage(String message) {
                                    Log.e(TAG, "onMessage: "+message );
                                    Chat chat=gson.fromJson(message,Chat.class);
                                    new ReceiveChatThread(message).start();
                                    BtslandApplication.playChatRing(chat.getFromUser());
                                }
                            });
                            chatWebScoket.connect();
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
                                                    }
                                                }
                                            }
                                        }
                                    });
                                    break;
                                case UserTypeCode.ADMIN:
                                    break;
                            }
                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    UserHttp.registerAccount(BtslandApplication.accountObject.name, "", new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json = response.body().string();
                                            if (json.indexOf("error") != -1) {

                                            } else {
                                                try {
                                                    Thread.sleep(3000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                queryC2CAccount(name);
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                }
            });

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
                                HelpManageFragment.sendBroadcast(getInstance());
                            }
                        }
                    }
                });
            }
        }
    }
    private static  AssetThread assetThread;
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
            Log.e("AssetThread", "execute: " );
            if (account!= null && !account.equals("")) {
                Double totalCNY=0.0;
                Double totalBTS=0.0;
                try {
                    assets = getMarketStat().mWebsocketApi.list_account_balances_by_name(account);
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
                                            }
                                        } catch (NetworkStatusException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    iAssets.add(asset);
                                }
                                totalBTS += asset.totalBTS;
                                totalCNY += asset.totalCNY;
                            }
                            DetailedBuyAndSellFragment.sendBroadcast(getInstance());
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
                            synchronized (inDataList) {
                                synchronized (outDataList) {
                                    inDataList.clear();
                                    outDataList.clear();
                                    for (int i = 0; i < BtslandApplication.dealers.size(); i++) {
                                        DealerListAdapter.DealerData dealerData = new DealerListAdapter.DealerData();
                                        User user = BtslandApplication.dealers.get(i);
                                        if (user.getType() == 3) {
                                            stringDealers.put(user.getDealerId(),user);
                                            dealerData.user = user;
                                            dealerData.maxCNY = getMarketStat().mWebsocketApi.getAssetTotalByAccountAndCoin(user.getDealerId(), "CNY");
                                            dealerData.usableCNY = dealerData.maxCNY - dealerData.user.userRecord.getInHavingTotal();
                                            inDataList.add(dealerData);
                                            outDataList.add(dealerData);
                                        }
                                    }
                                    DealerListFragment.sendBroadcast(getInstance());
                                }

                            }

                        }
                    }
                }
            });
        }
    }
    class QueryAllHelp extends Thread{
        @Override
        public void run() {
            HelpHttp.queryAllHelp(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json=response.body().string();
                    if(json.indexOf("error")!=-1){

                    }else {
                        List<Help> helps=gson.fromJson(json,new TypeToken<List<Help>>(){}.getType());
                        if(helps!=null&&helps.size()>0){
                            for (int i = 0;i<helps.size();i++){
                                Help help=helps.get(i);
                                dealerHelpMap.put(help.getDealerid(),help.getHelpid());
                            }
                        }
                    }
                }
            });
        }
    }
    public static void orderDealer(int type,int order){

        if(type==1){
            switch (order){
                case 0:
                    orderByTotalIn(inDataList);
                    break;
                case 1:
                    orderByCount(inDataList);
                    break;
                case 2:
                    orderByBroIn(inDataList);
                    break;
                case 3:
                    orderByTime(inDataList);
                    break;
                case 4:
                    orderByLevel(inDataList);
                    break;
                case 5:
                    orderByLowIn(inDataList);
                    break;
            }
        }else if(type==2){
            switch (order){
                case 0:
                    orderByTotalOut(outDataList);
                    break;
                case 1:
                    orderByCount(outDataList);
                    break;
                case 2:
                    orderByBroOut(outDataList);
                    break;
                case 3:
                    orderByTime(outDataList);
                    break;
                case 4:
                    orderByLevel(outDataList);
                    break;
                case 5:
                    orderByLowOut(outDataList);
                    break;
            }
        }
    }


    /**
     * 根据完成时间排序
     * @param dataList
     */
    private static void orderByTime(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) ((t0.user.userRecord.getTime()*10)-(t1.user.userRecord.getTime()*10));
                if(a>0){
                    return 1;
                }else if(a==0) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });
    }

    /**
     * 根据等级排序
     * @param dataList
     */
    private static void orderByLevel(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) ((t0.user.userInfo.getLevel()*10)-(t1.user.userInfo.getLevel()*10));
                if(a>0){
                    return -1;
                }else if(a==0) {
                    return 0;
                }else{
                    return 1;
                }
            }
        });
    }

    /**
     * 根据完成单数排序
     * @param dataList
     */
    private static void orderByCount(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a=(t0.user.userRecord.getInClinchCount()+t0.user.userRecord.getOutClinchCount())-(t1.user.userRecord.getInClinchCount()+t1.user.userRecord.getOutClinchCount());
                if(a>0){
                    return -1;
                }else if(a==0) {
                    return 0;
                }else{
                    return 1;
                }
            }
        });
    }

    /**
     *  根据充值总额排序
     */
    private static void orderByTotalIn(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) (t0.user.userRecord.getInClinchTotal()-t1.user.userRecord.getInClinchTotal());
                if(a>0){
                    return -1;
                }else if(a==0) {
                    return 0;
                }else{
                    return 1;
                }
            }
        });
    }
    /**
     *  根据提现总额排序
     */
    private static void orderByTotalOut(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) (t0.user.userRecord.getOutClinchTotal()-t1.user.userRecord.getOutClinchTotal());
                if(a>0){
                    return -1;
                }else if(a==0) {
                    return 0;
                }else{
                    return 1;
                }
            }
        });
    }
    /**
     *  根据充值手续费
     */
    private static void orderByBroIn(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) ((t0.user.getBrokerageIn()*1000)-(t1.user.getBrokerageIn()*1000));
                if(a>0){
                    return 1;
                }else if(a==0) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });
    }
    /**
     *  根据提现手续费
     */
    private static void orderByBroOut(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) ((t0.user.getBrokerageOut()*1000)-(t1.user.getBrokerageOut()*1000));
                if(a>0){
                    return 1;
                }else if(a==0) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });
    }
    /**
     *  根据提现下限
     */
    public static void orderByLowIn(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) (t0.user.getLowerLimitIn()-t1.user.getLowerLimitIn());
                if(a>0){
                    return 1;
                }else if(a==0) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });
    }
    /**
     *  根据提现下限
     */
    private static void orderByLowOut(List<DealerListAdapter.DealerData> dataList){
        Collections.sort(dataList, new Comparator<DealerListAdapter.DealerData>() {
            @Override
            public int compare(DealerListAdapter.DealerData t0, DealerListAdapter.DealerData t1) {
                int a= (int) (t0.user.getLowerLimitOut()-t1.user.getLowerLimitOut());
                if(a>0){
                    return 1;
                }else if(a==0) {
                    return 0;
                }else{
                    return -1;
                }
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(account!=null) {
                queryAsset(account);
            }
        }
    };
    public static boolean saveChatAccounts(){
        Gson gson=new Gson();
        String strChatAccounts=gson.toJson(chatAccounts);
        editor.putString(account,strChatAccounts);
        return editor.commit();
    }

    public static boolean saveListMap(){
        Gson gson=new Gson();
        String strListMap=gson.toJson(listMap);
        editor.putString("listMap",strListMap);
        return editor.commit();
    }

    public static boolean saveIsRefurbish(){
        editor.putBoolean("IsRefurbish",isRefurbish);
        return editor.commit();
    }
    public static boolean saveFluctuationType(){
        editor.putInt("fluctuationType",fluctuationType);
        return editor.commit();
    }
    public static boolean saveStrServer(){
        editor.putString("strServer",strServer);
        return editor.commit();
    }
    public static boolean saveChargeUnit(){
        editor.putString("chargeUnit",chargeUnit);
        return editor.commit();
    }
    public static boolean saveLanguage(){
        editor.putString("Language",Language);
        return editor.commit();
    }
    public static List<String> readChatAccounts(){
        Gson gson=new Gson();
        String strChatAccounts=sharedPreferences.getString(account,"");
        chatAccounts=gson.fromJson(strChatAccounts,new TypeToken<List<String>>(){}.getType());
        if(chatAccounts!=null) {
            chatAccounts=new ArrayList<>(new LinkedHashSet<>(chatAccounts));//去除重复
        }
        Log.e(TAG, "readChatAccounts: "+ chatAccounts);
        return chatAccounts;
    }
    public static int readChatRing(){
        return sharedPreferences.getInt("chatRing",chatRing);
    }
    public static int readNoteRing(){
        return sharedPreferences.getInt("noteRing",noteRing);
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
