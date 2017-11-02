package info.btsland.app.api;


import java.math.BigInteger;


public class asset {
    public long amount;
    public String asset_id;

    public asset(long lAmount, String assetObjectobjectId) {
        amount = lAmount;
        asset_id = assetObjectobjectId;
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
}
