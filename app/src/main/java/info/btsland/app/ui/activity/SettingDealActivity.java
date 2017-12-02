package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.CoinsRowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset_object;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;

public class SettingDealActivity extends AppCompatActivity {
    private static String TAG="SettingDealActivity";
    private ListView lvCoins;
    private ListView lvNewCoins;
    private EditText editText;
    private TextView tvOk;
    private TextView tvCancel;
    private TextView tvConfirm;

    private HeadFragment headFragment;

    private CoinsRowAdapter coinsAdapter;
    private CoinsRowAdapter newCoinsAdapter;

    private int type;
    private List<String> strings=new ArrayList<>();

    public static void startAction(Context context, int type){
        Intent intent=new Intent(context,SettingDealActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_deal);
        this.type=getIntent().getIntExtra("type",1);
        fillInHead();
        init();
        fillIn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BtslandApplication.allAsset==null||BtslandApplication.allAsset.size()==0) {
            headFragment.showPBar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BtslandApplication.queryAllAsset(queryhandler);
                }
            }).start();
        }
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"编辑交易对");
            transaction.add(R.id.fra_setDeal_head, headFragment);
        }
        transaction.commit();
    }

    private void init(){
        lvCoins=findViewById(R.id.lv_setDeal_coins);
        lvNewCoins=findViewById(R.id.lv_setDeal_newCoins);
        editText=findViewById(R.id.ed_setDeal);
        tvOk=findViewById(R.id.tv_setDeal_ok);
        tvCancel=findViewById(R.id.tv_setDeal_cancel);
        tvConfirm=findViewById(R.id.tv_setDeal_confirm);
    }
    private void fillIn(){
        coinsAdapter=new CoinsRowAdapter(SettingDealActivity.this);
        lvCoins.setAdapter(coinsAdapter);
        if(type==1){
            coinsAdapter.setCoins(BtslandApplication.baseList);
        }else {
            coinsAdapter.setCoins(BtslandApplication.quoteList);
        }
        newCoinsAdapter=new CoinsRowAdapter(SettingDealActivity.this);
        lvNewCoins.setAdapter(newCoinsAdapter);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String coin=editText.getEditableText().toString();
                        List<asset_object> assetObjects = BtslandApplication.getAssetByName(coin);
                        Log.e(TAG, "run: assetObjects.size():"+assetObjects.size() );
                        strings=new ArrayList<>();
                        if(assetObjects!=null){
                            for(int i=0;i<assetObjects.size();i++){
                                strings.add(assetObjects.get(i).symbol);
                            }
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        lvNewCoins.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppDialog appDialog=new AppDialog(SettingDealActivity.this);
                appDialog.setMsg("编辑交易对功能正在完善中，敬请期待！");
                appDialog.show();
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            newCoinsAdapter.setCoins(strings);
        }
    };

    private Handler queryhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(headFragment.isAdded()){
                headFragment.hidePBar();
            }

        }
    };
}
