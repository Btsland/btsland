package info.btsland.app.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.MarketFragment;

/**
 * 作者：谢建华
 * 创建时间：2017/09/27
 * 完成时间：
 */
public class MainActivity extends Activity implements MarketFragment.OnFragmentInteractionListener {
    private ImageView ivNavUser ;
    private TextView tvNavHome;
    private TextView tvNavMarket;
    private TextView tvNavPurse;
    private TextView tvNavSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //Setting
//        tvNavSet = (TextView)findViewById(R.id.tv_nav_set);
//        tvNavSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    /**
     * 初始化
     */
    protected void init(){
        //初始化
        ivNavUser=(ImageView)findViewById(R.id.iv_nav_user);
        tvNavHome=(TextView)findViewById(R.id.tv_nav_home);
        tvNavMarket=(TextView)findViewById(R.id.tv_nav_market);
        tvNavPurse=(TextView)findViewById(R.id.tv_nav_purse);
        //绑定监听器
        ivNavUser.setOnClickListener(new ivNavUserOnClick());
        tvNavHome.setOnClickListener(new NavOnClickListener());
        tvNavMarket.setOnClickListener(new NavOnClickListener());
        tvNavPurse.setOnClickListener(new NavOnClickListener());


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 底部导航栏操作效果
     */
    class NavOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_nav_home:
                    touchColor(tvNavHome,tvNavMarket,tvNavPurse);//控件特效

                    break;
                case R.id.tv_nav_market:
                    touchColor(tvNavMarket,tvNavHome,tvNavPurse);//控件特效

                    break;
                case R.id.tv_nav_purse:
                    touchColor(tvNavPurse,tvNavHome,tvNavMarket);//控件特效

                    break;
            }
        }

        /**
         * 导航栏交互特效
         * @param facingTextView 当前的控件
         * @param textView1
         * @param textView2
         */
        protected void touchColor(TextView facingTextView,TextView textView1,TextView textView2){
            facingTextView.setBackground(getDrawable(R.drawable.tv_border_touch));
            textView1.setBackground(getDrawable(R.drawable.tv_border));
            textView2.setBackground(getDrawable(R.drawable.tv_border));
            facingTextView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_yellow_red,null));
            textView1.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_black,null));
            textView2.setTextColor(ResourcesCompat.getColor(getResources(),R.color.color_black,null));
        }
    }


    /**
     * 单击事件功能实现
     */
    class ivNavUserOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            Intent intent=new Intent(MainActivity.this,UserActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

}
