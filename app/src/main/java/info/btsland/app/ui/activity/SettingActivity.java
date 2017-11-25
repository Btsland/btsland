package info.btsland.app.ui.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.RowAdapter;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.util.PreferenceUtil;


public class SettingActivity extends BaseActivity{
    private HeadFragment headFragment;

    private ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(info.btsland.app.R.layout.activity_setting);
        fillInHead();
        init();
        fillIn();
    }

    /**
     * 初始化
     */
    private void init(){
        listView=findViewById(R.id.lv_set_body);
    }
    private void fillIn(){
        List<RowAdapter.RowItemData> rowItemDataList=new ArrayList<>();
        RowAdapter.RowItemData refurbishItemData=new RowAdapter.RowItemData("",
                "自动刷新",
                "开启后将会自动刷新数据",
                false,
                true,
                false,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(refurbishItemData);
        RowAdapter.RowItemData languageItemData=new RowAdapter.RowItemData("",
                "语言",
                "选择您的语言",
                false,
                false,
                true,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showlanguageDialog();
                    }
                },
                null
        );
        rowItemDataList.add(languageItemData);
        RowAdapter.RowItemData changeItemData=new RowAdapter.RowItemData("",
                "涨跌幅",
                "设置您偏好的涨跌幅颜色",
                false,
                false,
                true,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showchangeDialog();
                    }
                },
                null
        );
        rowItemDataList.add(changeItemData);
        RowAdapter.RowItemData nodeItemData=new RowAdapter.RowItemData("",
                "节点选择",
                "选择您需要连接的服务器节点",
                false,
                false,
                true,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(nodeItemData);
        RowAdapter.RowItemData chargeUnitItemData=new RowAdapter.RowItemData("",
                "计价单位",
                "设置您偏好的计价单位",
                false,
                false,
                true,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(chargeUnitItemData);
        RowAdapter.RowItemData helpItemData=new RowAdapter.RowItemData("",
                "使用指南",
                "",
                false,
                false,
                true,
                false,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(helpItemData);
        RowAdapter.RowItemData versionItemData=new RowAdapter.RowItemData("",
                "版本信息",
                "",
                false,
                false,
                true,
                false,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(versionItemData);
        RowAdapter.RowItemData weItemData=new RowAdapter.RowItemData("",
                "关于我们",
                "",
                false,
                false,
                true,
                false,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                },
                null
        );
        rowItemDataList.add(weItemData);
        RowAdapter adapter=new RowAdapter(rowItemDataList,SettingActivity.this);
        listView.setAdapter(adapter);
    }


//
//    /*
//     *点击事件
//     */
//    class TextViewOnClickListener implements View.OnClickListener{
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.tv_set_language_title:
//                    showListDialog();
//                    break;
//                case R.id.tv_set_theme:
//                    showthemeDialog();
//                    break;
//                case R.id.tv_set_we:
//                    Log.i("dddddd", "onClick: ");
//                    Intent i = new Intent(SettingActivity.this , AboutUsActivity.class);
//                    startActivity(i);
//                    break;
//                case R.id.tv_set_edition:
//                    Intent ii = new Intent(SettingActivity.this , VersionInformationActivity.class);
//                    startActivity(ii);
//                    break;
//            }
//        }
//    }
//
//
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,getString(R.string.set));
            transaction.add(R.id.fra_set_head,headFragment);
        }
        transaction.commit();
    }

    /**
     * 跳出Dialog窗口
     */

    int index = 0 ;//设置默认选项，作为checkedItem参数传入。

    private void showlanguageDialog() {

        AlertDialog.Builder listDialog = new AlertDialog.Builder(SettingActivity.this);
        listDialog.setTitle(getString(R.string.selectlanguage));

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
    private void showchangeDialog() {
        AlertDialog.Builder listDialog = new AlertDialog.Builder(SettingActivity.this);
        listDialog.setTitle(getString(R.string.selectlanguage));

        final String[] items={"红涨绿跌","绿涨红跌"};

        listDialog.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        listDialog.show();
    }
//
//    int index2 = 0 ;//设置默认选项，作为checkedItem参数传入。
//
//    private void showthemeDialog(){
//
//        final String[] theme={"白昼模式","黑夜模式"};
//        theme[0]=getString(R.string.daytime);
//        theme[1]=getString(R.string.night);
//
//        AlertDialog.Builder themeDialog = new AlertDialog.Builder(SettingActivity.this);
//        themeDialog.setTitle("主题选择").setIcon(android.R.drawable.ic_dialog_info)
//                .setSingleChoiceItems(theme,index2, new DialogInterface.OnClickListener() {
//                    @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                if (theme[i]==getString(R.string.daytime)){
//                                    boolean sf = getSharedPreferences("cons",MODE_PRIVATE).edit()
//                                            .putInt("theme", R.style.SwitchTheme1).commit();
//                                } else if (theme[i]==getString(R.string.night)){
//                                    boolean sf = getSharedPreferences("cons",MODE_PRIVATE).edit()
//                                            .putInt("theme", R.style.SwitchTheme2).commit();
//                                }
//                                dialog.dismiss();
//                                finish();
//
//                        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
//                        //开始新的activity同时移除之前所有的activity
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                            }
//                    });
//            themeDialog.show();
//        }
//
//
//
////    /**
////     * 单击特效
////     * @param textView 被单击的tv
////     * @param motionEvent 当前状态
////     */
////    protected void touchColor(TextView textView,MotionEvent motionEvent){
////        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
////            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch,null));
////        }
////        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
////            textView.setBackground(getResources().getDrawable(R.drawable.tv_row,null));
////        }
////    }
////    class TextViewOnTouchListener implements View.OnTouchListener{
////        @Override
////        public boolean onTouch(View view, MotionEvent motionEvent) {
////            switch (view.getId()) {
////                case R.id.tv_set_language:
////                    touchColor(tvSetLanguage,motionEvent);
////                    break;
////                case R.id.tv_set_theme:
////                    touchColor(tvSetTheme,motionEvent);
////                    break;
////                case R.id.tv_set_guide:
////                    touchColor(tvSetGuide,motionEvent);
////                    break;
////                case R.id.tv_set_we:
////                    touchColor(tvSetWe,motionEvent);
////                    break;
////
////                case R.id.tv_set_edition:
////                    touchColor(tvSetEdition,motionEvent);
////                    break;
////            }
////            return false;
////        }
////    }
}
