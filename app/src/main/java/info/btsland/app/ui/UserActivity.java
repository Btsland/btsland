package info.btsland.app.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


public class UserActivity extends Activity {
    private TextView tvUserInfo;
    private TextView tvUserWhiteList;
    private TextView tvUserPower;
    private TextView tvUserRecent;
    private TextView tvUserEntrust;
    private TextView tvUserSeting;
    private HeadFragment headFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Log.i("UserActivity", "onCreate: ");
        fillInHead();
        init();
    }

    /**
     * 初始化
     */
    protected void init(){
        //初始化
        tvUserInfo=(TextView)findViewById(R.id.tv_user_info);
        tvUserWhiteList=(TextView)findViewById(R.id.tv_user_whiteList);
        tvUserPower=(TextView)findViewById(R.id.tv_user_power);
        tvUserRecent=(TextView)findViewById(R.id.tv_user_recent);
        tvUserEntrust=(TextView)findViewById(R.id.tv_user_entrust);
        tvUserSeting=(TextView)findViewById(R.id.tv_user_set);
        //绑定监听器
        tvUserInfo.setOnTouchListener(new TextViewListener());
        tvUserWhiteList.setOnTouchListener(new TextViewListener());
        tvUserPower.setOnTouchListener(new TextViewListener());
        tvUserRecent.setOnTouchListener(new TextViewListener());
        tvUserEntrust.setOnTouchListener(new TextViewListener());
        tvUserSeting.setOnTouchListener(new TextViewListener());
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=new HeadFragment();
            transaction.add(R.id.fra_user_head,headFragment);
        }
        transaction.commit();
    }
    /**
     * 单击特效
     * @param textView 被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView,MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch,null));
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row,null));
        }
    }
    class TextViewListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.tv_user_info:
                    touchColor(tvUserInfo,motionEvent);

                    break;
                case R.id.tv_user_whiteList:
                    touchColor(tvUserWhiteList,motionEvent);
                    break;
                case R.id.tv_user_power:
                    touchColor(tvUserPower,motionEvent);
                    break;
                case R.id.tv_user_recent:
                    touchColor(tvUserRecent,motionEvent);
                    break;
                case R.id.tv_user_entrust:
                    touchColor(tvUserEntrust,motionEvent);
                    break;
                case R.id.tv_user_set:
                    touchColor(tvUserSeting,motionEvent);
                    break;
            }
            return false;
        }
    }
}
