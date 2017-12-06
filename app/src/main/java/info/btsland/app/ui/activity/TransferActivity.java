package info.btsland.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.sha256_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.model.Market;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.PurseFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.app.util.NumericUtil;

/**
 * 转账操作
 */

public class TransferActivity extends AppCompatActivity {
    private final String TAG="TransferActivity";
    private EditText edFrom;
    private EditText edTo;
    private WebView wvFrom;
    private WebView wvTo;
    private Spinner spCoin;
    private EditText edCoinNum;
    private EditText edRemarkText;
    private TextView tvSend;
    private TextView tvBalanceNum;
    private TextView tvSecond;
    private TextView tvMsg;
    private PasswordDialog passwordDialog;

    private Map<String,IAsset> iAssetMap;

    private HeadFragment headFragment;
    private TextView tvPoint;
    private TextView tvCancel;
    private int time=10;
    private Timer timer;

    private String password;

    String from;
    String to;
    String vol;
    String symbol;
    String memo;
    Double volNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        fillInHead();
        flinIn();
    }
    private void init(){
        edFrom=findViewById(R.id.ed_transfer_from);
        edTo=findViewById(R.id.ed_transfer_to);
        wvFrom=findViewById(R.id.wv_pho_from);
        wvTo=findViewById(R.id.wv_pho_to);
        spCoin=findViewById(R.id.sp_transfer_coin);
        edCoinNum=findViewById(R.id.ed_transfer_coinNum);
        edRemarkText=findViewById(R.id.tv_transfer_remark_text);
        tvSend=findViewById(R.id.tv_transfer_send);
        tvCancel=findViewById(R.id.tv_transfer_cancel);
        tvBalanceNum=findViewById(R.id.tv_transfer_balanceNum);
        tvPoint=findViewById(R.id.tv_transfer_point);
        List<IAsset> iAssets=BtslandApplication.iAssets;
        iAssetMap=new HashMap<>();
        for(int i=0;i<iAssets.size();i++){
            iAssetMap.put(iAssets.get(i).coinName,iAssets.get(i));
        }
        tvSecond=findViewById(R.id.tv_transfer_second);
        tvMsg=findViewById(R.id.tv_transfer_msg);
    }
    private void flinIn(){
        tvSecond.setVisibility(View.INVISIBLE);
        tvMsg.setVisibility(View.INVISIBLE);
        createPortrait(wvFrom,BtslandApplication.accountObject.name);
        edFrom.setText(BtslandApplication.accountObject.name);
        edFrom.setFocusable(false);
        edFrom.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edFrom.setClickable(false);
        edTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str=editable.toString();
                createPortrait(wvTo,str);
            }
        });

        edCoinNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s= editable.toString();
                if(s.equals(".")){
                    editable.insert(0,"0");
                    return;
                }
                String coin=spCoin.getSelectedItem().toString();
                IAsset iasset=iAssetMap.get(coin);
                Double d=NumericUtil.parseDouble(s);
                if(d>iasset.total){
                    tvPoint.setTextColor(getResources().getColor(R.color.color_font_red));
                    tvPoint.setText("余额不足！");
                }else {
                    tvPoint.setTextColor(getResources().getColor(R.color.color_green));
                    tvPoint.setText("余额充足！");
                }
            }
        });

        List<asset> assets = BtslandApplication.accountObject.assetlist;

        List<object_id<asset_object>> assetObjects=new ArrayList<>();
        List<asset_object> asset_objects=null;
        for (int i=0;i<assets.size();i++){
            assetObjects.add(assets.get(i).asset_id);
        }
        try {
            asset_objects = BtslandApplication.getMarketStat().mWebsocketApi.get_assets(assetObjects);
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
        if(asset_objects==null){
            return;
        }
        String[] strings=new String[asset_objects.size()];
        for(int i=0;i<asset_objects.size();i++){
            strings[i]=asset_objects.get(i).symbol;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.coin_item,R.id.tv_transfer_coinName,strings);
        spCoin.setAdapter(adapter);
        spCoin.setSelection(0);
        spCoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String coin = (String) spCoin.getItemAtPosition(i);
                iAssetMap.get(coin);
                tvBalanceNum.setText(String .valueOf(iAssetMap.get(coin).total));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        timer=new Timer();
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(time==0){
                    return;
                }
                if(time!=10){
                    time=10;
                    if(timer!=null){
                        timer.cancel();
                        timer=null;
                    }
                    transfer();
                }else {
                    from=edFrom.getText().toString();
                    to=edTo.getText().toString();
                    vol=edCoinNum.getText().toString();
                    symbol=spCoin.getSelectedItem().toString();
                    memo=edRemarkText.getText().toString();
                    volNum = NumericUtil.parseDouble(vol);
                    passwordDialog=new PasswordDialog(TransferActivity.this);
                    passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm(final AlertDialog dialog, final String passwordString) {
                            dialog.dismiss();
                            account_object accountObject= null;
                            try {
                                accountObject = BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                            } catch (NetworkStatusException e) {
                                e.printStackTrace();
                            }
                            if(accountObject!=null) {
                                password=passwordString;
                                if(timer==null){
                                    timer=new Timer();
                                }
                                TimerTask mTimerTask= new TimerTask() {
                                    @Override
                                    public void run() {
                                        if(time>0){
                                            time--;
                                            timehandler.sendEmptyMessage(time);
                                        }
                                        if(time<=0){
                                            time=10;
                                            if(timer!=null){
                                                timer.cancel();
                                                timer=null;
                                            }
                                            transfer();
                                        }
                                    }
                                };
                                timer.schedule(mTimerTask, 0, 1000);
                            }else {
                                handler.sendEmptyMessage(-2);
                            }
                        }
                        @Override
                        public void onReject(AlertDialog dialog) {

                        }
                    });
                    passwordDialog.show();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(time==0){
                    return;
                }
                tvSecond.setVisibility(View.INVISIBLE);
                tvMsg.setVisibility(View.INVISIBLE);
                tvSend.setText("确定");
                if(timer!=null){
                    timer.cancel();
                    timer=null;
                    time=10;
                    AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","取消成功！");
                    appDialog.show();
                }else {
                    AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","当前未发起转账！");
                    appDialog.show();
                }


            }
        });

    }
    public void transfer(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if (BtslandApplication.getWalletApi().unlock(password) == 0) {
                    try {
                        if (BtslandApplication.getWalletApi().transfer(from, to, vol, symbol, memo) != null) {
                            handler.sendEmptyMessage(1);

                        } else {
                            handler.sendEmptyMessage(-1);
                        }
                        if(timer!=null){
                            timer.cancel();
                            timer=null;
                        }
                    } catch (NetworkStatusException e) {
                        e.printStackTrace();
                    }
                }else {
                    handler.sendEmptyMessage(-1);
                }
            }
        });
        thread.start();
    }

    /**
     * 设置头像
     */
    public void createPortrait(WebView webView,String name) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 40 + ",user-scalable=no/></head><body><canvas width=" + 40 + " height=" + 40 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"转账");
            transaction.add(R.id.fra_transfer_set_head,headFragment);
        }
        transaction.commit();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tvSecond.setVisibility(View.INVISIBLE);
            tvMsg.setVisibility(View.INVISIBLE);
            if(msg.what==1){
                Toast.makeText(BtslandApplication.getInstance(),"转账成功",Toast.LENGTH_SHORT).show();
                tvSend.setText("确定");
            }else if(msg.what==-1) {
                AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","转账失败！");
                appDialog.show();
            }else if(msg.what==-2) {
                AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","密码错误！");
                appDialog.show();
            }
        }
    };
    private Handler timehandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tvSend.setText("确定("+msg.what+")");
            tvSecond.setVisibility(View.VISIBLE);
            tvSecond.setText(""+msg.what);
            tvMsg.setVisibility(View.VISIBLE);
            if(msg.what==0){
                tvSend.setText("正在转账。。。");
            }
        }
    };
}
