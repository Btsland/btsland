package info.btsland.app.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.btsland.app.Adapter.DealerTypesAdaper;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.account_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AccountTypeListDialog;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.app.ui.view.ToDealerTranDialog;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.RealAsset;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.http.RealAssetHttp;
import info.btsland.exchange.http.TradeHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import info.btsland.exchange.utils.NoteStatCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExchangeDetailedActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private int type=1;
    private String noteNo;
    private static int IN=1;
    private static int OUT=2;
    private Note note;

    private TextView tvDealerId;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvBrokerage;
    private TextView tvNum;
    private TextView tvRemarkCode;
    private TextView tvInNo;
    private TextView tvInNoTab;
    private TextView tvInNoBtn;
    private TextView tvOutNo;
    private TextView tvOutNoTab;
    private TextView tvStartTime;
    private TextView tvAccountReTime;
    private TextView tvEndTime;
    private EditText edDepict;
    private TextView tvStat;
    private TextView tvCodeCopy;
    private TextView tvNumTab;
    private TextView tvRemarkCodeTab;

    private TextView tvBtn;
    private TextView tvBtn0;
    private TextView tvCancel;
    private ViewPager viewPager;
    private LinearLayout llTable;
    private DealerTypesAdaper dealerTypesAdaper;

    private RealAsset real;
    private String TAG="ExchangeDetailedActivity";

    private String depict="未知";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detailed);
        if(savedInstanceState!=null){
            noteNo = savedInstanceState.getString("noteNo");
            type = savedInstanceState.getInt("type");
        }
        if(getIntent()!=null){
            noteNo = getIntent().getStringExtra("noteNo");
            type = getIntent().getIntExtra("type",type);
        }
        fillInHead();
        init();
    }


    private void init() {
        tvDealerId=findViewById(R.id.tv_excDet_dealerId);
        tvName=findViewById(R.id.tv_excDet_name);
        tvPhone=findViewById(R.id.tv_excDet_phone);
        tvBrokerage=findViewById(R.id.tv_excDet_brokerage);
        tvNum=findViewById(R.id.tv_excDet_num);
        tvRemarkCode=findViewById(R.id.tv_excDet_remarkCode);
        tvInNo=findViewById(R.id.tv_excDet_inNo);
        tvInNoBtn=findViewById(R.id.tv_excDet_inNo_btn);
        tvOutNo=findViewById(R.id.tv_excDet_outNo);
        tvStartTime=findViewById(R.id.tv_excDet_startTime);
        tvAccountReTime=findViewById(R.id.tv_excDet_accountReTime);
        tvEndTime=findViewById(R.id.tv_excDet_endTime);
        edDepict=findViewById(R.id.ed_excDet_depict);
        tvStat=findViewById(R.id.tv_excDet_stat);
        tvNumTab=findViewById(R.id.tv_excDet_num_tab);
        tvRemarkCodeTab=findViewById(R.id.tv_excDet_remarkCode_tab);
        tvBtn=findViewById(R.id.tv_excDet_btn);
        tvBtn0=findViewById(R.id.tv_excDet_btn0);
        tvCancel=findViewById(R.id.tv_excDet_cancel);
        tvInNoTab=findViewById(R.id.tv_excDet_inNo_tab);
        tvOutNoTab=findViewById(R.id.tv_excDet_outNo_tab);
        viewPager=findViewById(R.id.vp_excDet_types);
        llTable=findViewById(R.id.ll_excDet_types_table);
        tvCodeCopy=findViewById(R.id.tv_excDet_codeCopy);
        if(type==IN){
            dealerTypesAdaper=new DealerTypesAdaper(ExchangeDetailedActivity.this);
            viewPager.setAdapter(dealerTypesAdaper);
            dealerTypesAdaper.setClickListener(new DealerTypesAdaper.OnItemOnClickListener() {
                @Override
                public void onItemOnClick(String no) {
                    ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", no);
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(ExchangeDetailedActivity.this,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
                }
            });
        }else if(type==OUT) {
        }

    }
    private void fillIn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteHttp.queryNote(noteNo, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json=response.body().string();
                        if(json.indexOf("error")!=-1){
                            BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                        }else {
                            fillNote(json);
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
            }
        }).start();

    }

    private void fillNote(String json) {
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson=gsonBuilder.create();
        note=gson.fromJson(json,Note.class);
        if(note==null){

        }
        depict=note.getDepict();
        if(type==IN){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RealAssetHttp.queryRealAsset(note.getDealerId(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json=response.body().string();
                            if(json.indexOf("error")!=-1){
                                BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                            }else {
                                Log.e(TAG, "onResponse: "+json );
                                fillRealAssets(json);
                                typeHandler.sendEmptyMessage(1);
                            }
                        }
                    });
                }
            }).start();
        }
    }
    private List<RealAsset> realAssets=new ArrayList<>();
    private void fillRealAssets(String json) {
        if(json.equals("")){
            return;
        }
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson=gsonBuilder.create();
        realAssets=gson.fromJson(json,new TypeToken<List<RealAsset>>(){}.getType());
    }

    private void fillInBody(){
        if(note!=null){
            tvDealerId.setText(note.getDealerId());
            tvName.setText(note.getDealerName());
            tvPhone.setText(note.getDealerPhone());
            if(note.getBrokerage()!=null){
                tvBrokerage.setText(""+note.getBrokerage()*100+"%");
            }else {
                tvBrokerage.setText("0.0");
            }

            String depict="未知";
            if(note.getRealDepict()!=null&&!note.getRealDepict().equals("")){
                int a=note.getRealDepict().indexOf("(");
                if(a!=-1){
                    depict="("+note.getRealDepict().substring(0,a)+")";
                }else{
                    depict="("+note.getRealDepict()+")";
                }
            }
            if(note.getRealNo()!=null){
                tvInNo.setText(note.getRealNo()+depict);
            }
            if(note.getAccount()!=null){
                tvOutNo.setText(note.getAccount());
            }
            final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd/HH:mm");
            if(note.getStartTime()!=null) {
                tvStartTime.setText(format.format(note.getStartTime()));
            }
            if(note.getAccountReTime()!=null){
                tvAccountReTime.setText(format.format(note.getAccountReTime()));
            }
            if(note.getEndTime()!=null) {
                tvEndTime.setText(format.format(note.getEndTime()));
            }
            tvStat.setText(NoteStatCode.getTabAccount(note.getStatNo()));

            tvBtn.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvBtn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ExchangeDetailedActivity.this,ChatActivity.class);
                    intent.putExtra("account",BtslandApplication.stringDealers.get(note.getDealerId()).getAccount());
                    startActivity(intent);
                }
            });
            if(type==IN){
                if(note.getStatNo()==NoteStatCode.ACCOUNT_TRANSFERRING||note.getStatNo()==NoteStatCode.ACCOUNT_FILLING){
                    tvBtn.setVisibility(View.VISIBLE);
                    tvBtn.setText("确认已转账");
                    final String finalDepict = depict;
                    tvBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newDepict=edDepict.getText().toString();
                            if(newDepict!=null&& !finalDepict.equals(newDepict)){
                                TradeHttp.updateNoteDepict(noteNo, newDepict, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json = response.body().string();
                                        if (json.indexOf("error") != -1) {
                                            BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                                        } 
                                    }
                                });
                            }
                            if(note.getRealNo()!=null&&!note.getRealNo().equals("")) {
                                TradeHttp.updateNoteStat(noteNo, NoteStatCode.ACCOUNT_CONFIRMED, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json = response.body().string();
                                        if (json.indexOf("error") != -1) {
                                            BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                                        } else {
                                            int a = Integer.parseInt(json);
                                            dialogHandler.sendEmptyMessage(a);
                                        }
                                    }
                                });
                            }else {
                                AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this);
                                appDialog.setMsg("请设置付款账号");
                                appDialog.show();
                            }
                        }
                    });
                }else {
                    tvBtn.setVisibility(View.GONE);
                }
                if(note.getStatNo()==NoteStatCode.ADMIN_CONFIRMED||note.getStatNo()==NoteStatCode.HELP_CONFIRMED){
                    viewPager.setVisibility(View.GONE);
                }
            }else if(type==OUT) {
                if(note.getStatNo()==NoteStatCode.ACCOUNT_TRANSFERRING||note.getStatNo()==NoteStatCode.ACCOUNT_FILLING){
                    tvBtn.setVisibility(View.VISIBLE);
                    tvBtn.setText("转账");
                    tvBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            if(note.getRealNo()!=null&&!note.getRealNo().equals("")) {
                                from=BtslandApplication.accountObject.name;
                                to=note.getDealerId();
                                vol= String.valueOf(note.getAssetNum());
                                symbol="CNY";
                                memo="btsland U2U提现:"+note.getRemarkCode();
                                ToDealerTranDialog toDealerTranDialog=new ToDealerTranDialog(ExchangeDetailedActivity.this,from,to,vol,symbol,memo);
                                toDealerTranDialog.setListener(new ToDealerTranDialog.OnDialogInterationListener() {
                                    @Override
                                    public void onConfirm() {
                                        PasswordDialog passwordDialog=new PasswordDialog(ExchangeDetailedActivity.this);
                                        passwordDialog.setListener(new PasswordDialog.OnDialogInterationListener() {
                                            @Override
                                            public void onConfirm(AlertDialog dialog, String passwordString) {
                                                password=passwordString;
                                                hud=KProgressHUD.create(ExchangeDetailedActivity.this);
                                                hud.setLabel("请稍等。。。");
                                                hud.show();
                                                transfer();
                                            }

                                            @Override
                                            public void onReject(AlertDialog dialog) {

                                            }
                                        });
                                        passwordDialog.show();
                                    }


                                @Override
                                public void onReject() {

                                }
                            });
                            toDealerTranDialog.show();
                        } else {
                                AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this);
                                appDialog.setMsg("请设置收款账号");
                                appDialog.show();
                            }

                        }
                    });
                }
                if(note.getStatNo()==NoteStatCode.ADMIN_CONFIRMED||note.getStatNo()==NoteStatCode.HELP_CONFIRMED){
                    viewPager.setVisibility(View.GONE);
                }
            }

            if(NoteStatCode.getTabAccount(note.getStatNo()).equals(NoteStatCode.TRANSFER)){
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this);
                        appDialog.setMsg("您真的要取消吗？取消后将无法撤回，请慎重！");
                        appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                            @Override
                            public void onConfirm() {
                                TradeHttp.updateNoteStat(noteNo, NoteStatCode.CANCELLED, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json= response.body().string();
                                        if(json.indexOf("error")!=-1){
                                            BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                                        }else {
                                            int a= Integer.parseInt(json);
                                            dialogHandler.sendEmptyMessage(a);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onReject() {

                            }
                        });
                        appDialog.show();

                    }
                });
            }
            if(note.getDepict()!=null) {
                edDepict.setText((note.getDepict()));
            }
            tvNum.setText(note.getAssetNum()+note.getAssetCoin());
            tvRemarkCode.setText(note.getRemarkCode());
            if(type==IN){
                tvNumTab.setText("充值额度");
                tvRemarkCodeTab.setText("充值码");
                tvInNoTab.setText("收币账号");
                tvOutNoTab.setText("付款账号");
            }else if(type==OUT){
                tvNumTab.setText("提现额度");
                tvRemarkCodeTab.setText("提现码");
                tvInNoTab.setText("付币账号");
                tvOutNoTab.setText("收款账号");
            }
            tvCodeCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", tvRemarkCode.getText().toString());
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(ExchangeDetailedActivity.this,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
                }
            });
            if(note.getStatNo()!=NoteStatCode.ACCOUNT_FILLING&&note.getStatNo()!=NoteStatCode.ACCOUNT_TRANSFERRING){
                tvInNoBtn.setVisibility(View.GONE);
                tvCodeCopy.setVisibility(View.GONE);
            }
            tvInNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BtslandApplication.dealer.realAssets!=null) {
                        AccountTypeListDialog accountTypeListDialog = new AccountTypeListDialog(ExchangeDetailedActivity.this, BtslandApplication.dealer.realAssets);
                        accountTypeListDialog.setListener(new AccountTypeListDialog.OnDialogInterationListener() {
                            @Override
                            public void onConfirm(final RealAsset realAsset, final String type) {
                                TradeHttp.updateNoteReal(note.getNoteNo(), realAsset.getRealAssetNo(), realAsset.getRealAssetType(), realAsset.getDepict(), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json=response.body().string();
                                        int index=json.indexOf("error");
                                        if(index!=-1){
                                            BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                                        }else {
                                            int a= Integer.parseInt(json);
                                            dialogHandler.sendEmptyMessage(a);
                                            real = realAsset;
                                            Message message= Message.obtain();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("type",type);
                                            message.setData(bundle);
                                            inNoHandler.sendMessage(message);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onReject() {

                            }
                        });
                        accountTypeListDialog.show();
                    }else {
                        String msg="";
                        if(type==IN){
                            msg="您没有选择付款账号，请选择";
                        }else if(type==OUT){
                            msg="您没有选择收款账号，请选择";
                        }
                        AppDialog appDialog = new AppDialog(ExchangeDetailedActivity.this);
                        appDialog.setMsg(msg);
                        appDialog.show();

                    }
                }
            });
        }
    }
    private String password;
    private String from;
    private String to;
    private String vol;
    private String symbol;
    private String memo;
    private KProgressHUD hud;
    public void transfer(){
        Log.e(TAG, "transfer: " );
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    account_object accountObject = BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,password);
                    if(accountObject!=null){
                        if (BtslandApplication.getWalletApi().unlock(password) == 0) {
                            try {
                                if (BtslandApplication.getWalletApi().transfer(from, to, vol, symbol, memo) != null) {
                                    tranHandler.sendEmptyMessage(1);
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.ACCOUNT_CONFIRMED, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json = response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                dialogHandler.sendEmptyMessage(-1);
                                                BtslandApplication.sendBroadcastDialog(ExchangeDetailedActivity.this,json);
                                            } else {
                                                int a = Integer.parseInt(json);
                                                dialogHandler.sendEmptyMessage(a);
                                            }
                                        }
                                    });
                                } else {
                                    tranHandler.sendEmptyMessage(-1);
                                }
                            } catch (NetworkStatusException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tranHandler.sendEmptyMessage(-2);
                        }
                    }else {
                        tranHandler.sendEmptyMessage(-2);
                    }
                } catch (NetworkStatusException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"详情");
            transaction.add(R.id.fra_excDet_head,headFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillIn();
        if(type==IN){
            headFragment.setTitleName("充值详情");
        }else if(type==OUT){
            headFragment.setTitleName("提现详情");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("type",type);
        outState.putString("noteNo",noteNo);
        super.onSaveInstanceState(outState);
    }
    private Handler inNoHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text=msg.getData().getString("type");
            tvInNo.setText(text);
        }
    } ;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                fillInBody();
            }
        }
    };

    private Handler typeHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                fillInTypes();
            }
        }
    };

    private void fillInTypes() {
        viewPager.setAdapter(dealerTypesAdaper);
        dealerTypesAdaper.setRealAssets(realAssets);
        final List<View> views=new ArrayList<>();
        LayoutInflater inflater= LayoutInflater.from(ExchangeDetailedActivity.this);
        llTable.removeAllViews();
        for(int i=0;i<realAssets.size();i++){
            View view = inflater.inflate(R.layout.textview_table,null);
            if(i==0){
                view.findViewById(R.id.tv_table).setBackground(getDrawable(R.drawable.textview_table_touch));
            }
            views.add(view);
            llTable.addView(view);
        }
        dealerTypesAdaper.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<views.size();i++){
                    if(i==position){
                        views.get(i).findViewById(R.id.tv_table).setBackground(getDrawable(R.drawable.textview_table_touch));
                    }else {
                        views.get(i).findViewById(R.id.tv_table).setBackground(getDrawable(R.drawable.textview_table));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Handler dialogHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud!=null&&hud.isShowing()){
                hud.dismiss();
                hud=null;
            }
            fillIn();
            AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this);
            if(msg.what>0){
                appDialog.setMsg("更新订单成功。");
            }else {
                appDialog.setMsg("更新订单失败。");
            }
            appDialog.show();
        }
    };

    private Handler tranHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this,"提示","转账成功！");
                appDialog.show();
            }else if(msg.what==-1) {
                AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this,"提示","转账失败！");
                appDialog.show();
            }else if(msg.what==-2) {
                AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this,"提示","密码错误！");
                appDialog.show();
            }
        }
    };
}
