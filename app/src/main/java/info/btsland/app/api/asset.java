package info.btsland.app.api;


import java.math.BigInteger;


public class asset implements Cloneable {
    public long amount=0;
    public object_id<asset_object> asset_id;

    public asset() {
    }

    public asset(long lAmount, object_id<asset_object> assetObjectobjectId) {
        amount = lAmount;
        asset_id = assetObjectobjectId;
    }

    @Override
    public asset clone() throws CloneNotSupportedException {
        return (asset) super.clone();
    }

    public asset multipy(price priceObject) {
        BigInteger bigAmount = BigInteger.valueOf(amount);
        BigInteger bigQuoteAmount = BigInteger.valueOf(priceObject.quote.amount);
        BigInteger bigBaseAmount = BigInteger.valueOf(priceObject.base.amount);
        if (asset_id.equals(priceObject.base.asset_id)) {
            BigInteger bigResult = bigAmount.multiply(bigQuoteAmount).divide(bigBaseAmount);
            return new asset(bigResult.longValue(), priceObject.quote.asset_id);

        } else if (asset_id.equals(priceObject.quote.asset_id)) {
            BigInteger bigResult = bigAmount.multiply(bigBaseAmount).divide(bigQuoteAmount  );
            return new asset(bigResult.longValue(), priceObject.base.asset_id);
        } else {
            throw new RuntimeException("invalid price object");
        }
    }

    @Override
    public String toString() {
        return "asset{" +
                "amount=" + amount +
                ", asset_id=" + asset_id +
                '}';
    }
}
