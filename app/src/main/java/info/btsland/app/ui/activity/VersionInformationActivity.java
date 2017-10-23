package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class VersionInformationActivity extends AppCompatActivity {

    private HeadFragment headFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_information);
        fillInHead();
        init();
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
            headFragment.setTitleName("版本信息");
            transaction.add(R.id.fra_version_head, headFragment);
        }
        transaction.commit();
    }
}
