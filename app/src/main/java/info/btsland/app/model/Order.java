package info.btsland.app.model;


public class Order {
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
