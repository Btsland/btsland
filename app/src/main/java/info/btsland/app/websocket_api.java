package info.btsland.app;


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
     * 当远程套接字接受Web套接字并开始传输消息时调用
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
     *
     * 当Web套接字因读取或写入网络错误而关闭时调用。传出消息和传入消息都可能丢失。将不再向该侦听器发出进一步的调用。
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    private boolean login(String strUserName, String strPassword) {
        String ok = "{\"id\":1,\"method\":\"call\",\"params\":[1,\"login\",[\"\",\"\"]]}";
        boolean aa = mWebsocket.send(ok);

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

        String history = "{\"id\":7,\"method\":\"call\",\"params\":[1,\"history\",[]]}";
        mWebsocket.send(history);

        String network_broadcast = "{\"id\":9,\"method\":\"call\",\"params\":[1,\"network_broadcast\",[]]}";
        mWebsocket.send(network_broadcast);
        String query5="{\"id\":5,\"method\":\"call\",\"params\":[1,\"list_account_balances\",[\"li-88888\"]]}";
        mWebsocket.send(query5);

//        String query6 = "{\"id\":10,\"method\":\"call\",\"params\":[2,\"get_account_by_name\",[\"li-8888\"]]}";
//        mWebsocket.send(query6);

  /*      String query7 = "{\"id\":11,\"method\":\"call\",\"params\":[2,\"verify_account_authority\",[\"li-8888\",\"\"]]}";
        mWebsocket.send(query7);*/


//        String query2 = "{\"id\":52,\"method\":\"call\",\"params\":[3,\"get_market_history\",[\"CNY\",\"BTS\",300,\"Nov 2, 2017 12:54:23 AM\",\"Nov 3, 2017 5:34:23 PM\"]]}";
//        mWebsocket.send(query2);

        /*String query3 = "{\"id\":53,\"method\":\"call\",\"params\":[3,\"get_market_history\",[\"1.3.121\",\"1.3.0\",300,\"Nov 1, 2017 08:25:19\",\"Nov 2, 2017 01:05:19\"]]}";
        mWebsocket.send(query3);
        String query4 = "{\"id\":21,\"method\":\"call\",\"params\":[3,\"get_market_history\",[\"1.3.121\",\"1.3.0\",300,\"2017-11-01T17:13:06\",\"2017-11-03T09:53:06\"]]}";
        mWebsocket.send(query4);

        String query = "{\"id\":5,\"method\":\"call\",\"params\":[2,\"get_ticker\",[\"CNY\",\"BTS\"]]}";
        mWebsocket.send(query);
*/




    }


}


















