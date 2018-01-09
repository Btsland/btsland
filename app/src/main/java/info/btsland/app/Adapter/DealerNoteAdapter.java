package info.btsland.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.btsland.app.R;
import info.btsland.exchange.entity.Note;
import info.btsland.exchange.utils.NoteStatCode;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DealerNoteAdapter extends BaseAdapter {

    private static final String TAG = "AssetRowAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Note> notes=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


    public DealerNoteAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.dealer_having_note_item, null);
        }
        if(notes==null){
            return null;
        }
        final Note note=notes.get(i);
        if(note==null){
            return null;
        }
        TextView tvPay=convertView.findViewById(R.id.tv_dealer_having_item_pay);
        TextView tvCode=convertView.findViewById(R.id.tv_dealer_having_item_code);
        TextView tvAccount=convertView.findViewById(R.id.tv_dealer_having_item_remark);
        TextView tvNum=convertView.findViewById(R.id.tv_dealer_having_item_num);
        TextView tvCoin=convertView.findViewById(R.id.tv_dealer_having_item_coin);
        TextView tvStat=convertView.findViewById(R.id.tv_dealer_having_item_stat);
        TextView tvTime=convertView.findViewById(R.id.tv_dealer_having_item_time);
        String depict="未知";
        if(note.getRealDepict()!=null&&!note.getRealDepict().equals("")) {
            int a = note.getRealDepict().indexOf("(");
            if (a != -1) {
                depict = "(" + note.getRealDepict().substring(0, a) + ")";
            } else {
                depict = "(" + note.getRealDepict() + ")";
            }
        }
        if(note.getRealNo()!=null){
            tvPay.setText(note.getRealNo()+depict);
        }
        if(note.getRemarkCode()!=null){
            tvCode.setText(note.getRemarkCode());
        }
        if(note.getAssetCoin()!=null){
            tvCoin.setText(note.getAssetCoin());
        }
        if(note.getAccount()!=null){
            tvAccount.setText(note.getAccount());
        }
        if(note.getStatNo()!=null){
            tvStat.setText(NoteStatCode.getTabDealer(note.getStatNo()));
        }
        if(note.getAssetNum()!=null){
            tvNum.setText(""+note.getAssetNum());
        }
        if(note.getStartTime()!=null){
            SimpleDateFormat format=new SimpleDateFormat("MM-dd/HH:MM");
            tvTime.setText(format.format(note.getStartTime()));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(note);
            }
        });
        return convertView;
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
    }
}
