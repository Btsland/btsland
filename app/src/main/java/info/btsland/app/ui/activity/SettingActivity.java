package info.btsland.app.ui.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Method;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.util.PreferenceUtil;


public class SettingActivity extends BaseActivity{
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

//        //绑定特效事件
//        TextViewOnTouchListener OnTouchListener=new TextViewOnTouchListener();
//        tvSetLanguage.setOnTouchListener(OnTouchListener);
//        tvSetTheme.setOnTouchListener(OnTouchListener);
//        tvSetGuide.setOnTouchListener(OnTouchListener);
//        tvSetWe.setOnTouchListener(OnTouchListener);
//        tvSetEdition.setOnTouchListener(OnTouchListener);

        //绑定点击事件
        TextViewOnClickListener OnClickListener=new TextViewOnClickListener();
        tvSetLanguage.setOnClickListener(OnClickListener);
        tvSetTheme.setOnClickListener(OnClickListener);
        tvSetGuide.setOnClickListener(OnClickListener);
        tvSetWe.setOnClickListener(OnClickListener);
        tvSetEdition.setOnClickListener(OnClickListener);

        //判断checkedItem的值
        String lo = PreferenceUtil.getString("language", "zh");
        Log.i("init", "init: "+lo);
        if (lo.equals("zh")){
            index=0;
        }else if (lo.equals("en")){
            index=1;
        }
        Log.i("init", "init: "+index);

        int theme = getSharedPreferences("cons", MODE_PRIVATE).getInt("theme",R.style.SwitchTheme1);
        if (theme==(R.style.SwitchTheme1)){
            index2=0;
        }else if (theme==(R.style.SwitchTheme2)){
            index2=1;
        }
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
                    showthemeDialog();
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

    int index = 0 ;//设置默认选项，作为checkedItem参数传入。

    private void showListDialog() {

        AlertDialog.Builder listDialog = new AlertDialog.Builder(SettingActivity.this);
        listDialog.setTitle(getString(R.string.selectlanguage));

        listDialog.setIcon(android.R.drawable.ic_dialog_info);

        final String[] items={"中文","英文"};
        items[0]=getString(R.string.stringzh);
        items[1]=getString(R.string.stringen);

        listDialog.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which]==getString(R.string.stringzh)){
                    switchLanguage("zh");
                }else if(items[which]==getString(R.string.stringen)){
                    switchLanguage("en");
                }
                dialog.dismiss();
                finish();

                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                //开始新的activity同时移除之前所有的activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        listDialog.show();
    }

    int index2 = 0 ;//设置默认选项，作为checkedItem参数传入。

    private void showthemeDialog(){

        final String[] theme={"白昼模式","黑夜模式"};
        theme[0]=getString(R.string.daytime);
        theme[1]=getString(R.string.night);

        AlertDialog.Builder themeDialog = new AlertDialog.Builder(SettingActivity.this);
        themeDialog.setTitle("主题选择").setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(theme,index2, new DialogInterface.OnClickListener() {
                    @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (theme[i]==getString(R.string.daytime)){
                                    boolean sf = getSharedPreferences("cons",MODE_PRIVATE).edit()
                                            .putInt("theme", R.style.SwitchTheme1).commit();
                                } else if (theme[i]==getString(R.string.night)){
                                    boolean sf = getSharedPreferences("cons",MODE_PRIVATE).edit()
                                            .putInt("theme", R.style.SwitchTheme2).commit();
                                }
                                dialog.dismiss();
                                finish();
                            }
                    });
            themeDialog.show();
        }



//    /**
//     * 单击特效
//     * @param textView 被单击的tv
//     * @param motionEvent 当前状态
//     */
//    protected void touchColor(TextView textView,MotionEvent motionEvent){
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch,null));
//        }
//        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//            textView.setBackground(getResources().getDrawable(R.drawable.tv_row,null));
//        }
//    }
//    class TextViewOnTouchListener implements View.OnTouchListener{
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch (view.getId()) {
//                case R.id.tv_set_language:
//                    touchColor(tvSetLanguage,motionEvent);
//                    break;
//                case R.id.tv_set_theme:
//                    touchColor(tvSetTheme,motionEvent);
//                    break;
//                case R.id.tv_set_guide:
//                    touchColor(tvSetGuide,motionEvent);
//                    break;
//                case R.id.tv_set_we:
//                    touchColor(tvSetWe,motionEvent);
//                    break;
//
//                case R.id.tv_set_edition:
//                    touchColor(tvSetEdition,motionEvent);
//                    break;
//            }
//            return false;
//        }
//    }
}
