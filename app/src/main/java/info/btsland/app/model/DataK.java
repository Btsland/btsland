package info.btsland.app.model;

import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;

import info.btsland.app.api.MarketStat;

/**
 * Created by Administrator on 2017/11/7.
 */

public class DataK {
    public float max;
    public float min;
    public List<CandleEntry> candleEntries;
    public List<MarketStat.HistoryPrice> prices;
}
