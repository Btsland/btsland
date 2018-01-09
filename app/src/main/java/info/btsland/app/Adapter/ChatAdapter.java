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
import info.btsland.app.api.sha256_object;
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

public class ChatAdapter extends BaseAdapter {

    private static final String TAG = "ChatAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Chat> chats=new ArrayList<>();
    private String from;
    private String to;
    private User fromUser=new User();
    private User toUser=new User();
    private View leftView;
    private View rightView;

    public ChatAdapter(Context context,String from,String to) {
        this.context = context;
        this.from = from;
        this.to = to;
        this.inflater = LayoutInflater.from(context);

        new QueryUserThread(to, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                if(json.indexOf("error")!=-1){

                }else {
                    GsonBuilder gsonBuilder=new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                    Gson gson=gsonBuilder.create();
                    toUser=gson.fromJson(json,User.class);
                    if(toUser!=null){
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }).start();
        new QueryUserThread(from, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                if(json.indexOf("error")!=-1){

                }else {
                    GsonBuilder gsonBuilder=new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                    Gson gson=gsonBuilder.create();
                    fromUser=gson.fromJson(json,User.class);
                    if(fromUser!=null){
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }).start();
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
    public void addChat(Chat chat){
        this.chats.add(chat);
    }

    public ChatAdapter setFrom(String from) {
        this.from = from;
        return this;
    }

    public ChatAdapter setTo(String to) {
        this.to = to;
        return this;
    }

    class QueryUserThread extends Thread{
        private String account;
        private Callback callback;

        public QueryUserThread(String account,Callback callback) {
            this.account = account;
            this.callback=callback;
        }

        @Override
        public synchronized void run() {
            UserHttp.queryAccount(account, callback);
        }
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

    private class ViewHolder{
        public ConstraintLayout leftLayout;
        public TextView tvLeftName;
        public WebView wbLeftPho;
        public TextView tvLeftLevel;
        public TextView tvLeftTime;
        public TextView tvLeftContext;

        public ConstraintLayout rightLayout;
        public TextView tvRightName;
        public WebView wbRightPho;
        public TextView tvRightLevel;
        public TextView tvRightTime;
        public TextView tvRightContext;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Log.e(TAG, "getView: "+ i);
        if(chats==null){
            return convertView;
        }
        if(i>chats.size()){
            return convertView;
        }
        Chat chat = chats.get(i);
        if(chat==null){
            return convertView;
        }
        ViewHolder viewHolder=null;
        if(convertView==null){
            Log.e(TAG, "getView: 建造convertView" );
            convertView = inflater.inflate(R.layout.chat_list_item, null);
            viewHolder=new ViewHolder();
            viewHolder.rightLayout=convertView.findViewById(R.id.cl_chat_item_right);
            viewHolder.tvRightContext=convertView.findViewById(R.id.tv_chat_item_context_right);
            viewHolder.tvRightLevel=convertView.findViewById(R.id.tv_chat_item_level_right);
            viewHolder.tvRightName=convertView.findViewById(R.id.tv_chat_item_account_right);
            viewHolder.tvRightTime=convertView.findViewById(R.id.tv_chat_item_time_right);
            viewHolder.wbRightPho=convertView.findViewById(R.id.wb_chat_item_pho_right);

            viewHolder.leftLayout=convertView.findViewById(R.id.cl_chat_item_left);
            viewHolder.tvLeftContext=convertView.findViewById(R.id.tv_chat_item_context_left);
            viewHolder.tvLeftLevel=convertView.findViewById(R.id.tv_chat_item_level_left);
            viewHolder.tvLeftName=convertView.findViewById(R.id.tv_chat_item_account_left);
            viewHolder.tvLeftTime=convertView.findViewById(R.id.tv_chat_item_time_left);
            viewHolder.wbLeftPho=convertView.findViewById(R.id.wb_chat_item_pho_left);

            //右
            viewHolder.tvRightName.setText(from);
            createPortrait(viewHolder.wbRightPho,from);
            //左
            viewHolder.tvLeftName.setText(to);
            createPortrait(viewHolder.wbLeftPho,to);
            convertView.setTag(viewHolder);
        }else {
            Log.e(TAG, "getView: 复用convertView" );
            viewHolder= (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd HH:mm");
        if(chat.getFromUser()!=null&&chat.getFromUser().equals(from)){
            Log.e(TAG, String.valueOf("getView: "+(viewHolder==null)));
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.tvRightContext.setText(chat.getContext()==null?"":chat.getContext());
            viewHolder.tvRightTime.setText(chat.getContext()==null?"":simpleDateFormat.format(chat.getTime()));
        }else if(chat.getFromUser()!=null&&chat.getFromUser().equals(to)) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.tvLeftContext.setText(chat.getContext()==null?"":chat.getContext());
            viewHolder.tvLeftTime.setText(chat.getContext()==null?"":simpleDateFormat.format(chat.getTime()));

        }
        if(fromUser.getType()!=null){
            if(fromUser.getType()==UserTypeCode.DEALER){
                viewHolder.tvRightLevel.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvRightLevel.setVisibility(View.GONE);
            }
        }
        viewHolder.tvRightLevel.setBackground(fromUser.userInfo==null||fromUser.userInfo.getLevel()==null||fromUser.userInfo.getLevel()==0.0? getLevelDrawable(-1.0) : getLevelDrawable(fromUser.userInfo.getLevel()));
        if(toUser.getType()!=null){
            if(toUser.getType()==UserTypeCode.DEALER){
                viewHolder.tvLeftLevel.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvLeftLevel.setVisibility(View.GONE);
            }
        }
        viewHolder.tvLeftLevel.setBackground(toUser.userInfo==null||toUser.userInfo.getLevel()==null||toUser.userInfo.getLevel()==0.0? getLevelDrawable(-1.0) : getLevelDrawable(toUser.userInfo.getLevel()));

        return convertView;
    }

    private Drawable getLevelDrawable(Double level){
        Drawable drawable= BtslandApplication.getInstance().getDrawable(R.mipmap.level1);
        if(level==-1.0){
            drawable=BtslandApplication.getInstance().getDrawable(R.color.transparent);
            return drawable;
        }
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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };
}
