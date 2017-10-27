package info.btsland.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


/**
 * author：lw1000
 * function：近期活动
 * 2017/10/25.
 */


public class RecentActivity extends AppCompatActivity{


     private HeadFragment headFragment;
     private ListView lvRecent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recent);
        init();

        fillInHead();


    }

    private void init() {

    lvRecent=(ListView) findViewById(R.id.lv_recent);

    }


    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (headFragment==null){
            headFragment=new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            transaction.add(R.id.fra_user_recent_head,headFragment);
            headFragment.setTitleName("近期交易");
        }

        transaction.commit();

    }


}























