package info.btsland.app;

import android.support.multidex.MultiDexApplication;

import java.security.Security;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BtslandApplication  extends MultiDexApplication {
    private static BtslandApplication theApp;
    /*
    * 是否需要把涨跌的颜色互换
     */
    public static BtslandApplication getInstance() {
        return theApp;
    }

    public BtslandApplication() {
        theApp = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
