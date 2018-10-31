package p03.example.li.xuncha;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;
import com.mcxtzhang.layoutmanager.swipecard.RenRenCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Zuixinxiaoxi extends Fragment implements MainActivity.upmDatasListenr {
    private Map<String, String> datess = new HashMap<>();
    private List<Map<String, String>> mDatas = new ArrayList<Map<String, String>>();
    private HomeAdapter mAdapter = null;
    private String jihua, userId;
    private RecyclerView recyclerView;
    private Activity activity;

    private RecyclerView mRv_jiaoshi, mRv1_xuesheng;
    private KuaiJieYongYueAdapt mAdapter_kuaijie, mAdapter1;
    private List<Map<String, String>> mDatas_kuaijie = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> mDatass = new ArrayList<Map<String, String>>();
    private Map<String, String> map = new HashMap<String, String>();
    private TextView tv_xinJian, tv_tianjia;
    private long id = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDatas = ((MainActivity) activity).getJihua();
        userId = ((MainActivity) activity).getUserId();
        ((MainActivity) activity).setLinstenr(this);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_zuixinxiaoxi, null);
//        System.out.println("最新消息：" + mDatas.get(0).get("str"));
        recyclerView = view.findViewById(R.id.relativeLayout_zuixin);
//        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                mAdapter.removeData(position);
//
//            }
//        });
        mRv_jiaoshi = view.findViewById(R.id.rv);
        mRv1_xuesheng = view.findViewById(R.id.rv1);
        tv_tianjia = view.findViewById(R.id.kuaijie_tianjia);
        tv_tianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow(R.id.rv1);
                mAdapter_kuaijie.notifyDataSetChanged();
                mAdapter1.notifyDataSetChanged();
                ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
                CardConfig.initConfig(getContext());
                ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv1_xuesheng, mAdapter1, mDatass);
                ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);
                itemTouchHelper1.attachToRecyclerView(mRv1_xuesheng);
            }
        });
