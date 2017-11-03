package info.btsland.app.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.Adapter.ImagePageAdapter;
import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.fragment.DetailedBuyAndSellFragment;
import info.btsland.app.ui.fragment.DetailedHaveInHandFragment;
import info.btsland.app.ui.fragment.DetailedKFragment;
import info.btsland.app.ui.fragment.HeadFragment;

public class MarketDetailedActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private MarketTicker market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detailed);
        market= (MarketTicker) getIntent().getSerializableExtra("MarketTicker");
        fillInHead();
        init();
    }
    /**
     * 初始化
     */
    private void init(){
        ViewPager viewPager= (ViewPager) findViewById(R.id.vp_detailed_page);
        String[] titles={"详情","买/卖","进行中"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        DetailedKFragment detailedKFragment=new DetailedKFragment();
        DetailedBuyAndSellFragment detailedBuyAndSellFragment=new DetailedBuyAndSellFragment();
        DetailedHaveInHandFragment detailedHaveInHandFragment=new DetailedHaveInHandFragment();
        fragments.add(detailedKFragment);
        fragments.add(detailedBuyAndSellFragment);
        fragments.add(detailedHaveInHandFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_detailed_title);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new OnPage());
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
    class OnPage implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
