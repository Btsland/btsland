package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.AssetSimpleCursorAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * 全部资产类
 */

public class PurseAssetActivity extends AppCompatActivity {
    private static final String TAG="PurseAssetActivity";
    private HeadFragment headFragment;

    private ListView lvAsset;
    private List<asset> assets=new ArrayList <>();
    private AssetSimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        Log.i("PurseAssetActivity", "onCreate: ");
        fillInHead();
        init();
        BtslandApplication.purseHandler=handler;
    }

//    public void queryAllAsset(){
//
//        BtslandApplication.queryAsset(handler);
//
//
//    }


    @Override
    protected void onStart() {
        super.onStart();

        setLvAsset();

        
    }

    private void setLvAsset(){
//        assets =BtslandApplication.accountObject.assetlist;
//        Log.e(TAG, "setLvAsset: "+assets.size() );
        adapter=new AssetSimpleCursorAdapter(this,BtslandApplication.iAssets);
        lvAsset.setAdapter(adapter);

    }


    /**
     * 初始化
     */
    private void init() {

        lvAsset=  (ListView) findViewById(R.id.lv_asset);

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




    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){

                adapter.notifyDataSetChanged();

            }


        }
    };



}

