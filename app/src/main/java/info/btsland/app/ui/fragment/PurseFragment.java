package info.btsland.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DetailedFragmentAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.exchange.utils.UserTypeCode;

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
    private static String TAG="PurseFragment";
    private ViewPager viewPager;
    private DetailedFragmentAdapter adapter;
    private PurseReceiver purseReceiver;
    public PurseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purseReceiver = new PurseReceiver() ;
        IntentFilter intentFilter =new IntentFilter(PurseReceiver.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(purseReceiver,intentFilter);
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
    private void init(View view) {
        viewPager=view.findViewById(R.id.vp_purse);
        List<Fragment> fragments=new ArrayList<>();
        UserManageFragment userManageFragment=new UserManageFragment();
        fragments.add(userManageFragment);
        adapter=new DetailedFragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
    }
    private void fillIn() {
        Log.e(TAG, "fillIn: " );
        List<Fragment> fragments=new ArrayList<>();
        UserManageFragment userManageFragment=new UserManageFragment();
        fragments.add(userManageFragment);
        if(BtslandApplication.dealer!=null){
            if(BtslandApplication.dealer.getType()== UserTypeCode.HELP){
                Log.e(TAG, "fillIn: help" );
                HelpManageFragment helpManageFragment=new HelpManageFragment();
                fragments.add(helpManageFragment);
            }else if(BtslandApplication.dealer.getType()== UserTypeCode.DEALER) {
                Log.e(TAG, "fillIn: dealer" );
                DealerManageFragment dealerManageFragment=new DealerManageFragment();
                fragments.add(dealerManageFragment);
            }else if(BtslandApplication.dealer.getType()== UserTypeCode.ADMIN){
                Log.e(TAG, "fillIn: admin" );
            }
        }

        adapter.setFragmentList(fragments);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(purseReceiver);
    }

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(PurseReceiver.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class PurseReceiver extends BroadcastReceiver{
        public static final String EVENT="PurseReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " );
            fillIn();
        }
    }

}