package info.btsland.app.ui.activity;

import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import info.btsland.app.Adapter.MainAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class RingActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private MainAdapter mAdapter;
    ListView listView;
    Button sureBtn;
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        if(savedInstanceState!=null){
            type = savedInstanceState.getInt("type");
        }
        if(getIntent()!=null){
            type = getIntent().getIntExtra("type", type);
        }
        fillInHead();
		/*初始化SharedPreferences*/

		/*初始化listView*/
        listView = findViewById(R.id.ring_lv);
        mAdapter = new MainAdapter(this, BtslandApplication.sharedPreferences.getInt("ring", 0));
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(mOnItemClickListener);

		/*初始化返回按钮和保存按钮*/
        Button backBtn = (Button) findViewById(R.id.back_btn);
        sureBtn = (Button) findViewById(R.id.sure_btn);
        backBtn.setOnClickListener(mOnClickListener);
        sureBtn.setOnClickListener(mOnClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(type==1) {
            headFragment.setTitleName("聊天铃声");
        }else if(type==2){
            headFragment.setTitleName("承兑订单铃声");
        }
    }

    /*listView的按钮点击事件*/
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            MainAdapter.ViewHolder mHolder = new MainAdapter.ViewHolder(parent);
			/*设置Imageview不可被点击*/
            mHolder.iv.setClickable(false);
			/*清空map*/
            mAdapter.map.clear();
            // mAdapter.map.put(position, 1);
			/*将所点击的位置记录在map中*/
            mAdapter.map.put(position, true);
			/*刷新数据*/
            mAdapter.notifyDataSetChanged();

			/*position为0是跟随系统，先得到系统所使用的铃声，然后播放*/
            if (position - 1 == -1) {

                Uri uri = RingtoneManager.getActualDefaultRingtoneUri(
                        RingActivity.this, RingtoneManager.TYPE_NOTIFICATION);
                RingtoneManager.getRingtone(RingActivity.this, uri).play();
            }else {
                /*判断位置不为0则播放的条目为position-1*/
                try {

                    RingtoneManager rm = new RingtoneManager(RingActivity.this);
                    rm.setType(RingtoneManager.TYPE_NOTIFICATION);
                    rm.getCursor();
                    rm.getRingtone(position - 1).play();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    };

    /*按钮点击事件*/
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
			/*返回按钮时退出demo*/
                case R.id.back_btn:
                    finish();
                    break;
			/*保存按钮则保存SharedPreferences中的数据*/
                case R.id.sure_btn:
                    if(type==1){
                        BtslandApplication.editor.putInt("chatRing", listView.getCheckedItemPosition()-1).commit();
                    }else if(type==2){
                        BtslandApplication.editor.putInt("noteRing", listView.getCheckedItemPosition()-1).commit();
                    }
                    Toast.makeText(RingActivity.this, "提示音保存成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"设置通知铃声");
            transaction.add(R.id.fra_ring_head,headFragment);
        }
        transaction.commit();
    }
}
