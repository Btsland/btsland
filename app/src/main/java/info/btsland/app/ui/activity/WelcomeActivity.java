package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import info.btsland.app.R;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.ui.view.AppDialog;

public class WelcomeActivity extends AppCompatActivity{
    private MFinishReceiver mFinishReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinishReceiver=new MFinishReceiver();
        //注册广播
        IntentFilter intentFilter = new IntentFilter(MFinishReceiver.EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mFinishReceiver,intentFilter);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFinishReceiver);
    }

    public static void sendBroadcast(Context context, int nRet){
        Intent intent=new Intent(MFinishReceiver.EVENT);
        intent.putExtra("nRet",nRet);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
    public class MFinishReceiver extends BroadcastReceiver{
        private static final String EVENT="MBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                int nRet=intent.getIntExtra("nRet",Websocket_api.WEBSOCKET_CONNECT_INVALID);
                if(nRet==Websocket_api.WEBSOCKET_CONNECT_SUCCESS){
                    final Intent intent2=new Intent(WelcomeActivity.this,MainActivity.class);
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(intent2);
                            finish();
                        }
                    }, 2000);

                }else if(nRet==Websocket_api.WEBSOCKET_CONNECT_NO_NETWORK) {
                    AppDialog appDialog=new AppDialog(WelcomeActivity.this);
                    appDialog.setMsg("无网络连接，请确保网络正常后重新打开软件。");
                    appDialog.show();
                }
            }
        }
    }
}
