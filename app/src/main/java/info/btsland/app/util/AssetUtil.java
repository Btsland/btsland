package info.btsland.app.util;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.object_id;
import info.btsland.app.api.price;
import info.btsland.app.exception.NetworkStatusException;

/**
 * Created by Administrator on 2018/1/24.
 */

public class AssetUtil {
    public static double priceToReal(price p) {
        asset_object baseOb= assetToAssetObject(p.base.asset_id);
        asset_object quoteOb= assetToAssetObject(p.quote.asset_id);
        return assetToReal(p.quote, quoteOb.precision)
                / assetToReal(p.base, baseOb.precision);
    }

    public static double assetToReal(asset a, long p) {
        return assetToReal(a.amount,p);
    }

    public static double assetToReal(long a, long p) {
        return (double)a / Math.pow(10, p);
    }
    public static double assetToReal(long a, object_id<asset_object> id) {
        return assetToReal(a, assetToAssetObject(id).precision);
    }
    public static double assetToReal(asset a,object_id<asset_object> id){
        return assetToReal(a, assetToAssetObject(id).precision);
    }

    public static asset_object assetToAssetObject(object_id<asset_object> id){
        asset_object assetObject= BtslandApplication.assetObjectMap.get(id);
        if(assetObject==null){
            List<object_id<asset_object>> object_ids=new ArrayList<>();
            object_ids.add(id);
            try {
                List<asset_object> objects=BtslandApplication.getMarketStat().mWebsocketApi.get_assets(object_ids);
                assetObject=objects.get(0);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }
        }
        return assetObject;
    }
}
