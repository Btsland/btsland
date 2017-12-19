package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.spongycastle.jcajce.provider.symmetric.TEA;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import info.btsland.app.Adapter.ExchangeNoteAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExchangeListFragment extends Fragment {
    private int type=1;

    public static final int IN=1;//进（充值）
    public static final int OUT=2;//出（提现）

    private TextView tvHavingHint;
    private TextView tvClinchHint;
    private LinearLayout llHaving;
    private LinearLayout llClinch;
    private ListView lvHaving;
    private ListView lvClinch;

    private List<Note> havingNoteList;
    private List<Note> clinchNoteList;

    private ExchangeNoteAdapter havingAdapter;
    private ExchangeNoteAdapter clinchAdapter;

    public ExchangeListFragment() {
    }

    public static ExchangeListFragment newInstance(int type) {
        ExchangeListFragment fragment = new ExchangeListFragment();
        Bundle args = new Bundle();
        args.putInt("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.type=getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_list, container, false);
        init(view);
        fillIn();
        return view;
    }


    private void init(View view) {
        tvClinchHint=view.findViewById(R.id.tv_excNote_clinch_hint);
        tvHavingHint=view.findViewById(R.id.tv_excNote_having_hint);
        llClinch=view.findViewById(R.id.ll_excNote_clinch);
        llHaving=view.findViewById(R.id.ll_excNote_having);
        lvHaving=view.findViewById(R.id.lv_excNote_having);
        lvClinch=view.findViewById(R.id.lv_excNote_clinch);
        havingAdapter=new ExchangeNoteAdapter(getActivity());
        clinchAdapter=new ExchangeNoteAdapter(getActivity());
        lvClinch.setAdapter(clinchAdapter);
        lvHaving.setAdapter(havingAdapter);
    }
    private void fillIn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(BtslandApplication.accountObject!=null){
                    String coin="";
                    if(type==IN){
                        coin="CNY";
                    }else if (type==OUT) {
                        coin="RMB";
                    }
                    NoteHttp.queryAllClinchNoteByAccount(BtslandApplication.accountObject.name, coin, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            fillInClinchList(json);
                            handler.sendEmptyMessage(1);
                        }
                    });
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(BtslandApplication.accountObject!=null){
                    String coin="";
                    if(type==IN){
                        coin="CNY";
                    }else if (type==OUT) {
                        coin="RMB";
                    }
                    NoteHttp.queryAllHavingNoteByAccount(BtslandApplication.accountObject.name, coin, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            fillInHavingList(json);
                            handler.sendEmptyMessage(2);
                        }
                    });
                }
            }
        }).start();
    }

    private void fillInClinchList(String json) {
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson = gsonBuilder.create();
        clinchNoteList=gson.fromJson(json,new TypeToken<List<Note>>() {}.getType());
    }

    private void fillInHavingList(String json){
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson = gsonBuilder.create();
        havingNoteList=gson.fromJson(json,new TypeToken<List<Note>>() {}.getType());
    }
    private void refurbishClinch(){
        if(clinchNoteList!=null&&clinchNoteList.size()>0){
            clinchAdapter.setNotes(clinchNoteList);
            clinchAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvClinch);
            llClinch.setVisibility(View.VISIBLE);
            lvClinch.setVisibility(View.VISIBLE);
            tvClinchHint.setVisibility(View.VISIBLE);
        }else {
            llClinch.setVisibility(View.GONE);
            lvClinch.setVisibility(View.GONE);
            tvClinchHint.setVisibility(View.GONE);
        }

    }
    private void refurbishHaving(){
        if(havingNoteList!=null&&havingNoteList.size()>0){
            havingAdapter.setNotes(havingNoteList);
            havingAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvHaving );
            llHaving.setVisibility(View.VISIBLE);
            lvHaving.setVisibility(View.VISIBLE);
            tvHavingHint.setVisibility(View.VISIBLE);
        }else {
            llHaving.setVisibility(View.GONE);
            lvHaving.setVisibility(View.GONE);
            tvHavingHint.setVisibility(View.GONE);
        }

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
            if(msg.what==1){
                refurbishClinch();
            }else if(msg.what==2){
                refurbishHaving();
            }
        }
    };
}
