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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DealerInfoAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.DealerNoteListActivity;
import info.btsland.app.util.BaseThread;
import info.btsland.exchange.entity.User;

public class HelpManageFragment extends Fragment {
    private String TAG="HelpManageFragment";
    private ListView listView;
    private TextView tvBtn;
    private DealerInfoAdapter dealerInfoAdapter;
    private HelpManageDealerList helpManageDealerList;
    private BaseThread fillIn;
    public HelpManageFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static HelpManageFragment newInstance() {
        HelpManageFragment fragment = new HelpManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter =new IntentFilter(HelpManageDealerList.EVENT) ;
        helpManageDealerList=new HelpManageDealerList() ;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(helpManageDealerList,intentFilter);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_manage, container, false);
        dealerInfoAdapter=new DealerInfoAdapter(getActivity());
        init(view);
        fillIn=new FillIn();
        fillIn.start();
        return view;
    }
    private void init(View view) {
        listView=view.findViewById(R.id.lv_help_dealerList);
        listView.setAdapter(dealerInfoAdapter);
        dealerInfoAdapter.setOnItemClickListener(new DealerInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i, User user) {
                Intent intent=new Intent(getActivity(), DealerNoteListActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("dealerId",user.getDealerId());
                getActivity().startActivity(intent);
            }
        });
    }
    private List<User> users=new ArrayList<>();
    private void fillIn() {
        if (BtslandApplication.helpUserMap!=null) {
            users.clear();
            for (String name : BtslandApplication.helpUserMap.keySet()) {
                users.add(BtslandApplication.helpUserMap.get(name));
            }
        }
        dealerInfoAdapter.setDealers(users);
        handler.sendEmptyMessage(1);
    }
    class FillIn extends BaseThread{
        @Override
        public void execute() {
            Log.e(TAG, "execute: " );
            fillIn();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(helpManageDealerList);
        fillIn.kill();
    }
    public static void sendBroadcast(Context context){
        Intent intent=new Intent(HelpManageDealerList.EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    class HelpManageDealerList extends BroadcastReceiver{
        public static final String EVENT="HelpManageDealerList";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " );
            fillIn();
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dealerInfoAdapter.notifyDataSetChanged();
        }
    };
}
