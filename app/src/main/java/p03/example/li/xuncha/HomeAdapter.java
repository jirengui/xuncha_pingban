package p03.example.li.xuncha;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2018/1/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>implements View.OnClickListener
{
    private List<Map<String, String> > mDatas = new ArrayList<Map<String, String> >();
    private Context context;
    private String userId;
    public HomeAdapter (List<Map<String, String>> mDatas, Context context, String userId){
        this.mDatas = mDatas;
        this.context = context;
        this.userId = userId;
    }
    public void  up( List<Map<String, String> > mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public  interface OnItemClickListener {
        void onItemClick(View view , int position);
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
        final MyViewHolder holder = new MyViewHolder(view);

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
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.tv.setText(mDatas.get(position).get("str"));
        holder.textView.setText(mDatas.get(position).get("bot"));
        holder.itemView.setTag(position);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("");
                builder.setMessage("确定已经完成了今天的巡查任务");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.textView.setText("已完成");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                                try {
                                    socketFuWuQi.upjihua(userId, mDatas.get(position).get("time"), "true");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                }
                        }).start();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                    }
                });

    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
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

        }
    }
}