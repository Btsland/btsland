package info.btsland.app;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
     class MyWebSocketListener extends WebSocketListener{
        private String TAG="MyWebSocketListener";

         private WebSocket _WebSocket=null;

         private MyWebSocketListener MyWebSocketListener=null;

         public MyWebSocketListener() {
         }

         @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            _WebSocket=webSocket;
            Log.i(TAG, "onOpen: ");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.i(TAG, "onMessage: ");
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.i(TAG, "onMessage: ");
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.i(TAG, "onClosing: ");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.i(TAG, "onClosed: ");
        }
        /**
         * 初始化WebSocket服务器
         */
        private void run() {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(0,  TimeUnit.MILLISECONDS).build();
            Request request = new Request.Builder().url("ws://10.254.20.222:9502")
                    .build();
            client.newWebSocket(request, this);
            client.dispatcher().executorService().shutdown();
        }
         public boolean sendMessage(String s){
             return _WebSocket.send(s);
         }

         public void closeWebSocket(){
             MyWebSocketListener=null;
             _WebSocket.close(1000,"主动关闭");
             Log.e("close","关闭成功");
         }
     }
}