package info.btsland.app.api;

import static info.btsland.app.R.string.asset;

public class price {
    public asset base;
    public asset quote;

    public price(asset assetBase, asset assetQuote) {
        base = assetBase;
        quote = assetQuote;
    }

    public static price unit_price(String assetObjectobjectId) {
        return new price(new asset(1, assetObjectobjectId), new asset(1, assetObjectobjectId));
    }
}
