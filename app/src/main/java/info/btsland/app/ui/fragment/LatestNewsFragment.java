package info.btsland.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import info.btsland.app.Adapter.NewsAdapter;
import info.btsland.app.R;
import info.btsland.app.model.BitNew;
import info.btsland.app.model.News;
import info.btsland.app.ui.activity.DealerExchangeDetailedActivity;
import info.btsland.app.ui.activity.NewsContentActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.util.BaseThread;
import info.btsland.app.util.NewsHttp;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.http.NoteHttp;
import info.btsland.exchange.utils.GsonDateAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LatestNewsFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView newsTitleListView;
    private NewsAdapter adapter;
    private boolean isTwoPane;
    private  BitNew news;
    private BaseThread queryLatestNews;
    List<BitNew> newsList1=new ArrayList<>();
    private String TAG="LatestNewsFragment";


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        queryLatestNews=new QueryNews();
        queryLatestNews.setTime(600);
        queryLatestNews.start();
        //加载fragment_latest_news.xml 布局
        View view = inflater.inflate(R.layout.fragment_latest_news, container, false);
        //得到ListView的实例
        newsTitleListView = (ListView) view.findViewById(R.id.news_latest_view);
        Collections.reverse(newsList1);
        adapter = new NewsAdapter(getActivity(), newsList1);
        //启动ListView的适配器，这样ListView就能与适配器的数据相关联了
        newsTitleListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(BitNew bitnews) {
                //如果是单页模式（手机），就启动一个新的活动去显示新闻内容。
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("news", bitnews);
                getActivity().startActivity(intent);    //标记位置
            }
        });

//        //为ListView中的子项设置监听器
//        newsTitleListView.setOnItemClickListener(this);

        return view;
}



    private void fillInNews(String json){
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<BitNew>>() {}.getType();

        newsList1= gson.fromJson(json,type);
        Log.e(TAG, "fillInNews: "+newsList1.size() );
//        news= gson.fromJson(json,News.class);

        if(newsList1!=null){
            handler.sendEmptyMessage(1);
        }
    }


    private Handler handler;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    Collections.reverse(newsList1);
                    adapter.setNewsList(newsList1);
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BitNew news = newsList1.get(position);
        //如果是单页模式（手机），就启动一个新的活动去显示新闻内容。
        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        intent.putExtra("news", news);
        getActivity().startActivity(intent);    //标记位置
    }

    class QueryNews extends BaseThread {
        @Override
        public void execute() {
            NewsHttp.queryLatest(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json=response.body().string();
                    Log.e(TAG, "onResponse: "+json );
                    fillInNews(json);
                }
            });

        }

    }





}
