package info.btsland.app.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;


public class SettingActivity extends Activity {
    private HeadFragment headFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(info.btsland.app.R.layout.activity_setting);
        Log.i("SettingActivity", "onCreate: ");
        fillInHead();
        init();
    }

    /**
     * 初始化
     */
    private void init(){

    }
    /**
     * 装载顶部导航
     */
    private void fillInHead(){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment=new HeadFragment();
            headFragment.setType(HeadFragment.HeadType.BACK_NULL);
            transaction.add(R.id.fra_set_head,headFragment);
        }
        transaction.commit();
    }
}
