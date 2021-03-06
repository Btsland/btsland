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
import android.widget.TextView;

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
    private TotalNumReceiver totalNumReceiver;
    private TextView tvTotal;
    private TextView tvTotalCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        purseAssetReceiver =new PurseAssetReceiver();
        IntentFilter intentFilter =new IntentFilter(PurseAssetReceiver.EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(purseAssetReceiver,intentFilter);
        totalNumReceiver=new TotalNumReceiver();
        IntentFilter intentFilter1=new IntentFilter(TotalNumReceiver.EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(totalNumReceiver,intentFilter1);
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
        tvTotal=findViewById(R.id.tv_total);
        tvTotalCoin=findViewById(R.id.tv_total_coin);
        adapter=new AssetSimpleCursorAdapter(this);
        adapter.setAssets(BtslandApplication.iAssetsClone);
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

    private void setTotal(Double total,String coin){
        if(BtslandApplication.chargeUnit.equals(coin)){
            if(String.valueOf(total)==null){
                tvTotal.setText("0.0");
            }else {
                tvTotal.setText(String.format("%.4f",total));
            }
            tvTotalCoin.setText(coin);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(purseAssetReceiver);
    }
    public static void sendTotalBroadcast(Context context,Double total,String coin){
        Intent intent=new Intent(TotalNumReceiver.EVENT);
        intent.putExtra("total",total);
        intent.putExtra("coin",coin);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class TotalNumReceiver extends  BroadcastReceiver{
        public static final String EVENT = "TotalNumReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Double total=intent.getDoubleExtra("total",0.0);
            String coin=intent.getStringExtra("coin");
            setTotal(total,coin);
        }
    }

    public static void sendBroadcast(Context context){
        Intent intent=new Intent(PurseAssetReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private class PurseAssetReceiver extends BroadcastReceiver {
        public static final String EVENT = "PurseAssetReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
        }
    }
}

