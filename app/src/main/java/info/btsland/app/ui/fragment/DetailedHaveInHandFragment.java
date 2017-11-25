package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import info.btsland.app.Adapter.HaveInHandRecyclerViewAdapter;
import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.object_id;
import info.btsland.app.api.operation_history_object;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.OpenOrder;
import info.btsland.app.ui.view.AppDialog;


public class DetailedHaveInHandFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
    private static String MARKET="market";
    private KProgressHUD hud;

    private RecyclerView rlv;

    private HaveInHandRecyclerViewAdapter adapter;
    private List<OpenOrder> orders;
    public DetailedHaveInHandFragment() {
        // Required empty public constructor
    }

    public static DetailedHaveInHandFragment newInstance(MarketTicker market) {
        DetailedHaveInHandFragment fragment = new DetailedHaveInHandFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        hud=KProgressHUD.create(this.getActivity());
        hud.setLabel(getResources().getString(R.string.please_wait));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detailed_have_in_hand, container, false);
        init(view);
        fillIn();
        return view;
    }

    private void fillIn() {
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HaveInHandRecyclerViewAdapter(getActivity(),handler2);
        rlv.setAdapter(adapter);
        rlv.setItemAnimator(null);
    }

    private void init(View view) {
        rlv=view.findViewById(R.id.rlv_detailed_haveInHand);
    }

    private void refurbish(){
        BtslandApplication.getMarketStat().getFullAccounts(MarketStat.STAT_MARKET_OPEN_ORDER,MarketStat.DEFAULT_UPDATE_SECS,this);
    }
    @Override
    public void onStart() {
        super.onStart();
        refurbish();
    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if(stat.openOrders!=null&&stat.openOrders.size()>0){
            orders=stat.openOrders;
            handler.sendEmptyMessage(1);
        }else {
            handler.sendEmptyMessage(-1);
        }

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
            if(!isAdded()){
                return;
            }
            if(msg.what==1){
                adapter.setList(orders);
                adapter.notifyDataSetChanged();
            }else {
                AppDialog appDialog=new AppDialog(getActivity(),"提示","数据更新失败！");
                appDialog.show();
            }
        }
    };

    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(hud.isShowing()){
                hud.dismiss();
            }
            if (msg.what==1){
                AppDialog appDialog=new AppDialog(getActivity(),"提示","取消成功！");
                appDialog.show();
                hud.show();
                refurbish();
            }else if(msg.what==-1){
                AppDialog appDialog=new AppDialog(getActivity(),"提示","取消失败！");
                appDialog.show();
            }
        }
    };
}
