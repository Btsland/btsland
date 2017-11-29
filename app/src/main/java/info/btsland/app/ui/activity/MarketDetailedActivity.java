package info.btsland.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.MarketStat;
import info.btsland.app.exception.NetworkStatusException;
import info.btsland.app.model.MarketTicker;
import info.btsland.app.ui.fragment.DetailedBuyAndSellFragment;
import info.btsland.app.ui.fragment.DetailedHaveInHandFragment;
import info.btsland.app.ui.fragment.DetailedKFragment;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppListDialog;
import info.btsland.app.ui.view.IViewPager;
import info.btsland.app.util.KeyUtil;

public class MarketDetailedActivity extends AppCompatActivity{
    private static String TAG="MarketDetailedActivity";
    private HeadFragment headFragment;
    public static MarketTicker market=new MarketTicker("CNY","BTS");

    public static String title;
    private int index = 1 ;
    public static String dataKey;
    public static String orderKey;

    private List<MarketTicker> tickers;

    public static RefurbishK refurbishK;

    public static RefurbishBuyAndSell refurbishBuyAndSell;


    public static void startAction(Context context, MarketTicker market,int index){
        Intent intent = new Intent(context, MarketDetailedActivity.class);
        if(market!=null){
            intent.putExtra("MarketTicker", market);
        }
        intent.putExtra("index", index);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detailed);
        if(getIntent()!=null) {
            if (getIntent().getSerializableExtra("MarketTicker") != null) {
                this.market = (MarketTicker) getIntent().getSerializableExtra("MarketTicker");
            }
            this.title = market.quote + ":" + market.base;
            this.index = getIntent().getIntExtra("index", index);
        }else {
            if(savedInstanceState!=null){
                this.index = savedInstanceState.getInt("index", 1);
            }

        }
        dataKey= KeyUtil.constructingDateKKey(market.base,market.quote,DetailedKFragment.range,DetailedKFragment.ago);
        orderKey=KeyUtil.constructingOrderBooksKey(market.base,market.quote);
        this.tickers=new ArrayList<>();
        for(int i=0;i<BtslandApplication.bases.length;i++){
            for(int j=0;j<BtslandApplication.quotes2.length;j++){
                String base=BtslandApplication.bases[i];
                String quote=BtslandApplication.quotes2[j];
                if(!base.equals(quote)){
                    MarketTicker ticker=new MarketTicker(base,quote);
                    this.tickers.add(ticker);
                }
            }
        }
        fillInHead();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index",index);
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化
     */
    private void init(){

        IViewPager viewPager= (IViewPager) findViewById(R.id.vp_detailed_page);
        viewPager.setOffscreenPageLimit(2);
        String[] titles={getString(R.string.str_detailed_information),getString(R.string.str_buy_sell),getString(R.string.have_in_hand)};
        List<Fragment> fragments=new ArrayList<Fragment>();
        DetailedKFragment detailedKFragment=DetailedKFragment.newInstance(market);
        DetailedBuyAndSellFragment detailedBuyAndSellFragment=DetailedBuyAndSellFragment.newInstance(market);
        DetailedHaveInHandFragment detailedHaveInHandFragment=DetailedHaveInHandFragment.newInstance(market);
        fragments.add(detailedKFragment);
        fragments.add(detailedBuyAndSellFragment);
        fragments.add(detailedHaveInHandFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_detailed_title);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new OnPage());
    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=HeadFragment.newInstance(HeadFragment.HeadType.BACK_SELECT_NULL,"");
            transaction.add(R.id.fra_detailed_head,headFragment);
        }
        transaction.commit();
        headFragment.setTitleName(title);
        headFragment.setSelectListener(new HeadFragment.OnSelectOnClickListener() {
            @Override
            public void onClick(View view) {
                AppListDialog listDialog=new AppListDialog(MarketDetailedActivity.this,tickers);
                listDialog.setListener(new AppListDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(final MarketTicker market) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MarketTicker newMarket=null;
                                try {
                                    newMarket=BtslandApplication.getMarketStat().mWebsocketApi.get_ticker(market.base,market.quote);
                                } catch (NetworkStatusException e) {
                                    e.printStackTrace();
                                }
                                BtslandApplication.getMarketStat().unsubscribe(MarketDetailedActivity.market.base,MarketDetailedActivity.market.quote,MarketStat.STAT_MARKET_OPEN_ORDER);
                                MarketDetailedActivity.market=newMarket;//设置当前的交易对
                                handler.sendEmptyMessage(1);//通知主线程设置标题
                                //设置取K图数据的键
                                dataKey=KeyUtil.constructingDateKKey(MarketDetailedActivity.market.base,MarketDetailedActivity.market.quote,
                                        DetailedKFragment.range,DetailedKFragment.ago);
                                //设置取广播数据的键
                                orderKey=KeyUtil.constructingOrderBooksKey(MarketDetailedActivity.market.base,MarketDetailedActivity.market.quote);
                                if(refurbishK!=null){
                                    refurbishK.refurbish();
                                }
                                if(refurbishBuyAndSell!=null){
                                    refurbishBuyAndSell.refurbish();
                                }
                            }
                        }).start();


                    }

                    @Override
                    public void onConfirm(String market) {

                    }

                    @Override
                    public void onReject() {

                    }
                });
                listDialog.setTitle("请选择交易对");
                listDialog.show();

            }
        });

    }


    class OnPage implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(TAG, "onPageScrolled: "+position);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: "+position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i(TAG, "onPageScrollStateChanged: "+state);

        }
    }
    public interface RefurbishK{
        void refurbish();
    }
    public interface RefurbishBuyAndSell{
        void refurbish();
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                title=MarketDetailedActivity.market.quote+":"+MarketDetailedActivity.market.base;
                headFragment.setTitleName(title);
            }
        }
    };
}
