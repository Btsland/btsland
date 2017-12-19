package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.btsland.app.Adapter.DealerListAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.C2CExchangeActivity;
import info.btsland.app.ui.view.AppDialog;
import info.btsland.exchange.entity.User;
import info.btsland.exchange.http.UserHttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DealerListFragment extends Fragment {
    private static String TAG="DealerListFragment";

    private int type=1;

    public static final int IN=1;//进（充值）
    public static final int OUT=2;//出（提现）
    private ListView listView;
    private DealerListAdapter dealerListAdapter;
    private List<DealerListAdapter.DealerData> dataList;

    public DealerListFragment() {
    }
    public static DealerListFragment newInstance(int type) {
        DealerListFragment fragment = new DealerListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dealer_list, container, false);
        this.type=getArguments().getInt("type");
        init(view);
        fillIn();
        return view;
    }

    private void init(View view){
        listView=view.findViewById(R.id.lv_c2c_list);
    }

    private void fillIn() {
        dataList=new ArrayList<>();
        dealerListAdapter=new DealerListAdapter(getActivity());
        dealerListAdapter.setType(type);
        listView.setAdapter(dealerListAdapter);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                queryAllUser();
            }
        },0,10000);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DealerListAdapter.DealerData dealerData = dataList.get(i);
                toExchange(dealerData.user);
            }
        });

    }
    private void toExchange(User user){
        if(BtslandApplication.accountObject==null){
            AppDialog appDialog=new AppDialog(getActivity());
            appDialog.setMsg("您未登录，请先登录！");
            appDialog.show();
        }else {
            C2CExchangeActivity.startAction(getActivity(), BtslandApplication.accountObject.name, type, user.getDealerId());
        }
    }

    private void queryAllUser(){
        UserHttp.queryAllDealer(0, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(str!=null&&!str.equals("")){
                    fillInUser(str);
                }
            }
        });
    }

    private void fillInUser(String json){
        Gson gson=new Gson();
        Log.e(TAG, "fillInList: json"+json );
        List<User> users = gson.fromJson(json,new TypeToken<List<User>>() {}.getType());
        for(int i=0;i<users.size();i++){
            DealerListAdapter.DealerData dealerData=new DealerListAdapter.DealerData();
            User user=users.get(i);
            dealerData.user=user;
            if (dataList.size()>i&&dataList.get(i)!=null) {
                dataList.get(i).replce(dealerData);
            } else {
                dataList.add(dealerData);
            }
        }
        handler.sendEmptyMessage(1);
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dealerListAdapter.setDataList(dataList);
        }
    };
}
