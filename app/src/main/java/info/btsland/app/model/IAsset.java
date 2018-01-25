package info.btsland.app.model;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.exception.NetworkStatusException;

/**
 * author：lw1000
 * function：分类资产
 * 2017/11/15.
 */

public class IAsset implements Cloneable {
    public asset mAsset;
    public Double total=0.0;
    public Double usable=0.0;
    public Double borrow=0.0;
    public Double orders=0.0;
    public String coinName;
    public Double totalCNY=0.0;
    public Double totalBTS=0.0;

    public IAsset( String coinName){
        this.coinName=coinName;
        mAsset=new asset();
    }
    public IAsset(asset mAsset) {
        this.mAsset=mAsset;
        if(!fillIn()){
            this.mAsset=null;
        }
    }
    public IAsset(asset mAsset,Double totalCNY) {
        this.mAsset=mAsset;
        this.totalCNY=totalCNY;
        if(!fillIn()){
            this.mAsset=null;
        }
    }
    public IAsset() {
    }



    @Override
    public IAsset clone() throws CloneNotSupportedException {
        IAsset iAsset = (IAsset)super.clone();
        iAsset.mAsset=mAsset.clone();
        return iAsset;
    }

    /**
     *
     */
    private boolean fillIn(){
        List<object_id<asset_object>> object_ids=new ArrayList <>();
        object_ids.add(mAsset.asset_id);
        try {
            List<asset_object> assetObjects = BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
            if(assetObjects!=null){
                asset_object objects = assetObjects.get(0);
                coinName=objects.symbol;
                usable=mAsset.amount/Math.pow(10,objects.precision);//计算可用额
                if(BtslandApplication.borrowMap!=null){
                    if(coinName.equals("BTS")){
                        for(int i=0;i<BtslandApplication.Iborrows.size();i++){
                            Borrow borrow1=BtslandApplication.Iborrows.get(i);
                            borrow+=borrow1.collateral;
                        }
                    }else {
                        Borrow borrow1=BtslandApplication.borrowMap.get(mAsset.asset_id);
                        if(borrow1!=null){
                            if(mAsset.asset_id.equals(borrow1.call_price.quote.asset_id)){
                                borrow=borrow1.debt;
                            }
                        }
                    }
                }
                if(BtslandApplication.openOrders!=null){
                    for(int i=0;i<BtslandApplication.openOrders.size();i++) {
                        OpenOrder openOrder = BtslandApplication.openOrders.get(i);
                        if(mAsset.asset_id.equals(openOrder.base.asset_id)){
                            orders += openOrder.baseNum;
                        }
                    }
                }
                if(coinName.equals("BTS")){
                    total=usable+orders+borrow;//如果该资产是BTS，那么总额为可用额加挂单额加抵押额
                }else {
                    total=usable+orders;//如果该资产不是BTS，那么总额为可用额加挂单额
                }
                return true;
            }else {
                return false;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
        return false;
    }

}
