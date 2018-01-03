package info.btsland.app.Adapter;

/**
 * Created by zyf on 2017/10/8.
 * 创建新闻列表的适配器,因为数据不能直接在ListView中显示，需要使用到适配器传给LiseView,
 * 在 getView()方法中,获取到了相应位置上的 newsList 类,并让新闻的标题在列表中进行显示
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.BitNew;
import info.btsland.app.model.News;

public class NewsAdapter extends BaseAdapter {

    private List<BitNew> newsList=new ArrayList<>();
    private LayoutInflater inflater;
    private List<View>  viewList= new ArrayList<>();
    private String baseUrl="http://123.1.154.214:8880/upload/";
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(NewsAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NewsAdapter() {
    }

    public NewsAdapter(Context context, List<BitNew> objects) {
        this.newsList=objects;
        this.inflater=LayoutInflater.from(context);
    }
    public NewsAdapter(Context context) {
        this.inflater=LayoutInflater.from(context);
    }
    public void setNewsList(List<BitNew> newsList) {
        this.newsList = newsList;
        viewList.clear();
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    //重写父类ArrayAdapter的getView方法
    public View getView(int position, View view, ViewGroup parent) {
        if (position<viewList.size()){
            if(viewList.get(position)!=null){
                return viewList.get(position);
            }
        }


            view=inflater.inflate(R.layout.activity_newsitem, null);


        if(newsList==null||newsList.get(position)==null){
            return null;
        }
         final BitNew news = newsList.get(position);
        //标题
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        //内容
        TextView newsTitleContentText = (TextView) view.findViewById(R.id.news_titlecontent);
        //时间
        TextView newsDateText = (TextView) view.findViewById(R.id.news_date);
        //作者
        TextView newsAuthorText = (TextView) view.findViewById(R.id.news_author);
        //图片
        WebView webView = (WebView) view.findViewById(R.id.news_images);
        //让新闻的标题在列表中进行显示
            newsTitleText.setText(news.getTitle());
        //让新闻的内容在列表中进行显示
        newsTitleContentText.setText(news.getContent());
        //让日期在列表中进行显示
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd/hh:mm");
        newsDateText.setText(sdf.format(news.getTime()));
        //让新闻的作者在列表中进行显示
        newsAuthorText.setText(news.getAuthor());
        //让新闻的图片在列表中进行显示
        if (news.getIcon()!=null
                ){
            webView.loadUrl(baseUrl + news.getIcon());
        }else {
            webView.loadUrl(baseUrl + "logo.png");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(news);
                }
            }
        });
        viewList.add(view);
        return view;
    }

    public  interface  onItemClickListener{
       void onItemClick( BitNew bitnews);
    }

}

