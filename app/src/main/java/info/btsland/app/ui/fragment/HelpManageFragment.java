package info.btsland.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import info.btsland.app.R;

public class HelpManageFragment extends Fragment {
    private ListView listView;
    private TextView tvBtn;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_manage, container, false);
        init(view);
        fillIn();
        return view;
    }

    private void fillIn() {

    }

    private void init(View view) {
        listView=view.findViewById(R.id.lv_help_dealerList);
        tvBtn=view.findViewById(R.id.tv_help_btn);
    }

}
