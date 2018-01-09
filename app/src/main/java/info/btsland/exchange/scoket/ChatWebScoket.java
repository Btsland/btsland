package info.btsland.exchange.scoket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;

import info.btsland.app.BtslandApplication;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class ChatWebScoket extends WebSocketClient {
    public int mnConnectStatus;
    public static int SUCCESS=1;
    public static int CLOSED=2;
    public static int ERROR=3;
    private String TAG="ChatWebScoket";
    private boolean isShop=false;

    private ChatOnMessageListener chatOnMessageListener;

    public void setChatOnMessageListener(ChatOnMessageListener chatOnMessageListener) {
        this.chatOnMessageListener = chatOnMessageListener;
    }

    public static ChatWebScoket createWebScoket(String name){
        String url="ws://"+ BtslandApplication.ipServer+":8080/ws/";
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
        isShop=true;
        Log.e(TAG, "onOpen: " );
    }

    @Override
    public void onMessage(String message) {
        chatOnMessageListener.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        mnConnectStatus = CLOSED;
        while (true) {
            if (isShop) {
                try {
                    connect();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isShop = false;
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "onClose: " );
    }

    @Override
    public void onError(Exception ex) {
        mnConnectStatus = ERROR;
        while (true) {
            if (isShop) {
                try {
                    connect();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                isShop=false;
                break;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ex.printStackTrace();
    }

    public interface ChatOnMessageListener{
        void onMessage(String message);
    }
}
