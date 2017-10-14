package info.btsland.app.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * author：lys
 * function：行情类
 * 2017/9/27.
 */

public class Market {
    /*
     * 左边货币
     */
    private String leftCoin;
    /**
     * 右边货币
     */
    private  String rightCoin;

    /**
     * 最新成交价
     */
    private String newPrice;



    /**
     * 最高买价
     */
    private  String bestBid;

    /**
     * 最低卖价
     */
    private  String bestAsk;

    /**
     * 成交量
     */
    private  String  volume;


    /**
     * 涨/跌幅
     */
    private float fluctuation;





    public Market() {

    }

    /**
     *
     * @param leftCoin 左边货币
     * @param rightCoin 右边货币
     * @param newPrice 最新成交价
     * @param bestBid 最高买价
     * @param bestAsk 最低卖价
     * @param volume 成交量
     * @param fluctuation 涨/跌幅
     */
    public Market(String leftCoin, String rightCoin, String newPrice, String bestBid, String bestAsk, String volume, float fluctuation) {
        this.leftCoin = leftCoin;
        this.rightCoin = rightCoin;
        this.newPrice = newPrice;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.volume = volume;
        this.fluctuation = fluctuation;

    }

    @Override
    public String
    toString() {
        return "Market{" +
                "leftCoin='" + leftCoin + '\'' +
                ", rightCoin='" + rightCoin + '\'' +
                ", newPrice='" + newPrice + '\'' +
                ", bestBid='" + bestBid + '\'' +
                ", bestAsk='" + bestAsk + '\'' +
                ", volume='" + volume + '\'' +
                ", fluctuation='" + fluctuation + '\'' +
                '}';
    }

    public String getLeftCoin() {
        return leftCoin;
    }

    public void setLeftCoin(String leftCoin) {
        this.leftCoin = leftCoin;
    }

    public String getRightCoin() {
        return rightCoin;
    }

    public void setRightCoin(String rightCoin) {
        this.rightCoin = rightCoin;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getBestBid() {
        return bestBid;
    }

    public void setBestBid(String bestBid) {
        this.bestBid = bestBid;
    }

    public String getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(String bestAsk) {
        this.bestAsk = bestAsk;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public float getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(float fluctuation) {
        this.fluctuation = fluctuation;
    }


}
