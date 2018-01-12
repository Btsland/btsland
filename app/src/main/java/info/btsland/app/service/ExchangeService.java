package info.btsland.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import info.btsland.exchange.scoket.ChatWebScoket;

/**
 * Created by Administrator on 2018/1/11.
 */

public class ExchangeService extends Service {
    private String TAG="ExchangeService";
    private final IBinder binder=new LocalBinder();
    private ChatWebScoket chatWebScoket;

    private String account;

    public class LocalBinder extends Binder{
        public ExchangeService getService() {
            return ExchangeService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: " );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " );
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: " );
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: " );
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind: " );
        if(intent!=null){
            account=intent.getStringExtra("account");
        }
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        if(intent!=null){
            account=intent.getStringExtra("account");
        }
        if(!account.equals("")){
            chatService(account);
        }
        return binder;
    }

    private void chatService(String account){
        chatWebScoket = ChatWebScoket.createWebScoket(account);
        chatWebScoket.setChatOnMessageListener(new ChatWebScoket.ChatOnMessageListener() {
            @Override
            public void onMessage(String message) {
                Log.e(TAG, "onMessage: " + message);
            }
        });
        chatWebScoket.connect();
    }
}
