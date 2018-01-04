package info.btsland.exchange.scoket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class ChatWebScoket extends WebSocketClient {
    private static String url="ws://123.1.154.214:8080/ws/";
    public int mnConnectStatus;
    public static int SUCCESS=1;
    public static int CLOSED=2;
    public static int ERROR=3;
    private String TAG="ChatWebScoket";

    private ChatOnMessageListener chatOnMessageListener;

    public void setChatOnMessageListener(ChatOnMessageListener chatOnMessageListener) {
        this.chatOnMessageListener = chatOnMessageListener;
    }

    public static ChatWebScoket createWebScoket(String name){
        URI uri=URI.create(url+name);
        ChatWebScoket chatWebScoket=new ChatWebScoket(uri);
        return chatWebScoket;
    }

    public ChatWebScoket(URI serverURI) {
        super(serverURI,new Draft_17());
    }

    public void sendMsg(String text) throws NotYetConnectedException {
        //write a message
        send(text);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        mnConnectStatus = SUCCESS;
        Log.e(TAG, "onOpen: " );
    }

    @Override
    public void onMessage(String message) {
        chatOnMessageListener.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        mnConnectStatus = CLOSED;
        Log.e(TAG, "onClose: " );
    }

    @Override
    public void onError(Exception ex) {
        mnConnectStatus = ERROR;
        Log.e(TAG, "onError: " );
        ex.printStackTrace();
    }

    public interface ChatOnMessageListener{
        void onMessage(String message);
    }
}
