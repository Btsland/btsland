package info.btsland.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.btsland.app.R;
import info.btsland.exchange.entity.User;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DealerInfoAdapter extends BaseAdapter {

    private static final String TAG = "DealerInfoAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<User> dealers=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DealerInfoAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDealers(List<User> dealers) {
        this.dealers = dealers;
    }

    @Override
    public int getCount() {
        return dealers.size();
    }

    @Override
    public Object getItem(int i) {
        if(i<dealers.size()) {
            return dealers.get(i);
        }else {
            return null;
        }
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dealer_info_item, null);
        }
        if (dealers == null) {
            return null;
        }
        final User user = dealers.get(i);
        TextView tvName=convertView.findViewById(R.id.tv_dealer_info_item_name);
        TextView tvHint=convertView.findViewById(R.id.tv_dealer_info_item_hint);
        TextView tvNum=convertView.findViewById(R.id.tv_dealer_info_item_num);
        TextView tvTime=convertView.findViewById(R.id.tv_dealer_info_item_time);
        tvName.setText(user.getDealerId()+"("+user.getDealerName()+")");
        Date havingTime=null;
        Date chatTime=null;
        tvHint.setVisibility(View.INVISIBLE);
        tvNum.setVisibility(View.INVISIBLE);
        tvTime.setVisibility(View.INVISIBLE);
        if(user.havingNotes!=null&&user.helpNewChatList!=null){
            Log.e(TAG, "getView: "+user.havingNotes.size()+"////"+ user.helpNewChatList.size());
            tvNum.setText(""+(user.havingNotes.size()+user.helpNewChatList.size()));
            tvNum.setVisibility(View.VISIBLE);
        }else if(user.havingNotes!=null&&user.helpNewChatList==null) {
            tvNum.setText(""+(user.havingNotes.size()));
            tvNum.setVisibility(View.VISIBLE);
        }else if(user.helpNewChatList!=null&&user.havingNotes==null) {
            tvNum.setText(""+(user.helpNewChatList.size()));
            tvNum.setVisibility(View.VISIBLE);
        }
        if(user.havingNotes!=null){
            int a = user.havingNotes.size()-1;
            if(a>=0&&user.havingNotes.size()>0) {
                havingTime = user.havingNotes.get(a).getDealerReTime();
            }
        }
        if(user.helpNewChatList!=null&&user.helpNewChatList.size()>0){
            int a=user.helpNewChatList.size()-1;
            if(a>=0&&user.helpNewChatList.size()>0) {
                chatTime = user.helpNewChatList.get(a).getTime();
            }
        }
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("hh:mm:ss");
        if(havingTime!=null&&chatTime!=null){
            if(havingTime.getTime()<chatTime.getTime()){
                tvHint.setText("有新消息");
                tvHint.setVisibility(View.VISIBLE);
                tvTime.setText(simpleDateFormat.format(chatTime));
                tvTime.setVisibility(View.VISIBLE);
            }else{
                tvHint.setText("有新订单");
                tvHint.setVisibility(View.VISIBLE);
                tvTime.setText(simpleDateFormat.format(havingTime));
                tvTime.setVisibility(View.VISIBLE);
            }
        }else if(havingTime!=null&&chatTime==null) {
            tvHint.setText("有新订单");
            tvHint.setVisibility(View.VISIBLE);
            tvTime.setText(simpleDateFormat.format(havingTime));
            tvTime.setVisibility(View.VISIBLE);
        } else if(havingTime==null&&chatTime!=null) {
            tvHint.setText("有新消息");
            tvHint.setVisibility(View.VISIBLE);
            tvTime.setText(simpleDateFormat.format(chatTime));
            tvTime.setVisibility(View.VISIBLE);
        }else {
            tvHint.setVisibility(View.INVISIBLE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(i,user);
            }
        });
        return convertView;
    }
    public interface OnItemClickListener {
        void onItemClick(int i,User user);
    }
}
