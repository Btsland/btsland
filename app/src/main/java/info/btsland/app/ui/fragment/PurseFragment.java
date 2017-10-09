package info.btsland.app.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import info.btsland.app.R;
import info.btsland.app.ui.AssetActivity;
import info.btsland.app.ui.MainActivity;
import info.btsland.app.ui.UserActivity;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
    private OnFragmentInteractionListener mListener;

    public PurseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurseFragment newInstance(String param1, String param2) {
        PurseFragment fragment = new PurseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    private void init(){
        //全部资产
        tvPurseAllAsset=getActivity().findViewById(R.id.tv_purse_allAsset);
        tvPurseRW=getActivity().findViewById(R.id.tv_purse_rw);
        tvPurseDeal=getActivity().findViewById(R.id.tv_purse_deal);
        tvPurseAllRemain=getActivity().findViewById(R.id.tv_purse_allRemain);
        tvPurseBackup=getActivity().findViewById(R.id.tv_purse_backup);
        TextViewOnTouchListener onTouchlistener=new TextViewOnTouchListener();
        tvPurseAllAsset.setOnTouchListener(onTouchlistener);
        tvPurseRW.setOnTouchListener(onTouchlistener);
        tvPurseDeal.setOnTouchListener(onTouchlistener);
        tvPurseAllRemain.setOnTouchListener(onTouchlistener);
        tvPurseBackup.setOnTouchListener(onTouchlistener);
        TextViewOnCLickListener onCLickListener=new TextViewOnCLickListener();
        tvPurseAllAsset.setOnClickListener(onCLickListener);
        tvPurseRW.setOnClickListener(onCLickListener);
        tvPurseDeal.setOnClickListener(onCLickListener);
        tvPurseAllRemain.setOnClickListener(onCLickListener);
        tvPurseBackup.setOnClickListener(onCLickListener);
    }
    /**
     * 单击特效
     * @param textView 被单击的tv
     * @param motionEvent 当前状态
     */
    protected void touchColor(TextView textView,MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row_touch,null));
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            textView.setBackground(getResources().getDrawable(R.drawable.tv_row,null));
        }
    }
    class TextViewOnCLickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "全部", Toast.LENGTH_SHORT).show();
            switch (view.getId()) {

                case R.id.tv_purse_allAsset:
                    Intent intent=new Intent(getActivity(),AssetActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.tv_purse_rw:
                    break;
                case R.id.tv_purse_deal:
                    break;
                case R.id.tv_purse_allRemain:
                    break;
                case R.id.tv_purse_backup:
                    break;
            }
        }
    }
    class TextViewOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.tv_purse_allAsset:
                    touchColor(tvPurseAllAsset,motionEvent);
                    break;
                case R.id.tv_purse_rw:
                    touchColor(tvPurseRW,motionEvent);
                    break;
                case R.id.tv_purse_deal:
                    touchColor(tvPurseDeal,motionEvent);
                    break;
                case R.id.tv_purse_allRemain:
                    touchColor(tvPurseAllRemain,motionEvent);
                    break;
                case R.id.tv_purse_backup:
                    touchColor(tvPurseBackup,motionEvent);
                    break;
            }
            return false;
        }
    }












    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
