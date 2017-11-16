package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.app.ui.fragment.HeadFragment;


public class UserActivity extends AppCompatActivity {

    private TextView  tvUserName;
    private TextView tvUserInfo;
    private TextView tvUserWhiteList;
//    private TextView tvUserPower;
    private TextView tvUserRecent;
    private TextView tvUserEntrust;
    private TextView tvUserSeting;
    private TextView tvUserLogoff;
    private HeadFragment headFragment;
    private WebView portrait;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Log.i("UserActivity", "onCreate: ");
        fillInHead();
        init();
        if(BtslandApplication.accountObject!=null){
            createPortrait();//设置头像
            tvUserName.setText("用户名："+BtslandApplication.accountObject.name);
        }
        sharedPreferences= getSharedPreferences("Login", Context.MODE_PRIVATE);


    }

    /**
     * 初始化
     */
    protected void init() {
        //初始化
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserInfo = (TextView) findViewById(R.id.tv_user_info);
        tvUserWhiteList = (TextView) findViewById(R.id.tv_user_whiteList);
//        tvUserPower = (TextView) findViewById(R.id.tv_user_power);
        tvUserRecent = (TextView) findViewById(R.id.tv_user_recent);
        tvUserEntrust = (TextView) findViewById(R.id.tv_user_entrust);
        tvUserSeting = (TextView) findViewById(R.id.tv_user_set);
           tvUserLogoff=(TextView) findViewById(R.id.tv_user_logoff);
        portrait= (WebView) findViewById(R.id.iv_user_pho);
        //绑定监听器
        tvUserInfo.setOnClickListener(new TextViewListener());
        tvUserWhiteList.setOnClickListener(new TextViewListener());
        //tvUserPower.setOnClickListener(new TextViewListener());
        tvUserRecent.setOnClickListener(new TextViewListener());
        tvUserEntrust.setOnClickListener(new TextViewListener());
        tvUserSeting.setOnClickListener(new TextViewListener());
        tvUserLogoff.setOnClickListener(new TextViewListener());
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            transaction.add(R.id.fra_user_head, headFragment);
            headFragment.setTitleName("用户中心");
        }
        transaction.commit();
    }


    /**
     * 设置头像
     */
    public void createPortrait(){
        sha256_object.encoder encoder = new sha256_object.encoder();
        encoder.write(BtslandApplication.accountObject.name.getBytes());
        String htmlShareAccountName = "<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" +100+ ",user-scalable=no/></head><body><canvas width=" +100+ " height=" + 100+ " data-jdenticon-hash=" +encoder.result().toString()+ "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings = portrait.getSettings();
        webSettings.setJavaScriptEnabled(true);
        portrait.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }








    /**
     * 单击特效
     *
     * @param textView    被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch, null));
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row, null));
        }
    }


    class TextViewListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_user_info:
                    //touchColor(tvUserInfo, motionEvent);
                    Intent iUserInfo =new Intent(UserActivity.this,UserInfoActivity.class);
                    startActivity(iUserInfo);
                    break;
                case R.id.tv_user_whiteList:
                    // touchColor(tvUserWhiteList, motionEvent);
                    Intent iWhiteList=new Intent(UserActivity.this,WhiteListActivity.class);
                    startActivity(iWhiteList);
                    break;
//                case R.id.tv_user_power:
//                    touchColor(tvUserPower, motionEvent);
//
//                    break;
                case R.id.tv_user_recent:

                    Intent iRecent=new Intent(UserActivity.this,RecentActivity.class);
                    startActivity(iRecent);
                    break;
                case R.id.tv_user_entrust:
                    //touchColor(tvUserEntrust, motionEvent);
                    Intent iEntrust=new Intent(UserActivity.this,EntrustActivity.class);
                    startActivity(iEntrust);
                    break;
                case R.id.tv_user_set:
                    // touchColor(tvUserSetting, motionEvent);

                    Intent iUserSet=new Intent(UserActivity.this,UserSetActivity.class);
                    //启动
                    startActivity(iUserSet);
                    break;

                case R.id.tv_user_logoff:
                    Intent iUserLogin=new Intent(UserActivity.this,LoginActivity.class);
                    startActivity(iUserLogin);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    BtslandApplication.isLogin=false;
                    BtslandApplication.accountObject=null;


                    break;


            }
        }
    }
}
