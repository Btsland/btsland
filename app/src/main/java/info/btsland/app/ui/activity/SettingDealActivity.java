package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.SetDealFragment;

public class SettingDealActivity extends AppCompatActivity {
    private static String TAG="SettingDealActivity";
    private PagerSlidingTabStrip pstTitle;

    private ViewPager viewPager;

    private HeadFragment headFragment;

    public static void startAction(Context context){
        Intent intent=new Intent(context,SettingDealActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_deal);
        fillInHead();
        init();
        fillIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BtslandApplication.allAsset==null||BtslandApplication.allAsset.size()==0) {
            headFragment.showPBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BtslandApplication.queryAllAsset(queryhandler);
                }
            }).start();
        }
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"编辑交易对");
            transaction.add(R.id.fra_setDeal_head, headFragment);
        }
        transaction.commit();
    }

    private void init(){
        pstTitle=findViewById(R.id.psts_setDeal_title1);
        viewPager=findViewById(R.id.vp_setDeal);
    }
    private void fillIn(){
        List<String> titles=new ArrayList<>(BtslandApplication.listMap.keySet());
        List<Fragment> fragments=new ArrayList<Fragment>();
        for(String base : BtslandApplication.listMap.keySet()){
            SetDealFragment fragment=SetDealFragment.newInstance(base);
            fragments.add(fragment);
        }
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles.toArray(new String[]{}));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        pstTitle.setViewPager(viewPager);
    }


    private Handler queryhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(headFragment.isAdded()){
                headFragment.hidePBar();
            }
        }
    };
}
