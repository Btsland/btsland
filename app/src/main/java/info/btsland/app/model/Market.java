package info.btsland.app.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author：lys
 * function：行情类
 * 2017/9/27.
 */

public class Market implements Serializable {
    private Date date;
    /*
     * 左边货币
     */
    private String leftCoin;
    /**
     * 右边货币
     */
    private String rightCoin;

    /**
     * 最新成交价
     */
    private float newPrice;
    /**
     * 最高买价
     */
    private float bestBid;

    /**
     * 最低卖价
     */
    private float bestAsk;

    /**
     * 成交量
     */
    private float volume;


    /**
     * 涨/跌幅
     */
    private float fluctuation;


    public Market() {

    }

    /**
     * @param leftCoin    左边货币
     * @param rightCoin   右边货币
     * @param newPrice    最新成交价
     * @param bestBid     最高买价
     * @param bestAsk     最低卖价
     * @param volume      成交量
     * @param fluctuation 涨/跌幅
     */
    public Market(String leftCoin, String rightCoin, float newPrice, float bestBid, float bestAsk, float volume, float fluctuation) {
        this.leftCoin = leftCoin;
        this.rightCoin = rightCoin;
        this.newPrice = newPrice;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.volume = volume;
        this.fluctuation = fluctuation;

    }

    /**
     * @param leftCoin    左边货币
     * @param rightCoin   右边货币
     * @param newPrice    最新成交价
     * @param bestBid     最高买价
     * @param bestAsk     最低卖价
     * @param volume      成交量
     * @param fluctuation 涨/跌幅
     */
    public Market(Date date, String leftCoin, String rightCoin, float newPrice, float bestBid, float bestAsk, float volume, float fluctuation) {
        this.date = date;
        this.leftCoin = leftCoin;
        this.rightCoin = rightCoin;
        this.newPrice = newPrice;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.volume = volume;
        this.fluctuation = fluctuation;

    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return "Market{" +
//                "date=" + df.format(date) +
                ", leftCoin='" + leftCoin + '\'' +
                ", rightCoin='" + rightCoin + '\'' +
                ", newPrice=" + newPrice +
                ", bestBid=" + bestBid +
                ", bestAsk=" + bestAsk +
                ", volume=" + volume +
                ", fluctuation=" + fluctuation +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public Market setDate(Date date) {
        this.date = date;
        return this;
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

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public float getBestBid() {
        return bestBid;
    }

    public void setBestBid(float bestBid) {
        this.bestBid = bestBid;
    }

    public float getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(float bestAsk) {
        this.bestAsk = bestAsk;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(float fluctuation) {
        this.fluctuation = fluctuation;
    }

    @Override
    public Market clone() throws CloneNotSupportedException {
        Market market = new Market();
        market.bestAsk=this.bestAsk;
        market.bestBid=this.bestBid;
        return market;
    }
}
