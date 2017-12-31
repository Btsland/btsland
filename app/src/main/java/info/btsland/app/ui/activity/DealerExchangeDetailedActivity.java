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

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.util.BaseThread;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.http.TradeHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import info.btsland.exchange.utils.NoteStatCode;
import info.btsland.exchange.utils.UserTypeCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DealerExchangeDetailedActivity extends AppCompatActivity {
    private HeadFragment headFragment;

    private TextView tvAccount;
    private TextView tvRealNo;
    private TextView tvRealType;
    private TextView tvNum;
    private TextView tvNum2;
    private TextView tvCoin;
    private TextView tvCoin2;
    private TextView tvBrokerage;
    private TextView tvCode;
    private TextView tvCodeCopy;
    private TextView tvStat;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvAccountTime;
    private TextView tvRemark;
    private TextView tvRelationAccount;
    private TextView tvRelationHelp;
    private TextView tvConfirm;

    private String noteNo;
    private Note note;
    private BaseThread queryNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_exchange_detailed);
        if(getIntent()!=null){
            noteNo=getIntent().getStringExtra("noteNo");
        }
        init();
        fillInHead();
        queryNote=new QueryNote();
        queryNote.start();
    }

    private void init() {
        tvAccount=findViewById(R.id.tv_dealer_excDet_item_account);
        tvRealNo=findViewById(R.id.tv_dealer_excDet_item_realNo);
        tvRealType=findViewById(R.id.tv_dealer_excDet_item_realType);
        tvNum=findViewById(R.id.tv_dealer_excDet_item_num);
        tvNum2=findViewById(R.id.tv_dealer_excDet_item_num2);
        tvCoin=findViewById(R.id.tv_dealer_excDet_item_coin);
        tvCoin2=findViewById(R.id.tv_dealer_excDet_item_coin2);
        tvCode=findViewById(R.id.tv_dealer_excDet_item_code);
        tvCodeCopy=findViewById(R.id.tv_dealer_excDet_item_codeCopy);
        tvBrokerage=findViewById(R.id.tv_dealer_excDet_item_brokerage);
        tvStat=findViewById(R.id.tv_dealer_excDet_item_stat);
        tvStartTime=findViewById(R.id.tv_dealer_excDet_item_startTime);
        tvAccountTime=findViewById(R.id.tv_dealer_excDet_item_accountTime);
        tvEndTime=findViewById(R.id.tv_dealer_excDet_item_endTime);
        tvRemark=findViewById(R.id.tv_dealer_excDet_item_remark);
        tvRelationAccount=findViewById(R.id.tv_dealer_excDet_item_relationAccount);
        tvRelationHelp=findViewById(R.id.tv_dealer_excDet_item_relationHelp);
        tvConfirm=findViewById(R.id.tv_dealer_excDet_item_confirm);
    }

    private void fillInHead(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"详情");
            transaction.add(R.id.fra_dealer_excDet_head,headFragment);
        }
        transaction.commit();
    }
    private void fillInNote(String json){
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson = gsonBuilder.create();
        note = gson.fromJson(json,Note.class);
        if(note!=null){
            handler.sendEmptyMessage(1);
        }
    }
    private void fillIn(){
        if(note!=null){
            tvAccount.setText(note.getAccount());
            tvRealNo.setText(note.getRealNo());
            int a=note.getRealDepict().indexOf("(");
            String depict;
            if(a!=-1){
                depict=note.getRealDepict().substring(0,a);
            }else {
                depict=note.getRealDepict();
            }
            tvRealType.setText(depict);
            tvNum.setText(""+note.getAssetNum());
            tvCoin.setText(note.getAssetCoin());
            tvNum2.setText(""+(note.getAssetNum()*(1-note.getBrokerage())));
            tvCoin2.setText(note.getAssetCoin());
            tvBrokerage.setText(note.getBrokerage()*100+"%");
            tvStat.setText(NoteStatCode.getTabDealer(note.getStatNo()));
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");


            if(note.getStartTime()!=null){
                tvStartTime.setText(format.format(note.getStartTime()));
            }
            if(note.getAccountReTime()!=null){
                tvAccountTime.setText(format.format(note.getAccountReTime()));
            }
            if(note.getEndTime()!=null){
                tvEndTime.setText(format.format(note.getEndTime()));
            }
            tvRemark.setText(note.getDepict());
            tvCode.setText(note.getRemarkCode());
            if(BtslandApplication.dealer.getType()!=UserTypeCode.ACCOUNT){
                tvCodeCopy.setVisibility(View.GONE);
            }else {
                tvCodeCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            tvRelationAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            switch (note.getStatNo()){
                case NoteStatCode.ACCOUNT_CONFIRMED:
                    switch (BtslandApplication.dealer.getType()){
                        case UserTypeCode.DEALER:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("确认已收款");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.DEALER_TRANSFERRING, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a= Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系客服");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                        case UserTypeCode.HELP:
                            tvConfirm.setVisibility(View.GONE);
                            tvRelationHelp.setText("联系承兑商");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                        case UserTypeCode.ADMIN:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("承兑商确认已收款");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.ADMIN_TRANSFERRING, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a = Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系承兑商");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                    }
                    break;
                case NoteStatCode.DEALER_CONFIRMING:
                    switch (BtslandApplication.dealer.getType()){
                        case UserTypeCode.DEALER:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("确认已收款");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.DEALER_TRANSFERRING, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a= Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系客服");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                        case UserTypeCode.HELP:
                            tvConfirm.setVisibility(View.GONE);
                            break;
                        case UserTypeCode.ADMIN:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("承兑商确认已收款");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.ADMIN_TRANSFERRING, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a= Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系承兑商");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                    }

                    break;
                case NoteStatCode.DEALER_TRANSFERRING:
                    switch (BtslandApplication.dealer.getType()){
                        case UserTypeCode.DEALER:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("确认已提议");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.HELP_CONFIRMING, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a= Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系客服");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                        case UserTypeCode.HELP:
                            tvConfirm.setVisibility(View.GONE);
                            break;
                        case UserTypeCode.ADMIN:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("管理员确认提议");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.ADMIN_CONFIRMED, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a = Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系承兑商");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                    }
                    break;
                case NoteStatCode.HELP_CONFIRMING:
                    switch (BtslandApplication.dealer.getType()){
                        case UserTypeCode.DEALER:
                            tvConfirm.setVisibility(View.GONE);
                            break;
                        case UserTypeCode.HELP:
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvConfirm.setText("确认同意提议");
                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TradeHttp.updateNoteStat(noteNo, NoteStatCode.HELP_CONFIRMED, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {}
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String json= response.body().string();
                                            if (json.indexOf("error") != -1) {
                                                BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                                            } else {
                                                int a= Integer.parseInt(json);
                                                if (a > 0) {
                                                    handler.sendEmptyMessage(10);
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            tvRelationHelp.setText("联系承兑商");
                            tvRelationHelp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            break;
                        case UserTypeCode.ADMIN:
                            tvConfirm.setVisibility(View.GONE);
                            break;
                    }
                    break;
                default:
                    tvConfirm.setVisibility(View.GONE);
                    break;
            }


        }

    }
    private void refurbish(){
        fillIn();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            refurbish();
            if(msg.what==10){
                AppDialog appDialog=new AppDialog(DealerExchangeDetailedActivity.this);
                appDialog.setMsg("订单更新成功！");
                appDialog.show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(queryNote!=null){
            queryNote.kill();
        }
    }

    class QueryNote extends BaseThread{
        @Override
        public void execute() {
            NoteHttp.queryNote(noteNo, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    if (json.indexOf("error") != -1) {
                        BtslandApplication.sendBroadcastDialog(DealerExchangeDetailedActivity.this,json);
                    } else {
                        fillInNote(json);
                    }
                }
            });
        }
    }

}
