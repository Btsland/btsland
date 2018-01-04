package info.btsland.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.CreateChatDialog;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChatAccountListActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private ListView listView;
    private TextView tvBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_account_list);
        fillInHead();
        init();
        fillIn();
    }

    private void init(){
        listView=findViewById(R.id.lv_chat_account_list);
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
}
