package info.btsland.app.model;

/**
 * author：lys
 * function：行情类
 * 2017/9/27.
 */

public class Market {
    /*
     * 商品货币
     */
    private String gcoin;
    /**
     * 交易货币
     */
    private  String coin;

    /**
     * 最新成交价
     */
    private String newprice;



    /**
     * 最高买价
     */
    private  String bestbid;

    /**
     * 最低卖价
     */
    private  String bestask;

    /**
     * 成交量
     */
    private  String  volume;

    /**
     * 成交额
     */
    private  String  Turnover;

    /**
     * 涨/跌幅
     */
    private String fluctuation;

    /**
     * 余额
     */
    private  String balance;



    public Market() {

    }

    public Market(String gcoin, String coin, String newprice, String bestbid, String bestask, String volume, String turnover, String fluctuation, String balance) {
        this.gcoin = gcoin;
        this.coin = coin;
        this.newprice = newprice;
        this.bestbid = bestbid;
        this.bestask = bestask;
        this.volume = volume;
        Turnover = turnover;
        this.fluctuation = fluctuation;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "MarketService{" +
                "gcoin='" + gcoin + '\'' +
                ", coin='" + coin + '\'' +
                ", newprice='" + newprice + '\'' +
                ", bestbid='" + bestbid + '\'' +
                ", bestask='" + bestask + '\'' +
                ", volume='" + volume + '\'' +
                ", Turnover='" + Turnover + '\'' +
                ", fluctuation='" + fluctuation + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    public String getGcoin() {
        return gcoin;
    }

    public void setGcoin(String gcoin) {
        this.gcoin = gcoin;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getNewprice() {
        return newprice;
    }

    public void setNewprice(String newprice) {
        this.newprice = newprice;
    }

    public String getBestbid() {
        return bestbid;
    }

    public void setBestbid(String bestbid) {
        this.bestbid = bestbid;
    }

    public String getBestask() {
        return bestask;
    }

    public void setBestask(String bestask) {
        this.bestask = bestask;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTurnover() {
        return Turnover;
    }

    public void setTurnover(String turnover) {
        Turnover = turnover;
    }

    public String getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(String fluctuation) {
        this.fluctuation = fluctuation;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
