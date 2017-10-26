package info.btsland.app.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

import info.btsland.app.model.Market;

/**
 * author：
 * function：
 * 2017/9/27.
 */

public interface MarketService{
    /**
     *
     *获取最新成交价
     */
    List getnewprice();

    /**
     *
     * @return
     */
    Map<String,List<Market>> getallinformation();

    /**
     * 根据交易对查询该交易对的最新交易信息
     * @param LeftCoin 市场内的出售的代币
     * @param rightCoin 市场通用代币
     * @return 一条最新的交易信息
     */
    Market queryMarket(String LeftCoin,String rightCoin);

    /**
     * 根据交易对查询该交易对的历史交易信息
     * @param LeftCoin 市场内的出售的代币
     * @param rightCoin 市场通用代币
     * @param date 查询深度
     * @return 一定深度内的交易信息记录集合
     */
    List<Market> queryMarkets(String LeftCoin,String rightCoin,String date);

    /**
     * 根据市场查询该市场的所有最新交易信息
     * @param rightCoin 市场通用代币
     * @return 所有最新交易信息记录集合
     */
    List<Market> queryMarkets(String rightCoin);

}
