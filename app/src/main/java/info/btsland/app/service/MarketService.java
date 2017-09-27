package info.btsland.app.service;

import java.util.List;

import info.btsland.app.model.Market;

/**
 * author：
 * function：
 * 2017/9/27.
 */

public interface MarketService {
    /**
     *
     *获取最新成交价
     */
    List getnewprice(String newprice);
}
