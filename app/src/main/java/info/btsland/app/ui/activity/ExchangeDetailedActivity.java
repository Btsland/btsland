package info.btsland.app.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.http.NoteHttp;
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
    private TextView tvRealNo;
    private TextView tvRealType;
    private TextView tvStartTime;
    private TextView tvAccountReTime;
    private TextView tvEndTime;
    private TextView tvDepict;
    private TextView tvStat;

    private TextView tvNumTab;
    private TextView tvRemarkCodeTab;
    private TextView tvRealNoTab;

    private TextView tvBtn;
    private TextView tvCancel;

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
        tvRealNo=findViewById(R.id.tv_excDet_realNo);
        tvRealType=findViewById(R.id.tv_excDet_realType);
        tvStartTime=findViewById(R.id.tv_excDet_startTime);
        tvAccountReTime=findViewById(R.id.tv_excDet_accountReTime);
        tvEndTime=findViewById(R.id.tv_excDet_endTime);
        tvDepict=findViewById(R.id.tv_excDet_depict);
        tvStat=findViewById(R.id.tv_excDet_stat);
        tvNumTab=findViewById(R.id.tv_excDet_num_tab);
        tvRealNoTab=findViewById(R.id.tv_excDet_realNo_tab);
        tvRemarkCodeTab=findViewById(R.id.tv_excDet_remarkCode_tab);
        tvBtn=findViewById(R.id.tv_excDet_btn);
        tvCancel=findViewById(R.id.tv_excDet_cancel);
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
                        fillNote(json);
                        handler.sendEmptyMessage(1);

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
    }

    private void fillInBody(){
        if(note!=null){
            tvDealerId.setText(note.getDealerId());
            tvName.setText(note.getDealerName());
            tvPhone.setText(note.getDealerPhone());
            tvBrokerage.setText(""+note.getBrokerage()*100+"%");
            tvRealNo.setText(note.getRealNo());
            tvRealType.setText(note.getRealType());
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd/HH:mm");
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
            if(NoteStatCode.getTabAccount(note.getStatNo()).equals(NoteStatCode.TRANSFER)){
                tvBtn.setVisibility(View.VISIBLE);
                tvBtn.setText("确认已转账");
                tvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TradeHttp.updateNoteStat(noteNo, NoteStatCode.ACCOUNT_CONFIRMED, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                int a= Integer.parseInt(response.body().string());
                                dialogHandler.sendEmptyMessage(a);
                            }
                        });
                    }
                });
            }
            if(NoteStatCode.getTabAccount(note.getStatNo()).equals(NoteStatCode.TRANSFER)||NoteStatCode.getTabAccount(note.getStatNo()).equals(NoteStatCode.TRADING)){
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
                                        int a= Integer.parseInt(response.body().string());
                                        dialogHandler.sendEmptyMessage(a);
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
            tvDepict.setText(note.getDepict());
            tvNum.setText(note.getAssetNum()+note.getAssetCoin());
            tvRemarkCode.setText(note.getRemarkCode());
            if(type==IN){
                tvNumTab.setText("充值额度");
                tvRealNoTab.setText("付款帐号");
                tvRemarkCodeTab.setText("充值码");
            }else if(type==OUT){
                tvNumTab.setText("提现额度");
                tvRealNoTab.setText("收款帐号");
                tvRemarkCodeTab.setText("提现码");
            }
        }
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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                fillInBody();
            }
        }
    };

    private Handler dialogHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            fillIn();
            AppDialog appDialog=new AppDialog(ExchangeDetailedActivity.this);
            if(msg.what>0){
                appDialog.setMsg("更新订单成功。");
            }else {
                appDialog.setMsg("更新订单失败。");
            }
            appDialog.show();
            appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                @Override
                public void onConfirm() {
                    fillIn();
                }

                @Override
                public void onReject() {
                    fillIn();
                }
            });
        }
    };
}
