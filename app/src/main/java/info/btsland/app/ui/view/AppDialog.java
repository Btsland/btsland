package info.btsland.app.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import info.btsland.app.R;
import info.btsland.app.api.MarketStat;

/**
 * Created by Administrator on 2017/11/8.
 */

public class AppDialog extends DialogFragment {
    private Context context;

    public AppDialog(Context context) {
        this.context=context;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(context);

        //创建对话框
        AlertDialog dialog =mydialog.create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
