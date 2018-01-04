package info.btsland.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.exchange.entity.Chat;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ChatAdapter extends BaseAdapter {

    private static final String TAG = "ChatAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Chat> chats;
    private String from;
    private String to;
    private List<View> views=new ArrayList<>();

    public ChatAdapter(Context context,String from,String to) {
        this.context = context;
        this.from = from;
        this.to = to;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAsset(List<Chat> chats) {
        this.chats = chats;
    }

    public ChatAdapter setFrom(String from) {
        this.from = from;
        return this;
    }

    public ChatAdapter setTo(String to) {
        this.to = to;
        return this;
    }

    @Override
    public int getCount() {
        if(chats!=null){
            return chats.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if(chats==null){
            return null;
        }
        if(i>chats.size()){
            return null;
        }
        Chat chat = chats.get(i);
        if(chat==null){
            return null;
        }
        if(i<views.size()&&views.get(i)!=null){
            return views.get(i);
        }
        if(chat.getFromUser().equals(from)){
            convertView = inflater.inflate(R.layout.chat_right_item, null);
        }else if(chat.getFromUser().equals(to)){
            convertView = inflater.inflate(R.layout.chat_left_item, null);
        }
        WebView wbPho=convertView.findViewById(R.id.wb_chat_item_pho);
        TextView tvLevel=convertView.findViewById(R.id.tv_chat_item_level);
        TextView tvAccount=convertView.findViewById(R.id.tv_chat_item_account);
        TextView tvContext=convertView.findViewById(R.id.tv_chat_item_context);
        createPortrait(wbPho,chat.getFromUser());
        Double level=0.0;
//        if(from.getType()== UserTypeCode.DEALER&&from.userInfo != null&&from.userInfo.getLevel() != null){
//            level=from.userInfo.getLevel();
//        }else if(to.getType()== UserTypeCode.DEALER&&to.userInfo != null&&to.userInfo.getLevel() != null) {
//            level=from.userInfo.getLevel();
//        }
//        if(level>0.0){
//            tvLevel.setBackground(getLevelDrawable(level));
//        }else {
//            tvLevel.setVisibility(View.GONE);
//        }
        tvAccount.setText(chat.getFromUser());
        tvContext.setText(chat.getContext());
        views.add(convertView);
        return convertView;
    }

    private Drawable getLevelDrawable(Double level){
        Drawable drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level1);
        int a = (int) (level/20);
        switch (a) {
            case 0:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level1);
                break;
            case 1:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level2);
                break;
            case 2:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level3);
                break;
            case 3:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level4);
                break;
            case 4:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level5);
                break;
            case 5:
                drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level5);
                break;
        }
        return drawable;
    }

    /**
     * 设置头像
     */
    private void createPortrait(WebView webView,String name) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 40 + ",user-scalable=no/></head><body><canvas width=" + 40 + " height=" + 40 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }

}
