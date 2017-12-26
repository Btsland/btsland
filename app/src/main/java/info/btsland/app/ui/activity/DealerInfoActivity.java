package info.btsland.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import info.btsland.app.R;
import info.btsland.app.ui.fragment.HeadFragment;

public class DealerInfoActivity extends AppCompatActivity {
    private HeadFragment headFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_info);
        fillInHead();
    }

    private void fillInHead(){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (headFragment==null){
            headFragment= HeadFragment.newInstance(HeadFragment.HeadType.BACK_NULL,"账户信息");
            transaction.add(R.id.fra_dealer_info_head,headFragment);
        }
        transaction.commit();
    }
}
