package info.btsland.app.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONObject;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HomeFragment;
import info.btsland.app.ui.fragment.MarketFragment;
import info.btsland.app.ui.fragment.PurseFragment;

/**
 * 作者：谢建华
 * 创建时间：2017/09/27
 * 完成时间：
 */
public class MainActivity extends Activity implements MarketFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,PurseFragment.OnFragmentInteractionListener {
    private ImageView ivNavUser ;
    private TextView tvNavHome;
    private TextView tvNavMarket;
    private TextView tvNavPurse;
    private TextView tvNavSet;
    private Fragment marketFragment;
    private Fragment homeFragment;
    private Fragment purseFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //Setting
        tvNavSet = (TextView)findViewById(R.id.tv_nav_set);
        tvNavSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化
     */
    protected void init(){
        //初始化
        ivNavUser=(ImageView)findViewById(R.id.iv_nav_user);
        tvNavHome=(TextView)findViewById(R.id.tv_nav_home);
        tvNavMarket=(TextView)findViewById(R.id.tv_nav_market);
        tvNavPurse=(TextView)findViewById(R.id.tv_nav_purse);
        //绑定监听器
        ivNavUser.setOnClickListener(new ivNavUserOnClick());
        tvNavHome.setOnClickListener(new NavOnClickListener());
        tvNavMarket.setOnClickListener(new NavOnClickListener());
        tvNavPurse.setOnClickListener(new NavOnClickListener());
        //初始化fragment
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        if (marketFragment==null){
            marketFragment=new MarketFragment();
            transaction.add(R.id.fragment,marketFragment);
        }
        hideFragment(transaction);
        //默认显示行情页
        transaction.show(marketFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 隐藏所有fragment
      * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction){
        if(marketFragment != null){
            transaction.hide(marketFragment);
        }
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(purseFragment != null){
            transaction.hide(purseFragment);
        }
    }
    private void showFragment(TextView textView){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        switch (textView.getId()){
            case R.id.tv_nav_home:
                if (homeFragment==null){
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.fragment,homeFragment);
                }
                hideFragment(transaction);
                transaction.show(homeFragment);
                break;
            case R.id.tv_nav_market:
                if (marketFragment==null){
                    marketFragment=new MarketFragment();
                    transaction.add(R.id.fragment,marketFragment);
                }
                hideFragment(transaction);
                transaction.show(marketFragment);
                break;
            case R.id.tv_nav_purse:
                if (purseFragment==null){
                    purseFragment=new PurseFragment();
                    transaction.add(R.id.fragment,purseFragment);
                }
                hideFragment(transaction);
                transaction.show(purseFragment);
                break;
        }

        transaction.commit();
    }
    /**
     * 底部导航栏操作效果
     */
    class NavOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_nav_home:
                    touchColor(tvNavHome,tvNavMarket,tvNavPurse);//控件特效
                    showFragment(tvNavHome);
                    break;
                case R.id.tv_nav_market:
                    touchColor(tvNavMarket,tvNavHome,tvNavPurse);//控件特效
                    showFragment(tvNavMarket);
                    break;
                case R.id.tv_nav_purse:
                    touchColor(tvNavPurse,tvNavHome,tvNavMarket);//控件特效
                    showFragment(tvNavPurse);
                    break;
            }
        }

        /**
         * 导航栏交互特效
         * @param facingTextView 当前的控件
         * @param textView1
         * @param textView2
         */
        protected void touchColor(TextView facingTextView,TextView textView1,TextView textView2){
            facingTextView.setBackground(getDrawable(R.drawable.tv_border_touch));
            textView1.setBackground(getDrawable(R.drawable.tv_border));
            textView2.setBackground(getDrawable(R.drawable.tv_border));
            facingTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_yellow_red,null));
            textView1.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_black,null));
            textView2.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_black,null));
        }
    }


    /**
     * 头像单击事件功能实现
     */
    class ivNavUserOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            Intent intent=new Intent(MainActivity.this,UserActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }
    /**
     * 标识是否关闭程序
     */
    private boolean isExit=false;
    /**
     * 检测物理按键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void exit(){
        if (isExit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        } else {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
