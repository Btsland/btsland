package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class PurseWalletBackupActivity extends AppCompatActivity{
    private HeadFragment headFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_backup);
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
            transaction.add(R.id.fra_backup_head, headFragment);
        }
        transaction.commit();
    }
}
