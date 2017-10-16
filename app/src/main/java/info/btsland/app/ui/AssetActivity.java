package info.btsland.app.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class AssetActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        Log.i("AssetActivity", "onCreate: ");
        fillInHead();
        init();
    }
    /**
     * 初始化
     */
    private void init(){

    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            transaction.add(R.id.fra_asset_head,headFragment);
        }
        transaction.commit();
    }
}
