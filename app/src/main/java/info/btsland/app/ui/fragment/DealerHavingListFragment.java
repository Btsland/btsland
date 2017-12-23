package info.btsland.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import info.btsland.app.Adapter.DealerHavingNoteAdapter;
import info.btsland.app.Adapter.ExchangeNoteAdapter;
import info.btsland.app.R;
import info.btsland.app.ui.activity.DealerExchangeDetailedActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.http.TradeHttp;
import info.btsland.exchange.utils.NoteStatCode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DealerHavingListFragment extends Fragment {
    private ListView listView;

    private DealerHavingNoteAdapter adapter;

    public DealerHavingListFragment() {
        // Required empty public constructor
    }

    public static DealerHavingListFragment newInstance() {
        DealerHavingListFragment fragment = new DealerHavingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dealer_having_list, container, false);
        init(view);
        fillIn();
        return view;
    }
    private void init(View view) {
        listView=view.findViewById(R.id.lv_dealer_having_list);
    }
    private void fillIn() {
        adapter=new DealerHavingNoteAdapter(getActivity());
        adapter.setOnItemClickListener(new DealerHavingNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(getActivity(), DealerExchangeDetailedActivity.class);
                intent.putExtra("noteNo",note.getNoteNo());
                getActivity().startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }
    public void refurbish(List<Note> notes){
        adapter.setNotes(notes);
        handler.sendEmptyMessage(1);
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
