package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * author：le1000
 * function：白名单
 * 2017/10/30.
 */

public class WhiteListActivity extends AppCompatActivity {


    private HeadFragment headFragment;
    private ImageView ivWhitelistPho;
    private TextView tvWhitelistName;




    public WhiteListActivity() {

    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);

        fillInHead();
        init();

    }





    protected  void  init(){

         ivWhitelistPho=(ImageView)  findViewById(R.id.iv_whitelist_pho);
        tvWhitelistName=(TextView)  findViewById(R.id.tv_whitelist_name);


    }





    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            transaction.add(R.id.fra_whitelist_head, headFragment);
            headFragment.setTitleName("白名单");
        }
        transaction.commit();
    }



}































