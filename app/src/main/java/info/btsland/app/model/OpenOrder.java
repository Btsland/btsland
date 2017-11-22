package info.btsland.app.model;


import info.btsland.app.api.asset;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.limit_order_object;

public class OpenOrder {
    public limit_order_object limitOrder;
    public asset base;
    public asset quote;
    public String baseName;
    public String quoteName;
    public Double baseNum;
    public Double quoteNum;
    public double price;

    @Override
    public String toString() {
        return "OpenOrder{" +
                "limitOrder=" + limitOrder +
                ", base=" + base +
                ", quote=" + quote +
                ", baseName='" + baseName + '\'' +
                ", quoteName='" + quoteName + '\'' +
                ", baseNum=" + baseNum +
                ", quoteNum=" + quoteNum +
                ", price=" + price +
                '}';
    }
}
