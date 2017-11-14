package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.activity.MarketDetailedActivity;

public class DetailedBuyAndSellFragment extends Fragment implements MarketStat.OnMarketStatUpdateListener {
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
        setText();
    }
    private void setText(){
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
        tvTotalCoin.setText(market.base);
        tvChageCoin.setText("BTS");
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
        } else {
            highBuyPrice = -1;
        }
        if (stat.orderBook.asks != null && !stat.orderBook.asks.isEmpty()) {
            rlvSellAdapter.setList(stat.orderBook.asks.subList(0, 15));
            lowSellPrice = stat.orderBook.asks.get(0).price;
        } else {
            lowSellPrice = -1;
        }
    }
}
