package info.btsland.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset_object;
import info.btsland.app.api.price;
import info.btsland.app.api.sha256_object;
import info.btsland.app.model.Borrow;
import info.btsland.app.util.AssetUtil;
import info.btsland.exchange.entity.Chat;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import info.btsland.exchange.utils.UserTypeCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/16.
 */

public class BorrowAdapter extends BaseAdapter {

    private static final String TAG = "ChatAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Borrow> borrows;

    public BorrowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public BorrowAdapter setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
        return this;
    }

    @Override
    public int getCount() {
        if(borrows!=null){
            return borrows.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(borrows!=null){
            return borrows.get(i);
        }else {
            return 0;
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        public WebView webView;
        public TextView tvName;
        public TextView tvRatio;
        public TextView tvDebt;
        public TextView tvDebtNum;
        public TextView tvDebtCoin;
        public TextView tvCollateral;
        public TextView tvCollateralNum;
        public TextView tvCollateralCoin;
        public TextView tvPrice;
        public TextView tvPriceNum;
        public TextView tvPriceCoin;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Log.e(TAG, "getView: "+ i);
        if(borrows==null){
            return convertView;
        }
        Borrow borrow = borrows.get(i);
        if(borrow==null){
            return convertView;
        }
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.borrow_item, null);
            viewHolder=new ViewHolder();
            viewHolder.tvName=convertView.findViewById(R.id.tv_name);
            viewHolder.webView=convertView.findViewById(R.id.webView);
            viewHolder.tvRatio=convertView.findViewById(R.id.tv_ratio);
            viewHolder.tvDebt=convertView.findViewById(R.id.tv_debt);
            viewHolder.tvDebtNum=convertView.findViewById(R.id.tv_debt_num);
            viewHolder.tvDebtCoin=convertView.findViewById(R.id.tv_debt_coin);
            viewHolder.tvCollateral=convertView.findViewById(R.id.tv_collateral);
            viewHolder.tvCollateralNum=convertView.findViewById(R.id.tv_collateral_num);
            viewHolder.tvCollateralCoin=convertView.findViewById(R.id.tv_collateral_coin);
            viewHolder.tvPrice=convertView.findViewById(R.id.tv_price);
            viewHolder.tvPriceNum=convertView.findViewById(R.id.tv_price_num);
            viewHolder.tvPriceCoin=convertView.findViewById(R.id.tv_price_coin);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        createPortrait(viewHolder.webView,borrow.borrower.name);
        viewHolder.tvName.setText(borrow.borrower.name);
        viewHolder.tvDebtNum.setText(String.format("%.4f",borrow.debt));
        asset_object quote= AssetUtil.assetToAssetObject(borrow.call_price.quote.asset_id);
        viewHolder.tvDebtCoin.setText(quote.symbol);
        viewHolder.tvCollateralNum.setText(String.format("%.4f",borrow.collateral));
        asset_object base= AssetUtil.assetToAssetObject(borrow.call_price.base.asset_id);
        viewHolder.tvCollateralCoin.setText(base.symbol);
        viewHolder.tvPriceNum.setText(String.format("%.4f",borrow.price));
        viewHolder.tvPriceCoin.setText(quote.symbol+"/"+base.symbol);
        Double price = BtslandApplication.feedDoubleMap.get(quote.id);
        Double ratio = borrow.collateral/(borrow.debt/price);
        viewHolder.tvRatio.setText(String.format("%.0f",ratio*100)+"%");
        return convertView;
    }

    /**
     * 设置头像
     */
    private void createPortrait(WebView webView,String name) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 50 + ",user-scalable=no/></head><body><canvas width=" + 50 + " height=" + 50 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }

}
