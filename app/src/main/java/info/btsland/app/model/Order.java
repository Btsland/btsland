package info.btsland.app.model;


import info.btsland.app.api.account_object;
import info.btsland.app.api.object_id;

public class Order {
    public object_id<account_object> seller;
    public double price;
    public double quote;
    public double base;

    @Override
    public String toString() {
        return "Order{" +
                "price=" + price +
                ", quote=" + quote +
                ", base=" + base +
                '}';
    }
}
