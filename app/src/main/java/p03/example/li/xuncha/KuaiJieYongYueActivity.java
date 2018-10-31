package p03.example.li.xuncha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KuaiJieYongYueActivity extends AppCompatActivity {

    private RecyclerView mRv_jiaoshi, mRv1_xuesheng;
    private KuaiJieYongYueAdapt mAdapter_kuaijie, mAdapter1;
    private List<Map<String, String>> mDatas_kuaijie = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> mDatass = new ArrayList<Map<String, String>>();
    private Map<String, String> map = new HashMap<String, String>();
    private String userId = "-1";
    private TextView tv_xinJian;
    private ImageView imageView;
    private long id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kuai_jie_yong_yue);
//        Toolbar toolbar = findViewById(R.id.kuaijie_toolbar);
//        toolbar.setBackgroundColor(0xff0082c8);
        Intent mintent = getIntent();
        userId = mintent.getStringExtra("userId");
        mRv_jiaoshi = findViewById(R.id.rv);
        mRv1_xuesheng = findViewById(R.id.rv1);
//        tv_xinJian = findViewById(R.id.xinjian);
//        imageView = findViewById(R.id.fanhui_kuaijie);
        mRv_jiaoshi.setLayoutManager(new OverLayCardLayoutManager());
        mRv1_xuesheng.setLayoutManager(new OverLayCardLayoutManager());
        CardConfig.initConfig(this);
        KuaiJieDatabase kuaiJieDatabase = new KuaiJieDatabase(KuaiJieYongYueActivity.this, "KuaiJieDatabase.db", null, 2);
        final SQLiteDatabase db = kuaiJieDatabase.getWritableDatabase();
        Cursor cursor = db.query("KuaiJieDatabase",new String[]{"t1", "t2", "t3", "t4","provider", "id", "status"}, "userId=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst())
        {
            do {
                map = new HashMap<>();
                map.put("1", cursor.getString(cursor.getColumnIndex("t1")));
                map.put("2", cursor.getString(cursor.getColumnIndex("t2")));
                map.put("3", cursor.getString(cursor.getColumnIndex("t3")));
                map.put("4", cursor.getString(cursor.getColumnIndex("t4")));
                map.put("5", "提供者：" + cursor.getString(cursor.getColumnIndex("provider")));
                map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                map.put("status", cursor.getString(cursor.getColumnIndex("status")));
                if (map.get("status").equals("老师状态")) {
                    mDatas_kuaijie.add(map);
                }else{
                    mDatass.add(map);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        mAdapter_kuaijie = new KuaiJieYongYueAdapt(this, mDatas_kuaijie);
        mAdapter1 = new KuaiJieYongYueAdapt(this, mDatass);
        mRv_jiaoshi.setAdapter(mAdapter_kuaijie);
        mRv1_xuesheng.setAdapter(mAdapter1);
        ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
        ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv1_xuesheng, mAdapter1, mDatass);
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);
        itemTouchHelper1.attachToRecyclerView(mRv1_xuesheng);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter_kuaijie.setOnItemDeleteClickListener(new KuaiJieYongYueAdapt.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                //每个最上面的数据都是列表中第 0 个
                db.delete("KuaiJieDatabase","id=?",new String[]{mDatas_kuaijie.get(0).get("id")});
                mDatas_kuaijie.remove(0);
                mAdapter_kuaijie.notifyDataSetChanged();
                ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
                CardConfig.initConfig(KuaiJieYongYueActivity.this);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);

            }
        });
        mAdapter_kuaijie.setonItemUseListener(new KuaiJieYongYueAdapt.onItemUseListener() {
            @Override
            public void onUseClick(int i) {
                ContentValues values1= new ContentValues();
                values1.put("flag","false");
                db.update("KuaiJieDatabase", values1, "flag=?",new String[]{"teacher_true"});
                ContentValues values= new ContentValues();
                values.put("flag","teacher_true");
                db.update("KuaiJieDatabase", values, "id=?",new String[]{mDatas_kuaijie.get(0).get("id")});
                Toast toast;
                toast = Toast.makeText(KuaiJieYongYueActivity.this,"教师快捷用语选择成功。",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        });
        mAdapter1.setonItemUseListener(new KuaiJieYongYueAdapt.onItemUseListener() {
            @Override
            public void onUseClick(int i) {
                ContentValues values1= new ContentValues();
                values1.put("flag","false");
                db.update("KuaiJieDatabase", values1, "flag=?",new String[]{"studet_true"});
                ContentValues values= new ContentValues();
                values.put("flag","studet_true");
                db.update("KuaiJieDatabase", values, "id=?",new String[]{mDatass.get(0).get("id")});
                Toast toast;
                toast = Toast.makeText(KuaiJieYongYueActivity.this,"学生快捷用语选择成功。",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        mAdapter1.setOnItemDeleteClickListener(new KuaiJieYongYueAdapt.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                //每个最上面的数据都是列表中第 0 个
                db.delete("KuaiJieDatabase","id=?",new String[]{mDatass.get(0).get("id")});
                mDatass.remove(0);
                mAdapter1.notifyDataSetChanged();
                ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv_jiaoshi, mAdapter1, mDatass);
                CardConfig.initConfig(KuaiJieYongYueActivity.this);
                ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
                itemTouchHelper1.attachToRecyclerView(mRv_jiaoshi);
            }
        });
        tv_xinJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow(R.id.rv1);
                mAdapter_kuaijie.notifyDataSetChanged();
                mAdapter1.notifyDataSetChanged();
                ItemTouchHelper.Callback callback = new RenRenCallback(mRv_jiaoshi, mAdapter_kuaijie, mDatas_kuaijie);
                CardConfig.initConfig(KuaiJieYongYueActivity.this);
                ItemTouchHelper.Callback callback1 = new RenRenCallback(mRv1_xuesheng, mAdapter1, mDatass);
                ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(callback1);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(mRv_jiaoshi);
                itemTouchHelper1.attachToRecyclerView(mRv1_xuesheng);
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
        final AutoCompleteTextView autoCompleteTextView;
        ArrayAdapter<String> arrayAdapter;
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tianjia_yongyue, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        ed_1 = (EditText) view.findViewById(R.id.ed_1);
        ed_2 = (EditText) view.findViewById(R.id.ed_2);
        ed_3 = (EditText) view.findViewById(R.id.ed_3);
        ed_4 = (EditText) view.findViewById(R.id.ed_4);
        List<String> list = new ArrayList<String>();
        list.add("老师状态");
        list.add("学生状态");
//        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autotextView);
//        arrayAdapter = new ArrayAdapter<String>(KuaiJieYongYueActivity.this, android.R.layout.simple_list_item_1,list);
////        autoCompleteTextView.setAdapter(arrayAdapter);
//        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autoCompleteTextView.showDropDown();
//            }
//        });
        button = (Button) view.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_1[0] = ed_1.getText().toString();
                str_2[0] = ed_2.getText().toString();
                str_3[0] = ed_3.getText().toString();
                str_4[0] = ed_4.getText().toString();
//                str_audo[0] = autoCompleteTextView.getText().toString();
                KuaiJieDatabase kuaiJieDatabase = new KuaiJieDatabase(KuaiJieYongYueActivity.this, "KuaiJieDatabase.db", null, 2);
                SQLiteDatabase db = kuaiJieDatabase.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("t1", ed_1.getText().toString());
                values.put("t2", ed_2.getText().toString());
                values.put("t3", ed_3.getText().toString());
                values.put("t4", ed_4.getText().toString());
                values.put("flag", "false");
                values.put("provider", userId);//提供者
//                values.put("status", autoCompleteTextView.getText().toString());
                values.put("userId", userId);//使用者
                id = db.insert("KuaiJieDatabase", null, values);
                values.clear();
                db.close();
                map.put("1", str_1[0]);
                map.put("2", str_2[0]);
                map.put("3", str_3[0]);
                map.put("4", str_4[0]);
                map.put("5","提供者 " + userId);
                map.put("id", String.valueOf(id));
                if (str_audo[0].equals("老师状态"))
                {
                    mDatas_kuaijie.add(0,map);
                }else {
                    mDatass.add(0,map);
                }
                window.dismiss();
            }
        });


        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(KuaiJieYongYueActivity.this.findViewById(layout_id),
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
