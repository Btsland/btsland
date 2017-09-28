package info.btsland.app.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import info.btsland.app.model.Market;
import info.btsland.app.service.MarketService;

/**
 * author：
 * function：
 * 2017/9/27.
 */

public class MarketServiceImpl implements MarketService {
    /**
     *
     * @param leftCoin 左边货币
     * @param rightCoin 右边货币
     * @param newPrice 最新成交价
     * @param bestBid 最高买价
     * @param bestAsk 最低卖价
     * @param volume 成交量
     * @param turnover 成交额
     * @param fluctuation 涨/跌幅
     */
    @Override
    public List getallinformation() {

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
        List<Market> listBTS=new ArrayList<Market>();
        listBTS.add(new Market("BTC","BTS","0.00002109","0.00002105","0.00002121","20.734","+3.11%"));
        listBTS.add(new Market("bitCNY","BTS","1.76806","1.77905","1.77937","6.31M","-9.02%"));
        listBTS.add(new Market("bitUSD","BTS","11.93180","11.80174","11.92885","346.71k","-10.71%"));
        listBTS.add(new Market("ETH","BTS","3504.27338","3536.06789","3577.92045","186.82k","-4.95%"));
        listBTS.add(new Market("IMIAO","BTS","1","1","1","1","0%"));
        /**
         * bitCNY测试数据
         */
        List<Market> listbitCNY =new ArrayList<Market>();
        listbitCNY.add(new Market("BTC","bitCNY","27490.00171","26999.99919","27489.99802","156.95k","10.27%"));
        listbitCNY.add(new Market("BTS","bitCNY","0.576000","0.573000","0.576300","3.44M","13.18%"));
        listbitCNY.add(new Market("bitUSD","bitCNY","6.80921","6.78063","6.80850","94.02k","1.76%"));
        listbitCNY.add(new Market("ETH","bitCNY","2000.00000","1998.62926","2000.00000","113.84k","7.94%"));
        listbitCNY.add(new Market("IMIAO","bitCNY","1","1","1","1","0%"));
        /**
         * BTC测试数据
         */

        List<Market> listBTC =new ArrayList<Market>();
        listBTC.add(new Market("BTS","BTC","48167.88078","47508.33115","48167.56325","1.02M","-1.72%"));
        listBTC.add(new Market("bitCNY","BTC","1.70000","1.72554","1.74664","6.68M","-11.84%"));
        listBTC.add(new Market("bitUSD","BTC","11.78503","11.78503","11.90351","366.58k","-10.14%"));
        listBTC.add(new Market("ETH","BTC","3518.64900","3476.00237","3518.64867","176.46k","-4.28%"));
        listBTC.add(new Market("IMIAO","BTC","1","1","1","1","0%"));
        /**
         * bitUSD测试数据
         */
        List<Market> listbitUSD =new ArrayList<Market>();
        listbitUSD.add(new Market("BTS","bitUSD","10.084100","0.084114","0.084848","29.80k","10.30%"));
        listbitUSD.add(new Market("bitCNY","bitUSD","0.14749","0.14600","0.14748","14.12k","-0.85%"));
        listbitUSD.add(new Market("BTC","bitUSD","4051.07188","4028.05566","4049.99978","15.52k","7.20%"));
        listbitUSD.add(new Market("ETH","bitUSD","301.16279","284.39281","301.44213","308.734k","50.58%"));
        listbitUSD.add(new Market("IMIAO","bitUSD","1","1","1","1","0%"));


        Map<String,List>  map=new HashMap<String,List>();
        map.put("BTC",listBTS);
        map.put("bitCNY",listbitCNY);
        map.put("BTS",listBTC);
        map.put("bitUSD",listbitUSD);

        return null;
    }


    @Override
    public List getnewprice() {

        List list =new ArrayList();
        list.add(1000);

        return list;
    }


}
