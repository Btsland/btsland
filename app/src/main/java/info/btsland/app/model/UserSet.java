package info.btsland.app.model;

/**
 * author：lw1000
 * function：用户设置类
 * 2017/10/20.
 */


public class UserSet {


    /**
     * 用户资产
     */
    private String uAsset;


    /**
     * 换算值
     */
    private String scaledValue;



    /**
     * 供给占比
     */
    private String supplyShare;




    public UserSet(String uAsset, String scaledValue, String supplyShare) {
        this.uAsset = uAsset;
        this.scaledValue = scaledValue;
        this.supplyShare = supplyShare;
    }


    public String getuAsset() {
        return uAsset;
    }


    public void setuAsset(String uAsset) {
        this.uAsset = uAsset;
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






























