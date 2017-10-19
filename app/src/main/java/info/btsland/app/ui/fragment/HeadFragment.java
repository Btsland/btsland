package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.btsland.app.R;
import info.btsland.app.model.Market;
import info.btsland.app.model.News;
import info.btsland.app.ui.activity.SettingActivity;
import info.btsland.app.ui.activity.UserActivity;

/**
 * 通用顶部导航栏碎片类
 * 2017/10/10
 * 可通过getXXX()获得内部控件进行定制
 */
public class HeadFragment extends Fragment {

    private int type=HeadType.BACK_SET;

    private String titleName;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private TextView leftTextView;
    private TextView rightTextView;
    private TextView titleTextView;


    public HeadFragment() {
        super();
    }

    public HeadFragment(int type, String titleName) {
        this.type = type;
        this.titleName = titleName;
    }

    public HeadFragment(int type) {
        this.type = type;
    }

    public HeadFragment(String titleName) {
        this.titleName = titleName;
    }

    /**
     * 初始化
     */
    private void init(){
        Log.i("HeadFragment", "init: ");
        leftTextView=getActivity().findViewById(R.id.tv_head_left);
        rightTextView=getActivity().findViewById(R.id.tv_head_right);
        titleTextView=getActivity().findViewById(R.id.tv_head_title);
    }

    /**
     * 填充内容
     */
    private void fillIn(int type){
        Log.i("HeadFragment", "fillIn: type="+type);
        BackOnClickListener back=new BackOnClickListener();
        ToUserOnClickListener toUser=new ToUserOnClickListener();
        ToSettingOnClickListener toSetting=new ToSettingOnClickListener();
        switch (type){
            case HeadType.BACK_NULL:
                leftTextView.setOnClickListener(back);
                rightTextView.setVisibility(View.GONE);
                break;
            case HeadType.BACK_SET:
                leftTextView.setOnClickListener(back);
                rightTextView.setOnClickListener(toSetting);
                break;
            case HeadType.NULL_NULL:
                leftTextView.setVisibility(View.GONE);
                rightTextView.setVisibility(View.GONE);
                break;
            case HeadType.USER_SET:
                leftTextView.setBackground(getActivity().getDrawable(R.mipmap.ic_btsland));
                leftTextView.setOnClickListener(toUser);
                rightTextView.setOnClickListener(toSetting);
                break;
        }
    }
    class BackOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    }
    class ToUserOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            getActivity().startActivity(intent);
            News news=new News();
            Market market=new Market();
        }
    }
    class ToSettingOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        fillIn(type);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract static class  HeadType{
        public static final int BACK_SET=1;
        public static final int USER_SET=2;
        public static final int BACK_NULL=3;
        public static final int NULL_NULL=4;
    }
}
