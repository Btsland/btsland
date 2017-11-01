package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * author：lw1000
 * function：用户信息
 * 2017/10/31.
 */




public class UserInfoActivity extends AppCompatActivity{




    private HeadFragment headFragment;


    public UserInfoActivity() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userinfo);

        fillInHead();

        init();

    }



    protected  void  init(){


    }


    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            transaction.add(R.id.fra_userinfo_head, headFragment);
            headFragment.setTitleName("用户资料");
        }
        transaction.commit();
    }







}
















