package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.Websocket_api;
import info.btsland.app.util.InternetUtil;

public class WelcomeActivity extends AppCompatActivity{
    private MFinishReceiver mFinishReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinishReceiver=new MFinishReceiver();
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
                    Intent intent2=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
        }
    }
}
