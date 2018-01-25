package info.btsland.app.model;

import info.btsland.app.api.account_object;
import info.btsland.app.api.call_order_object;
import info.btsland.app.api.price;
import info.btsland.app.util.AssetUtil;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Borrow {
    public String id;
    public account_object borrower;
    public double collateral;
    public double debt;
    public double price;
    public price call_price;
    public double ratio;

    public Borrow(call_order_object call_order_object) {
        this.id=call_order_object.id;
        this.collateral= AssetUtil.assetToReal(Long.decode(call_order_object.collateral),call_order_object.call_price.base.asset_id);
        this.debt= AssetUtil.assetToReal(Long.decode(call_order_object.debt),call_order_object.call_price.quote.asset_id);
        this.price=AssetUtil.priceToReal(call_order_object.call_price);
        this.call_price=call_order_object.call_price;
    }
}
