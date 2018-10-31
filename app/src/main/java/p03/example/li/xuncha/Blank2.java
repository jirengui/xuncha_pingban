package p03.example.li.xuncha;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Blank2 extends Fragment {
    private RecyclerView mRecyclerView;
    private SideBar1 sideBar;
    private TextView dialog;
    private SortAdapter1 adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;
    private SortModel sortModel;
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();
    private String[] jilu2;
    private  String userId;
    private String[] a;
    private String[] b1;

    /**
     * 根据时间来排列RecyclerView里面的数据类
     */
    private Persons pinyinComparator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View newsLayout = inflater.inflate(R.layout.activity_blank2, container,
                false);
        pinyinComparator = new Persons();
        sideBar = (SideBar1)newsLayout.findViewById(R.id.sideBar);
        dialog = (TextView) newsLayout.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        jilu2 = getArguments().getStringArray("jiLu");
        userId = getArguments().getString("userId");
        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar1.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0), s.charAt(1));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }

            }
        });

        mRecyclerView = (RecyclerView) newsLayout.findViewById(R.id.recyclerView);
        int a1 = jilu2.length;
        a = new String[a1];
        b1 = new String[a1];
        DateFormat format1 = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        for (int i = 0; i < a1 ; i++)
        {
            String[] string = jilu2[i].split(",");
            a[i] = string[0] + "," +string[1];
            try {
               date = format1.parse(string[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateString = format1.format(date);
            String s_nd = dateString.substring(0, 4); // 年份
            String s_yf = dateString.substring(5, 7); // 月份
            dateString = s_nd + "年" + s_yf + "月";
            b1[i] = dateString;
        }

        SourceDateList = filledData(a,jilu2,b1);
        Collections.sort(SourceDateList, pinyinComparator);
        //RecyclerView社置manager
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SortAdapter1(getContext(), SourceDateList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SortAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                Intent mintent = new Intent();
                mintent.putExtra("positiion1", position);
                mintent.putExtra("userId", userId);
                mintent.putExtra("xx", (Serializable) SourceDateList);
                mintent.setClass(getContext(), XunchaDetailsActivity.class);
                startActivity(mintent);
            }
        });
        mClearEditText = (ClearEditText) newsLayout.findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return newsLayout;
    }


    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date, String[] volid, String[] shijian) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();//增加个volid储存值
            sortModel.setName(date[i]);
            sortModel.setVolid(volid[i]);
            sortModel.setLetters(shijian[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            mSortList.add(sortModel);
        }
        return mSortList;

    }
    public void update(int key){
        if (key == 1){
            DateFormat format1 = new SimpleDateFormat("yyyy-MM");
            Date date = null;
            int j = 0;
            for (int i = 0; i < jilu2.length  ; i++) {
                String[] string = jilu2[i].split(",");
                if (string.length >= 8) {
                    if (string[7] != null && !string[7].equals("null")) {
                        j++;
                    }
                }
            }
            final String[] b = new String[j];
            final String[] b2 = new String[j];
            final String[] jilu1 = new String[j];
            int j1 = 0;
            for (int i = 0; i < jilu2.length ; i++) {
                String[] string = jilu2[i].split(",");
                if (string.length >= 8) {
                    if (string[7] != null && !string[7].equals("null")) {
                        b[j1] = string[0] + "," + string[1];
                        jilu1[j1] = jilu2[i];
                        try {
                            date = format1.parse(string[0]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String dateString = format1.format(date);
                        String s_nd = dateString.substring(0, 4); // 年份
                        String s_yf = dateString.substring(5, 7); // 月份
                        dateString = s_nd + "年" + s_yf + "月";
                        b2[j1] = dateString;
                        j1++;
                    }
                }
            }
            SourceDateList = filledData(b,jilu1,b2);
            Collections.sort(SourceDateList, pinyinComparator);
            //RecyclerView社置manager
            manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            adapter = new SortAdapter1(getContext(), SourceDateList);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new SortAdapter1.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                    Intent mintent = new Intent();
                    mintent.putExtra("positiion1", position);
                    mintent.putExtra("userId", userId);
                    mintent.putExtra("xx", (Serializable) SourceDateList);
                    mintent.setClass(getContext(), XunchaDetailsActivity.class);
                    startActivity(mintent);
                }
            });
        }else if (key == 2){
            SourceDateList = filledData(a,jilu2,b1);
            Collections.sort(SourceDateList, pinyinComparator);
            //RecyclerView社置manager
            manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            adapter = new SortAdapter1(getContext(), SourceDateList);
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new SortAdapter1.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
                    Intent mintent = new Intent();
                    mintent.putExtra("positiion1", position);
                    mintent.putExtra("userId", userId);
                    mintent.putExtra("xx", (Serializable) SourceDateList);
                    mintent.setClass(getContext(), XunchaDetailsActivity.class);
                    startActivity(mintent);
                }
            });
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);

    }

}