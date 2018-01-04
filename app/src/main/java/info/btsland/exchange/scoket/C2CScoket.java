package info.btsland.exchange.scoket;


import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import info.btsland.app.BtslandApplication;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public  class C2CScoket extends WebSocketListener {
    private static String url="ws://192.168.1.105:8080/ws/";
//    private static String url="ws://123.1.154.214:8080/websocket/";
    private static int mnConnectStatus;
    private static int SUCCESS=1;
    private static int CLOSED=2;
    private static int FAILURE=3;
    private static WebSocket mWebsocket;
    private static IWebSocketListener iWebSocketListener=new IWebSocketListener();

    private static OnMessageListener onMessageListener;

    private static String TAG="C2CScoket";

    public static void setOnMessageListener(OnMessageListener onMessageListener) {
        C2CScoket.onMessageListener = onMessageListener;
    }

    static class IWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.e(TAG, "onOpen: " );
            synchronized (mWebsocket) {
                mnConnectStatus = SUCCESS;
                mWebsocket.notify();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.e(TAG, "onMessage: " +text);
            onMessageListener.onMessage(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.e(TAG, "onMessage: " );
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.e(TAG, "onClosing: " );
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.e(TAG, "onClosed: " );
            mnConnectStatus = CLOSED;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.e(TAG, "onFailure() returned: " );
            Log.e(TAG, "onFailure: "+t.getMessage() );
            mnConnectStatus = FAILURE;
        }
    }
    public static void send(final String message){
        if(mnConnectStatus==SUCCESS){
            mWebsocket.send(message);
        }else {
            Connet connet=new Connet();
            connet.setConnetResult(new Connet.OnConnetResult() {
                @Override
                public void onConnent() {
                    send(message);
                }
            });
            connet.start();
        }
    }

    private static class Connet extends Thread{
        private String TAG="Connet";

        private OnConnetResult connetResult;

        public void setConnetResult(OnConnetResult connetResult) {
            this.connetResult = connetResult;
        }

        @Override
        public void run() {
            while (true){
                int a=connect(BtslandApplication.accountObject.name);
                Log.e(TAG, "run: " );
                if(a==SUCCESS){
                    connetResult.onConnent();
                    break;
                }
                try {
                    Thread.sleep(256);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public interface OnConnetResult{
            void onConnent();
        }
    }

    private static synchronized int connect(String userId) {
        if (StringUtils.isEmpty(url+userId)) {
            return -9;
        }
        final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        };
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        try {
            String PROTOCOL = "SSL";
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
            KeyManager[] keyManagers = null;
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(keyManagers, trustManagers, secureRandom);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okHttpClientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        Request request = new Request.Builder().url(url+userId).build();
        Log.e(TAG, "connect: "+url+userId);
        mWebsocket = okHttpClient.newWebSocket(request, iWebSocketListener);
        synchronized (mWebsocket) {
            if (mnConnectStatus !=SUCCESS) {
                try {
                    mWebsocket.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        Log.e(TAG, "connect: "+ mnConnectStatus);
        return mnConnectStatus;
    }


    protected interface OnMessageListener{
        void onMessage(String message);
    }
}
