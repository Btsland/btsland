package info.btsland.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.btsland.app.Adapter.ChatAccountListAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.CreateChatDialog;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChatAccountListActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private ListView listView;
    private TextView tvBtn;
    private ChatAccountListAdapter listAdapter;
    private ChatAccountReceiver chatAccountReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_account_list);
        chatAccountReceiver=new ChatAccountReceiver();
        IntentFilter intentFilter=new IntentFilter(ChatAccountReceiver.EVENT);
        LocalBroadcastManager.getInstance(ChatAccountListActivity.this).registerReceiver(chatAccountReceiver,intentFilter);
        listAdapter=new ChatAccountListAdapter(ChatAccountListActivity.this);

        listAdapter.setUsers(toList(BtslandApplication.chatUsers));
        fillInHead();
        init();
        fillIn();
    }
    private List<User> toList(Map<String,User> stringUserMap){
        List<User> users=new ArrayList<>();
        for(String name : stringUserMap.keySet()){
            users.add(stringUserMap.get(name));
        }
        return users;
    }
    private void init(){
        listView=findViewById(R.id.lv_chat_account_list);
        listView.setAdapter(listAdapter);
        tvBtn=findViewById(R.id.tv_chat_account_btn);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateChatDialog createChatDialog=new CreateChatDialog(ChatAccountListActivity.this);
                createChatDialog.setListener(new CreateChatDialog.OnDialogInterationListener() {
                    @Override
                    public void onConfirm(AlertDialog dialog, final String account) {
                        UserHttp.queryAccount(account, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                int a=json.indexOf("error");
                                if(a!=-1){
//                                    Toast.makeText(ChatAccountListActivity.this,json,Toast.LENGTH_LONG).show();
                                }else if(json==null||json.equals("")||json.equals("null")) {
                                    AppDialog appDialog=new AppDialog(ChatAccountListActivity.this);
                                    appDialog.setMsg("该用户未使用过承兑系统，承兑系统未记录该用户的信息，无法创建聊天");
                                    appDialog.show();
                                }else {
                                    Intent intent=new Intent(ChatAccountListActivity.this,ChatActivity.class);
                                    intent.putExtra("account",account);
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @Override
                    public void onReject(AlertDialog dialog) {

                    }
                });
                createChatDialog.show();
            }
        });
        listAdapter.setOnItemnClickListener(new ChatAccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent=new Intent(ChatAccountListActivity.this,ChatActivity.class);
                intent.putExtra("account",user.getAccount());
                startActivity(intent);
            }
        });
    }

    private void fillIn(){

    }

    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"即时聊天");
            transaction.add(R.id.fra_chat_head,headFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ChatAccountListActivity.this).unregisterReceiver(chatAccountReceiver);
    }

    public static void sendBroadcast(Context context){
        Intent intent=new Intent(ChatAccountReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    class ChatAccountReceiver extends BroadcastReceiver {
        public static final String EVENT="ChatAccountReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(1);
        }
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(listAdapter==null){
                listAdapter=new ChatAccountListAdapter(ChatAccountListActivity.this);
                listView.setAdapter(listAdapter);
            }
            listAdapter.setUsers(toList(BtslandApplication.chatUsers));
            listAdapter.notifyDataSetChanged();
        }
    };
}
