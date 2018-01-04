package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Chat> chatList=new ArrayList<>();
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
        from=BtslandApplication.accountObject.name;
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        gson=gsonBuilder.create();
        fillInHead();
        init();
        fillIn();
    }

    private void init() {
        listView=findViewById(R.id.lv_chat_list);
        edText=findViewById(R.id.ed_chat_text);
        tvBtn=findViewById(R.id.tv_chat_btn);
    }
    private void fillIn(){
        chatAdapter=new ChatAdapter(ChatActivity.this,from,account);
        chatAdapter.setAsset(chatList);
        listView.setAdapter(chatAdapter);
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
                Log.e(TAG, "onClick: "+message );
                if(BtslandApplication.chatWebScoket==null||BtslandApplication.chatWebScoket.mnConnectStatus!=ChatWebScoket.SUCCESS){
                    BtslandApplication.chatWebScoket= ChatWebScoket.createWebScoket(BtslandApplication.accountObject.name);
                    BtslandApplication.chatWebScoket.connect();
                }
                if(BtslandApplication.chatWebScoket.mnConnectStatus==ChatWebScoket.SUCCESS) {
                    BtslandApplication.chatWebScoket.setChatOnMessageListener(new IChatListener());
                    BtslandApplication.chatWebScoket.sendMsg(message);
                }
            }
        });
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
    class IChatListener implements ChatWebScoket.ChatOnMessageListener {
        @Override
        public void onMessage(String message) {
            ProcessingMessages messages=new ProcessingMessages(message);
            messages.start();
        }
    }
    class ProcessingMessages extends Thread{
        private String message;

        public ProcessingMessages(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            Chat chat=gson.fromJson(message,Chat.class);
            if(chat!=null){
                chatList.add(chat);
                handler.sendEmptyMessage(1);
            }
        }
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: "+chatList.size() );
            chatAdapter.notifyDataSetChanged();
            listView.setSelection(listView.getBottom());
            edText.getEditableText().clear();
        }
    };

    class ChatReceiver extends BroadcastReceiver {
        public static final String EVENT="Chat";
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
