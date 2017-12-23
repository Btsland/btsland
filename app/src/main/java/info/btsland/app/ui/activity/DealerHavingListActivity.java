package info.btsland.app.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.DealerHavingListFragment;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.util.BaseThread;
import info.btsland.exchange.entity.Note;


public class DealerHavingListActivity extends AppCompatActivity {


    private HeadFragment headFragment;

    private int type=1;
    private int IN=1;
    private int OUT=2;

    private List<Note> inNotes=new ArrayList<>();
    private List<Note> outNotes=new ArrayList<>();
    private int index=0;

    private DealerHavingListFragment InFragment;
    private DealerHavingListFragment OutFragment;
    private BaseThread notesThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_having_list);
        if(savedInstanceState!=null){
            type=savedInstanceState.getInt("type");
        }
        if(getIntent()!=null){
            type=getIntent().getIntExtra("type",type);
            index=getIntent().getIntExtra("index",index);
        }
        fillInHead();
        init();
        fillIn();
    }



    private void init() {

    }
    class QueryHaving extends BaseThread {
        @Override
        public void execute() {
            inNotes.clear();
            outNotes.clear();
            if (BtslandApplication.dealerHavingNotes != null && BtslandApplication.dealerHavingNotes.size() > 0) {
                for (int i = 0; i < BtslandApplication.dealerHavingNotes.size(); i++) {
                    Note note = BtslandApplication.dealerHavingNotes.get(i);
                    if (note.getAssetCoin().equals("CNY")) {
                        synchronized (inNotes) {
                            inNotes.add(note);
                        }
                    } else if (note.getAssetCoin().equals("RMB")) {
                        synchronized (outNotes) {
                            outNotes.add(note);
                        }
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }
    }
    class QueryClinch extends BaseThread {
        @Override
        public void execute() {
            inNotes.clear();
            outNotes.clear();
            if (BtslandApplication.dealerClinchNotes != null && BtslandApplication.dealerClinchNotes.size() > 0) {
                for (int i = 0; i < BtslandApplication.dealerClinchNotes.size(); i++) {
                    Note note = BtslandApplication.dealerClinchNotes.get(i);
                    if (note.getAssetCoin().equals("CNY")) {
                        synchronized (inNotes) {
                            inNotes.add(note);
                        }
                    } else if (note.getAssetCoin().equals("RMB")) {
                        synchronized (outNotes) {
                            outNotes.add(note);
                        }
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }
    }
    private void fillIn() {
        ViewPager viewPager=findViewById(R.id.vp_dealerHaving_page);
        viewPager.setOffscreenPageLimit(2);
        String[] titles=new String[]{"充值","提现"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        InFragment=DealerHavingListFragment.newInstance();
        OutFragment=DealerHavingListFragment.newInstance();
        fragments.add(InFragment);
        fragments.add(OutFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip =findViewById(R.id.psts_dealerHaving_title);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new OnPage());
        if(type==IN){
            notesThread=new QueryHaving();
            notesThread.start();
        }else if(type==OUT) {
            notesThread=new QueryClinch();
            notesThread.start();
        }
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
        if(type==IN){
            headFragment.setTitleName("进行中的订单");
        }else if(type==OUT) {
            headFragment.setTitleName("已完成的订单");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notesThread.kill();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("type",type);
        outState.putInt("index",index);
        super.onSaveInstanceState(outState);
    }

    private String TAG="DealerHavingListActivity";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: "+inNotes.size() );
            Log.e(TAG, "handleMessage: "+outNotes.size() );
            if(InFragment!=null&&InFragment.isAdded()){
                InFragment.refurbish(inNotes);
            }
            if(OutFragment!=null&&OutFragment.isAdded()){
                OutFragment.refurbish(outNotes);
            }
        }
    };
}
