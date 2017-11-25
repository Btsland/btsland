package info.btsland.app.api;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import info.btsland.app.BtslandApplication;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.MarketTrade;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static info.btsland.app.BtslandApplication._nDatabaseId;
import static info.btsland.app.BtslandApplication._nHistoryId;

public class Websocket_api extends WebSocketListener {
    public static final int WEBSOCKET_CONNECT_NO_NETWORK =-2 ;
    private String TAG="websocket_api";
    private OkHttpClient mOkHttpClient;
    private WebSocket mWebsocket;


    public int mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
    public static int WEBSOCKET_CONNECT_INVALID = -1;
    public static int WEBSOCKET_CONNECT_SUCCESS = 0;
    public static int WEBSOCKET_CONNECT_CLOSING = -2;
    public static int WEBSOCKET_CONNECT_CLOSED = -3;
    public static int WEBSOCKET_CONNECT_FAILURE = -9;
    public static int WEBSOCKET_ALL_READY = 0;

    public static String CONNECT="connect";
    private HashMap<Integer, IReplyObjectProcess> mHashMapIdToProcess = new HashMap<>();

    private AtomicInteger mnCallId = new AtomicInteger(1);


    public Websocket_api() {
    }


    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        synchronized (mWebsocket) {
            mnConnectStatus = WEBSOCKET_CONNECT_SUCCESS;
            Log.i(TAG, "onOpen: ");
            mWebsocket.notify();
        }
    }

    /** Invoked when a text (type {@code 0x1}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            Gson gson = global_config_object.getInstance().getGsonBuilder().create();
            int id = Integer.parseInt(new JSONObject(text).getString("id"));
            Log.i(TAG, "onMessage: id:"+id);
//            ReplyBase replyObjectBase = gson.fromJson(text, ReplyBase.class);
//            Log.i(TAG, "onMessage: replyObjectBase:" +replyObjectBase);
            IReplyObjectProcess iReplyObjectProcess = null;
            synchronized (mHashMapIdToProcess) {
                if (mHashMapIdToProcess.containsKey(id)) {
                    iReplyObjectProcess = mHashMapIdToProcess.get(id);
                }
            }

            if (iReplyObjectProcess != null) {
                iReplyObjectProcess.processTextToObject(text);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** Invoked when a binary (type {@code 0x2}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
    }

    /**
     * Invoked when the remote peer has indicated that no more incoming messages will be
     * transmitted.
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        mnConnectStatus = WEBSOCKET_CONNECT_CLOSING;
        Log.i(TAG, "onClosing: ");
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        mnConnectStatus = WEBSOCKET_CONNECT_CLOSED;
        Log.i(TAG, "onClosed: ");
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i(TAG, "onFailure: ");
        mnConnectStatus = WEBSOCKET_CONNECT_FAILURE;
        MarketStat marketStat = BtslandApplication.getMarketStat();
        MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,BtslandApplication.getListener());
        connect.start();

    }

    private boolean login(String strUserName, String strPassword) throws NetworkStatusException {
        Log.i(TAG, "login: ");
        Call callObject = new Call();

        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add("login");

        List<Object> listLoginParams = new ArrayList<>();
        listLoginParams.add(strUserName);
        listLoginParams.add(strPassword);
        callObject.params.add(listLoginParams);

        ReplyObjectProcess<Reply<Boolean>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<Boolean>>(){}.getType());
        Reply<Boolean> replyLogin = sendForReplyImpl(callObject, replyObject);


        return replyLogin.result;
    }

    public synchronized int connect() {

        Log.e(TAG, "connect: "+Thread.currentThread().getName());
//        Request request = new Request.Builder().url("wss://bitshares.openledger.info/ws").build();
//        mOkHttpClient = new OkHttpClient();
//        mWebsocket = mOkHttpClient.newWebSocket(request, this);
//
//        int nRet = 0;
//        try {
//            boolean bLogin = login("", "");
//            if (bLogin == true) {
//                _nDatabaseId = get_websocket_bitshares_api_id("database");
//                _nHistoryId = get_websocket_bitshares_api_id("history");
//                _nBroadcastId = get_websocket_bitshares_api_id("network_broadcast");
//            } else {
//                nRet = ERROR_CONNECT_SERVER_FAILD;
//            }
//        } catch (NetworkStatusException e) {
//            e.printStackTrace();
//            nRet = ERROR_CONNECT_SERVER_FAILD;
//        }
//
//        if (nRet != 0) {
//            mWebsocket.close(1000, "");
//            mWebsocket = null;
//        }
//        return nRet;
        if (mnConnectStatus == WEBSOCKET_ALL_READY) {
            return 0;
        }

        if (StringUtils.isEmpty(BtslandApplication.strServer)) {
            return -9;
        }

        Request request = new Request.Builder().url(BtslandApplication.strServer).build();
        mOkHttpClient = new OkHttpClient();
        mWebsocket = mOkHttpClient.newWebSocket(request, this);
        synchronized (mWebsocket) {
            if (mnConnectStatus == WEBSOCKET_CONNECT_INVALID) {
                try {
                    mWebsocket.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
                    return -9;
                }
            }
        }

        int nRet = 0;


        boolean bLogin = false;
        try {
            bLogin = login("","");
            if (bLogin == true) {
                _nDatabaseId = get_websocket_bitshares_api_id("database");
                _nHistoryId = get_websocket_bitshares_api_id("history");
                BtslandApplication._nBroadcastId = get_websocket_bitshares_api_id("network_broadcast");
                Log.i(TAG, "connect: _nDatabaseId:"+ _nDatabaseId);
                Log.i(TAG, "connect: _nHistoryId:"+ _nHistoryId);
                Log.i(TAG, "connect: _nBroadcastId:"+BtslandApplication._nBroadcastId);
//                String query9="{\"id\":111111,\"method\":\"call\",\"params\":[2,\"list_account_balances\",[\"li-88888\",[]]]}";
//                Log.e(TAG, "connect: 99999999999999999999999999999999999999999" );
//                mWebsocket.send(query9);
            } else {
                nRet = -9;
            }
        } catch (NetworkStatusException e) {
            nRet=-9;
            e.printStackTrace();
        }

        if (nRet != 0) {
            mWebsocket.close(1000, "");
            mWebsocket = null;
            mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
        } else {
            BtslandApplication.mWebsocket=mWebsocket;
            mnConnectStatus = WEBSOCKET_CONNECT_SUCCESS;
        }

        return nRet;
    }
    public List<operation_history_object> get_account_history(object_id<account_object> accountId,
                                                              object_id<operation_history_object> startId,
                                                              int nLimit) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nHistoryId);
        callObject.params.add("get_account_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add(startId);
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add("1.11.0");
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);

        return replyAccountHistory.result;
    }




    public List<asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_assets");

        List<Object> listAssetsParam = new ArrayList<>();

        List<Object> listObjectId = new ArrayList<>();
        listObjectId.addAll(listAssetObjectId);

        listAssetsParam.add(listObjectId);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        List<asset_object> assetObjects=replyObject.result;
        for(int i=0;i<assetObjects.size();i++){
            BtslandApplication.assetObjectMap.put(assetObjects.get(i).id,assetObjects.get(i));
        }
        return assetObjects;
    }
    public account_object get_account_by_name(String name) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_account_by_name");
        List<String> list=new ArrayList<>();
        list.add(name);

        callObject.params.add(list);
        ReplyObjectProcess<Reply<account_object>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<account_object>>(){}.getType());
        Reply<account_object> replyAccountObjectList = sendForReply(callObject, replyObject);
        return replyAccountObjectList.result;
    }
    public List<account_object> get_accounts(List<object_id<account_object>> listAccountObjectId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_accounts");

        List<Object> listAccountIds = new ArrayList<>();
        listAccountIds.add(listAccountObjectId);

        List<Object> listAccountNamesParams = new ArrayList<>();
        listAccountNamesParams.add(listAccountIds);

        callObject.params.add(listAccountIds);
        ReplyObjectProcess<Reply<List<account_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<account_object>>>(){}.getType());
        Reply<List<account_object>> replyAccountObjectList = sendForReply(callObject, replyObject);

        return replyAccountObjectList.result;
    }
    public sha256_object get_chain_id() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_chain_id");

        List<Object> listDatabaseParams = new ArrayList<>();

        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<sha256_object>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<sha256_object>>(){}.getType());
        Reply<sha256_object> replyDatabase = sendForReply(callObject, replyObject);

        return replyDatabase.result;
    }
    private int get_websocket_bitshares_api_id(String strApiName) throws NetworkStatusException {
        Log.i(TAG, "get_websocket_bitshares_api_id: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add(strApiName);

        List<Object> listDatabaseParams = new ArrayList<>();
        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<Integer>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>(){}.getType());
        Reply<Integer> replyApiId = sendForReplyImpl(callObject, replyObject);

        return replyApiId.result;
    }
    public asset_object lookup_asset_symbols(String strAssetSymbol) throws NetworkStatusException {
        Log.i(TAG, "lookup_asset_symbols: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("lookup_asset_symbols");

        List<Object> listAssetsParam = new ArrayList<>();

        List<Object> listAssetSysmbols = new ArrayList<>();
        listAssetSysmbols.add(strAssetSymbol);

        listAssetsParam.add(listAssetSysmbols);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        asset_object assetObject = replyObject.result.get(0);
        BtslandApplication.assetObjectMap.put(assetObject.id,assetObject);
        return assetObject;
    }
    public int broadcast_transaction(signed_transaction tx) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nBroadcastId);
        callObject.params.add("broadcast_transaction");
        List<Object> listTransaction = new ArrayList<>();
        listTransaction.add(tx);
        callObject.params.add(listTransaction);

        ReplyObjectProcess<Reply<Object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>(){}.getType());
        Reply<Object> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject.error != null) {
            throw new NetworkStatusException(replyObject.error.message);
        } else {
            return 0;
        }
    }
    public global_property_object get_global_properties() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_global_properties");

        callObject.params.add(new ArrayList<>());

        ReplyObjectProcess<Reply<global_property_object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<global_property_object>>(){}.getType());
        Reply<global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }
    public dynamic_global_property_object get_dynamic_global_properties() throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_dynamic_global_properties");

        callObject.params.add(new ArrayList<Object>());

        ReplyObjectProcess<Reply<dynamic_global_property_object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<dynamic_global_property_object>>(){}.getType());
        Reply<dynamic_global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;

    }
    public List<asset> list_account_balances_by_id(object_id<account_object> accountId) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_account_balances");

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(accountId);
        listAccountBalancesParam.add(new ArrayList<Object>());
        callObject.params.add(listAccountBalancesParam);


        ReplyObjectProcess<Reply<List<asset>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset>>>(){}.getType());
        Reply<List<asset>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }
    public List<full_account_object> get_full_accounts(List<String> names, boolean subscribe)
            throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_full_accounts");

        List<Object> listParams = new ArrayList<>();
        listParams.add(names);
        listParams.add(subscribe);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<full_account_object_reply>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<full_account_object_reply>>>(){}.getType());
        Reply<List<full_account_object_reply>> reply = sendForReply(callObject, replyObject);

        List<full_account_object> fullAccountObjectList = new ArrayList<>();
        for (full_account_object_reply fullAccountObjectReply : reply.result) {
            fullAccountObjectList.add(fullAccountObjectReply.fullAccountObject);
        }

        return fullAccountObjectList;
    }
    public void list(object_id<account_object> accountId) throws NetworkStatusException {
        Log.e(TAG, "list: 22222222222222222222222222222" );
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_proposed_transactions");

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(accountId);
        listAccountBalancesParam.add(new ArrayList<Object>());
        callObject.params.add(listAccountBalancesParam);


        ReplyObjectProcess<Reply<List<asset>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset>>>(){}.getType());
        Reply<List<asset>> replyLookupAccountNames = sendForReply(callObject, replyObject);

    }
    public List<asset> list_account_balances_by_name(String accountName) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_named_account_balances");

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(accountName);
        listAccountBalancesParam.add(new ArrayList<Object>());
        callObject.params.add(listAccountBalancesParam);


        ReplyObjectProcess<Reply<List<asset>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset>>>(){}.getType());
        Reply<List<asset>> replyLookupAccountNames = sendForReply(callObject, replyObject);

        return replyLookupAccountNames.result;
    }
    public List<bucket_object>  get_market_history(object_id<asset_object> assetObjectId1,
                                                   object_id<asset_object> assetObjectId2,
                                                   int nBucket,
                                                   Date dateStart,
                                                   Date dateEnd) throws NetworkStatusException {
        Log.i(TAG, "get_market_history: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_market_history");

        List<Object> listParams = new ArrayList<>();
        listParams.add(assetObjectId1.toString());
        listParams.add(assetObjectId2.toString());
        listParams.add(nBucket);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        listParams.add(df.format(dateStart));
        listParams.add(df.format(dateEnd));
        callObject.params.add(listParams);
        ReplyObjectProcess<Reply<List<bucket_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<bucket_object>>>(){}.getType());
        Reply<List<bucket_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;

    }
    public limit_order_object get_limit_order(object_id<limit_order_object> id)
            throws NetworkStatusException {
        return get_limit_orders(Collections.singletonList(id)).get(0);
    }
    public List<limit_order_object> get_limit_orders(List<object_id<limit_order_object>> ids)
            throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_objects");

        List<Object> listParams = new ArrayList<>();
        listParams.add(ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> reply = sendForReply(callObject, replyObject);

        return reply.result;
    }





    public List<limit_order_object> get_limit_orders(object_id<asset_object> baseid,
                                                     object_id<asset_object> quoteid,
                                                     int limit) throws NetworkStatusException {
        Log.i(TAG, "get_limit_orders: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_limit_orders");

        List<Object> listParams = new ArrayList<>();
        listParams.add(baseid.toString());
        listParams.add(quoteid.toString());
        listParams.add(limit);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;
    }
    public List<MarketTrade> get_trade_history(String base, String quote, Date start, Date end, int limit)
            throws NetworkStatusException {
        Log.i(TAG, "get_trade_history: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_trade_history");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
        listParams.add(start);
        listParams.add(end);
        listParams.add(limit);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<MarketTrade>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<MarketTrade>>>(){}.getType());
        Reply<List<MarketTrade>> reply = sendForReply(callObject, replyObject);

        return reply.result;
    }
    public MarketTicker get_ticker(String base, String quote) throws NetworkStatusException {
        if(base.equals(quote)){
            return null;
        }
        Log.i(TAG, "get_ticker: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(BtslandApplication._nDatabaseId);
        callObject.params.add("get_ticker");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<MarketTicker>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<MarketTicker>>(){}.getType());
//        ReplyObjectProcess<Reply<MarketTicker>> replyObject =
//                new ReplyObjectProcess<>();
        Reply<MarketTicker> reply = sendForReply(callObject, replyObject);
        Log.i(TAG, "get_ticker: "+reply.result);
        return reply.result;
    }
    private <T> Reply<T> sendForReply(Call callObject,
                                      ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Log.i(TAG, "sendForReply: ");
        if (mWebsocket == null || mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
            MarketStat marketStat = BtslandApplication.getMarketStat();
            MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,BtslandApplication.getListener());
            connect.start();
        }

        return sendForReplyImpl(callObject, replyObjectProcess);
    }

    private <T> Reply<T> sendForReplyImpl(Call callObject,
                                          ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Log.i(TAG, "sendForReplyImpl: ");
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        String strMessage = gson.toJson(callObject);
        Log.i(TAG, "sendForReplyImpl: strMessage:"+strMessage );
        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }

        synchronized (replyObjectProcess) {
            while (true){
                boolean bRet=false;
                if(mWebsocket!=null){
                    bRet = mWebsocket.send(strMessage);
                    Log.i(TAG, "sendForReplyImpl: "+bRet);
                }else if(BtslandApplication.mWebsocket!=null) {
                    bRet = BtslandApplication.mWebsocket.send(strMessage);
                }else {
                    MarketStat marketStat = BtslandApplication.getMarketStat();
                    MarketStat.Connect connect = marketStat.connect(MarketStat.STAT_COUNECT,BtslandApplication.getListener());
                    connect.start();
                }

                if (bRet==false) {

                }else {
                    break;
                }
            }
            try {
                replyObjectProcess.wait();
                Log.i(TAG, "sendForReplyImpl: wait");
                Reply<T> replyObject = replyObjectProcess.getReplyObject();
                //Log.e("websocket2", "sendForReplyImpl: replyObject:"+replyObject);
                String strError = replyObjectProcess.getError();
                if (TextUtils.isEmpty(strError) == false) {
                    throw new NetworkStatusException(strError);
                } else if (replyObjectProcess.getException() != null) {
                    throw new NetworkStatusException(replyObjectProcess.getException());
                } else if (replyObject == null) {
                    throw new NetworkStatusException("Reply object is null.\n" + replyObjectProcess.getResponse());
                }else if (replyObject.error != null) {
                    throw new NetworkStatusException(gson.toJson(replyObject.error));
                }
                //打包好返回的数据
                return replyObject;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public void get_ticker() {
        Log.i(TAG, "get_ticker: ");
        String db = "{\"id\":2,\"method\":\"call\",\"params\":[1,\"database\",[]]}";
        mWebsocket.send(db);

        String history = "{\"id\":3,\"method\":\"call\",\"params\":[1,\"history\",[]]}";
        mWebsocket.send(history);

        String network_broadcast = "{\"id\":4,\"method\":\"call\",\"params\":[1,\"network_broadcast\",[]]}";
        mWebsocket.send(network_broadcast);

        String query = "{\"id\":5,\"method\":\"call\",\"params\":[2,\"get_ticker\",[\"CNY\",\"BTS\"]]}";
        mWebsocket.send(query);
    }
    private interface IReplyObjectProcess<T> {
        void processTextToObject(String strText);
        T getReplyObject();
        String getError();
        void notifyFailure(Throwable t);
        Throwable getException();
        String getResponse();
    }
    private class ReplyObjectProcess<T> implements IReplyObjectProcess<T> {
        private String strError;
        private T mT;
        private Type mType;
        private Throwable exception;
        private String strResponse;
        public ReplyObjectProcess(Type type) {
            mType = type;
            Log.i(TAG, "ReplyObjectProcess: mType:"+mType);
        }
        public ReplyObjectProcess() {

        }
//
        public void processTextToObject(String strText) {
            Log.i(TAG, "processTextToObject: strText:"+strText);
            Log.i(TAG, "processTextToObject: mType:"+mType);
            try {
                Log.i(TAG, "processTextToObject: ");
                Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                mT = gson.fromJson(strText, mType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
                return;
            } catch (Exception e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
                return;
            }
            synchronized (this) {
                Log.i(TAG, "processTextToObject: synchronized");
                notify();
            }
        }

        @Override
        public T getReplyObject() {
            return mT;
        }

        @Override
        public String getError() {
            return strError;
        }

        @Override
        public void notifyFailure(Throwable t) {
            exception = t;
            synchronized (this) {
                notify();
            }
        }

        @Override
        public Throwable getException() {
            return exception;
        }

        @Override
        public String getResponse() {
            return strResponse;
        }
    }
    class Call {
        int id;
        String method;
        List<Object> params;
    }
    class WebsocketError {
        int code;
        String message;
        Object data;
    }
    class ReplyBase {
        int id;
        String jsonrpc;
        String result;

        @Override
        public String toString() {
            return "ReplyBase{" +
                    "id=" + id +
                    ", jsonrpc='" + jsonrpc + '\'' +
                    ", result='" + result + '\'' +
                    '}';
        }
    }

    class Reply<T> {
        String id;
        String jsonrpc;
        T result;
        WebsocketError error;
    }
}
