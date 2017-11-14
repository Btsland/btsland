package info.btsland.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.btsland.app.R;
import info.btsland.app.ui.activity.PurseAccessRecordActivity;
import info.btsland.app.ui.activity.PurseAssetActivity;
import info.btsland.app.ui.activity.PurseTradingRecordActivity;
import info.btsland.app.ui.activity.PurseWalletBackupActivity;
import info.btsland.app.ui.activity.PurseWholeGuadanActivity;

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
    //全部资产
    private TextView tvPurseAllAsset;
    //充提记录
    private TextView tvPurseRW;
    //交易记录
    private TextView tvPurseDeal;
    //全部挂单
    private TextView tvPurseAllRemain;
    //备份
    private TextView tvPurseBackup;

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
        return inflater.inflate(R.layout.fragment_purse, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        //全部资产
        tvPurseAllAsset = getActivity().findViewById(R.id.tv_purse_allAsset);
        //充提记录
        tvPurseRW = getActivity().findViewById(R.id.tv_purse_rw);
        //交易记录
        tvPurseDeal = getActivity().findViewById(R.id.tv_purse_deal);
        //全部挂单
        tvPurseAllRemain = getActivity().findViewById(R.id.tv_purse_allRemain);
        //钱包备份
        tvPurseBackup = getActivity().findViewById(R.id.tv_purse_backup);

//        TextViewOnTouchListener onTouchlistener = new TextViewOnTouchListener();
//        tvPurseAllAsset.setOnTouchListener(onTouchlistener);
//        tvPurseRW.setOnTouchListener(onTouchlistener);
//        tvPurseDeal.setOnTouchListener(onTouchlistener);
//        tvPurseAllRemain.setOnTouchListener(onTouchlistener);
//        tvPurseBackup.setOnTouchListener(onTouchlistener);

        TextViewOnCLickListener onCLickListener = new TextViewOnCLickListener();
        tvPurseAllAsset.setOnClickListener(onCLickListener);
        tvPurseRW.setOnClickListener(onCLickListener);
        tvPurseDeal.setOnClickListener(onCLickListener);
        tvPurseAllRemain.setOnClickListener(onCLickListener);
        tvPurseBackup.setOnClickListener(onCLickListener);
    }

    /**
     * 单击特效
     *
     * @param textView    被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch, null));
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row, null));
        }
    }

    class TextViewOnCLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "全部", Toast.LENGTH_SHORT).show();
            switch (view.getId()) {

                case R.id.tv_purse_allAsset:
                    //全部资产
                    Intent intent = new Intent(getActivity(), PurseAssetActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.tv_purse_rw:
                    //充值记录
                    Intent rw = new Intent(getActivity(), PurseAccessRecordActivity.class);
                    getActivity().startActivity(rw);
                    break;
                case R.id.tv_purse_deal:
                    //交易记录
                    Intent deal = new Intent(getActivity(), PurseTradingRecordActivity.class);
                    getActivity().startActivity(deal);
                    break;
                case R.id.tv_purse_allRemain:
                    //全部挂单
                    Intent allremain = new Intent(getActivity(), PurseWholeGuadanActivity.class);
                    getActivity().startActivity(allremain);
                    break;
                case R.id.tv_purse_backup:
                    //钱包备份
                    Intent backup = new Intent(getActivity(), PurseWalletBackupActivity.class);
                    getActivity().startActivity(backup);
                    break;
            }
        }
    }

    class TextViewOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.tv_purse_allAsset:
                    touchColor(tvPurseAllAsset, motionEvent);
                    break;
                case R.id.tv_purse_rw:
                    touchColor(tvPurseRW, motionEvent);
                    break;
                case R.id.tv_purse_deal:
                    touchColor(tvPurseDeal, motionEvent);
                    break;
                case R.id.tv_purse_allRemain:
                    touchColor(tvPurseAllRemain, motionEvent);
                    break;
                case R.id.tv_purse_backup:
                    touchColor(tvPurseBackup, motionEvent);
                    break;
            }
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
