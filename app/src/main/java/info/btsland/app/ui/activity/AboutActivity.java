package info.btsland.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class AboutActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    private TextView  tvAboutBtsland;
//    private TextView  tvAboutIfast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        fillInHead();
        init();
        fillIn();
    }
    private void init(){
        tvAboutBtsland=findViewById(R.id.tv_about_btsland);
//        tvAboutIfast=findViewById(R.id.tv_about_ifast);

    }

    private void   fillIn(){
        tvAboutBtsland.setAutoLinkMask(Linkify.ALL);
        tvAboutBtsland.setMovementMethod(LinkMovementMethod.getInstance());

//        String html2="快子网https://www.ifast.pro";
//        tvAboutIfast.setText(html2);
//        tvAboutIfast.setAutoLinkMask(Linkify.ALL);
//        tvAboutIfast.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void onStart() {
        super.onStart();
        headFragment.setBackground(getDrawable(R.color.transparent));
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"关于我们");
            transaction.add(R.id.about_head,headFragment);
        }
        transaction.commit();
    }

}
