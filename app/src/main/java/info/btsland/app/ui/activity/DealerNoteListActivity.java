package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.DealerNoteListFragment;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.exchange.utils.UserTypeCode;


public class DealerNoteListActivity extends AppCompatActivity {
    private String TAG="DealerHavingListActivity";
    private HeadFragment headFragment;

    private int type=1;
    private int HAVING=1;
    private int CLINCH=2;
    private int index=0;
    public String dealerId;

    private DealerNoteListFragment InFragment;
    private DealerNoteListFragment OutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_having_list);
        if(savedInstanceState!=null){
            type=savedInstanceState.getInt("type");
            index=savedInstanceState.getInt("index");
            dealerId=savedInstanceState.getString("dealerId");
        }
        if(getIntent()!=null){
            type=getIntent().getIntExtra("type",type);
            index=getIntent().getIntExtra("index",index);
            dealerId=getIntent().getStringExtra("dealerId");
        }
        fillInHead();
        init();
        fillIn();
    }

    private void init() {

    }
    private void fillIn() {
        ViewPager viewPager=findViewById(R.id.vp_dealerHaving_page);
        viewPager.setOffscreenPageLimit(2);
        String[] titles=new String[]{"充值","提现"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        InFragment=DealerNoteListFragment.newInstance(1,type);
        OutFragment=DealerNoteListFragment.newInstance(2,type);
        fragments.add(InFragment);
        fragments.add(OutFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip =findViewById(R.id.psts_dealerHaving_title);
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
            index=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"");
            transaction.add(R.id.fra_dealerHaving_head,headFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (BtslandApplication.dealer.getType()){
            case UserTypeCode.DEALER:
                if(type==HAVING){
                    headFragment.setTitleName("进行中的订单");
                }else if(type==CLINCH) {
                    headFragment.setTitleName("已完成的订单");
                }
                break;
            case UserTypeCode.HELP:
                headFragment.setTitleName(dealerId);
                break;
            case UserTypeCode.ADMIN:
                headFragment.setTitleName(dealerId);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("type",type);
        outState.putInt("index",index);
        super.onSaveInstanceState(outState);
    }



}
