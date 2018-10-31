package p03.example.li.xuncha;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
/**
 * Created by li on 2018/2/27.
 */

public class KuaiJieYongYueAdapt extends RecyclerView.Adapter<KuaiJieYongYueAdapt.MasonryView> {
    private List<Map<String, String>> mlist;
    private Activity activity;
    public KuaiJieYongYueAdapt(Activity activity ,List<Map<String, String>> list) {
        mlist = list;
        this.activity = activity;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kuaijieyongyue, parent,false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(final MasonryView holder, final int position) {
        holder.textView1.setText(mlist.get(0).get("1"));
        holder.textView2.setText(mlist.get(0).get("2"));
        holder.textView3.setText(mlist.get(0).get("3"));
        holder.textView4.setText(mlist.get(0).get("4"));
        holder.textView5.setText(mlist.get(0).get("5"));
        holder.iv_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });
        holder.iv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemUseListener.onUseClick(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return mlist.size();
    }
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }
    public interface onItemUseListener {
        void onUseClick(int i);
    }
    private onItemDeleteListener mOnItemDeleteListener;
    private onItemUseListener mOnItemUseListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }
    public void setonItemUseListener(onItemUseListener mOnItemUseListener) {
        this.mOnItemUseListener = mOnItemUseListener;
    }
    @Override
    public int getItemCount() {
        if (mlist != null){
            return mlist.size();
        }
        return 0;
    }

    public static class MasonryView extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, textView4, textView5;
        ImageView iv_shanchu, iv_queding;

        public MasonryView(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.item_1);
            textView2 = (TextView) itemView.findViewById(R.id.item_2);
            textView3 = (TextView) itemView.findViewById(R.id.item_3);
            textView4 = (TextView) itemView.findViewById(R.id.item_4);
            textView5 = (TextView) itemView.findViewById(R.id.item_5);
            iv_queding = (ImageView) itemView.findViewById(R.id.queding);
            iv_shanchu = (ImageView) itemView.findViewById(R.id.shanchu);
        }
    }

}
