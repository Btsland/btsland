package info.btsland.app.service.Impl;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import info.btsland.app.model.Market;
import info.btsland.app.service.MarketService;

/**
 * author：
 * function：
 * 2017/9/27.
 */

public class MarketServiceImpl implements MarketService {

    @Override
    public Map<String,List<Market>> getallinformation() {

    /*    Market market =new Market();
        market.setLeftCoin("BTS");
        market.setRightCoin("BTC");
        market.setNewPrice("0.00002109");
        market.setBestBid("0.00002105");
        market.setBestAsk("0.00002121");
        market.setVolume("20.734");
        market.setTurnover("1.02M");
        market.setFluctuation("+3.11%");

        List<Market> list=new ArrayList<Market>();
        list.add(market);*/
/**
 * BTS测试数据
 */
        List<Market> listBTC=new ArrayList<Market>();
        listBTC.add(new Market("BTS","BTC",0.00002109f,0.00002105f,0.00002121f,7.808f,-0.88f));
        listBTC.add(new Market("bitCNY","BTC",0.00003593f,0.00001987f,0.00002000f,3.634f,-4.72f));
        listBTC.add(new Market("bitUSD","BTC",0.00024433f,0.00024433f,0.00024804f,2.588f,22.17f));
        listBTC.add(new Market("ETH","BTC",0.7018957f,0.06978142f,0.07018208f,0.147f,-0.48f));
        listBTC.add(new Market("IMIAO","BTC",1f,1f,1f,1f,0f));
        listBTC.add(new Market("BTS","BTC",0.00002109f,0.00002105f,0.00002121f,7.808f,-0.88f));
        listBTC.add(new Market("bitCNY","BTC",0.00003593f,0.00001987f,0.00002000f,3.634f,-4.72f));
        listBTC.add(new Market("bitUSD","BTC",0.00024433f,0.00024433f,0.00024804f,2.588f,22.17f));
        listBTC.add(new Market("ETH","BTC",0.7018957f,0.06978142f,0.07018208f,0.147f,-0.48f));
        listBTC.add(new Market("IMIAO","BTC",1f,1f,1f,1f,0f));
        listBTC.add(new Market("BTS","BTC",0.00002109f,0.00002105f,0.00002121f,7.808f,-0.88f));
        listBTC.add(new Market("bitCNY","BTC",0.00003593f,0.00001987f,0.00002000f,3.634f,-4.72f));
        listBTC.add(new Market("bitUSD","BTC",0.00024433f,0.00024433f,0.00024804f,2.588f,17f));
        listBTC.add(new Market("ETH","BTC",0.7018957f,0.06978142f,0.07018208f,0.147f,-0.48f));
        listBTC.add(new Market("IMIAO","BTC",1f,1f,1f,1f,0f));
        /**
         * bitCNY测试数据
         */
        List<Market> listbitCNY =new ArrayList<Market>();
        listbitCNY.add(new Market("BTC","bitCNY",27490.00171f,26999.99919f,27489.99802f,156.95f,10.27f));
        listbitCNY.add(new Market("BTS","bitCNY",0.576000f,0.573000f,0.576300f,3.44f,13.18f));
        listbitCNY.add(new Market("bitUSD","bitCNY",6.80921f,6.78063f,6.80850f,94.02f,1.76f));
        listbitCNY.add(new Market("ETH","bitCNY",2000.00000f,1998.62926f,2000.00000f,113.84f,7.94f));
        listbitCNY.add(new Market("IMIAO","bitCNY",1,1,1,1,0f));
        listbitCNY.add(new Market("BTC","bitCNY",27490.00171f,26999.99919f,27489.99802f,156.95f,10.27f));
        listbitCNY.add(new Market("BTS","bitCNY",0.576000f,0.573000f,0.576300f,3.44f,13.18f));
        listbitCNY.add(new Market("bitUSD","bitCNY",6.80921f,6.78063f,6.80850f,94.02f,1.76f));
        listbitCNY.add(new Market("ETH","bitCNY",2000.00000f,1998.62926f,2000.00000f,113.84f,7.94f));
        listbitCNY.add(new Market("IMIAO","bitCNY",1,1,1,1,0f));
        listbitCNY.add(new Market("BTC","bitCNY",27490.00171f,26999.99919f,27489.99802f,156.95f,10.27f));
        listbitCNY.add(new Market("BTS","bitCNY",0.576000f,0.573000f,0.576300f,3.44f,13.18f));
        listbitCNY.add(new Market("bitUSD","bitCNY",6.80921f,6.78063f,6.80850f,94.02f,1.76f));
        listbitCNY.add(new Market("ETH","bitCNY",2000.00000f,1998.62926f,2000.00000f,113.84f,7.94f));
        listbitCNY.add(new Market("IMIAO","bitCNY",1,1,1,1,0f));
        /**
         * BTC测试数据
         */

        List<Market> listBTS =new ArrayList<Market>();
        listBTS.add(new Market("BTC","BTS",48167.88078f,47508.33115f,48167.56325f,1.02f,-1.72f));
        listBTS.add(new Market("bitCNY","BTS",1.70000f,1.72554f,1.74664f,6.68f,-11.84f));
        listBTS.add(new Market("bitUSD","BTS",11.78503f,11.78503f,11.90351f,366.58f,-10.14f));
        listBTS.add(new Market("ETH","BTS",3518.64900f,3476.00237f,3518.64867f,176.46f,-4.28f));
        listBTS.add(new Market("IMIAO","BTS",1f,1f,1f,1f,0f));
        listBTS.add(new Market("BTC","BTS",48167.88078f,47508.33115f,48167.56325f,1.02f,-1.72f));
        listBTS.add(new Market("bitCNY","BTS",1.70000f,1.72554f,1.74664f,6.68f,-11.84f));
        listBTS.add(new Market("bitUSD","BTS",11.78503f,11.78503f,11.90351f,366.58f,-10.14f));
        listBTS.add(new Market("ETH","BTS",3518.64900f,3476.00237f,3518.64867f,176.46f,-4.28f));
        listBTS.add(new Market("IMIAO","BTS",1f,1f,1f,1f,0f));
        listBTS.add(new Market("BTC","BTS",48167.88078f,47508.33115f,48167.56325f,1.02f,-1.72f));
        listBTS.add(new Market("bitCNY","BTS",1.70000f,1.72554f,1.74664f,6.68f,-11.84f));
        listBTS.add(new Market("bitUSD","BTS",11.78503f,11.78503f,11.90351f,366.58f,-10.14f));
        listBTS.add(new Market("ETH","BTS",3518.64900f,3476.00237f,3518.64867f,176.46f,-4.28f));
        listBTS.add(new Market("IMIAO","BTS",1f,1f,1f,1f,0f));
        /**
         * bitUSD测试数据
         */
        List<Market> listbitUSD =new ArrayList<Market>();
        listbitUSD.add(new Market("BTS","bitUSD",10.084100f,0.084114f,0.084848f,29.80f,10.30f));
        listbitUSD.add(new Market("bitCNY","bitUSD",0.14749f,0.14600f,0.14748f,14.12f,-0.85f));
        listbitUSD.add(new Market("BTC","bitUSD",4051.07188f,4028.05566f,4049.99978f,15.52f,7.20f));
        listbitUSD.add(new Market("ETH","bitUSD",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listbitUSD.add(new Market("IMIAO","bitUSD",1f,1f,1f,1f,0f));
        listbitUSD.add(new Market("BTS","bitUSD",10.084100f,0.084114f,0.084848f,29.80f,10.30f));
        listbitUSD.add(new Market("bitCNY","bitUSD",0.14749f,0.14600f,0.14748f,14.12f,-0.85f));
        listbitUSD.add(new Market("BTC","bitUSD",4051.07188f,4028.05566f,4049.99978f,15.52f,7.20f));
        listbitUSD.add(new Market("ETH","bitUSD",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listbitUSD.add(new Market("IMIAO","bitUSD",1f,1f,1f,1f,0f));
        listbitUSD.add(new Market("BTS","bitUSD",10.084100f,0.084114f,0.084848f,29.80f,10.30f));
        listbitUSD.add(new Market("bitCNY","bitUSD",0.14749f,0.14600f,0.14748f,14.12f,-0.85f));
        listbitUSD.add(new Market("BTC","bitUSD",4051.07188f,4028.05566f,4049.99978f,15.52f,7.20f));
        listbitUSD.add(new Market("ETH","bitUSD",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listbitUSD.add(new Market("IMIAO","bitUSD",1f,1f,1f,1f,0f));


        List<Market> listETH =new ArrayList<Market>();
        listETH.add(new Market("BTC","ETH",13.99164550f,14.0074603f,14.28569275f,3.358f,-0.63f));
        listETH.add(new Market("bitUSD","ETH",0.00354500f,0.00347075f,20.00353766f,4.052f,3.68f));
        listETH.add(new Market("bitCNY","ETH",0.00028378f,0.00028327f,0.00028503f,26.012f,-0.70f));
        listETH.add(new Market("BTS","ETH",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listETH.add(new Market("IMIAO","ETH",1f,1f,1f,1f,0f));
        listETH.add(new Market("BTC","ETH",13.99164550f,14.0074603f,14.28569275f,3.358f,-0.63f));
        listETH.add(new Market("bitUSD","ETH",0.00354500f,0.00347075f,20.00353766f,4.052f,3.68f));
        listETH.add(new Market("bitCNY","ETH",0.00028378f,0.00028327f,0.00028503f,26.012f,-0.70f));
        listETH.add(new Market("BTS","ETH",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listETH.add(new Market("IMIAO","ETH",1f,1f,1f,1f,0f));
        listETH.add(new Market("BTC","ETH",13.99164550f,14.0074603f,14.28569275f,3.358f,-0.63f));
        listETH.add(new Market("bitUSD","ETH",0.00354500f,0.00347075f,20.00353766f,4.052f,3.68f));
        listETH.add(new Market("bitCNY","ETH",0.00028378f,0.00028327f,0.00028503f,26.012f,-0.70f));
        listETH.add(new Market("BTS","ETH",301.16279f,284.39281f,301.44213f,308.734f,50.58f));
        listETH.add(new Market("IMIAO","ETH",1f,1f,1f,1f,0f));

        Map<String,List<Market>>  map=new HashMap<String,List<Market>>();
        map.put("BTS",listBTS);
        map.put("bitCNY",listbitCNY);
        map.put("BTC",listBTC);
        map.put("bitUSD",listbitUSD);
        map.put("ETH",listETH);

        return map;
    }

    @Override
    public Market queryMarket(String LeftCoin, String rightCoin) {
        return null;
    }

    @Override
    public List<Market> queryMarkets(String LeftCoin, String rightCoin, String date) {
        Log.i("queryMarkets", "queryMarkets: LeftCoin:"+LeftCoin+"rightCoin:"+rightCoin);
        List<Market> markets=new ArrayList<Market>();
        Date date1=new Date();
        int max=60;
        int min=1;
        Random random = new Random();
        int a = random.nextInt(max)%(max-min+1) + min;
        int b = random.nextInt(max)%(max-min+1) + min;
        int c = random.nextInt(max)%(max-min+1) + min;
        int d = random.nextInt(max)%(max-min+1) + min;
        int[] h={a,b,c,d};
        Arrays.sort(h);
        Calendar beforeTime = Calendar.getInstance();
        float price=301.16279f;
        beforeTime.add(Calendar.MINUTE, -(60*5));
        for (int i=60;i>=1;i--){
            float newPrice=0;
            float price2=random.nextFloat()*100;
            Market market=null;
            beforeTime.add(Calendar.MINUTE, +5);
            Date beforeD = beforeTime.getTime();
            Log.i("queryMarkets", "queryMarkets: beforeD:"+beforeD);
            if(i>d){
                newPrice=price-price2;
                market=new Market(beforeD,LeftCoin,rightCoin,newPrice,284.39281f,301.44213f,308.734f,50.58f);
            }else if(i>c){
                newPrice=price+price2;
                market=new Market(beforeD,LeftCoin,rightCoin,newPrice,284.39281f,301.44213f,308.734f,50.58f);
            } else if(i>b){
                newPrice=price-price2;
                market=new Market(beforeD,LeftCoin,rightCoin,newPrice,284.39281f,301.44213f,308.734f,50.58f);
            } else if(i>a){
                newPrice=price+price2;
                market=new Market(beforeD,LeftCoin,rightCoin,newPrice,284.39281f,301.44213f,308.734f,50.58f);
            }else {
                newPrice=price-price2;
                market=new Market(beforeD,LeftCoin,rightCoin,newPrice,284.39281f,301.44213f,308.734f,50.58f);
            }
            Log.i("queryMarkets", "queryMarkets: market:"+market.toString());
            markets.add(market);
        }

        Log.i("queryMarkets", "queryMarkets: markets.size():"+markets.size());
        return markets;
    }

    @Override
    public List<Market> queryMarkets(String rightCoin) {
        return null;
    }


    @Override
    public List getnewprice() {

        List list =new ArrayList();
        list.add(1000);

        return list;
    }


}
