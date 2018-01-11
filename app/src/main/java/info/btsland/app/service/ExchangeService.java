package info.btsland.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/11.
 */

public class ExchangeService extends Service {
    private String TAG="ExchangeService";
    private final IBinder binder=new LocalBinder();

    private String account;

    public class LocalBinder extends Binder{
        public ExchangeService getService() {
            return ExchangeService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            account=intent.getStringExtra("account");
        }
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
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return binder;
    }
}
