package info.btsland.app.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.ui.fragment.HeadFragment;

public class MarketDetailedActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private Market market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detailed);
        market= (Market) getIntent().getSerializableExtra("market");
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
            transaction.add(R.id.fra_detailed_head,headFragment);
        }
        transaction.commit();
    }
}
