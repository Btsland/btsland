package info.btsland.app;

import android.support.multidex.MultiDexApplication;

import java.security.Security;

import info.btsland.app.api.MarketStat;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication implements MarketStat.OnMarketStatUpdateListener {

    public static boolean isWel=false;
    public BtslandApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onMarketStatUpdate(MarketStat.Stat stat) {

    }

}
