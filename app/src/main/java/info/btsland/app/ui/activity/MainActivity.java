package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.C2CFragment;
import info.btsland.app.ui.fragment.DealerManageFragment;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.HomeFragment;
import info.btsland.app.ui.fragment.MarketFragment;
import info.btsland.app.ui.fragment.NewsFragment;
import info.btsland.app.ui.fragment.PurseFragment;
import info.btsland.app.util.PreferenceUtil;

/**
 * 作者：谢建华
 * 创建时间：2017/09/27
 * 完成时间：
 */
public class MainActivity extends BaseActivity implements DealerManageFragment.ShowPoint {
    public static String title;
    private String TAG = "MainActivity";
    private TextView tvNavHome;
    private TextView tvNavMarket;
    private TextView tvNavPurse;
    private TextView tvNavC2C;
    private Fragment marketFragment;
    private NewsFragment homeFragment;
    private Fragment purseFragment;
    private HeadFragment headFragment;
    private TextView tvNavPursePoint;
    public static String dataKKey;
    private FragmentManager manager;
    private int index = 1;
    private Fragment c2cFragment;
    private MainReceiver mainReceiver;
    private int point;



    public void setPoint(int point) {
        this.point = point;
        if(point==0){
            tvNavPursePoint.setText(""+point);
            tvNavPursePoint.setVisibility(View.GONE);
        }else {
            tvNavPursePoint.setText(""+point);
            tvNavPursePoint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化PreferenceUtil
        PreferenceUtil.init(this);
        // 依据上次的语言设置，又一次设置语言
        Log.e(TAG, "onCreate: " + BtslandApplication.Language);
        switchLanguage(BtslandApplication.Language);
        manager=getSupportFragmentManager();
        mainReceiver=new MainReceiver();
        IntentFilter intentFilter = new IntentFilter(MainActivity.MainReceiver.EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mainReceiver,intentFilter);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index", 1);
            homeFragment = (NewsFragment) manager.findFragmentByTag("home");
            marketFragment = manager.findFragmentByTag("market");
            purseFragment = manager.findFragmentByTag("purse");
            c2cFragment = manager.findFragmentByTag("c2c");
            headFragment = (HeadFragment) manager.findFragmentByTag("head");
        }else {
            fillInHead();
            fillInBody();
        }
        init();
        showFragment(index);

    }


    /**
     * 初始化
     */
    protected void init() {
        //初始化
        tvNavHome = (TextView) findViewById(R.id.tv_nav_home);
        tvNavMarket = (TextView) findViewById(R.id.tv_nav_market);
        tvNavPurse = (TextView) findViewById(R.id.tv_nav_purse);
        tvNavC2C = findViewById(R.id.tv_nav_c2c);
        tvNavPursePoint=findViewById(R.id.tv_nav_purse_point);
        //绑定监听器
        tvNavHome.setOnClickListener(new NavOnClickListener());
        tvNavMarket.setOnClickListener(new NavOnClickListener());
        tvNavPurse.setOnClickListener(new NavOnClickListener());
        tvNavC2C.setOnClickListener(new NavOnClickListener());

    }

