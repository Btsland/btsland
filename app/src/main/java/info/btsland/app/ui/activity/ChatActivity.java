package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import info.btsland.app.Adapter.ChatAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.exchange.entity.Chat;
import info.btsland.exchange.scoket.ChatWebScoket;
import info.btsland.exchange.utils.GsonDateAdapter;

public class ChatActivity extends AppCompatActivity {

    private HeadFragment headFragment;
    private String account;
    private ListView listView;
    private EditText edText;
    private TextView tvBtn;
    private ChatAdapter chatAdapter;
    private ChatReceiver chatReceiver;
    private String TAG="ChatActivity";
    private Gson gson;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatReceiver=new ChatReceiver();
        IntentFilter intentFilter=new IntentFilter(ChatReceiver.EVENT);
        LocalBroadcastManager.getInstance(ChatActivity.this).registerReceiver(chatReceiver,intentFilter);
        if(savedInstanceState!=null){
            account = savedInstanceState.getString("account");
        }
        if(getIntent()!=null){
            account = getIntent().getStringExtra("account");
        }
        from=BtslandApplication.account;
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        gson=gsonBuilder.create();
        fillInHead();
        init();
        if(BtslandApplication.chatWebScoket==null||BtslandApplication.chatWebScoket.mnConnectStatus!=ChatWebScoket.SUCCESS){
            BtslandApplication.chatWebScoket= ChatWebScoket.createWebScoket(BtslandApplication.accountObject.name);
            BtslandApplication.chatWebScoket.connect();
        }
        fillIn();
    }

    private void init() {
        listView=findViewById(R.id.lv_chat_list);
        edText=findViewById(R.id.ed_chat_text);
        tvBtn=findViewById(R.id.tv_chat_btn);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context=edText.getText().toString();
                Chat chat=new Chat();
                chat.setFromUser(BtslandApplication.accountObject.name);
                chat.setToUser(account);
                chat.setContext(context);
                GsonBuilder gsonBuilder=new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
                Gson gson=gsonBuilder.create();
                String message=gson.toJson(chat);
                new BtslandApplication.ReceiveChatThread(message).start();
                Log.e(TAG, "onClick: "+message );
                if(BtslandApplication.chatWebScoket==null||BtslandApplication.chatWebScoket.mnConnectStatus!=ChatWebScoket.SUCCESS){
                    BtslandApplication.chatWebScoket= ChatWebScoket.createWebScoket(BtslandApplication.accountObject.name);
                    BtslandApplication.chatWebScoket.connect();
                }
                BtslandApplication.chatWebScoket.sendMsg(message);
            }
        });
        chatAdapter=new ChatAdapter(ChatActivity.this,from,account);
        listView.setAdapter(chatAdapter);
    }
    private void fillIn(){
        chatAdapter.setChats(BtslandApplication.chatListMap.get(account));
        if(BtslandApplication.chatUsers.get(account)!=null){
            BtslandApplication.chatUsers.get(account).chatPoint=0;
        }
        ChatAccountListActivity.sendBroadcast(ChatActivity.this);
        chatAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();
        headFragment.setTitleName(account);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ChatActivity.this).unregisterReceiver(chatReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("account",account);
        super.onSaveInstanceState(outState);
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"");
            transaction.add(R.id.fra_chat_head,headFragment);
        }
        transaction.commit();
    }

    public static void sendBroadcast(Context context){
        Intent intent=new Intent(ChatReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class ChatReceiver extends BroadcastReceiver {
        public static final String EVENT="ChatReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            fillIn();
        }
    }
}
