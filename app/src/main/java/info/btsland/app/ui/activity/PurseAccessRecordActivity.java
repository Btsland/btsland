package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.ExchangeListFragment;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class PurseAccessRecordActivity extends AppCompatActivity{
    private HeadFragment headFragment;

    private int index=0;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_access);
        fillInHead();
        init();
        fillIn();

    }
    /**
     * 初始化
     */
    private void init() {
        viewPager =findViewById(R.id.vp_access);
        tabStrip =findViewById(R.id.psts_access_title1);
    }
    private void fillIn() {
        viewPager.setOffscreenPageLimit(2);
        String[] titles = {"充值", "提现"};
        List<Fragment> fragments = new ArrayList<Fragment>();
        ExchangeListFragment inFragment=ExchangeListFragment.newInstance(1);
        ExchangeListFragment outFragment=ExchangeListFragment.newInstance(2);
        fragments.add(inFragment);
        fragments.add(outFragment);
        DetailedFragmentAdapter adapter = new DetailedFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new OnPage());
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



    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"充提记录");
            transaction.add(R.id.fra_access_head, headFragment);
        }
        transaction.commit();
    }

}

