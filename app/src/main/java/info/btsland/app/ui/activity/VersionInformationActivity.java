package info.btsland.app.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class VersionInformationActivity extends BaseActivity {

    private HeadFragment headFragment;
    private TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edition);
        fillInHead();
        init();
        fillIn();
    }

    private void fillIn() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo(getPackageName(),0);
            String verName=info.versionName;
            tvVersionName.setText(verName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        tvVersionName=findViewById(R.id.tv_about_verName);
    }

    /**
     * 装载顶部导航
     */
    private void fillInHead() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment == null) {
            headFragment = HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,getString(R.string.edition));
            transaction.add(R.id.fra_version_head, headFragment);
        }
        transaction.commit();
    }
}
