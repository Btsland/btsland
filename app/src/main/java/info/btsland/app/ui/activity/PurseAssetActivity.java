package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import info.btsland.app.Adapter.AssetSimpleCursorAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * 全部资产类
 */

public class PurseAssetActivity extends AppCompatActivity {
    private static final String TAG="PurseAssetActivity";
    private HeadFragment headFragment;

    private ListView lvAsset;
    private AssetSimpleCursorAdapter adapter;
    private PurseAssetReceiver purseAssetReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        purseAssetReceiver =new PurseAssetReceiver();
        IntentFilter intentFilter =new IntentFilter(PurseAssetReceiver.EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(purseAssetReceiver,intentFilter);
        Log.i("PurseAssetActivity", "onCreate: ");
        fillInHead();
        init();
        fillIn();
    }

    private void fillIn(){
        adapter.notifyDataSetChanged();
    }


    /**
     * 初始化
     */
    private void init() {
        lvAsset= findViewById(R.id.lv_asset);
        adapter=new AssetSimpleCursorAdapter(this,BtslandApplication.iAssets);
        lvAsset.setAdapter(adapter);
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,getString(R.string.asset));
            transaction.add(R.id.fra_asset_head, headFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(purseAssetReceiver);
    }

    public static void sendBroadcast(Context context){
        Intent intent=new Intent(PurseAssetReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class PurseAssetReceiver extends BroadcastReceiver {
        public static final String EVENT = "PurseAssetReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (BtslandApplication.iAssets){
                if(adapter!=null&&BtslandApplication.iAssets!=null){
                    adapter.setAssets(BtslandApplication.iAssets);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}

