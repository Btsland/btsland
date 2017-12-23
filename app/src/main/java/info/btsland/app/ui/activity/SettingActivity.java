package info.btsland.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.btsland.app.Adapter.RowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppListDialog;


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
                getString(R.string.auto_refresh),
                getString(R.string.will_be_refreshed),
                BtslandApplication.isRefurbish,
                false,
                true,
                false,
                true,
                false,
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            BtslandApplication.isRefurbish=true;
                            BtslandApplication.saveIsRefurbish();
                            BtslandApplication.getMarketStat().restartSubscription();
                        }else {
                            BtslandApplication.isRefurbish=false;
                            BtslandApplication.saveIsRefurbish();
                        }
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
                getString(R.string.language),
                getString(R.string.select_your_language),
                BtslandApplication.isRefurbish,
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
                getString(R.string.price_limit),
                getString(R.string.set_preference_color),
                BtslandApplication.isRefurbish,
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
                        showluctuationTyFpeDialog();
                    }
                },
                null
        );
        rowItemDataList.add(changeItemData);
        RowAdapter.RowItemData nodeItemData=new RowAdapter.RowItemData("",
                getString(R.string.node_selection),
                getString(R.string.set_server_node),
                BtslandApplication.isRefurbish,
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
                        showStrServerDialog();
                    }
                },
                null
        );
        rowItemDataList.add(nodeItemData);
        RowAdapter.RowItemData chargeUnitItemData=new RowAdapter.RowItemData("",
                getString(R.string.charge_unit),
                getString(R.string.set_pricing_unit),
                BtslandApplication.isRefurbish,
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
                        showChargeUnitDialog();
                    }
                },
                null
        );
        rowItemDataList.add(chargeUnitItemData);
        RowAdapter.RowItemData helpItemData=new RowAdapter.RowItemData("",
                getString(R.string.operating_guide),
                "",
                BtslandApplication.isRefurbish,
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
                getString(R.string.version_information),
                "",
                BtslandApplication.isRefurbish,
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
                        Intent intent=new Intent(SettingActivity.this,VersionInformationActivity.class);
                        startActivity(intent);
                    }
                },
                null
        );
        rowItemDataList.add(versionItemData);
        RowAdapter.RowItemData weItemData=new RowAdapter.RowItemData("",
                getString(R.string.about),
                "",
                BtslandApplication.isRefurbish,
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


        final List<String> string1= Arrays.asList("zh","en");
        final List<String> string2= Arrays.asList("中文","English");
        int checkNum=0;
        for(int i=0;i<string1.size();i++){
            if(string1.get(i).equals(BtslandApplication.Language)){
                checkNum=i;
                break;
            }
        }
        AppListDialog dialog=new AppListDialog(SettingActivity.this,string2,checkNum);
        dialog.setTitle("请选择语言");
        dialog.setListener(new AppListDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(MarketTicker market) {

            }

            @Override
            public void onConfirm(String server) {
                for(int i=0;i<string2.size();i++){
                    if(string2.get(i).equals(server)){
                        BtslandApplication.Language = string1.get(i);
                        BtslandApplication.saveLanguage();
                        restart();
                        break;
                    }
                }
                BtslandApplication.saveLanguage();
            }

            @Override
            public void onReject() {

            }
        });
        dialog.show();
    }
    private void showChargeUnitDialog() {

        List<String> strings= Arrays.asList("CNY","BTS");
        int checkNum=0;
        for(int i=0;i<strings.size();i++){
            if(strings.get(i).equals(BtslandApplication.chargeUnit)){
                checkNum=i;
                break;
            }
        }
        AppListDialog dialog=new AppListDialog(SettingActivity.this,strings,checkNum);
        dialog.setTitle("请选择币种");
        dialog.setListener(new AppListDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(MarketTicker market) {

            }

            @Override
            public void onConfirm(String server) {
                BtslandApplication.chargeUnit=server;
                BtslandApplication.saveChargeUnit();
            }

            @Override
            public void onReject() {

            }
        });
        dialog.show();
    }
    private void showStrServerDialog() {

        List<String> mListNode=BtslandApplication.mListNode;
        int checkNum=0;
        for(int i=0;i<mListNode.size();i++){
            if(mListNode.get(i).equals(BtslandApplication.strServer)){
                checkNum=i;
                break;
            }
        }
        AppListDialog dialog=new AppListDialog(SettingActivity.this,mListNode,checkNum);

        dialog.setTitle("请选择节点");
        dialog.setListener(new AppListDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(MarketTicker market) {

            }

            @Override
            public void onConfirm(String server) {
                BtslandApplication.strServer=server;
                BtslandApplication.saveStrServer();
                BtslandApplication.getMarketStat().restartSubscription();
            }

            @Override
            public void onReject() {

            }
        });
        dialog.show();
    }
    private void showluctuationTyFpeDialog() {

        final List<String> string1=Arrays.asList("绿涨红跌","红涨绿跌");
        final List<Integer> ints=Arrays.asList(1,2);
        int checkNum=0;
        for(int i=0;i<ints.size();i++){
            if(ints.get(i)==BtslandApplication.fluctuationType){
                checkNum=i;
                break;
            }
        }
        AppListDialog dialog=new AppListDialog(SettingActivity.this,string1,checkNum);
        dialog.setTitle("请选择类型");
        dialog.setListener(new AppListDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(MarketTicker market) {

            }

            @Override
            public void onConfirm(String server) {
                for(int i=0;i<string1.size();i++){
                    if(string1.get(i).equals(server)){
                        BtslandApplication.fluctuationType=ints.get(i);
                        BtslandApplication.saveFluctuationType();
                        BtslandApplication.setFluctuationType();
                        BtslandApplication.getMarketStat().restartSubscription();
                        break;
                    }
                }
            }

            @Override
            public void onReject() {

            }
        });

        dialog.show();

    }
    public void restart() {
        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
        //开始新的activity同时移除之前所有的activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
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
