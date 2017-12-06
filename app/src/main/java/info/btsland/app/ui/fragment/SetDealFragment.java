package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.Adapter.CoinsRowAdapter;
import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.api.asset_object;
import info.btsland.app.ui.view.AppDialog;



public class SetDealFragment extends Fragment {
    private static String TAG="SetDealFragment";
    private String base="CNY";

    private TextView tvSearch;
    private EditText edName;
    private ListView lvCoins;
    private ListView lvNewCoins;
    private TextView tvCancel;
    private TextView tvConfirm;

    private CoinsRowAdapter coinRowAdapter;
    private CoinsRowAdapter newCoinsRowAdapter;
    private List<String> oldQuotes=new ArrayList<>();
    private List<String> quotes=new ArrayList<>();


    public SetDealFragment() {
        // Required empty public constructor
    }


    public static SetDealFragment newInstance(String base) {
        SetDealFragment fragment = new SetDealFragment();
        Bundle bundle=new Bundle();
        bundle.putString("base",base);
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_deal, container, false);
        this.base=getArguments().getString("base",base);
        init(view);
        fillIn();
        return view;
    }
    private void init(View view){
        tvCancel=view.findViewById(R.id.tv_setDeal_cancel);
        tvConfirm=view.findViewById(R.id.tv_setDeal_confirm);
        tvSearch=view.findViewById(R.id.tv_setDeal_search);
        edName=view.findViewById(R.id.ed_setDeal_name);
        lvCoins=view.findViewById(R.id.lv_setDeal_coins);
        lvNewCoins=view.findViewById(R.id.lv_setDeal_newCoins);
    }
    private void fillIn(){
        coinRowAdapter=new CoinsRowAdapter(getActivity(),-1);
        coinRowAdapter.setListener(new CoinOnItemClickListener());
        lvCoins.setAdapter(coinRowAdapter);
        quotes.addAll(BtslandApplication.listMap.get(base));
        oldQuotes.addAll(BtslandApplication.listMap.get(base));
        coinRowAdapter.setCoins(quotes);
        newCoinsRowAdapter=new CoinsRowAdapter(getActivity(),1);
        newCoinsRowAdapter.setListener(new NewCoinOnItemClickListener());
        lvNewCoins.setAdapter(newCoinsRowAdapter);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edName.getEditableText().toString();
                List<asset_object> list = BtslandApplication.getAssetByName(name);
                List<String> strings=new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    strings.add(list.get(i).symbol);
                }
                newCoinsRowAdapter.setCoins(strings);
            }
        });
        ITextWatcher iTextWatcher=new ITextWatcher();
        edName.addTextChangedListener(iTextWatcher);
        ConfirmOnItemClickListener confirmListener=new ConfirmOnItemClickListener();
        tvConfirm.setOnClickListener(confirmListener);
        CancelOnItemClickListener cancelListener=new CancelOnItemClickListener();
        tvCancel.setOnClickListener(cancelListener);
    }
    class ITextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    class CoinOnItemClickListener implements CoinsRowAdapter.OnItemClickListener{
        @Override
        public void onClick(String coin, int index) {
            quotes.remove(index);
            coinRowAdapter.setCoins(quotes);
        }
    }

    class NewCoinOnItemClickListener implements CoinsRowAdapter.OnItemClickListener{
        @Override
        public void onClick(String coin, int index) {
            for(int i=0;i<quotes.size();i++){
                if(coin.equals(base)){
                    AppDialog appDialog=new AppDialog(getActivity());
                    appDialog.setMsg(coin+"与当前市场("+base+")相同，无法设置！");
                    appDialog.show();
                    return;
                }
                if(quotes.get(i).equals(coin)){
                    AppDialog appDialog=new AppDialog(getActivity());
                    appDialog.setMsg(coin+"已存在，请勿重复设置！");
                    appDialog.show();
                    return;
                }
            }
            quotes.add(coin);
            coinRowAdapter.setCoins(quotes);
        }
    }
    private KProgressHUD hud;
    class ConfirmOnItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            hud=KProgressHUD.create(getActivity());
            hud.setLabel("请稍等。。。");
            hud.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BtslandApplication.listMap.get(base).clear();
                    BtslandApplication.listMap.get(base).addAll(quotes);
                    if(BtslandApplication.saveListMap()){
                        handler.sendEmptyMessage(1);
                    }else {
                        handler.sendEmptyMessage(-1);
                    }
                }
            }).start();

        }
    }
    class CancelOnItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            coinRowAdapter.setCoins(oldQuotes);
            Log.e(TAG, "onClick: oldQuotes:"+oldQuotes.size()+",quotes:"+quotes.size()+"BtslandApplication.listMap.get(base):"+BtslandApplication.listMap.get(base).size());
            quotes.clear();
            quotes.addAll(oldQuotes);
        }
    }
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                if(hud.isShowing()){
                    hud.dismiss();
                }
                AppDialog appDialog=new AppDialog(getActivity());
                appDialog.setMsg("保存成功，重启后生效！");
                appDialog.show();
            }else if(msg.what==-1){
                if(hud.isShowing()){
                    hud.dismiss();
                }
                AppDialog appDialog=new AppDialog(getActivity());
                appDialog.setMsg("保存失败！");
                appDialog.show();
            }
        }
    };
}
