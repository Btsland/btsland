package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * author：lw1000
 * function：用户设置窗口
 * 2017/10/21.
 */

public class UserSetActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private ListView lv;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv=(ListView) findViewById(R.id.lv_userset);

        setContentView(R.layout.activity_userset);


       fillInHead();

        uptdaListView();


    }


    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (headFragment == null) {
            headFragment = new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            headFragment.setTitleName("用户资产设置");
            transaction.add(R.id.fra_user_set_head, headFragment);
        }
        transaction.commit();
    }


    public void uptdaListView(){



    }



}











