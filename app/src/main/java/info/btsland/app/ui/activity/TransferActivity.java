package info.btsland.app.ui.activity;

import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.sha256_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.IAsset;
import info.btsland.app.ui.fragment.HeadFragment;
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
    private PasswordDialog passwordDialog;
    private KProgressHUD hud;

    private Map<String,IAsset> iAssetMap;

    private HeadFragment headFragment;
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
        tvBalanceNum=findViewById(R.id.tv_transfer_balanceNum);
        List<IAsset> iAssets=BtslandApplication.iAssets;
        iAssetMap=new HashMap<>();
        for(int i=0;i<iAssets.size();i++){
            iAssetMap.put(iAssets.get(i).coinName,iAssets.get(i));
        }
    }
    private void flinIn(){
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
                    AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","余额不足！");
                    appDialog.show();
                    return;
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

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String from=edFrom.getText().toString();
                final String to=edTo.getText().toString();
                final String vol=edCoinNum.getText().toString();
                final String symbol=spCoin.getSelectedItem().toString();
                final String memo=edRemarkText.getText().toString();
                Double volNum = NumericUtil.parseDouble(vol);
                passwordDialog=new PasswordDialog(TransferActivity.this);
                passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(final AlertDialog dialog, final String passwordString) {
                        dialog.dismiss();
                        hud= KProgressHUD.create(TransferActivity.this);
                        hud.setLabel(getResources().getString(R.string.please_wait));
                        hud.show();
                        final Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    account_object accountObject= BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                                    if(accountObject!=null){
                                        if (BtslandApplication.getWalletApi().unlock(passwordString) == 0) {
                                            if(BtslandApplication.getWalletApi().transfer(from,to,vol,symbol,memo)!=null){
                                                handler.sendEmptyMessage(1);
                                            }else {
                                                handler.sendEmptyMessage(-1);
                                            }
                                        }else {
                                            handler.sendEmptyMessage(-2);
                                        }
                                    }else {
                                        handler.sendEmptyMessage(-2);
                                    }
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                        thread.start();

                    }

                    @Override
                    public void onReject(AlertDialog dialog) {

                    }
                });
                passwordDialog.show();


            }
        });

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
            if(hud.isShowing()){
                hud.dismiss();
            }
            if(msg.what==1){
                Toast.makeText(TransferActivity.this,"转账成功",Toast.LENGTH_SHORT).show();
            }else if(msg.what==-1) {
                AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","转账失败！");
                appDialog.show();
            }else {
                AppDialog appDialog=new AppDialog(TransferActivity.this,"提示","密码错误！");
                appDialog.show();
            }
        }
    };
}
