package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.AccountTypesAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AccountTpyeDialog;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.RealAsset;
import info.btsland.exchange.http.RealAssetHttp;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountC2CTypesActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private ListView listView;
    private TextView tvAdd;
    private AccountTypesAdapter adapter;

    private int type=0;

    private int ACCOUNT=0;
    private int DEALER=1;
    private String TAG="AccountC2CTypesActivity";
    private AccountTypeReceiver accountTypeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            type=savedInstanceState.getInt("type");
        }
        if(getIntent()!=null){
            type=getIntent().getIntExtra("type",type);
        }
        accountTypeReceiver=new AccountTypeReceiver();
        IntentFilter intentFilter=new IntentFilter(AccountTypeReceiver.EVENT);
        LocalBroadcastManager.getInstance(AccountC2CTypesActivity.this).registerReceiver(accountTypeReceiver,intentFilter);
        setContentView(R.layout.activity_account_c2_ctypes);
        fillInHead();
        init();
        fillIn();
        fillInBody();
        if(BtslandApplication.dealer==null) {
            Bundle bundle=new Bundle();
            bundle.putString("want","hud");
            Message msg=Message.obtain();
            msg.what=1;
            msg.setData(bundle);
            registerHandler.sendMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserHttp.registerAccount(BtslandApplication.accountObject.name, "", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            if (json.indexOf("error") != -1) {
                                BtslandApplication.sendBroadcastDialog(AccountC2CTypesActivity.this, json);
                                Bundle bundle = new Bundle();
                                bundle.putString("want", "hud");
                                Message msg = Message.obtain();
                                msg.setData(bundle);
                                msg.what=2;
                                registerHandler.sendMessage(msg);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("want", "register");
                                Message msg = Message.obtain();
                                msg.setData(bundle);
                                int a = Integer.parseInt(json);
                                if (a > 0) {
                                    msg.what = 1;
                                    registerHandler.sendMessage(msg);
                                } else {
                                    msg.what = 2;
                                    registerHandler.sendMessage(msg);
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private void init() {
        listView = findViewById(R.id.lv_account_types_list);
        tvAdd = findViewById(R.id.tv_account_types_add);
        adapter = new AccountTypesAdapter(AccountC2CTypesActivity.this);
        listView.setAdapter(adapter);
    }

    private void fillIn(){
        if(BtslandApplication.dealer!=null){
            if(BtslandApplication.dealer.realAssets!=null){
                List<RealAsset> accountRealAssets=new ArrayList<>();
                List<RealAsset> dealerRealAssets=new ArrayList<>();
                synchronized (BtslandApplication.dealer.realAssets) {
                    for (int i = 0; i < BtslandApplication.dealer.realAssets.size(); i++) {
                        RealAsset realAsset = BtslandApplication.dealer.realAssets.get(i);
                        if (realAsset.getType() == DEALER) {
                            dealerRealAssets.add(realAsset);
                        } else if (realAsset.getType() == ACCOUNT) {
                            accountRealAssets.add(realAsset);
                        }
                    }
                }
                if(type==DEALER){
                    adapter.setAsset(dealerRealAssets);
                }else if(type==ACCOUNT) {
                    adapter.setAsset(accountRealAssets);
                }
                handler.sendEmptyMessage(1);
            }
        }
    }
    private void fillInBody(){
        adapter.setOnCancelListener(new IOnCancelListener());
        adapter.setOnItemClickListener(new IOnItemClickListener());
        tvAdd.setOnClickListener(new IAddOnClickListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(AccountC2CTypesActivity.this).unregisterReceiver(accountTypeReceiver);
    }

    class IOnCancelListener implements AccountTypesAdapter.OnCancelListener {

        @Override
        public void onCancel(final List<RealAsset> realAssets, final int i) {
            if(BtslandApplication.dealer.getPassword()==null||BtslandApplication.dealer.getPassword().equals("")){
                if(i<realAssets.size()){
                    cancelRealAsset(realAssets.get(i));
                }
            }

        }
    }
    private void cancelRealAsset(final RealAsset realAsset){
        AccountHandler.sendEmptyMessage(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RealAssetHttp.removeRealAsset(BtslandApplication.dealer.getDealerId(),BtslandApplication.dealer.getPassword(), BtslandApplication.dealer.getAccount(), realAsset, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        if(json.indexOf("error")!=-1){
                            BtslandApplication.sendBroadcastDialog(AccountC2CTypesActivity.this,json);
                        }else {
                            int a= Integer.parseInt(json);
                            if (a > 0) {
                                AccountHandler.sendEmptyMessage(3);
                            } else {
                                AccountHandler.sendEmptyMessage(4);
                            }
                        }
                    }
                });
            }
        }).start();
    }


    class IOnItemClickListener implements AccountTypesAdapter.OnItemClickListener {
        @Override
        public void onItemClick(final List<RealAsset> realAssets, final int i) {
            updateRealAsset(realAssets.get(i));
        }
    }
    private void updateRealAsset(RealAsset realAsset){
        Log.e(TAG, "updateRealAsset: "+ realAsset);
        AccountTpyeDialog accountTpyeDialog=new AccountTpyeDialog(AccountC2CTypesActivity.this,realAsset);
        accountTpyeDialog.setListener(new AccountTpyeDialog.OnDialogInterationListener() {
            @Override
            public void onConfirm(final RealAsset realAsset) {
                AccountHandler.sendEmptyMessage(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: "+BtslandApplication.dealer );
                        RealAssetHttp.updateRealAsset(BtslandApplication.dealer.getDealerId(),realAsset, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                if(json.indexOf("error")!=-1){
                                    BtslandApplication.sendBroadcastDialog(AccountC2CTypesActivity.this,json);
                                    AccountHandler.sendEmptyMessage(-1);
                                }else {
                                    int a= Integer.parseInt(json);
                                    if (a > 0) {
                                        AccountHandler.sendEmptyMessage(3);
                                    } else {
                                        AccountHandler.sendEmptyMessage(4);
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onReject() {

            }
        });
        accountTpyeDialog.show();
    }
    class IAddOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (BtslandApplication.dealer != null) {
                Log.e(TAG, "onClick: " );
                RealAsset realAsset=new RealAsset();
                realAsset.setType(type);
                updateRealAsset(realAsset);
            } else {
                AccountHandler.sendEmptyMessage(5);
            }
        }
    }
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"收付款帐号");
            transaction.add(R.id.fra_account_types_head,headFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * 设置listView高度
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listView);
        }
    };
    private KProgressHUD hud;
    private Handler AccountHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: "+msg.what );
            if(msg.what==1){
                if(hud==null){
                    hud=KProgressHUD.create(AccountC2CTypesActivity.this);
                }
                hud.setLabel("请稍等。。。");
                hud.show();
            }else if(msg.what==2){
                if(hud!=null&&hud.isShowing()){
                    hud.dismiss();
                    hud=null;
                }
            }else if(msg.what==3){
                if(hud!=null&&hud.isShowing()){
                    hud.dismiss();
                    hud=null;
                }
                AppDialog appDialog=new AppDialog(AccountC2CTypesActivity.this);
                appDialog.setMsg("更新成功！");
                appDialog.show();
            }else if(msg.what==4){
                if(hud!=null&&hud.isShowing()){
                    hud.dismiss();
                }
                AppDialog appDialog=new AppDialog(AccountC2CTypesActivity.this);
                appDialog.setMsg("更新失败,请确认添加了重复数据");
                appDialog.show();
            }else if(msg.what==5){
                if(hud!=null&&hud.isShowing()){
                    hud.dismiss();
                }
                AppDialog appDialog=new AppDialog(AccountC2CTypesActivity.this);
                appDialog.setMsg("密码错误！");
                appDialog.show();
            }else if(msg.what==-1){
                if(hud!=null&&hud.isShowing()){
                    hud.dismiss();
                }
                AppDialog appDialog=new AppDialog(AccountC2CTypesActivity.this);
                appDialog.setMsg("更新失败！");
                appDialog.show();
            }
        }
    };

    public static void sendBroadcast(Context context){
        Intent intent=new Intent(AccountTypeReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public class AccountTypeReceiver extends BroadcastReceiver{
        public static final String EVENT="AccountTypeReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " );
            fillIn();
        }
    }

    private Handler registerHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String want = msg.getData().getString("want");
            if(want.equals("register")) {
                if (msg.what == 1) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(hud!=null&&hud.isShowing()){
                        hud.dismiss();
                        hud=null;
                    }
                    Toast.makeText(AccountC2CTypesActivity.this,"第一次使用注册成功",Toast.LENGTH_SHORT).show();
                    Intent types=new Intent(AccountC2CTypesActivity.this, AccountC2CTypesActivity.class);
                    types.putExtra("type",0);
                    AccountC2CTypesActivity.this.startActivity(types);
                }else {
                    if(hud!=null&&hud.isShowing()){
                        hud.dismiss();
                        hud=null;
                    }
                    Toast.makeText(AccountC2CTypesActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }
            }else if(want.equals("hud")){
                if(msg.what==1){
                    if(hud==null){
                        hud=KProgressHUD.create(AccountC2CTypesActivity.this);
                    }
                    hud.setLabel("请稍等。。。");
                    hud.show();
                }else {
                    if(hud!=null&&hud.isShowing()){
                        hud.dismiss();
                        hud=null;
                    }
                }

            }
        }
    };

}
