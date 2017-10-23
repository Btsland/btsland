package info.btsland.app.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private HeadFragment headFragment;

    private TextView tvSetLanguage;
    private TextView tvSetTheme;
    private TextView tvSetGuide;
    private TextView tvSetWe;
    private TextView tvSetEdition;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(info.btsland.app.R.layout.activity_setting);
        fillInHead();
        init();
}

    /**
     * 初始化
     */
    private void init(){
        tvSetLanguage= (TextView) findViewById(R.id.tv_set_language);
        tvSetTheme= (TextView) findViewById(R.id.tv_set_theme);
        tvSetGuide= (TextView) findViewById(R.id.tv_set_guide);
        tvSetWe= (TextView) findViewById(R.id.tv_set_we);
        tvSetEdition= (TextView) findViewById(R.id.tv_set_edition);

        //绑定特效事件
        TextViewOnTouchListener OnTouchListener=new TextViewOnTouchListener();
        tvSetLanguage.setOnTouchListener(OnTouchListener);
        tvSetTheme.setOnTouchListener(OnTouchListener);
        tvSetGuide.setOnTouchListener(OnTouchListener);
        tvSetWe.setOnTouchListener(OnTouchListener);
        tvSetEdition.setOnTouchListener(OnTouchListener);

        //绑定点击事件
        TextViewOnClickListener OnClickListener=new TextViewOnClickListener();
        tvSetLanguage.setOnClickListener(OnClickListener);
        tvSetGuide.setOnClickListener(OnClickListener);
        tvSetWe.setOnClickListener(OnClickListener);
        tvSetEdition.setOnClickListener(OnClickListener);
    }

    /*
     *点击事件
     */
    class TextViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_set_language:
                    showListDialog();
                    break;
                case R.id.tv_set_theme:
                    break;
                case R.id.tv_set_guide:
                    Intent iii = new Intent(SettingActivity.this , UsersGuidanceActivity.class);
                    startActivity(iii);
                    break;
                case R.id.tv_set_we:
                    Log.i("dddddd", "onClick: ");
                    Intent i = new Intent(SettingActivity.this , AboutUsActivity.class);
                    startActivity(i);
                    break;
                case R.id.tv_set_edition:
                    Intent ii = new Intent(SettingActivity.this , VersionInformationActivity.class);
                    startActivity(ii);
                    break;
            }
        }
    }


    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=new HeadFragment(HeadFragment.HeadType.BACK_NULL);
            headFragment.setTitleName(getString(R.string.set));
            transaction.add(R.id.fra_set_head,headFragment);
        }
        transaction.commit();
    }

    /**
     * 跳出Dialog窗口
     */
    private void showListDialog() {
        //创建Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_language, (LinearLayout) findViewById(R.id.language_dialog));

        //获得参数
        RadioButton chinese = layout.findViewById(R.id.select_chinese);
        RadioButton english = layout.findViewById(R.id.select_english);
        chinese.setOnClickListener(SettingActivity.this);
        english.setOnClickListener(SettingActivity.this);

        dialog.setView(layout);
        dialog.show();
    }
    //框内控件
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.select_chinese:
                switchLanguage("zh");
                break;
            case R.id.select_english:
                switchLanguage("en");
                break;
        }

        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
        //开始新的activity同时移除之前所有的activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    class TextViewOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.tv_set_language:
                    touchColor(tvSetLanguage,motionEvent);
                    break;
                case R.id.tv_set_theme:
                    touchColor(tvSetTheme,motionEvent);
                    break;
                case R.id.tv_set_guide:
                    touchColor(tvSetGuide,motionEvent);
                    break;
                case R.id.tv_set_we:
                    touchColor(tvSetWe,motionEvent);
                    break;

                case R.id.tv_set_edition:
                    touchColor(tvSetEdition,motionEvent);
                    break;
            }
            return false;
        }
    }
}
