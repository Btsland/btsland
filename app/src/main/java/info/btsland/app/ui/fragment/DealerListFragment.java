package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DealerListAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.C2CExchangeActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.User;

public class DealerListFragment extends Fragment {
    private static String TAG="DealerListFragment";

    private int type=1;

    public static final int IN=1;//进（充值）
    public static final int OUT=2;//出（提现）
    private ListView listView;
    private Spinner spOrder;
    private DealerListAdapter dealerListAdapter;
    public static List<DealerListAdapter.DealerData> dataList=new ArrayList<>();
    private AllDealersReceiver allDealersReceiver;
    private int inOrder=0;
    private int outOrder=0;

    public DealerListFragment() {
    }
    public static DealerListFragment newInstance(int type) {
        DealerListFragment fragment = new DealerListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type=getArguments().getInt("type");
        IntentFilter intentFilter=new IntentFilter(AllDealersReceiver.EVENT);
        allDealersReceiver=new AllDealersReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(allDealersReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dealer_list, container, false);
        init(view);
        fillIn();
        fillInUser();
        return view;
    }

    private void init(View view){
        listView=view.findViewById(R.id.lv_c2c_list);
        spOrder=view.findViewById(R.id.sp_c2c_list_order);
        List<String> list =new ArrayList<>();
        list.add("成交总额排序");
        list.add("成交总单数排序");
        list.add("手续费排序");
        list.add("完成时间排序");
        list.add("等级排序");
        list.add("承兑下限排序");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.coin_item,R.id.tv_transfer_coinName,list);
        spOrder.setAdapter(adapter);
        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(type==IN){
                    inOrder=i;
                }else if(type==OUT) {
                    outOrder=i;
                }
                BtslandApplication.orderDealer(type,i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fillIn() {
        dealerListAdapter=new DealerListAdapter(getActivity());
        dealerListAdapter.setType(type);
        listView.setAdapter(dealerListAdapter);
        dealerListAdapter.setClickListener(
                new DealerListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DealerListAdapter.DealerData dealerData) {
                        toExchange(dealerData);
                    }
                }
        );

    }
    private void toExchange(DealerListAdapter.DealerData dealerData){
        if(BtslandApplication.accountObject==null){
            AppDialog appDialog=new AppDialog(getActivity());
            appDialog.setMsg("您未登录，请先登录！");
            appDialog.show();
        }else {
            if(type==IN){
                C2CExchangeActivity.startAction(getActivity(), BtslandApplication.accountObject.name,dealerData.usableCNY, type, dealerData.user.getDealerId());
            }else if(type==OUT) {
                C2CExchangeActivity.startAction(getActivity(), BtslandApplication.accountObject.name,dealerData.user.getUpperLimitOut()-dealerData.user.userRecord.getOutHavingCount(), type, dealerData.user.getDealerId());
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(allDealersReceiver);
    }

    private synchronized void fillInUser(){
        if(type==IN) {
            if (BtslandApplication.inDataList != null && BtslandApplication.inDataList.size() > 0) {
                BtslandApplication.orderDealer(type,inOrder);
                dealerListAdapter.setDataList(BtslandApplication.inDataList);
                dealerListAdapter.notifyDataSetChanged();
            }
        }else if(type==OUT){
            if (BtslandApplication.outDataList != null && BtslandApplication.outDataList.size() > 0) {
                BtslandApplication.orderDealer(type,outOrder);
                dealerListAdapter.setDataList(BtslandApplication.outDataList);
                dealerListAdapter.notifyDataSetChanged();
            }
        }
    }
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(AllDealersReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    class AllDealersReceiver extends BroadcastReceiver{
        public static final String EVENT="AllDealersReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " );
            fillInUser();
        }
    }
}
