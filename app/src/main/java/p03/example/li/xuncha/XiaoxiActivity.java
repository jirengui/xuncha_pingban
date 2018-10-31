package p03.example.li.xuncha;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiaoxiActivity extends AppCompatActivity {
    private static final String TAG = "a";
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    private ArrayList<Map<String, String>> dataList = new ArrayList<>();
    private Map<String, String> map = new HashMap<String, String>();
    private List<String> mDatas = new ArrayList<String>();
    final ChatAdapter adapter = new ChatAdapter();
    private SocketFuWuQi socketFuWuQi_1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xiaoxi);
        Toolbar tb = (Toolbar) findViewById(R.id.xiaoxi_toolbar);
        tb.setBackgroundColor(0xff0082c8);
        setSupportActionBar(tb);
        ImageView fanhui = findViewById(R.id.fanhui_xiaoxi);

        mDatas = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    mDatas = socketFuWuQi.getLiebiao();
                    mDatas.add(0,"@全体成员 ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        final String userName = intent.getStringExtra("userName");
        recyclerView = findViewById(R.id.rc_xiaoxi);
        recyclerView.setHasFixedSize(true);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        editText = findViewById(R.id.ed_xiaoxi);
        button = findViewById(R.id.bt_send);

        recyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketFuWuQi_1 = new SocketFuWuQi(1);
                    while (true) {
                        final ArrayList<Map<String, String>> list1 = new ArrayList<>();
                        ArrayList<String> list = new ArrayList<>();
                        map = new HashMap<>();
                        try {
                            String s = socketFuWuQi_1.getXiaoxi(userId);
                            if (s != null) {
                                list.add(s);
                                for (int i = 0; i < list.size(); i++) {
                                    map = new HashMap<>();
                                    String[] ss = new String[2];
                                    if (list.get(i).indexOf(",") != -1) {
                                        ss = list.get(i).split(",");
                                    }
                                    System.out.println("我的消息回来了： " + list.get(i));
                                    map.put("leixing", "2");
                                    map.put("xiaoxi", ss[0]);
                                    map.put("name", ss[1]);
                                    list1.add(map);
                                }
                            }
                            //                            Message message = new Message();
//                            message.what = 5;
//                            Bundle bundle  = new Bundle();
//                            bundle.putSerializable("list1", list1);
//                            message.setData(bundle);
//                            handler.sendMessage(message);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addAll(list1);
                                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }).start();
        final LinearLayout tijiaoLayout = findViewById(R.id.xiaoxi_se);
        final int[] top = new int[1];
        top[0] = -1;
        ViewTreeObserver vto = tijiaoLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (top[0] == -1) {
                    top[0] = tijiaoLayout.getTop();
                }
                if (top[0] != tijiaoLayout.getTop()){
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    top[0] = tijiaoLayout.getTop();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                final String xx = editText.getText().toString();
                if (mDatas != null){
                    System.out.println("mDatas: " + mDatas.size());
                    pop(xx,userId, userName);
                }

            }
        });
    }
    private void pop(final String xx, final String userId , final String userName)
    {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.xiaoxi_liebiao, null);
        HomeAdapter1 mAdapter;
        mAdapter = new HomeAdapter1(mDatas, XiaoxiActivity.this);
        final RecyclerView recyclerView1 = view.findViewById(R.id.rc_liebiao);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(mAdapter);
        recyclerView1.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        final PopupWindow window = new PopupWindow(view,
                500, 800);
        final String[] user = {null};
        mAdapter.setOnItemClickLitener(new HomeAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                System.out.println("position：" + position);
                if (xx != null && !xx.equals("")){
                    final ArrayList<Map<String, String>> list1 = new ArrayList<>();
                    map = new HashMap<>();
                    map.put("leixing", "1");
                    map.put("xiaoxi", xx);
                    map.put("name", userName);
                    list1.add(map);
                    adapter.addAll(list1);
                    editText.getText().clear();
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    window.dismiss();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                            try {
                                socketFuWuQi.setXiaoxi(userId, xx,mDatas.get(position));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }else {
                    Toast toast;
                    toast = Toast.makeText(XiaoxiActivity.this,"请输入消息。",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    window.dismiss();
                }
            }
        });
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);


//         设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
//         在底部显示
        window.showAtLocation(XiaoxiActivity.this.findViewById(R.id.bt_send),
                Gravity.RIGHT|Gravity.BOTTOM, 0, 200);
        window.showAsDropDown(XiaoxiActivity.this.findViewById(R.id.bt_send));
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });
    }
}
