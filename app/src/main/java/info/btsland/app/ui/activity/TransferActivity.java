package info.btsland.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;

public class TransferActivity extends AppCompatActivity {
    private EditText edFrom;
    private EditText edTo;
    private WebView wvFrom;
    private WebView wvTo;
    private Spinner spCoin;
    private EditText edCoinNum;
    private TextView tvRemarkText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        flinIn();
    }
    private void init(){
        edFrom=findViewById(R.id.ed_transfer_from);
        edTo=findViewById(R.id.ed_transfer_to);
        wvFrom=findViewById(R.id.wv_pho_from);
        wvTo=findViewById(R.id.wv_pho_to);
        spCoin=findViewById(R.id.sp_transfer_coin);
        edCoinNum=findViewById(R.id.ed_transfer_coinNum);
        tvRemarkText=findViewById(R.id.tv_transfer_remark_text);
    }
    private void flinIn(){
        createPortrait(wvFrom,BtslandApplication.accountObject.name);
        edFrom.setText(BtslandApplication.accountObject.name);
        edFrom.setEnabled(true);

    }
    /**
     * 设置头像
     */
    public void createPortrait(WebView webView,String name) {
        sha256_object.encoder encoder=new sha256_object.encoder();
        encoder.write(name.getBytes());
        String htmlShareAccountName="<html><head><style>body,html { margin:0; padding:0; text-align:center;}</style><meta name=viewport content=width=" + 100 + ",user-scalable=no/></head><body><canvas width=" + 100 + " height=" + 100 + " data-jdenticon-hash=" + encoder.result().toString() + "></canvas><script src=https://cdn.jsdelivr.net/jdenticon/1.3.2/jdenticon.min.js async></script></body></html>";
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(htmlShareAccountName, "text/html", "UTF-8");
    }
}
