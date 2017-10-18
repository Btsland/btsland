package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.Market;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MarketRowAdapter extends BaseAdapter {
    private List<Market> markets;
    private LayoutInflater inflater;
    private Context context;
    public MarketRowAdapter(Context context, List<Market> markets) {
        this.markets = markets;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return markets.size();
    }

    @Override
    public Object getItem(int i) {
        return markets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Market market=markets.get(i);
        if (convertView==null){
            convertView=inflater.inflate(R.layout.fragment_market_row,null);
            TextView tvCoin=convertView.findViewById(R.id.tv_coin);
            TextView tvFluctuation=convertView.findViewById(R.id.tv_fluctuation);
            TextView tvNewPrice=convertView.findViewById(R.id.tv_newPrice);
            TextView tvBestAskNum=convertView.findViewById(R.id.tv_bestAskNum);
            TextView tvBestBidNum=convertView.findViewById(R.id.tv_bestBidNum);

            tvCoin.setText(market.getLeftCoin());
            tvFluctuation.setText(String.valueOf(market.getFluctuation()));
            tvNewPrice.setText(market.getNewPrice());
            tvBestAskNum.setText(market.getBestAsk());
            tvBestAskNum.setText(market.getBestBid());
            if (market.getFluctuation()>=0){
                ;
                tvFluctuation.setTextColor(context.getResources().getColor(R.color.color_green));
                tvNewPrice.setTextColor(context.getResources().getColor(R.color.color_green));
            }else{
                tvFluctuation.setTextColor(context.getResources().getColor(R.color.color_font_red));
                tvNewPrice.setTextColor(context.getResources().getColor(R.color.color_font_red));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        return convertView;
    }
}
