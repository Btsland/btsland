package info.btsland.app.model;


import info.btsland.app.api.asset_object;
import info.btsland.app.api.limit_order_object;

public class OpenOrder {
    public limit_order_object limitOrder;
    public asset_object base;
    public asset_object quote;
    public double price;
}
