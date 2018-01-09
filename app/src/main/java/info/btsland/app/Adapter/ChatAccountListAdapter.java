package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.exchange.entity.User;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ChatAccountListAdapter extends BaseAdapter {

    private static final String TAG = "AssetRowAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<User> users=new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private List<View> views=new ArrayList<>();

    public void setOnItemnClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChatAccountListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if(users==null){
            return null;
        }
        View view=null;
        final User user=users.get(i);
        if(i<views.size()){
            view=views.get(i);
        }
        if(view!=null){
            TextView tvTime=view.findViewById(R.id.tv_chat_account_item_time);
            TextView tvChat=view.findViewById(R.id.tv_chat_account_item_chat);
            TextView tvPoint=view.findViewById(R.id.tv_chat_account_item_point);
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            if(user.chatDate==null){
                user.chatDate=new Date();
            }
            tvTime.setText(sdf.format(user.chatDate));
            tvChat.setText(user.chat.getContext());
            if(user.chatPoint==0){
                tvPoint.setVisibility(View.GONE);
            }else {
                tvPoint.setText("" + user.chatPoint);
                tvPoint.setVisibility(View.VISIBLE);
            }
            return view;
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_account_item, null);
        }
        TextView tvName=convertView.findViewById(R.id.tv_chat_account_item_name);
        TextView tvTime=convertView.findViewById(R.id.tv_chat_account_item_time);
        TextView tvChat=convertView.findViewById(R.id.tv_chat_account_item_chat);
        TextView tvPoint=convertView.findViewById(R.id.tv_chat_account_item_point);
        WebView webView=convertView.findViewById(R.id.wb_chat_account_item_pho);
        tvName.setText(user.getAccount());
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        if(user.chatDate==null){
            user.chatDate=new Date();
        }
        tvTime.setText(sdf.format(user.chatDate));
        tvChat.setText(user.chat.getContext());
        if(user.chatPoint==0){
            tvPoint.setVisibility(View.GONE);
        }else {
            tvPoint.setText("" + user.chatPoint);
            tvPoint.setVisibility(View.VISIBLE);
        }
        createPortrait(webView,user.getAccount());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(user);
            }
        });
        views.add(convertView);
        return convertView;
    }

    public void createPortrait(WebView webView,String account) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(account.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 60 + ",user-scalable=no/></head><body><canvas width=" + 60 + " height=" + 60 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}
