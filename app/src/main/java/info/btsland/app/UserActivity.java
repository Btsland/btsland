package info.btsland.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends Activity {
    private TextView tvUserInfo;
    private TextView tvUserWhiteList;
    private TextView tvUserPower;
    private TextView tvUserRecent;
    private TextView tvUserEntrust;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
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
        //绑定监听器
        tvUserInfo.setOnTouchListener(new TextViewListener());
        tvUserWhiteList.setOnTouchListener(new TextViewListener());
        tvUserPower.setOnTouchListener(new TextViewListener());
        tvUserRecent.setOnTouchListener(new TextViewListener());
        tvUserEntrust.setOnTouchListener(new TextViewListener());
    }

    /**
     * 单击特效
     * @param textView 被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView,MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackgroundResource(R.color.colorAccent);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackgroundResource(R.color.color_white);
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
            }
            return true;
        }
    }
}
