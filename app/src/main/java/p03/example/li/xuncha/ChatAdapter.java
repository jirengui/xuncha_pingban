package p03.example.li.xuncha;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private ArrayList<Map<String, String>> dataList = new ArrayList<>();
    private Map<String, String> map = new HashMap<String, String>();

    public void replaceAll(ArrayList<Map<String, String>> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Map<String, String>> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyDataSetChanged();
//            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_a, parent, false));
            case 2:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_b, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(dataList.get(position).get("leixing"));
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Map<String, String> map) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter {
        private TextView tv, tv_name;

        public ChatAViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Map<String, String> map) {
            super.setData(map);
            tv.setText(map.get("xiaoxi"));
            tv.setTextIsSelectable(true);
            tv_name.setText(map.get("name"));
        }
    }

    private class ChatBViewHolder extends BaseAdapter {

        private TextView tv, tv_name;

        public ChatBViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Map<String, String> map) {
            super.setData(map);
            tv.setText(map.get("xiaoxi"));
            tv.setTextIsSelectable(true);
            tv_name.setText(map.get("name"));
        }
    }
}
