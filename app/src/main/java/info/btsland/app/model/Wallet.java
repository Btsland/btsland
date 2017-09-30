package info.btsland.app.model;

/**
 * author：lw1000
 * function：钱包类
 * 2017/9/29.
 */

public class Wallet {




    /**
     * 资产
     */
        private String assets;

    /**
     * 换算值
     */
    private String scaledValue;

    /**
     * 供给占比
     */
    private  String supplyShare;

  /*  *//**
     * 转账操作
     *//*
    private String transfer;


    */


    @Override
    public String toString() {
        return "Wallet{" +
                "assets='" + assets + '\'' +
                ", scaledValue='" + scaledValue + '\'' +
                ", supplyShare='" + supplyShare + '\'' +
                '}';
    }

    /**
     * 市场操作
     *//*
    private  String operate;*/




    public Wallet(String assets, String scaledValue, String supplyShare) {
        this.assets = assets;
        this.scaledValue = scaledValue;
        this.supplyShare = supplyShare;
    }

    public Wallet() {
    }


    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getScaledValue() {
        return scaledValue;
    }

    public void setScaledValue(String scaledValue) {
        this.scaledValue = scaledValue;
    }

    public String getSupplyShare() {
        return supplyShare;
    }

    public void setSupplyShare(String supplyShare) {
        this.supplyShare = supplyShare;
    }
}
