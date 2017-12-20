package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.app.model.IAsset;
import info.btsland.app.util.NumericUtil;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.utils.NoteStatCode;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ExchangeNoteAdapter extends BaseAdapter {

    private static final String TAG = "AssetRowAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Note> notes=new ArrayList<>();

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


    public ExchangeNoteAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exc_note_item, null);
        }
        if(notes==null){
            return null;
        }
        final Note note=notes.get(i);
        if(note==null){
            return null;
        }
        TextView tvName=convertView.findViewById(R.id.tv_exeNote_item_dealerName);
        TextView tvTime=convertView.findViewById(R.id.tv_exeNote_item_startTime);
        TextView tvNum=convertView.findViewById(R.id.tv_exeNote_item_num);
        TextView tvCoin=convertView.findViewById(R.id.tv_exeNote_item_coin);
        TextView tvStat=convertView.findViewById(R.id.tv_exeNote_item_stat);
        SimpleDateFormat format=new SimpleDateFormat("MM-dd/HH:mm");
        tvName.setText(note.getDealerId());
        tvTime.setText(format.format(note.getStartTime()));
        tvCoin.setText(note.getAssetCoin());
        tvNum.setText(String.valueOf(note.getAssetNum()));
        tvStat.setText(NoteStatCode.getTabAccount(note.getStatNo()));
        return convertView;
    }


}
