package info.btsland.app.service.Impl;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.model.Market;
import info.btsland.app.service.MarketService;

/**
 * author：
 * function：
 * 2017/9/27.
 */

public class MarketServiceImpl implements MarketService {
    @Override
    public List getnewprice(String newprice) {

        List list =new ArrayList();
        list.add(1000);

        return list;
    }


}
