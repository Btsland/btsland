package info.btsland.app.api;

/**
 * Created by Administrator on 2018/1/24.
 */

public class call_order_object {
    public String id;
    public object_id<account_object> borrower;
    public String collateral;
    public String debt;
    public price call_price;
}
