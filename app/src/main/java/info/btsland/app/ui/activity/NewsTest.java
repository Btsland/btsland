package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import info.btsland.app.R;

/**
 * author：
 * function：
 * 2017/12/8.
 */

public class NewsTest extends AppCompatActivity {


    private WebView wv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);
        wv = (WebView) findViewById(R.id.wv_news);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); //加此行即可出来网页

        // 加载需要显示的网页
        wv.loadUrl("http://www.fastchain.info/index.php");
        // 设置Web视图
        wv.setWebViewClient(new HelloWebViewClient());

        // 此方法可以处理webview 在加载时和加载完成时一些操作
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 这里是设置activity的标题， 也可以根据自己的需求做一些其他的操作
                    //title.setText("加载完成");
                } else {
                    //title.setText("加载中.......");
                }
            }
        });
    }
    public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
            wv.goBack();   //goBack()表示返回webView的上一页面
            return true;
        }
        return false;
    }
    // Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
