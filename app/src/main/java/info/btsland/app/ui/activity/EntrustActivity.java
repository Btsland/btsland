package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * author：lw1000
 * function：委单
 * 2017/10/31.
 */

public class EntrustActivity extends AppCompatActivity {


    private ListView lvEntrust;
    private HeadFragment headFragment;




    public EntrustActivity() {

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.acticity_entrust);

        fillInHead();

        init();

    }





    protected void  init(){

        findViewById(R.id.lv_entrust);

    }



    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            transaction.add(R.id.fra_entrust_head, headFragment);
            headFragment.setTitleName("用户中心");
        }
        transaction.commit();
    }














}


