//        tv_xinJian = findViewById(R.id.xinjian);
//        imageView = findViewById(R.id.fanhui_kuaijie);
        mRv_jiaoshi.setLayoutManager(new OverLayCardLayoutManager());
        mRv1_xuesheng.setLayoutManager(new OverLayCardLayoutManager());
        CardConfig.initConfig(getContext());
        KuaiJieDatabase kuaiJieDatabase = new KuaiJieDatabase(getContext(), "KuaiJieDatabase.db", null, 2);
        final SQLiteDatabase db = kuaiJieDatabase.getWritableDatabase();
        Cursor cursor = db.query("KuaiJieDatabase", new String[]{"t1", "t2", "t3", "t4", "provider", "id", "status"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            mDatass.clear();
            do {
                map = new HashMap<>();
                map.put("1", cursor.getString(cursor.getColumnIndex("t1")));
                map.put("2", cursor.getString(cursor.getColumnIndex("t2")));
                map.put("3", cursor.getString(cursor.getColumnIndex("t3")));
                map.put("4", cursor.getString(cursor.getColumnIndex("t4")));
                map.put("5", "提供者：" + cursor.getString(cursor.getColumnIndex("provider")));
                map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                map.put("status", cursor.getString(cursor.getColumnIndex("status")));
                System.out.println("数据库：" + map.get("1"));
                if (map.get("status").equals("老师状态")) {
                    mDatas_kuaijie.add(map);
                } else {
                    mDatass.add(map);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        mAdapter_kuaijie = new KuaiJieYongYueAdapt(getActivity(), mDatas_kuaijie);
        mAdapter1 = new KuaiJieYongYueAdapt(getActivity(), mDatass);
        mRv_jiaoshi.setAdapter(mAdapter_kuaijie);
        mRv1_xuesheng.setAdapter(mAdapter1);
        ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
        ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv1_xuesheng, mAdapter1, mDatass);
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);
        itemTouchHelper1.attachToRecyclerView(mRv1_xuesheng);

        mAdapter_kuaijie.setOnItemDeleteClickListener(new KuaiJieYongYueAdapt.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                //每个最上面的数据都是列表中第 0 个
                db.delete("KuaiJieDatabase", "id=?", new String[]{mDatas_kuaijie.get(0).get("id")});
                mDatas_kuaijie.remove(0);
                mAdapter_kuaijie.notifyDataSetChanged();
                ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
                CardConfig.initConfig(getContext());
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);

            }
        });
        mAdapter_kuaijie.setonItemUseListener(new KuaiJieYongYueAdapt.onItemUseListener() {
            @Override
            public void onUseClick(int i) {
                ContentValues values1 = new ContentValues();
                values1.put("flag", "false");
                db.update("KuaiJieDatabase", values1, "flag=?", new String[]{"teacher_true"});
                ContentValues values = new ContentValues();
                values.put("flag", "teacher_true");
                db.update("KuaiJieDatabase", values, "id=?", new String[]{mDatas_kuaijie.get(0).get("id")});
                Toast toast;
                toast = Toast.makeText(getContext(), "教师快捷用语选择成功。", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        });
        mAdapter1.setonItemUseListener(new KuaiJieYongYueAdapt.onItemUseListener() {
            @Override
            public void onUseClick(int i) {
                ContentValues values1 = new ContentValues();
                values1.put("flag", "false");
                db.update("KuaiJieDatabase", values1, "flag=?", new String[]{"studet_true"});
                ContentValues values = new ContentValues();
                values.put("flag", "studet_true");
                db.update("KuaiJieDatabase", values, "id=?", new String[]{mDatass.get(0).get("id")});
                Toast toast;
                toast = Toast.makeText(getContext(), "学生快捷用语选择成功。", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        mAdapter1.setOnItemDeleteClickListener(new KuaiJieYongYueAdapt.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                //每个最上面的数据都是列表中第 0 个
                db.delete("KuaiJieDatabase", "id=?", new String[]{mDatass.get(0).get("id")});
                mDatass.remove(0);
                mAdapter1.notifyDataSetChanged();
                ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv_jiaoshi, mAdapter1, mDatass);
                CardConfig.initConfig(getContext());
                ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
                itemTouchHelper1.attachToRecyclerView(mRv_jiaoshi);
            }
        });
//        tv_xinJian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopwindow(R.id.rv1);
//                mAdapter_kuaijie.notifyDataSetChanged();
//                mAdapter1.notifyDataSetChanged();
//                ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
//                CardConfig.initConfig(getContext());
//                ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv1_xuesheng, mAdapter1, mDatass);
//                ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
//                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//                itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);
//                itemTouchHelper1.attachToRecyclerView(mRv1_xuesheng);
//            }
//        });


        return view;
    }


    @Override
    public void getmDatas(final List<Map<String, String>> mDatas) {
        this.mDatas = mDatas;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter == null) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(mAdapter = new HomeAdapter(mDatas, getContext(), userId));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL_LIST));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                } else {
                    mAdapter.up(mDatas);
                }
            }
        });
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow(final int layout_id) {
        map = new HashMap<String, String>();
        Button button;
        final EditText ed_1, ed_2, ed_3, ed_4;
        final String[] str_1 = new String[1];
        final String[] str_2 = new String[1];
        final String[] str_3 = new String[1];
        final String[] str_4 = new String[1];
        final String[] str_audo = new String[1];
        final TextView xuesheng;
        TextView laoshi = null;
        ArrayAdapter<String> arrayAdapter;
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tianjia_yongyue, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                400,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        ed_1 = (EditText) view.findViewById(R.id.ed_1);
        ed_2 = (EditText) view.findViewById(R.id.ed_2);
        ed_3 = (EditText) view.findViewById(R.id.ed_3);
        ed_4 = (EditText) view.findViewById(R.id.ed_4);
        final String[] ss = {"-1"};
        xuesheng =  view.findViewById(R.id.xueshengzhuangtai);
        laoshi = view.findViewById(R.id.laoshizhuangtai);
        final TextView finalLaoshi1 = laoshi;
        xuesheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuesheng.setTextColor(Color.RED);
                finalLaoshi1.setTextColor(Color.BLACK);
                ss[0] = "学生状态";
            }
        });
        laoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalLaoshi1.setTextColor(Color.RED);
                xuesheng.setTextColor(Color.BLACK);
                ss[0] = "老师状态";
            }
        });
        button = (Button) view.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ss[0].equals("-1")){
                    Toast.makeText(getContext(),"请选择老师/学生状态", Toast.LENGTH_LONG).show();
                }else {
                    str_1[0] = ed_1.getText().toString();
                    str_2[0] = ed_2.getText().toString();
                    str_3[0] = ed_3.getText().toString();
                    str_4[0] = ed_4.getText().toString();
                    str_audo[0] = ss[0];
                    KuaiJieDatabase kuaiJieDatabase = new KuaiJieDatabase(getContext(), "KuaiJieDatabase.db", null, 2);
                    SQLiteDatabase db = kuaiJieDatabase.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("t1", ed_1.getText().toString());
                    values.put("t2", ed_2.getText().toString());
                    values.put("t3", ed_3.getText().toString());
                    values.put("t4", ed_4.getText().toString());
                    values.put("flag", "false");
                    values.put("provider", userId);//提供者
                    values.put("status", ss[0]);
                    values.put("userId", userId);//使用者
                    id = db.insert("KuaiJieDatabase", null, values);
                    values.clear();
                    db.close();
                    map.put("1", str_1[0]);
                    map.put("2", str_2[0]);
                    map.put("3", str_3[0]);
                    map.put("4", str_4[0]);
                    map.put("5", "提供者 " + userId);
                    map.put("id", String.valueOf(id));
                    if (str_audo[0].equals("老师状态")) {
                        mDatas_kuaijie.add(0, map);
                    } else {
                        mDatass.add(0, map);
                    }
                    window.dismiss();
                }
            }
        });


        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(getActivity().findViewById(layout_id),
                Gravity.CENTER, 0, 0);

//        window.showAsDropDown(KuaiJieYongYueActivity.this.findViewById(layout_id));
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });

    }
}
