package info.btsland.app.ui.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.HomeFragment;
import info.btsland.app.ui.fragment.MarketFragment;
import info.btsland.app.ui.fragment.PurseFragment;
import info.btsland.app.util.PreferenceUtil;

/**
 * 作者：谢建华
 * 创建时间：2017/09/27
 * 完成时间：
 */
public class MainActivity extends BaseActivity {
    private String TAG="MainActivity";
    private TextView tvNavHome;
    private TextView tvNavMarket;
    private TextView tvNavPurse;
    private MarketFragment marketFragment;
    private HomeFragment homeFragment;
    private PurseFragment purseFragment;
    private HeadFragment headFragment;
    private TextView tvHeadLeft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化PreferenceUtil
        PreferenceUtil.init(this);
        // 依据上次的语言设置，又一次设置语言
        switchLanguage(PreferenceUtil.getString("language", "zh"));
        setContentView(R.layout.activity_main);
        fillInHead();
        fillInBody();
        init();

    }


    /**
     * 初始化
     */
    protected void init() {
        //初始化
        tvNavHome = (TextView) findViewById(R.id.tv_nav_home);
        tvNavMarket = (TextView) findViewById(R.id.tv_nav_market);
        tvNavPurse = (TextView) findViewById(R.id.tv_nav_purse);
        //绑定监听器
        tvNavHome.setOnClickListener(new NavOnClickListener());
        tvNavMarket.setOnClickListener(new NavOnClickListener());
        tvNavPurse.setOnClickListener(new NavOnClickListener());

        touchColor(tvNavMarket, tvNavHome, tvNavPurse);//选中行情控件
        touchImage(tvNavMarket);
        showFragment(tvNavMarket);//显示行情页面
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.USER_SET);
            transaction.add(R.id.fra_main_head, headFragment);
        }
        transaction.commit();
    }

    /**
     * 装载主体内容
     */
    private void fillInBody() {
        //初始化fra_main_body
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (marketFragment == null) {
            marketFragment =new MarketFragment();
            transaction.add(R.id.fra_main_body, marketFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onStart() {
        if(BtslandApplication.isWel==false){
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            BtslandApplication.isWel=true;
        }

        super.onStart();
    }

    /**
     * 根据底部导航栏选定的控件进行切换fra
     *
     * @param textView 选定的控件
     */
    private void showFragment(TextView textView) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (textView.getId()) {
            case R.id.tv_nav_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fra_main_body, homeFragment);

                }
                hideFragment(transaction);
                transaction.show(homeFragment);
                break;
            case R.id.tv_nav_market:
                if (marketFragment == null) {
                    marketFragment = new MarketFragment();
                    transaction.add(R.id.fra_main_body, marketFragment);
                }
                hideFragment(transaction);
                transaction.show(marketFragment);
                break;
            case R.id.tv_nav_purse:
                if (purseFragment == null) {
                    purseFragment = new PurseFragment();
                    transaction.add(R.id.fra_main_body, purseFragment);
                }
                hideFragment(transaction);
                transaction.show(purseFragment);
                break;
        }
        transaction.commit();
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
    }

    /**
     * 底部导航栏操作效果
     */
    class NavOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_nav_home:
                    touchColor(tvNavHome, tvNavMarket, tvNavPurse);//控件特效
                    touchImage(tvNavHome);
                    showFragment(tvNavHome);
                    break;
                case R.id.tv_nav_market:
                    touchColor(tvNavMarket, tvNavHome, tvNavPurse);//控件特效
                    touchImage(tvNavMarket);
                    showFragment(tvNavMarket);
                    break;
                case R.id.tv_nav_purse:
                    touchColor(tvNavPurse, tvNavHome, tvNavMarket);//控件特效
                    touchImage(tvNavPurse);
                    showFragment(tvNavPurse);
                    break;
            }
        }


    }

    /**
     * 设置图片点击特效
     * @param textView
     */
    private void touchImage(TextView textView) {
        Drawable homeTouch = getResources().getDrawable(R.drawable.image_nav_home_touch,null);
        Drawable marketTouch = getResources().getDrawable(R.drawable.image_nav_market_touch,null);
        Drawable purseTouch = getResources().getDrawable(R.drawable.image_nav_purse_touch,null);
        Drawable home = getResources().getDrawable(R.drawable.image_nav_home,null);
        Drawable market = getResources().getDrawable(R.drawable.image_nav_market,null);
        Drawable purse = getResources().getDrawable(R.drawable.image_nav_purse,null);

        //显示图片
        homeTouch.setBounds(0,0,homeTouch.getMinimumWidth(),homeTouch.getMinimumHeight());
        marketTouch.setBounds(0,0,marketTouch.getMinimumWidth(),marketTouch.getMinimumHeight());
        purseTouch.setBounds(0,0,purseTouch.getMinimumWidth(),purseTouch.getMinimumHeight());
        home.setBounds(0,0,home.getMinimumWidth(),home.getMinimumHeight());
        market.setBounds(0,0,market.getMinimumWidth(),market.getMinimumHeight());
        purse.setBounds(0,0,purse.getMinimumWidth(),purse.getMinimumHeight());

        switch (textView.getId()) {
            case R.id.tv_nav_home:
                tvNavHome.setCompoundDrawables(null,homeTouch,null,null);
                tvNavMarket.setCompoundDrawables(null,market,null,null);
                tvNavPurse.setCompoundDrawables(null,purse,null,null);
                break;
            case R.id.tv_nav_market:
                tvNavHome.setCompoundDrawables(null,home,null,null);
                tvNavMarket.setCompoundDrawables(null,marketTouch,null,null);
                tvNavPurse.setCompoundDrawables(null,purse,null,null);
                break;
            case R.id.tv_nav_purse:
                tvNavHome.setCompoundDrawables(null,home,null,null);
                tvNavMarket.setCompoundDrawables(null,market,null,null);
                tvNavPurse.setCompoundDrawables(null,purseTouch,null,null);
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
    protected void touchColor(TextView facingTextView, TextView textView1, TextView textView2) {
        facingTextView.setBackground(getDrawable(R.drawable.tv_border_touch));
        textView1.setBackground(getDrawable(R.drawable.tv_border));
        textView2.setBackground(getDrawable(R.drawable.tv_border));
        facingTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_dullRed1, null));
        textView1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_black, null));
        textView2.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_black, null));
    }


    /**
     * 头像单击事件功能实现
     */
    class ivNavUserOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            MainActivity.this.startActivity(intent);
        }
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
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