    public HeadFragment getHeadFragment() {
        return headFragment;
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.SHARE_SET, "");
            transaction.add(R.id.fra_main_head, headFragment,"head");
        }
        transaction.commit();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", index);
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.remove(homeFragment);
//        transaction.remove(marketFragment);
//        transaction.remove(purseFragment);
//        transaction.remove(c2cFragment);
//        transaction.commitAllowingStateLoss();
        super.onSaveInstanceState(outState);
    }



    /**
     * 装载主体内容
     */
    private synchronized void fillInBody() {
        //初始化fra_main_body
        FragmentTransaction transaction = manager.beginTransaction();
        if (homeFragment == null) {
            homeFragment = new NewsFragment();
        }
        if (marketFragment == null) {
            marketFragment = new MarketFragment();
        }
        if (purseFragment == null) {
            purseFragment = new PurseFragment();
        }
        if (c2cFragment == null) {
            c2cFragment = new C2CFragment();
        }
        transaction.add(R.id.fra_main_body, homeFragment,"home");
        transaction.add(R.id.fra_main_body, marketFragment,"market");
        transaction.add(R.id.fra_main_body, purseFragment,"purse");
        transaction.add(R.id.fra_main_body, c2cFragment,"c2c");
        transaction.commitAllowingStateLoss();
    }
    private void showFragment(int index){
        if (index == 0) {
            touchColor(tvNavHome, tvNavMarket, tvNavPurse, tvNavC2C);
            touchImage(tvNavHome);
            showFragment(tvNavHome);
        } else if (index == 1) {
            touchColor(tvNavMarket, tvNavHome, tvNavPurse, tvNavC2C);
            touchImage(tvNavMarket);
            showFragment(tvNavMarket);//显示行情页面
        } else if (index == 2) {
            touchColor(tvNavPurse, tvNavHome, tvNavMarket, tvNavC2C);
            touchImage(tvNavPurse);
            showFragment(tvNavPurse);
        } else if (index == 3) {
            touchColor(tvNavC2C, tvNavPurse, tvNavHome, tvNavMarket);
            touchImage(tvNavC2C);
            showFragment(tvNavC2C);
        }
    }

    /**
     * 根据底部导航栏选定的控件进行切换fra
     *
     * @param textView 选定的控件
     */
    private void showFragment(TextView textView) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (textView.getId()) {
            case R.id.tv_nav_home:
                hideFragment(transaction);
                transaction.show(homeFragment);
                break;
            case R.id.tv_nav_market:
                hideFragment(transaction);
                transaction.show(marketFragment);
                break;
            case R.id.tv_nav_purse:
                hideFragment(transaction);
                transaction.show(purseFragment);
                break;
            case R.id.tv_nav_c2c:
                hideFragment(transaction);
                transaction.show(c2cFragment);
                break;
        }
    }

    /**
     * 隐藏所有fragment
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (marketFragment != null) {
            transaction.hide(marketFragment);
        }
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (purseFragment != null) {
            transaction.hide(purseFragment);
        }
        if (c2cFragment != null) {
            transaction.hide(c2cFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void show(int i) {
        if(tvNavPursePoint!=null){
            setPoint(i);
        }
    }

    /**
     * 底部导航栏操作效果
     */
    class NavOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_nav_home:
                    index = 0;
                    headFragment.setTitleName(getString(R.string.homepage));
                    touchColor(tvNavHome, tvNavMarket, tvNavPurse, tvNavC2C);//控件特效
                    touchImage(tvNavHome);
                    showFragment(tvNavHome);
                    break;
                case R.id.tv_nav_market:
                    index = 1;
                    headFragment.setTitleName(MainActivity.title);
                    touchColor(tvNavMarket, tvNavHome, tvNavPurse, tvNavC2C);//控件特效
                    touchImage(tvNavMarket);
                    showFragment(tvNavMarket);
                    break;
                case R.id.tv_nav_purse:
                    index = 2;
                    headFragment.setTitleName(getString(R.string.wallet));
                    touchColor(tvNavPurse, tvNavHome, tvNavMarket, tvNavC2C);//控件特效
                    touchImage(tvNavPurse);
                    showFragment(tvNavPurse);
                    break;
                case R.id.tv_nav_c2c:
                    index = 3;
                    headFragment.setTitleName(getString(R.string.c2c));
                    touchColor(tvNavC2C, tvNavPurse, tvNavHome, tvNavMarket);//控件特效
                    touchImage(tvNavC2C);
                    showFragment(tvNavC2C);
                    break;

            }
        }


    }

    /**
     * 设置图片点击特效
     *
     * @param textView
     */
    private void touchImage(TextView textView) {
        Drawable homeTouch = getResources().getDrawable(R.drawable.image_nav_home_touch, null);
        Drawable marketTouch = getResources().getDrawable(R.drawable.image_nav_market_touch, null);
        Drawable purseTouch = getResources().getDrawable(R.drawable.image_nav_purse_touch, null);
        Drawable c2cTouch = getResources().getDrawable(R.drawable.image_nav_c2c_touch, null);
        Drawable home = getResources().getDrawable(R.drawable.image_nav_home, null);
        Drawable market = getResources().getDrawable(R.drawable.image_nav_market, null);
        Drawable purse = getResources().getDrawable(R.drawable.image_nav_purse, null);
        Drawable c2c = getResources().getDrawable(R.drawable.image_nav_c2c, null);

        //显示图片
        homeTouch.setBounds(0, 0, homeTouch.getMinimumWidth(), homeTouch.getMinimumHeight());
        marketTouch.setBounds(0, 0, marketTouch.getMinimumWidth(), marketTouch.getMinimumHeight());
        purseTouch.setBounds(0, 0, purseTouch.getMinimumWidth(), purseTouch.getMinimumHeight());
        c2cTouch.setBounds(0, 0, c2cTouch.getMinimumWidth(), c2cTouch.getMinimumHeight());
        home.setBounds(0, 0, home.getMinimumWidth(), home.getMinimumHeight());
        market.setBounds(0, 0, market.getMinimumWidth(), market.getMinimumHeight());
        purse.setBounds(0, 0, purse.getMinimumWidth(), purse.getMinimumHeight());
        c2c.setBounds(0, 0, c2c.getMinimumWidth(), c2c.getMinimumHeight());

        switch (textView.getId()) {
            case R.id.tv_nav_home:
                tvNavHome.setCompoundDrawables(null, homeTouch, null, null);
                tvNavMarket.setCompoundDrawables(null, market, null, null);
                tvNavPurse.setCompoundDrawables(null, purse, null, null);
                tvNavC2C.setCompoundDrawables(null, c2c, null, null);
                break;
            case R.id.tv_nav_market:
                tvNavHome.setCompoundDrawables(null, home, null, null);
                tvNavMarket.setCompoundDrawables(null, marketTouch, null, null);
                tvNavPurse.setCompoundDrawables(null, purse, null, null);
                tvNavC2C.setCompoundDrawables(null, c2c, null, null);
                break;
            case R.id.tv_nav_purse:
                tvNavHome.setCompoundDrawables(null, home, null, null);
                tvNavMarket.setCompoundDrawables(null, market, null, null);
                tvNavPurse.setCompoundDrawables(null, purseTouch, null, null);
                tvNavC2C.setCompoundDrawables(null, c2c, null, null);
                break;
            case R.id.tv_nav_c2c:
                tvNavC2C.setCompoundDrawables(null, c2cTouch, null, null);
                tvNavHome.setCompoundDrawables(null, home, null, null);
                tvNavMarket.setCompoundDrawables(null, market, null, null);
                tvNavPurse.setCompoundDrawables(null, purse, null, null);
                break;
        }
    }

    /**
     * 导航栏交互特效
     *
     * @param facingTextView 当前的控件
     * @param textView1
     * @param textView2
     */
    protected void touchColor(TextView facingTextView, TextView textView1, TextView textView2, TextView textView3) {
        facingTextView.setBackground(getDrawable(R.drawable.tv_border_touch));
        textView1.setBackground(getDrawable(R.drawable.tv_border));
        textView2.setBackground(getDrawable(R.drawable.tv_border));
        textView3.setBackground(getDrawable(R.drawable.tv_border));
        facingTextView.getPaint().setFakeBoldText(true);
        textView1.getPaint().setFakeBoldText(false);
        textView2.getPaint().setFakeBoldText(false);
        textView3.getPaint().setFakeBoldText(false);
        facingTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_dullRed1, null));
        textView1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_black, null));
        textView2.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_black, null));
        textView3.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_black, null));
    }

    /**
     * 标识是否关闭程序
     */
    private boolean isExit = false;

    /**
     * 检测物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (homeFragment.isHidden()){
                exit();
            }else {
              boolean result=  homeFragment.onKeyDown();
              if (result){
                  exit();
              }
            }

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mainReceiver);
    }

    private void exit() {
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

    @Override
    protected void onStart() {
        super.onStart();
    }
    public static void sendBroadcast(Context context,int num){
        Intent intent=new Intent(MainReceiver.EVENT);
        intent.putExtra("num",num);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class MainReceiver extends BroadcastReceiver{
        public static final String EVENT="MainReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            int num=intent.getIntExtra("num",0);
            setPoint(num);

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

   public  interface  OnKeyDown{
      boolean onKeyDown();
   }


}



