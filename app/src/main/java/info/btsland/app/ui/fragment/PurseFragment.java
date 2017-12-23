package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.sha256_object;
import info.btsland.app.ui.activity.LoginActivity;
import info.btsland.app.ui.activity.LookActivity;
import info.btsland.app.ui.activity.MarketDetailedActivity;
import info.btsland.app.ui.activity.PurseAccessRecordActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.PurseTradingRecordActivity;
import info.btsland.app.ui.activity.PurseWalletBackupActivity;
import info.btsland.app.ui.activity.TransferActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.app.ui.view.MyConstraintLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * 行情
 */
public class PurseFragment extends Fragment {
    private ViewPager viewPager;
    public PurseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_purse, container, false);
        init(view);
        fillIn();
        return view;
    }

    private void fillIn() {
        List<Fragment> fragments=new ArrayList<Fragment>();
        UserManageFragment userManageFragment=new UserManageFragment();
        DealerManageFragment dealerManageFragment=new DealerManageFragment();
        HelpManageFragment helpManageFragment=new HelpManageFragment();
        fragments.add(userManageFragment);
        fragments.add(dealerManageFragment);
        fragments.add(helpManageFragment);
        DetailedFragmentAdapter adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
    }

    private void init(View view) {
        viewPager=view.findViewById(R.id.vp_purse);

    }
}