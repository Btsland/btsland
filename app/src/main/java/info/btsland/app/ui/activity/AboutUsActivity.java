package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class AboutUsActivity extends BaseActivity {
    private TextView textView;

    private HeadFragment headFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_we);
        fillInHead();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        textView=findViewById(R.id.tv_about);
        String html="比特股大陆:\n";
        html+="https://www.btsland.info";
        textView.setText(html);
        textView.setAutoLinkMask(Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"");
            headFragment.setTitleName(getString(R.string.we));
            transaction.add(R.id.fra_about_head, headFragment);
        }
        transaction.commit();
    }


}
