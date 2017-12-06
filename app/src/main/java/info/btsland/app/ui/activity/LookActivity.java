package info.btsland.app.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kaopiz.kprogresshud.KProgressHUD;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.DomesticInformationFragment;
import info.btsland.app.ui.fragment.ForeignInformationFragment;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.HotNewsFragment;
import info.btsland.app.ui.fragment.LatestNewsFragment;
import info.btsland.app.ui.fragment.LookAccountAssetFragment;
import info.btsland.app.ui.fragment.LookAccountOrderFragment;
import info.btsland.app.ui.view.AppDialog;

public class LookActivity extends AppCompatActivity {
    private KProgressHUD hud;
    private HeadFragment headFragment;
    private TextView tvSouSuo;
    private EditText edName;
    private PagerSlidingTabStrip pstTitle;
    private ViewPager viewPager;
    private LookAccountAssetFragment assetFragment;
    private LookAccountOrderFragment orderFragment;
    public Map<String,account_object> accountMap=new HashMap<>();

    public String name;
    private ReFurbishInfo orderRe;
    private ReFurbishInfo assetRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        fillInHead();
        init();
        fillIn();
    }

    private void fillIn() {
        String[] titles={"资产","操作记录"};
        List<Fragment> fragments=new ArrayList<Fragment>();
        assetFragment=new LookAccountAssetFragment();
        orderFragment=new LookAccountOrderFragment();
        fragments.add(assetFragment);
        fragments.add(orderFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        pstTitle.setViewPager(viewPager);
        assetRe=assetFragment;
        orderRe=orderFragment;

    }

    @Override
    protected void onResume() {
        super.onResume();
        show();
    }

    private void show() {
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvSouSuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud=KProgressHUD.create(LookActivity.this);
                hud.setLabel("请稍等。。。");
                name=edName.getEditableText().toString();
                if(name!=null&&!name.equals("")){
                    hud.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final account_object accountObject = BtslandApplication.getMarketStat().mWebsocketApi.get_account_by_name(name);
                                if(accountObject!=null){
                                    accountMap.put(accountObject.name,accountObject);
                                    //handler.sendEmptyMessage(1);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                List<asset> assets=BtslandApplication.getMarketStat().mWebsocketApi.list_account_balances_by_name(accountObject.name);
                                                if(assets!=null&&assets.size()>0) {
                                                    accountObject.assetlist = assets;
                                                    handler.sendEmptyMessage(2);
                                                }
                                            } catch (NetworkStatusException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                List<operation_history_object> listHistoryObject=BtslandApplication.getMarketStat().mWebsocketApi.get_account_history(accountObject.id, new object_id<operation_history_object>(0, operation_history_object.class),
                                                        100);
                                                if(listHistoryObject!=null&&listHistoryObject.size()>0) {
                                                    accountObject.listHistoryObject=listHistoryObject;
                                                    handler.sendEmptyMessage(3);
                                                }
                                            } catch (NetworkStatusException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }else {
                                    handler.sendEmptyMessage(-1);
                                }
                            } catch (NetworkStatusException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }else {
                    AppDialog appDialog=new AppDialog(LookActivity.this,"提示","请输入用户名");
                    appDialog.show();
                }
            }
        });
    }

    private void init(){
        tvSouSuo=findViewById(R.id.tv_look_sousuo);
        edName=findViewById(R.id.ed_look_name);
        pstTitle=findViewById(R.id.psts_look_title);
        viewPager=findViewById(R.id.vp_look);
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"观察");
            transaction.add(R.id.fra_look_head, headFragment);
        }
        transaction.commit();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
            if(msg.what==1){
                refurbishAccount();
            }else if(msg.what==2){
                refurbishAsset();
            }else if(msg.what==3){
                refurbishOrder();
            }else if(msg.what==-1) {
                AppDialog appDialog=new AppDialog(LookActivity.this,"提示","用户不存在");
                appDialog.show();
            }
        }
    };

    private void refurbishAccount() {


    }
    private void refurbishAsset() {
        assetRe.refurbish();
    }
    private void refurbishOrder() {
        orderRe.refurbish();
    }

    public interface ReFurbishInfo{
        void refurbish();
    }
}
