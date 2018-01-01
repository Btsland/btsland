package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


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
