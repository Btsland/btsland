package info.btsland.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import info.btsland.app.Adapter.NewsAdapter;
import info.btsland.app.R;
import info.btsland.app.model.BitNew;
import info.btsland.app.model.News;
import info.btsland.app.ui.activity.CourseActivity;
import info.btsland.app.ui.activity.NewsContentActivity;
import info.btsland.app.util.BaseThread;
import info.btsland.app.util.NewsHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "HotNewsFragment";
    private ListView newsTitleListView1;
    private List<News> newsList;
    private NewsAdapter adapter;
    private boolean isTwoPane;
    private BaseThread QueryHotNews;
    List<BitNew> newsList1=new ArrayList<>();

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        //初始化新闻数据
//        newsList = getNews();
////        adapter = new NewsAdapter(activity, R.layout.activity_newsitem, newsList);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //加载fragment_newstitle.xml 布局
        View view = inflater.inflate(R.layout.fragment_hot_news, container, false);
        //得到ListView的实例
        newsTitleListView1 = (ListView) view.findViewById(R.id.news_hot_view);
        Collections.reverse(newsList1);

        adapter = new NewsAdapter(getActivity(), newsList1);
        //启动ListView的适配器，这样ListView就能与适配器的数据相关联了
        newsTitleListView1.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(BitNew bitnews) {
                //如果是单页模式（手机），就启动一个新的活动去显示新闻内容。
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                getActivity().startActivity(intent);    //标记位置
            }
        });
        //为ListView中的子项设置监听器
        newsTitleListView1.setOnItemClickListener(this);
        return view;
    }

    public List<BitNew> getNewsList1() {
        BitNew bitNew=new BitNew();
        bitNew.setTitle("Btsland使用教程");
        bitNew.setAuthor("BTSLAND");
        Date date=new Date();
        bitNew.setTime(date);
        bitNew.setIcon("logo.png");
        newsList1.add(bitNew);
        return newsList1;
    }

    @Override
    //ListView子项目的点击事件
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //如果是单页模式（手机），就启动一个新的活动去显示新闻内容。
        Intent intent1 = new Intent(getActivity(), CourseActivity.class);
        getActivity().startActivity(intent1);
    }
//
//    private void fillInNews(String json){
//        GsonBuilder gsonBuilder=new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
//        Gson gson = gsonBuilder.create();
//        Type type = new TypeToken<ArrayList<BitNew>>() {}.getType();
//
//        newsList1= gson.fromJson(json,type);
//        Log.e(TAG, "fillInNews: "+newsList1.size() );
////        news= gson.fromJson(json,News.class);
//
//        if(newsList1!=null){
//            handler.sendEmptyMessage(1);
//        }
//    }


//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            if(msg.what==1){
//                Collections.reverse(newsList1);
//                adapter.setNewsList(newsList1);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };


//    class QueryHotNews extends BaseThread {
//        @Override
//        public void execute() {
//            NewsHttp.queryHot(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String json=response.body().string();
//                    Log.e(TAG, "onResponse: "+json );
//                    fillInNews(json);
//                }
//            });
//
//        }
//
//    }


}

