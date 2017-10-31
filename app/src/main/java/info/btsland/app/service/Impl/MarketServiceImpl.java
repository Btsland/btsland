package info.btsland.app.service.Impl;

import android.os.Parcel;
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
    private String sr;
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
        listBTC.add(new Market("BTS","BTC",0.00000887f,0.00000895f,0.00000899f,7.246f,-3.31f));
        listBTC.add(new Market("USD","BTC",0.00017105f,0.00016893f,0.00017163f,1.364f,-2.84f));
        listBTC.add(new Market("IMIAO","BTC",0.00011111f,0f,0f,0f,0.00f));
        listBTC.add(new Market("YOYOW","BTC",0.00000643f,0.00000596f,0.00000643f,0.028f,8.03f));
        listBTC.add(new Market("HPB","BTC",0.00010222f,0f,0.00500000f,0f,0f));
        listBTC.add(new Market("OCT","BTC",0.00002301f,0.00002301f,0.0002510f,0.0006f,0.00f));
        listBTC.add(new Market("bitCNY","BTC",0.00002572f,0.00002557f,0.00002572f,2.719f,-2.04f));
        listBTC.add(new Market("open.ETH","BTC",0.05056294f,0.5056371f,0.5082561f,0.603f,-1.96f));
        listBTC.add(new Market("open.DASH","BTC",0.04832520f,0.04832519f,0.04899461f,0.171f,-1.86f));
        listBTC.add(new Market("open.LTC","BTC",0.00910866f,0.00910866f,0.00970502f,0.000f,0.00f));
        listBTC.add(new Market("open.EOS","BTC",0.00002000f,0.00008901f,0.00078790f,0.000f,-97.47f));
        listBTC.add(new Market("open.OMG","BTC",0.00711111f,0.00001000f,0f,0f,0f));
        listBTC.add(new Market("open.STEEM","BTC",0.00016772f,0.00016674f,0.00016929f,0.009f,-4.31f));
        /**
         * bitCNY测试数据
         */
        List<Market> listbitCNY =new ArrayList<Market>();

        listbitCNY.add(new Market("BTS","CNY",0.346507f,0.344439f,0.346500f,773.23f,-0.88f));
        listbitCNY.add(new Market("USD","CNY",6.690000f,6.66684f,6.68850f,105.73f,-0.18f));
        listbitCNY.add(new Market("IMIAO","CNY",0.250000f,0.24998f,0.25000f,1.000f,0.00f));
        listbitCNY.add(new Market("YOYOW","CNY",0.250000f,0.244430f,0.257650f,74.96f,4.60f));
        listbitCNY.add(new Market("HPB","CNY",3.123000f,3.124000f,4.0000000f,19.37f,-21.92f));
        listbitCNY.add(new Market("OCT","CNY",0.980000f,0.886900f,0.967300f,10.57f,5.77f));
        listbitCNY.add(new Market("open.BTC","CNY",39844f,39128.44695f,39438.87315f,103.39f,4.61f));
        listbitCNY.add(new Market("open.ETH","CNY",1972f,1971.51006f,1985.00005f,52.56f,0.03f));
        listbitCNY.add(new Market("open.DASH","CNY",1891f,1980.88594f,1906.64831f,11276.138f,-0.22f));
        listbitCNY.add(new Market("open.LTC","CNY",400.00f,354.25367f,375.23800f,438.500f,15.10f));
        listbitCNY.add(new Market("open.EOS","CNY",3.699998f,3.50000f,3.70000f,325.596f,2.78f));
        listbitCNY.add(new Market("open.OMG","CNY",44.31f,0.00001f,0f,0f,0f));
        listbitCNY.add(new Market("open.STEEM","CNY",6.340754f,6.41026f,6.51773f,26.08f,-5.27f));

        /**
         * BTC测试数据
         */

        List<Market> listBTS =new ArrayList<Market>();
        listBTS.add(new Market("bitCNY","BTS",2.845760f,2.845760f,2.94777f,2.30f,-0.65f));
        listBTS.add(new Market("USD","BTS",19.12f,19.04783f,19.11728f,298.45f,-0.25f));
        listBTS.add(new Market("IMIAO","BTS",2.000000f,0.00001f,1.00000f,0.000f,0.00f));
        listBTS.add(new Market("YOYOW","BTS",0.749994f,0.73000f,0.74999f,15.46f,16.85f));
        listBTS.add(new Market("HPB","BTS",1.840000f,1.66667f,10000000f,0f,0.00f));
        listBTS.add(new Market("OCT","BTS",3.000000f,2.65385f,3.00000f,18.00f,11.11f));
        listBTS.add(new Market("open.BTC","BTS",111121f,110300f,111111.1111f,842.85f,-0.87f));
        listBTS.add(new Market("open.ETH","BTS",5611f,5590.54825f,5608.44800f,246.66f,-0.22f));
        listBTS.add(new Market("open.DASH","BTS",5401f,5306.29814f,5381.90433f,30.03f,-0.28f));
        listBTS.add(new Market("open.LTC","BTS",1074f,985.59998f,1069.02032f,811938f,5.11f));
        listBTS.add(new Market("open.EOS","BTS",11.00f,10.20000f,11.00000f,4931.388f,0.00f));
        listBTS.add(new Market("open.OMG","BTS",200.00f,200.00000f,300.00000f,355.870f,14.29f));
        listBTS.add(new Market("open.STEEM","BTS",18.44f,18.31576f,19.34175f,13.85f,-2.01f));
        /**
         * bitUSD测试数据
         */
        List<Market> listbitUSD =new ArrayList<Market>();
        listbitUSD.add(new Market("bitCNY","USD",0.149536f,0.149666f,0.1500000f,29.45f,0.04f));
        listbitUSD.add(new Market("bitBTS","USD",0.053785f,0.053355f,0.53941f,15.53f,2.88f));
        listbitUSD.add(new Market("IMIAO","USD",0.107378f,0.0f,0.0f,0.000f,0.00f));
        listbitUSD.add(new Market("YOYOW","USD",0.037393f,0.03578f,0.04330f,15.771f,-4.00f));
        listbitUSD.add(new Market("HPB","USD",0.098788f,0.0f,0.0f,0f,0.00f));
        listbitUSD.add(new Market("OCT","USD",0.132082f,0.13222f,0.14368f,11.794f,-0.11f));
        listbitUSD.add(new Market("open.BTC","USD",5732f,5731.58941f,5784.99952f,7393.336f,-1.98f));
        listbitUSD.add(new Market("open.ETH","USD",299.81f,294.28589f,299.81233f,879.370f,2.00f));
        listbitUSD.add(new Market("open.DASH","USD",282.52f,276.98003f,283.28930f,1947.357f,-1.35f));
        listbitUSD.add(new Market("open.LTC","USD",51.10f,53.19658f,56.43785f,0.0f,0.0f));
        listbitUSD.add(new Market("open.EOS","USD",0.523000f,0.519000f,0.573980f,1912.402f,-1.32f));
        listbitUSD.add(new Market("open.OMG","USD",0.000200f,0.00023f,10.90000f,0.0f,0.00f));
        listbitUSD.add(new Market("open.STEEM","USD",0.900000f,0.98940f,1.00904f,58.033f,-5.82f));


        List<Market> listETH =new ArrayList<Market>();
        listETH.add(new Market("bitCNY","ETH",0.00050583f,0.00050352f,0.00050832f,13.233f,-0.49f));
        listETH.add(new Market("bitBTS","ETH",0.00018036f,0.00018025f,0.00018152f,30.230f,1.36f));
        listETH.add(new Market("IMIAO","ETH",0.107378f,0.0f,0.0f,0.000f,0.00f));
        listETH.add(new Market("YOYOW","ETH",0.037393f,0.03578f,0.04330f,15.771f,-4.00f));
        listETH.add(new Market("HPB","ETH",0.098788f,0.0f,0.0f,0f,0.00f));
        listETH.add(new Market("OCT","ETH",0.132082f,0.13222f,0.14368f,11.794f,-0.11f));
        listETH.add(new Market("open.BTC","ETH",19.49959336f,19.45605892f,19.49953483f,1.446f,-2.16f));
        listETH.add(new Market("USD","ETH",0.00333542f,0.00333624f,0.00339806f,2.969f,-1.95f));
        listETH.add(new Market("open.DASH","ETH",282.52f,276.98003f,283.28930f,1947.357f,-1.35f));
        listETH.add(new Market("open.LTC","ETH",51.10f,53.19658f,56.43785f,0.0f,0.0f));
        listETH.add(new Market("open.EOS","ETH",0.523000f,0.519000f,0.573980f,1912.402f,-1.32f));
        listETH.add(new Market("open.OMG","ETH",0.000200f,0.00023f,10.90000f,0.0f,0.00f));
        listETH.add(new Market("open.STEEM","ETH",0.900000f,0.98940f,1.00904f,58.033f,-5.82f));

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
