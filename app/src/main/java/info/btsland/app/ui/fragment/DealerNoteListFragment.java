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

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.DealerNoteAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.DealerExchangeDetailedActivity;
import info.btsland.app.ui.activity.DealerNoteListActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.utils.UserTypeCode;


public class DealerNoteListFragment extends Fragment {
    private ListView listView;
    private int want=1;
    private int IN=1;
    private int OUT=2;
    private int type=1;
    private int HAVING=1;
    private int CLINCH=2;

    private String TAG="DealerNoteListFragment";
    private DealerNoteListReceiver dealerNoteListReceiver;

    private DealerNoteAdapter adapter;

    public DealerNoteListFragment() {
        // Required empty public constructor
    }

    public static DealerNoteListFragment newInstance(int want,int type) {
        DealerNoteListFragment fragment = new DealerNoteListFragment();
        Bundle args = new Bundle();
        args.putInt("want",want);
        args.putInt("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealerNoteListReceiver=new DealerNoteListReceiver();
        IntentFilter intentFilter =new IntentFilter(DealerNoteListReceiver.EVENT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(dealerNoteListReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dealer_having_list, container, false);
        if(savedInstanceState!=null){
            want=savedInstanceState.getInt("want");
            type=savedInstanceState.getInt("type");
        }
        if(getArguments()!=null){
            want=getArguments().getInt("want");
            type=getArguments().getInt("type");
        }
        init(view);
        fillIn();
        refurbish();
        return view;
    }
    private void init(View view) {
        listView=view.findViewById(R.id.lv_dealer_having_list);
    }
    private void fillIn() {
        adapter=new DealerNoteAdapter(getActivity());
        adapter.setOnItemClickListener(new DealerNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(getActivity(), DealerExchangeDetailedActivity.class);
                intent.putExtra("noteNo",note.getNoteNo());
                getActivity().startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }
    public static void sendBroadcast(Context context, int type){
        Intent intent=new Intent(DealerNoteListReceiver.EVENT);
        intent.putExtra("type",type);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class DealerNoteListReceiver extends BroadcastReceiver {
        public static final String EVENT="DealerNoteListReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            int newType=intent.getIntExtra("type",type);
            if(newType==type){
                refurbish();
            }
        }
    }
    public void refurbish(){
        String dealerId=((DealerNoteListActivity)getActivity()).dealerId;
        Log.e(TAG, "refurbish: dealerId:"+dealerId);
        if(want==IN){
            if(type==HAVING){
                List<Note> inHavingNotes=new ArrayList<>() ;
                switch (BtslandApplication.dealer.getType()){
                    case UserTypeCode.DEALER:
                        if(BtslandApplication.dealerHavingNotes!=null) {
                            for (int i = 0; i < BtslandApplication.dealerHavingNotes.size(); i++) {
                                if(BtslandApplication.dealerHavingNotes.get(i).getAssetCoin().equals("CNY")){
                                    inHavingNotes.add(BtslandApplication.dealerHavingNotes.get(i));
                                }
                            }
                        }
                        adapter.setNotes(inHavingNotes);
                        break;
                    case UserTypeCode.HELP:
                        Log.e(TAG, "refurbish: help" );
                        if(dealerId!=null&&!dealerId.equals("")){
                            if(BtslandApplication.helpUserMap!=null&&BtslandApplication.helpUserMap.get(dealerId).havingNotes!=null) {
                                for (int i = 0; i < BtslandApplication.helpUserMap.get(dealerId).havingNotes.size(); i++) {
                                    Log.e(TAG, "refurbish: i:"+i);
                                    if(BtslandApplication.helpUserMap.get(dealerId).havingNotes.get(i).getAssetCoin().equals("CNY")){
                                        inHavingNotes.add(BtslandApplication.helpUserMap.get(dealerId).havingNotes.get(i));
                                    }
                                }
                            }
                            adapter.setNotes(inHavingNotes);
                        }
                        break;
                    case UserTypeCode.ADMIN:

                        break;
                }
            }else if(type==CLINCH) {
                List<Note> inClinchNotes=new ArrayList<>() ;
                if(BtslandApplication.dealerClinchNotes!=null) {
                    for (int i = 0; i < BtslandApplication.dealerClinchNotes.size(); i++) {
                        if(BtslandApplication.dealerClinchNotes.get(i).getAssetCoin().equals("CNY")){
                            inClinchNotes.add(BtslandApplication.dealerClinchNotes.get(i));
                        }
                    }
                }
                adapter.setNotes(inClinchNotes);
            }

        }else if(want==OUT){
            if(type==HAVING){
                List<Note> inHavingNotes=new ArrayList<>() ;
                switch (BtslandApplication.dealer.getType()){
                    case UserTypeCode.DEALER:
                        if(BtslandApplication.dealerHavingNotes!=null) {
                            for (int i = 0; i < BtslandApplication.dealerHavingNotes.size(); i++) {
                                if(BtslandApplication.dealerHavingNotes.get(i).getAssetCoin().equals("RMB")){
                                    inHavingNotes.add(BtslandApplication.dealerHavingNotes.get(i));
                                }
                            }
                        }
                        adapter.setNotes(inHavingNotes);
                        break;
                    case UserTypeCode.HELP:
                        Log.e(TAG, "refurbish: help" );
                        if(dealerId!=null&&!dealerId.equals("")){
                            if(BtslandApplication.helpUserMap!=null&&BtslandApplication.helpUserMap.get(dealerId).havingNotes!=null) {
                                for (int i = 0; i < BtslandApplication.helpUserMap.get(dealerId).havingNotes.size(); i++) {
                                    if(BtslandApplication.helpUserMap.get(dealerId).havingNotes.get(i).getAssetCoin().equals("RMB")){
                                        inHavingNotes.add(BtslandApplication.helpUserMap.get(dealerId).havingNotes.get(i));
                                    }
                                }
                            }
                            adapter.setNotes(inHavingNotes);
                        }
                        break;
                    case UserTypeCode.ADMIN:

                        break;
                }
            }else if(type==CLINCH) {
                List<Note> outClinchNotes=new ArrayList<>() ;
                if(BtslandApplication.dealerClinchNotes!=null) {
                    for (int i = 0; i < BtslandApplication.dealerClinchNotes.size(); i++) {
                        if(BtslandApplication.dealerClinchNotes.get(i).getAssetCoin().equals("RMB")){
                            outClinchNotes.add(BtslandApplication.dealerClinchNotes.get(i));
                        }
                    }
                }
                adapter.setNotes(outClinchNotes);
            }
        }
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(dealerNoteListReceiver);
    }

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                adapter.notifyDataSetChanged();
            }else if(msg.what==2){
                AppDialog appDialog=new AppDialog(getActivity());
                appDialog.setMsg("更改状态成功");
                appDialog.show();
            }

        }
    };
}
