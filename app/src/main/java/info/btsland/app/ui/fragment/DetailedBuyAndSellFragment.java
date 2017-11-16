package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.view.ConfirmOrderDialog;
import info.btsland.app.ui.view.PasswordDialog;

public class DetailedBuyAndSellFragment extends Fragment
        implements MarketStat.OnMarketStatUpdateListener {
    private static final String MARKET = "market";
    public static String TAG = "DetailedBuyAndSellFragment";

    private RecyclerView rlvBuy;//买入列表
    private RecyclerView rlvSell;//卖出列表
    private TextView tvNewPrice;//最新价格
    private TextView tvNewPriceCoin;
    private EditText edPrice;//输入价格
    private TextView tvPriceCoin;//输入价格的货币
    private EditText edVol;//输入量
    private TextView tvVolCoin;//输入量的货币

    private TextView tvTotalNum;//总金额数值
    private TextView tvTotalCoin;//总金额货币

    private TextView tvChargeNum;//手续费数组
    private TextView tvChageCoin;//手续费货币

    private TextView tvBuy;
    private TextView tvSell;

    private Double total;

    private double lowSellPrice = -1;
    private double highBuyPrice = -1;

    private static DetailedBuyAndSellFragment listener;

    TransactionSellBuyRecyclerViewAdapter rlvBuyAdapter;
    TransactionSellBuyRecyclerViewAdapter rlvSellAdapter;


    public DetailedBuyAndSellFragment() {
        // Required empty public constructor
    }

    public static DetailedBuyAndSellFragment newInstance(MarketTicker market) {
        DetailedBuyAndSellFragment fragment = new DetailedBuyAndSellFragment();
        Bundle args = new Bundle();
        args.putSerializable(MARKET, market);
        fragment.setArguments(args);
        return fragment;
    }
    public static DetailedBuyAndSellFragment getListener() {
        return listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detailed_buy_and_sell, container, false);
        init(view);
        listener=this;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fillIn();
        startReceiveMarkets();
    }

    private void init(View view){
        rlvBuy=view.findViewById(R.id.rlv_detailed_buy);
        rlvSell=view.findViewById(R.id.rlv_detailed_sell);
        tvNewPrice=view.findViewById(R.id.tv_detailed_newPrice);
        tvNewPriceCoin=view.findViewById(R.id.tv_detailed_newPrice_coin);
        edPrice=view.findViewById(R.id.et_priceNum);
        edVol=view.findViewById(R.id.et_volNum);
        tvPriceCoin=view.findViewById(R.id.tv_priceCoin);
        tvVolCoin=view.findViewById(R.id.tv_volCoin);
        tvTotalNum=view.findViewById(R.id.tv_total_num);
        tvChargeNum=view.findViewById(R.id.tv_charge_num);
        tvTotalCoin=view.findViewById(R.id.tv_total_coin);
        tvChageCoin=view.findViewById(R.id.tv_charge_coin);

        tvBuy=view.findViewById(R.id.tv_detailed_buy);
        tvSell=view.findViewById(R.id.tv_detailed_sell);

        rlvBuy.setLayoutManager(new LinearLayoutManager(getContext()));
        rlvBuyAdapter = new TransactionSellBuyRecyclerViewAdapter();
        rlvBuy.setAdapter(rlvBuyAdapter);
        rlvBuy.setItemAnimator(null);

        rlvSell.setLayoutManager(new LinearLayoutManager(getContext()));
        rlvSellAdapter = new TransactionSellBuyRecyclerViewAdapter();
        rlvSell.setAdapter(rlvSellAdapter);
        rlvSell.setItemAnimator(null);

        tvChargeNum.setText("0.00");
        tvTotalNum.setText("0.00");
    }
    private void fillIn(){
        MarketTicker market=MarketDetailedActivity.market;
        tvNewPrice.setText(market.latest);
        if (MarketDetailedActivity.market.percent_change > 0) {
            tvNewPrice.setTextColor(getActivity().getResources().getColor(R.color.color_green));
            tvNewPrice.setCompoundDrawables(null,null,getActivity().getDrawable(R.drawable.ic_up),null);
        } else if(market.percent_change < 0) {
            tvNewPrice.setCompoundDrawables(null,null,getActivity().getDrawable(R.drawable.ic_down),null);
            tvNewPrice.setTextColor(getActivity().getResources().getColor(R.color.color_font_red));
        }else {
            tvNewPrice.setTextColor(getActivity().getResources().getColor(R.color.color_font_blue));
        }
        tvNewPriceCoin.setText(market.base+"/"+market.quote);
        tvPriceCoin.setText(market.base);
        tvVolCoin.setText(market.quote);
        edPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double price= 0.0;
                Double vol=0.0;
                String strPrice = editable.toString();
                if(strPrice!=null&&strPrice.length()!=0){
                    price= Double.valueOf(strPrice);
                }
                String strVol = edVol.getText().toString();
                if(strVol!=null&&strVol.length()!=0){
                    vol= Double.valueOf(edVol.getText().toString());
                }
                total=price*vol;
                tvTotalNum.setText(String.valueOf(total));
            }
        });
        edVol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double vol= 0.0;
                Double price=0.0;
                String strVol = editable.toString();
                if(strVol!=null&&strVol.length()!=0){
                    vol= Double.valueOf(strVol);
                }
                String strPrice = edPrice.getText().toString();
                if(strPrice!=null&&strPrice.length()!=0){
                    price= Double.valueOf(edPrice.getText().toString());
                }
                total=price*vol;
                tvTotalNum.setText(String.valueOf(total));
            }
        });
        tvTotalCoin.setText(market.base);
        tvChageCoin.setText("BTS");
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderDialog dialog=new ConfirmOrderDialog(getActivity(),
                        new ConfirmOrderDialog.ConfirmOrderData(
                                "买入",
                                edPrice.getText().toString(),
                                tvPriceCoin.getText().toString(),
                                tvTotalNum.getText().toString(),
                                edVol.getText().toString(),
                                tvChargeNum.getText().toString(),
                                tvVolCoin.getText().toString(),
                                tvTotalCoin.getText().toString(),
                                tvChageCoin.getText().toString()
                        ),
                        new DialogListener());
                dialog.show();
            }
        });
        tvSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderDialog dialog=new ConfirmOrderDialog(getActivity(),
                        new ConfirmOrderDialog.ConfirmOrderData(
                                "卖出",
                                edPrice.getText().toString(),
                                tvPriceCoin.getText().toString(),
                                tvTotalNum.getText().toString(),
                                edVol.getText().toString(),
                                tvChargeNum.getText().toString(),
                                tvVolCoin.getText().toString(),
                                tvTotalCoin.getText().toString(),
                                tvChageCoin.getText().toString()
                        ),
                        new DialogListener());
                dialog.show();
            }
        });
    }

    /**
     * 启动查询数据线程
     */
    public void startReceiveMarkets() {

        Log.i(TAG, "startReceiveMarkets: ");
        BtslandApplication.getMarketStat().subscribe(
                MarketDetailedActivity.market.base,
                MarketDetailedActivity.market.quote,
                MarketStat.STAT_MARKET_ORDER_BOOK,
                MarketStat.DEFAULT_UPDATE_SECS,
                getListener());
    }
    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if (getView() == null || stat.orderBook == null) {
            return;
        }
        if (stat.orderBook.bids != null && !stat.orderBook.bids.isEmpty()) {
            rlvBuyAdapter.setList(stat.orderBook.bids.subList(0, 15));
            highBuyPrice = stat.orderBook.bids.get(0).price;
            handler.sendEmptyMessage(1);
        } else {
            highBuyPrice = -1;
        }
        if (stat.orderBook.asks != null && !stat.orderBook.asks.isEmpty()) {
            rlvSellAdapter.setList(stat.orderBook.asks.subList(0, 15));
            lowSellPrice = stat.orderBook.asks.get(0).price;
            handler.sendEmptyMessage(2);
        } else {
            lowSellPrice = -1;
            handler.sendEmptyMessage(-1);
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                rlvBuyAdapter.notifyDataSetChanged();
            }else if(msg.what==2){
                rlvSellAdapter.notifyDataSetChanged();
            }else if(msg.what==-1){

            }
        }
    };

    class DialogListener implements ConfirmOrderDialog.OnDialogInterationListener {

        @Override
        public void onConfirm() {
            String strPrice = edPrice.getText().toString();
            String strVol = edVol.getText().toString();
            if (strPrice.equals("") || strVol.equals("")) {
                return;
            }

            final double price;
            final double vol;
            try {
                price = Double.parseDouble(strPrice);
                vol = Double.parseDouble(strVol);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (price <= 0 || vol <= 0) {
                return;
            }
            PasswordDialog builder = new PasswordDialog(getActivity());
            builder.setListener(new PasswordDialog.OnDialogInterationListener() {
                @Override
                public void onConfirm(AlertDialog dialog, String passwordString) {
                    dialog.dismiss();
                }

                @Override
                public void onReject(AlertDialog dialog) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        @Override
        public void onReject() {
        }
    }

 }
