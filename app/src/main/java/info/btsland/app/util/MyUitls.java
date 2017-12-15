package info.btsland.app.util;

import info.btsland.app.api.asset;

/**
 * Created by Administrator on 2017/12/6.
 */

public class MyUitls {
    public static double assetToReal(asset a, long p) {
        return (double)a.amount / Math.pow(10, p);
    }
}
