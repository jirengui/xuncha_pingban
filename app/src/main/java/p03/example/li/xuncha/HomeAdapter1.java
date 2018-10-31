package p03.example.li.xuncha;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2018/1/16.
 */

public class HomeAdapter1 extends RecyclerView.Adapter<HomeAdapter1.MyViewHolder>implements View.OnClickListener
{
    private List<String> mDatas = new ArrayList<String>();
    private Context context;
    public HomeAdapter1(List<String> mDatas, Context context){
        this.mDatas = mDatas;
        this.context = context;
    }
    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_zuixin, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tv.setText(mDatas.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
    public void addData(int position, String string) {
        mDatas.add(position, string);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv,textView;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);
            textView = view.findViewById(R.id.tv_wancheng);
            textView.setText("");
        }
    }
}