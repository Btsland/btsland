package info.btsland.app.ui.activity;

/**
 * Created by zyf on 2017/10/8.
 * 显示新闻内容的活动，与activity_newscontent.xml相对应
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.model.News;
import info.btsland.app.ui.fragment.HeadFragment;
import info.btsland.app.ui.fragment.NewsContentFragment;

public class NewsContentActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private TextView textView;

    //actionStart()方法，将启动NewsActivity所需要的参数导入进来
    public static void actionStart(Context context, News news) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        //将参数新闻标题、新闻内容（参数）保存到intent中
        intent.putExtra("news", news);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fillInHead();//装载顶部导航栏
        init();//初始化
        showNews();//展示新闻
    }
    /**
     * 获取当前点击位置是否为et
     * @param view 焦点所在View
     * @param event 触摸事件
     * @return
     */
    public  boolean isClickEt(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            //此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 點擊EditText以外的區域后鍵盤隱藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEt(view, event)) {

                //如果不是edittext，则隐藏键盘

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    //隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        }
        /**
         * 看源码可知superDispatchTouchEvent  是个抽象方法，用于自定义的Window
         * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent event)传递到onTouchEvent(MotionEvent event)
         * 必不可少，否则所有组件都不能触发 onTouchEvent(MotionEvent event)
         */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    /**
     * 初始化
     */
    private void init() {
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            headFragment.setTitleName("资讯详情");
            transaction.add(R.id.fra_news_head, headFragment);
        }
        transaction.commit();
    }

    /**
     * 展示新闻详细信息
     */
    private void showNews() {
        //取出Intent中保存的参数(即为新闻的标题、新闻的内容)
        News news = (News) getIntent().getSerializableExtra("news");
        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        // 通过findFragment()方法，在活动中得到碎片NewsContentFragment的实例，从而调用其refresh()
        newsContentFragment.refresh(news);
    }
}

