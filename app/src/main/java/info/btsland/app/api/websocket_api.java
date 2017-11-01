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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.MarketTrade;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static info.btsland.app.api.ErrorCode.*;

public class websocket_api extends WebSocketListener {
    private String TAG="websocket_api";
    private int _nDatabaseId = -1;
    private int _nHistoryId = -1;
    private int _nBroadcastId = -1;

    private String strServer="wss://bitshares.openledger.info/ws";
    private OkHttpClient mOkHttpClient;
    private WebSocket mWebsocket;

    private int mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
    private static int WEBSOCKET_CONNECT_INVALID = -1;
    private static int WEBSOCKET_CONNECT_SUCCESS = 0;
    private static int WEBSOCKET_ALL_READY = 0;
    private static int WEBSOCKET_CONNECT_FAIL = 1;
    private HashMap<Integer, IReplyObjectProcess> mHashMapIdToProcess = new HashMap<>();

    private AtomicInteger mnCallId = new AtomicInteger(1);


    public websocket_api() {
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
        Log.i(TAG, "onMessage: text:"+text);
        try {
            Gson gson = new Gson();
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
            } else {

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
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    private boolean login(String strUserName, String strPassword) throws NetworkStatusException {
        Log.i(TAG, "login: ");
        Call callObject = new Call();

        callObject.id = mnCallId.getAndIncrement();;
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
        Log.i(TAG, "connect: ");
        Log.e("websocket", "connect: "+Thread.currentThread().getName());
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

        if (StringUtils.isEmpty(strServer)) {
            return -9;
        }

        Request request = new Request.Builder().url(strServer).build();
        mOkHttpClient = new OkHttpClient();
        mWebsocket = mOkHttpClient.newWebSocket(request, this);
        synchronized (mWebsocket) {
            if (mnConnectStatus == WEBSOCKET_CONNECT_INVALID) {
                try {
                    mWebsocket.wait(10000);
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
                _nBroadcastId = get_websocket_bitshares_api_id("network_broadcast");
                Log.i(TAG, "connect: _nDatabaseId:"+_nDatabaseId);
                Log.i(TAG, "connect: _nHistoryId:"+_nHistoryId);
                Log.i(TAG, "connect: _nBroadcastId:"+_nBroadcastId);
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
            mnConnectStatus = WEBSOCKET_ALL_READY;
        }

        return nRet;
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

        return replyObject.result.get(0);
    }
    public List<MarketTicker>  get_ticker_base(String base,String[] quotes) throws NetworkStatusException {
        Log.i(TAG, "get_ticker_base: ");
        if(base==null||base=="") {
            return null;
        }
        List<MarketTicker> marketTickers=new ArrayList<MarketTicker>();
        for(int i=0;i<quotes.length;i++){
            MarketTicker marketTicker= get_ticker(base,quotes[i]);
            //Log.e("websocket_ap1", "get_ticker_base: marketTicker"+marketTicker.toString() );
            if(marketTicker!=null){
                marketTickers.add(marketTicker);
            }

        }
        if(marketTickers==null){
            return null;
        }
        return marketTickers;

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
        listParams.add(assetObjectId1);
        listParams.add(assetObjectId2);
        listParams.add(nBucket);
        listParams.add(dateStart);
        listParams.add(dateEnd);
        callObject.params.add(listParams);
        ReplyObjectProcess<Reply<List<bucket_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<bucket_object>>>(){}.getType());
        Reply<List<bucket_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        return replyObject.result;

    }
    public List<limit_order_object> get_limit_orders(object_id<asset_object> base,
                                                     object_id<asset_object> quote,
                                                     int limit) throws NetworkStatusException {
        Log.i(TAG, "get_limit_orders: ");
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_limit_orders");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
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
        callObject.params.add(_nDatabaseId);
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
        callObject.params.add(_nDatabaseId);
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

        return reply.result;
    }
    private <T> Reply<T> sendForReply(Call callObject,
                                      ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Log.i(TAG, "sendForReply: ");
        if (mWebsocket == null || mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
            int nRet = connect();
            if (nRet == -1) {
                throw new NetworkStatusException("It doesn't connect to the server.");
            }
        }

        return sendForReplyImpl(callObject, replyObjectProcess);
    }

    private <T> Reply<T> sendForReplyImpl(Call callObject,
                                          ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Log.i(TAG, "sendForReplyImpl: ");
        Gson gson = new Gson();
        String strMessage = gson.toJson(callObject);
        Log.i(TAG, "sendForReplyImpl: strMessage:"+strMessage );
        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }

        synchronized (replyObjectProcess) {
            boolean bRet = mWebsocket.send(strMessage);
            if (bRet == false) {
                throw new NetworkStatusException("Failed to send message to server.");
            }
            try {
                replyObjectProcess.wait();
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

        public void processTextToObject(String strText) {
            Log.i(TAG, "processTextToObject: strText:"+strText);
            Log.i(TAG, "processTextToObject: mType:"+mType);
            try {
                Gson gson = new Gson();
                mT = gson.fromJson(strText, mType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            } catch (Exception e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            }
            synchronized (this) {
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
