package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.InputStream;

import info.btsland.app.BtslandApplication;
import info.btsland.app.R;
import info.btsland.app.ui.activity.SettingActivity;

/**
 * 通用顶部导航栏碎片类
 * 2017/10/10
 * 可通过getXXX()获得内部控件进行定制
 */
public class HeadFragment extends Fragment {

    private int type = HeadType.BACK_SET;

    private String titleName = "btsland";

    private OnSelectOnClickListener listener;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
        if(titleTextView!=null){
            titleTextView.setText(titleName);
        }

    }


    public void setSelectListener(OnSelectOnClickListener listener) {
        this.listener = listener;
    }

    private TextView leftTextView;
    private TextView rightTextView;
    private TextView titleTextView;
    private TextView selectTextView;

    private Drawable drawable;


    public HeadFragment() {
        super();
    }
    public static HeadFragment newInstance(int type, String titleName){
        HeadFragment headFragment=new HeadFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        bundle.putString("titleName",titleName);
        headFragment.setArguments(bundle);
        return headFragment;
    }

    /**
     * 初始化
     */
    private void init(View view) {
        Log.i("HeadFragment", "init: ");
        leftTextView = view.findViewById(R.id.tv_head_left);
        rightTextView = view.findViewById(R.id.tv_head_right);
        titleTextView = view.findViewById(R.id.tv_head_title);
        selectTextView=view.findViewById(R.id.tv_head_select);
        drawable = view.getResources().getDrawable(R.drawable.image_share,null);
        //设置图片大小
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
    }

    /**
     * 填充内容
     */
    private void fillIn() {
        Log.i("HeadFragment", "fillIn: type=" + type);
        BackOnClickListener back = new BackOnClickListener();
        ToShareOnClickListener toShare = new ToShareOnClickListener();
        ToSettingOnClickListener toSetting = new ToSettingOnClickListener();
        titleTextView.setText(titleName);
        switch (type) {
            case HeadType.BACK_NULL:
                selectTextView.setVisibility(View.GONE);
                leftTextView.setOnClickListener(back);
                rightTextView.setVisibility(View.GONE);
                break;
            case HeadType.BACK_SET:
                selectTextView.setVisibility(View.GONE);
                leftTextView.setOnClickListener(back);
                rightTextView.setOnClickListener(toSetting);
                break;
            case HeadType.NULL_NULL:
                selectTextView.setVisibility(View.GONE);
                leftTextView.setVisibility(View.GONE);
                rightTextView.setVisibility(View.GONE);
                break;
            case HeadType.SHARE_SET:
                selectTextView.setVisibility(View.GONE);
                leftTextView.setCompoundDrawables(null,null,drawable,null);
                leftTextView.setOnClickListener(toShare);
                rightTextView.setOnClickListener(toSetting);
                break;
            case HeadType.BACK_SELECT_NULL:
                leftTextView.setOnClickListener(back);
                titleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                selectTextView.setVisibility(View.VISIBLE);
                selectTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(view);
                    }
                });
                rightTextView.setVisibility(View.GONE);
                break;

        }
    }

    class BackOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    }

    class ToShareOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "欢迎使用Btsland去中心化交易所，我们的官网地址是：https://www.btsland.info/。" +
                    "Btsland手机app请点击http://btsland.oss-cn-hangzhou.aliyuncs.com/Btsland.apk，欢迎您的使用。");
            shareIntent.setType("text/plain");
            //设置分享列表的标题，并且每次都显示分享列表
            startActivity(Intent.createChooser(shareIntent, "分享到"));



        }
    }

    class ToSettingOnClickListener implements View.OnClickListener {
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
        type=getArguments().getInt("type");
        String titleName=getArguments().getString("titleName");
        if(titleName!=null&&!titleName.equals("")){
            this.titleName=titleName;
        }
        init(view);
        fillIn();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract static class HeadType {

        public static final int BACK_SET = 1;
        public static final int SHARE_SET = 2;
        public static final int BACK_NULL = 3;
        public static final int NULL_NULL = 4;
        public static final int BACK_SELECT_NULL = 5;
        public static final int DEFAULT = 1;
    }
    public interface OnSelectOnClickListener{
        void onClick(View view);
    }
}
