package info.btsland.app.api;


import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class websocket_api extends WebSocketListener {
    private int _nDatabaseId = -1;
    private int _nHistoryId = -1;
    private int _nBroadcastId = -1;

    private String strServer;
    private OkHttpClient mOkHttpClient;
    private WebSocket mWebsocket;

    private int mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
    private static int WEBSOCKET_CONNECT_INVALID = -1;
    private static int WEBSOCKET_CONNECT_SUCCESS = 0;
    private static int WEBSOCKET_ALL_READY = 0;
    private static int WEBSOCKET_CONNECT_FAIL = 1;

    private AtomicInteger mnCallId = new AtomicInteger(1);

    public websocket_api(String strServer) {
        this.strServer = strServer;
    }


    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        synchronized (mWebsocket) {
            mnConnectStatus = WEBSOCKET_CONNECT_SUCCESS;
            System.out.println("onOpen");
            mWebsocket.notify();
        }
    }

    /** Invoked when a text (type {@code 0x1}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println(text);
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

    private boolean login(String strUserName, String strPassword) {
        String ok = "{\"id\":1,\"method\":\"call\",\"params\":[1,\"login\",[\"\",\"\"]]}";

        mWebsocket.send(ok);

        return true;
    }

    public synchronized int connect() {
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


        boolean bLogin = login("", "");
        if (bLogin == true) {

        } else {
            nRet = -9;
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

    public void get_ticker() {
        String db = "{\"id\":2,\"method\":\"call\",\"params\":[1,\"database\",[]]}";
        mWebsocket.send(db);

        String history = "{\"id\":3,\"method\":\"call\",\"params\":[1,\"history\",[]]}";
        mWebsocket.send(history);

        String network_broadcast = "{\"id\":4,\"method\":\"call\",\"params\":[1,\"network_broadcast\",[]]}";
        mWebsocket.send(network_broadcast);

        String query = "{\"id\":5,\"method\":\"call\",\"params\":[2,\"get_ticker\",[\"CNY\",\"BTS\"]]}";
        mWebsocket.send(query);
    }
}
