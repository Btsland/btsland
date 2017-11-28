package info.btsland.app.ui.fragment;

import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.kaopiz.kprogresshud.KProgressHUD;

import info.btsland.app.Adapter.TransactionSellBuyRecyclerViewAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.api.account_object;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.global_property_object;
import info.btsland.app.api.utils;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.model.Order;
import info.btsland.app.model.OrderBook;
import info.btsland.app.ui.activity.LoginActivity;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.ConfirmOrderDialog;
import info.btsland.app.ui.view.PasswordDialog;
import info.btsland.app.util.KeyUtil;
import info.btsland.app.util.NumericUtil;

public class DetailedBuyAndSellFragment extends Fragment
        implements MarketStat.OnMarketStatUpdateListener,MarketDetailedActivity.RefurbishBuyAndSell {
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

    private KProgressHUD hud;
    private PasswordDialog builder;
    private asset_object btsAssetObj;
    private asset_object baseAssetObj;
    private asset_object quoteAssetObj;
    private global_property_object globalPropertyObject;
    private boolean isAssetObjIsInit;

    private double lowSellPrice = -1;
    private double highBuyPrice = -1;

    private  MarketTicker market;

    private ConfirmOrderDialog dialog;

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
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在发布广播。。。")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        MarketDetailedActivity.refurbishBuyAndSell=this;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initFee();
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
        rlvBuyAdapter = new TransactionSellBuyRecyclerViewAdapter(edPrice);
        rlvBuy.setAdapter(rlvBuyAdapter);
        rlvBuy.setItemAnimator(null);

        rlvSell.setLayoutManager(new LinearLayoutManager(getContext()));
        rlvSellAdapter = new TransactionSellBuyRecyclerViewAdapter(edPrice);
        rlvSell.setAdapter(rlvSellAdapter);
        rlvSell.setItemAnimator(null);

        tvChargeNum.setText("0.00");
        tvTotalNum.setText("0.00");
    }
    private void fillIn(){
        market=MarketDetailedActivity.market;
        tvNewPrice.setText(market.latest);
        tvNewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPrice.getEditableText().clear();
                edPrice.getEditableText().insert(0,tvNewPrice.getText());
            }
        });
        if (MarketDetailedActivity.market.percent_change > 0) {
            tvNewPrice.setTextColor(BtslandApplication.goUp);
            tvNewPrice.setCompoundDrawables(null,null,getActivity().getDrawable(R.drawable.ic_up),null);
        } else if(market.percent_change < 0) {
            tvNewPrice.setCompoundDrawables(null,null,getActivity().getDrawable(R.drawable.ic_down),null);
            tvNewPrice.setTextColor(BtslandApplication.goDown);
        }else {
            tvNewPrice.setTextColor(BtslandApplication.suspend);
        }
        tvNewPriceCoin.setText(market.base+"/"+market.quote);

        tvPriceCoin.setText(market.base+"/"+market.quote);
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
                    if(strPrice.substring(0,1).equals(".")){
                        editable.insert(0,"0");
                    }
                    price= NumericUtil.parseDouble(strPrice, 0.0D);
                }
                String strVol = edVol.getText().toString();
                if(strVol!=null&&strVol.length()!=0){
                    vol = NumericUtil.parseDouble(strVol, 0.0D);
                }
                total=price*vol;
                tvTotalNum.setText(String.valueOf(total));
                double fee = calculateBuyFee(quoteAssetObj, baseAssetObj, price, vol);
                tvChargeNum.setText(String.valueOf(fee));
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
                    if(strVol.substring(0,1).equals(".")){
                        editable.insert(0,"0");
                    }
                    vol=  NumericUtil.parseDouble(strVol, 0.0D);
                }
                String strPrice = edPrice.getText().toString();
                if(strPrice!=null&&strPrice.length()!=0){
                    price= NumericUtil.parseDouble(strPrice, 0.0D);
                }
                total=price*vol;
                tvTotalNum.setText(String.valueOf(total));
                double fee = calculateBuyFee(quoteAssetObj, baseAssetObj, price, vol);
                tvChargeNum.setText(String.valueOf(fee));
            }
        });

        tvTotalCoin.setText(market.base);
        tvChageCoin.setText("BTS");
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BtslandApplication.accountObject==null){
                    AppDialog appDialog=new AppDialog(getActivity(),"提示","您没有登录，是否要去登录？");
                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent=new Intent(getActivity(), LoginActivity.class);
                            intent.putExtra("want",LoginActivity.GOLOGIN);
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    appDialog.show();
                }else {
                    dialog = new ConfirmOrderDialog(getActivity(),
                            new ConfirmOrderDialog.ConfirmOrderData(
                                    ConfirmOrderDialog.ConfirmOrderData.BUY,
                                    edPrice.getText().toString(),
                                    market.base,
                                    tvTotalNum.getText().toString(),
                                    edVol.getText().toString(),
                                    tvChargeNum.getText().toString(),
                                    market.quote,
                                    market.base,
                                    tvChageCoin.getText().toString()
                            ),
                            new DialogListener());
                    dialog.show();
                }
            }
        });
        tvSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BtslandApplication.accountObject==null) {
                    AppDialog appDialog = new AppDialog(getActivity(), "提示", "你没有登录，是否要去登录？");
                    appDialog.setListener(new AppDialog.OnDialogInterationListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.putExtra("want", LoginActivity.GOLOGIN);
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
                    appDialog.show();
                }else {
                    dialog = new ConfirmOrderDialog(getActivity(),
                            new ConfirmOrderDialog.ConfirmOrderData(
                                    ConfirmOrderDialog.ConfirmOrderData.SELL,
                                    edPrice.getText().toString(),
                                    market.base,
                                    tvTotalNum.getText().toString(),
                                    edVol.getText().toString(),
                                    tvChargeNum.getText().toString(),
                                    market.quote,
                                    market.base,
                                    tvChageCoin.getText().toString()
                            ),
                            new DialogListener());
                    dialog.show();
                }
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
                MarketStat.DEFAULT_UPDATE_MARKE_SECS,
                getListener());
    }
    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {
        if (getView() == null || stat.orderBook == null) {
            return;
        }
        String orderBookKey = KeyUtil.constructingOrderBooksKey(stat.orderBook.base,stat.orderBook.quote);
        if(BtslandApplication.orderBookMap.get(orderBookKey)!=null){
            BtslandApplication.orderBookMap.remove(orderBookKey);
        }
        BtslandApplication.orderBookMap.put(orderBookKey,stat.orderBook);
        if(orderBookKey.equals(MarketDetailedActivity.orderKey)){
            handler.sendEmptyMessage(1);
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                if(!isAdded()){
                    return;
                }
                if (BtslandApplication.orderBookMap.get(MarketDetailedActivity.orderKey) != null ) {
                    OrderBook orderBook=BtslandApplication.orderBookMap.get(MarketDetailedActivity.orderKey);
                    int maxBids=15;
                    if(orderBook.bids!=null&&orderBook.bids.size()!=0){
                        if(orderBook.bids.size()<15){
                            maxBids=orderBook.bids.size();
                        }

                        rlvBuyAdapter.setList(orderBook.bids.subList(0, maxBids));
                        highBuyPrice = orderBook.bids.get(0).price;
                        rlvBuyAdapter.notifyDataSetChanged();
                    }
                    if(orderBook.bids!=null&&orderBook.bids.size()!=0){
                        int maxAsks=15;
                        if(orderBook.asks.size()<15){
                            maxAsks=orderBook.asks.size();
                        }
                        rlvSellAdapter.setList(orderBook.asks.subList(0, maxAsks));
                        lowSellPrice = orderBook.asks.get(0).price;
                        rlvSellAdapter.notifyDataSetChanged();
                    }
                    fillIn();
                }

            }
        }
    };
    private String strVolCoin;
    private String strPriceCoin;
    private double price;
    private double vol;
    private String mwant;

    @Override
    public void refurbish() {
        startReceiveMarkets();
    }

    class DialogListener implements ConfirmOrderDialog.OnDialogInterationListener {
        @Override
        public void onConfirm(String want) {
            mwant=want;
            String strPrice = edPrice.getText().toString();
            String strVol = edVol.getText().toString();
            strVolCoin = market.quote;
            strPriceCoin= market.base;
            if (strPrice.equals("") || strVol.equals("")) {
                return;
            }
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
            if(!BtslandApplication.isLogin){
                builder = new PasswordDialog(getActivity());
                builder.setListener(new PasswordDialog.OnDialogInterationListener(){
                    @Override
                    public void onConfirm(AlertDialog dialog, String passwordString) {
                        hud=KProgressHUD.create(getActivity());
                        hud.setLabel(getResources().getString(R.string.please_wait));
                        hud.show();
                        try {

                            account_object accountObject= BtslandApplication.getWalletApi().import_account_password(BtslandApplication.accountObject.name,passwordString);
                            if(accountObject!=null){
                                hud.dismiss();
                                dialog.dismiss();
                                goTrading();
                            }else {
                                builder.setTvPoint(false);
                            }
                        } catch (NetworkStatusException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onReject(AlertDialog dialog) {

                    }
                });
                builder.show();
            }else {

                goTrading();
            }

        }

        @Override
        public void onReject() {
        }

        private void goTrading(){
            if (!BtslandApplication.getWalletApi().is_locked()) {
                hud.show();
                if(mwant.equals(ConfirmOrderDialog.ConfirmOrderData.BUY)){
                    buy();
                }else if(mwant.equals(ConfirmOrderDialog.ConfirmOrderData.SELL)){
                    sell();
                }
            } else {
                builder = new PasswordDialog(getActivity());
                builder.setListener(new PasswordDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(AlertDialog dialog, String passwordString) {
                        if (BtslandApplication.getWalletApi().unlock(passwordString) == 0) {
                            dialog.dismiss();
                            hud.show();
                            if(mwant.equals(ConfirmOrderDialog.ConfirmOrderData.BUY)){
                                buy();
                            }else if(mwant.equals(ConfirmOrderDialog.ConfirmOrderData.SELL)){
                                sell();
                            }
                        } else {
                            builder.setTvPoint(false);
                            //Toast.makeText(getContext(), getContext().getString(R.string.password_invalid), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onReject(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }
    private void buy() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BtslandApplication.getWalletApi().buy(strVolCoin,strPriceCoin,price,vol);
                    handler2.sendEmptyMessage(1);
                } catch (Exception e) {
                    handler2.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void sell() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BtslandApplication.getWalletApi().sell(strVolCoin, strPriceCoin,price,vol);
                    handler2.sendEmptyMessage(1);

                } catch (Exception e) {
                    handler2.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private double calculateBuyFee(asset_object symbolToReceive, asset_object symbolToSell, double rate,
                                   double amount) {
        if (!isAssetObjIsInit) {
            return 0;
        }
        asset a = BtslandApplication.getWalletApi().calculate_buy_fee(symbolToReceive, symbolToSell,
                rate, amount, globalPropertyObject);
        if (a.asset_id.equals(btsAssetObj.id)) {
            tvChageCoin.setText(btsAssetObj.symbol);
            return utils.get_asset_amount(a.amount, btsAssetObj);
        } else if (a.asset_id.equals(baseAssetObj.id)) {
            tvChageCoin.setText(baseAssetObj.symbol);
            return utils.get_asset_amount(a.amount, baseAssetObj);
        } else {
            tvChageCoin.setText(quoteAssetObj.symbol);
            return utils.get_asset_amount(a.amount, quoteAssetObj);
        }
    }
    private void initFee() {
        isAssetObjIsInit = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    btsAssetObj = BtslandApplication.getMarketStat().mWebsocketApi.lookup_asset_symbols("BTS");
                    baseAssetObj = BtslandApplication.getMarketStat().mWebsocketApi.lookup_asset_symbols(MarketDetailedActivity.market.base);
                    quoteAssetObj = BtslandApplication.getMarketStat().mWebsocketApi.lookup_asset_symbols(MarketDetailedActivity.market.quote);
                    globalPropertyObject = BtslandApplication.getMarketStat().mWebsocketApi.get_global_properties();
                    isAssetObjIsInit = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public class orderItemOnClickListener implements View.OnClickListener{
        private Order order;

        public orderItemOnClickListener(Order order) {
            this.order = order;
        }

        @Override
        public void onClick(View view) {
            edPrice.getEditableText().clear();
            edPrice.getEditableText().append(String.valueOf(order.price));
            if(edVol.getEditableText().toString()==null){
                edVol.getEditableText().append("1");
            }
        }
    }

    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
        if(hud.isShowing()){
            hud.dismiss();
        }
        if(msg.what==1){
            Toast.makeText(getActivity(), "广播发布成功", Toast.LENGTH_SHORT).show();
        }else if(msg.what==-1){
            Toast.makeText(getActivity(), "广播发布失败", Toast.LENGTH_SHORT).show();
        }
        }
    };

}
