package p03.example.li.xuncha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelView.OnWheelItemSelectedListener;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TianJiaXunChaActivity extends Fragment implements FreshImgCallBack {

    private TextView tv;
    private String name;
    private View view1;
    private WheelView mainWheelView;
    String[] teacher = new String[200];
    String[] keCheng = new String[200];
    String[] banji = new String[200];
    private int i;
    private WheelView subWheelView;
    private String userId;

    private WheelView.WheelViewStyle style;
    private WheelView childWheelView;
    private static final int REQUEST_CODE_GALLERY = 100;//打开相册
    private static final int REQUEST_CODE_PREVIEW = 101;//预览图片

    private GridView gvImage;
    private ImgGridAdapter adapter;
    private ArrayList<String> imgList = new ArrayList<String>();
    private ArrayList<String> imgList1 = new ArrayList<String>();
    private final static int maxImgSize = 5;
    private ImageView imageView;
    private WrapListView listView;
    private EditText editText_xuesheng;
    private EditText editText_jiaoshi;

    private ArrayList<String> list;
    private ArrayList<String> list1;
    private Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private ArrayAdapter<String> arrayAdapter;
    private String xiaoxi;
    private ImageView textView;
    private ImageView fanhui;
    private AutoCompleteTextView teacher_name;
    private AutoCompleteTextView course_name;
    private AutoCompleteTextView banji_name;
    private String teacherName;
    private  List<String> jiaoshi  = new ArrayList<>();


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
            }
            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
            UIHelper.stop();
            teacher_name.setText("");
            course_name.setText("");
            banji_name.setText("");
            tv.setText("请选择教学楼");
            editText_xuesheng.setText("");
            editText_jiaoshi.setText("");
            imgList.clear();
            System.out.println("初始化：" + imgList.size());
            adapter.notifyDataSetChanged();


//            Intent intent = new Intent();
//            intent.putExtra("a", "a");
//            intent.setClass(TianJiaXunChaActivity.this, MainActivity.class);
//            TianJiaXunChaActivity.this.setResult(2, intent);
//            finish();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        userId = ((MainActivity) activity).getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.activity_tian_jia_xun_cha, null);
        initFresco();
        initViews(view1);
        return view1;
    }


    public void initViews(final View view) {
        //让布局向上移来显示软键盘
    //        Intent intent = getIntent();
//        int bb = intent.getIntExtra("bb", 0);
//        final String useId = intent.getStringExtra("userId");
        imageView = view.findViewById(R.id.dropview_image);
        editText_xuesheng = view.findViewById(R.id.xuesheng_et);
        editText_jiaoshi = view.findViewById(R.id.jiaoshi_et);
        teacher_name = view.findViewById(R.id.jiaoshi_autotextView);
        teacher_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher_name.showDropDown();
            }
        });
        banji_name = view.findViewById(R.id.banji_autotextView);
        banji_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banji_name.showDropDown();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    teacher = socketFuWuQi.teacherInformation();
                    Set<String> set = new HashSet<String>();
                    list = new ArrayList<String>();
                    //去掉重复
                    for (int i = 0; i < teacher.length; i++) {
                        if (set.add(teacher[i]) && !teacher[i].isEmpty() && !teacher[i].equals("null")) {
                            list.add(teacher[i]);
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("改变后的teacher: " + list.get(i));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("改变r: ");
                            arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                            teacher_name.setAdapter(arrayAdapter);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        course_name = view.findViewById(R.id.kecheng_autotextView);
        course_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_name.showDropDown();
            }
        });
        teacher_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name = parent.getItemAtPosition(position).toString();
                teacherName = name;
                System.out.println("name: " + name);
                if (!name.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                            try {
                                keCheng = socketFuWuQi.keCheng(name).getKeCheng();
                                banji = socketFuWuQi.keCheng(name).getBanji();
                                Set<String> set = new HashSet<String>();
                                list1 = new ArrayList<String>();
                                list = new ArrayList<String>();
                                //去掉重复
                                for (int i = 0; i < keCheng.length; i++) {
                                    if (set.add(keCheng[i]) && !keCheng[i].isEmpty() && !keCheng[i].equals("null")) {
                                        list.add(keCheng[i]);
                                    }
                                }
                                for (int i = 0; i < banji.length; i++) {
                                    if (set.add(banji[i]) && !banji[i].isEmpty() && !banji[i].equals("null")) {
                                        list1.add(banji[i]);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                                    course_name.setAdapter(arrayAdapter);
                                    if (list.size() > 0) {
                                        course_name.setText(list.get(0));
                                    }
                                    arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list1);
                                    banji_name.setAdapter(arrayAdapter);
                                    if (list1.size()>0) {
                                        banji_name.setText(list1.get(0));
                                        final String banji = list1.get(0);
                                        System.out.println("banji: " + banji);

                                        if (!banji.isEmpty() && !teacherName.isEmpty()) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                                                    try {
                                                        jiaoshi = socketFuWuQi.banjiInformation(teacherName, banji);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (jiaoshi != null && jiaoshi.size() > 0) {
                                                                TianJiaXunChaActivity.this.name = jiaoshi.get(0);
                                                                tv.setText("你的选择是：" + jiaoshi.get(0));
                                                            }
                                                        }
                                                    });

                                                }
                                            }).start();
                                        }
                                    }

                                }
                            });

                        }
                    }).start();
                }
            }
        });
        banji_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String banji = parent.getItemAtPosition(position).toString();
                System.out.println("banji: " + banji);

                if (!banji.isEmpty() && !teacherName.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                            try {
                                jiaoshi = socketFuWuQi.banjiInformation(teacherName, banji);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (jiaoshi != null && jiaoshi.size() > 0) {
                                        name = jiaoshi.get(0);
                                        tv.setText("你的选择是：" + jiaoshi.get(0));
                                    }
                                }
                            });

                        }
                    }).start();
                }
            }
        });
//
//        fanhui = view.findViewById(R.id.fanhui);
//        fanhui.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        textView = view.findViewById(R.id.tijiao);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("teacher_name: " + teacher_name.getText());
                if (teacher_name.getText() == null || teacher_name.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "请填写教师名", Toast.LENGTH_SHORT).show();
                } else if (course_name.getText() == null || course_name.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "请填写课程名", Toast.LENGTH_SHORT).show();
                } else if (banji_name.getText() == null || banji_name.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "请填写班级名", Toast.LENGTH_SHORT).show();
                } else if (name == null || name.isEmpty()) {
                    Toast.makeText(getContext(), "请选择所在教室", Toast.LENGTH_SHORT).show();
                } else if (editText_jiaoshi.getText() == null || editText_jiaoshi.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "请填写教师状态", Toast.LENGTH_SHORT).show();
                } else if (course_name.getText() == null || course_name.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "请填写学生状态", Toast.LENGTH_SHORT).show();
                } else {
                    LianJieWangLuo tj = new LianJieWangLuo(userId, teacher_name.getText().toString(), course_name.getText().toString(), banji_name.getText().toString(), name, editText_jiaoshi.getText().toString(), editText_xuesheng.getText().toString(), imgList);
                    tj.start();
                    UIHelper.showDialogForLoading(getContext(), "正在上传，请稍后。");
                }
            }
        });
        setMap();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                showPopwindow(map.get("xuesheng"), R.id.dropview_image);
            }
        });
        imageView = view.findViewById(R.id.dropview_image1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                showPopwindow(map.get("jiaoshi"), R.id.dropview_image1);
            }
        });
        tv = view.findViewById(R.id.jiaoxuelou);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop(view);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

            }
        });
        gvImage = (GridView) view.findViewById(R.id.gvImage);
        adapter = new ImgGridAdapter(getContext(), imgList, maxImgSize, this);
        adapter.setImgShowFresh(this);//实现刷新接口
        gvImage.setAdapter(adapter);
//        initWheel1();
        if (name != null && !name.equals("")) {
            tv.setText("你的选择是：" + name);
        } else {
            tv.setText("请选择教学楼");

        }
//        if (bb == 1) {
//            String a1_teacher = intent.getStringExtra("teacher");
//            String a1_course_name = intent.getStringExtra("course_name");
//            String a1_class = intent.getStringExtra("class2");
//            String a1_classroom = intent.getStringExtra("classroom");
//            teacher_name.setText(a1_teacher);
//            course_name.setText(a1_course_name);
//            banji_name.setText(a1_class);
//            tv.setText(a1_classroom);
//            editText_jiaoshi.requestFocus();
//        }
    }

    /*  * 初始化Fresco
   * 使用ImagePipelineConfig的原因是为了支持不同格式图片的压缩
   */
    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getContext())
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(getContext(), config);
    }


    /**
     * 联动WheelView
     */
    private void initWheel1(View view) {
        mainWheelView = (WheelView) view.findViewById(R.id.main_wheelview);
        mainWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));  //设置滚轮数据适配器s
        mainWheelView.setSkin(WheelView.Skin.Holo);  //设置背景颜色
        mainWheelView.setWheelData(createMainDatas()); //设置滚轮数据
        style = new WheelView.WheelViewStyle(); //设置选中与未选中字体的样式
        style.selectedTextSize = 20;
        style.textSize = 16;
        mainWheelView.setStyle(style);
        subWheelView = (WheelView) view.findViewById(R.id.sub_wheelview);
        subWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        subWheelView.setSkin(WheelView.Skin.Holo);
        subWheelView.setWheelData(createSubDatas().get(createMainDatas().get(mainWheelView.getSelection())));
        subWheelView.setStyle(style);
        mainWheelView.join(subWheelView); //连接副WheelView  楼
        mainWheelView.joinDatas(createSubDatas()); //副WheelView 楼层

        childWheelView = (WheelView) view.findViewById(R.id.child_wheelview);
        childWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        childWheelView.setSkin(WheelView.Skin.Holo);
        childWheelView.setStyle(style);
        childWheelView.setWheelData(createChildDatas(0).get(createSubDatas().get(createMainDatas().get(mainWheelView
                .getSelection())).get(subWheelView.getSelection())));
        subWheelView.join(childWheelView); //连接副WheelView 教室
        mainWheelView.setOnWheelItemSelectedListener(new OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                i = position;
                subWheelView.joinDatas(createChildDatas(i)); //只需把联动函数放入滑动监听即可
//                name = o.toString();
//                System.out.println("滑轮位置：" + name);
                name = mainWheelView.getSelectionItem().toString() + " " + subWheelView.getSelectionItem().toString() + " " + childWheelView.getSelectionItem().toString();
                tv.setText("你的选择是：" + name);
            }
        });
        subWheelView.setOnWheelItemSelectedListener(new OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                name = mainWheelView.getSelectionItem().toString() + " " + subWheelView.getSelectionItem().toString() + " " + childWheelView.getSelectionItem().toString();
                tv.setText("你的选择是：" + name);
            }
        });
        childWheelView.setOnWheelItemSelectedListener(new OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                name = mainWheelView.getSelectionItem().toString() + " " + subWheelView.getSelectionItem().toString() + " " + childWheelView.getSelectionItem().toString();
                tv.setText("你的选择是：" + name);
            }
        });


    }

    private List<String> createMainDatas() {
        String[] strings = { "2号教学楼",  "咨询楼"};
        return Arrays.asList(strings);
    }


    private HashMap<String, List<String>> createSubDatas() {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        String[] strings = {"2号教学楼", "咨询楼"};
        String[] s1 = {"1楼", "2楼", "3楼", "4楼", "5楼"};
        String[] s2 = {"1楼", "2楼", "3楼", "4楼", "5楼"};
        String[][] ss = {s1, s2};
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], Arrays.asList(ss[i]));
        }
        return map;
    }


    private HashMap<String, List<String>> createChildDatas(int i) {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        String[] strings = {"1楼", "2楼", "3楼", "4楼", "5楼"};
        String[] s1 = new String[11];
        String[] s2 = new String[11];
        String[] s3 = new String[11];
        String[] s4 = new String[11];
        String[] s5 = new String[11];
        int b1, b2, b3, b4, b5;
        switch (i) {
            case 0: {
                b1 = 2101;
                b2 = 2201;
                b3 = 2301;
                b4 = 2401;
                b5 = 2501;
                for (int q = 0; q <= 10; q++) {
                    s1[q] = String.valueOf(b1);
                    s2[q] = String.valueOf(b2);
                    s3[q] = String.valueOf(b3);
                    s4[q] = String.valueOf(b4);
                    s5[q] = String.valueOf(b5);
                    b1++;
                    b2++;
                    b3++;
                    b4++;
                    b5++;
                }
                String[][] ss = {s1, s2, s3, s4, s5};
                for (int a = 0; a < strings.length; a++) {
                    map.put(strings[a], Arrays.asList(ss[a]));
                }
            }
            break;
            case 1: {
                b1 = 101;
                b2 = 201;
                b3 = 301;
                b4 = 401;
                b5 = 501;
                for (int q = 0; q <= 10; q++) {
                    s1[q] = String.valueOf(b1);
                    s2[q] = String.valueOf(b2);
                    s3[q] = String.valueOf(b3);
                    s4[q] = String.valueOf(b4);
                    s5[q] = String.valueOf(b5);
                    b1++;
                    b2++;
                    b3++;
                    b4++;
                    b5++;
                }
                String[][] ss = {s1, s2, s3, s4, s5};
                for (int a = 0; a < strings.length; a++) {
                    map.put(strings[a], Arrays.asList(ss[a]));
                }
            }
            break;
        }

        return map;
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CODE_GALLERY:
//                if (resultCode == RESULT_OK) {
//                    imgList.clear();//不可直接指向
//                    adapter.notifyDataSetChanged();
//                }
//                break;
//        }
//    }

    //更新图片：当前用于删除
    @Override
    public void updateGvImgShow(int position) {
        imgList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openGallery() {
        Album.camera(this)//打开相机
//                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .image() // 拍照  。
                .requestCode(1)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        imgList.add(result);
                        galleryAddPic();
                        adapter.notifyDataSetChanged();
                    }
                })
                .start();
    }

    //将图片添加进手机相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.parse("file://" + imgList.get(0)));
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void previewImg(int position) {
        Album.gallery(this)//预览图片
                .requestCode(REQUEST_CODE_PREVIEW)
                .checkedList(imgList)
                .currentPosition(position)
                .checkable(false)
                .start();
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow(ArrayList<String> list1, final int layout_id) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                300,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        listView = (WrapListView) view.findViewById(R.id.first_lv);
        //初始化适配器
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list1);
        //设置适配器
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xiaoxi = parent.getItemAtPosition(position).toString();
                switch (layout_id) {
                    case R.id.dropview_image:
                        editText_xuesheng.setText(editText_xuesheng.getText().toString() + xiaoxi);
                        editText_xuesheng.requestFocus();
                        editText_xuesheng.setSelection(editText_xuesheng.getText().toString().length());
                        window.dismiss();
                        break;
                    case R.id.dropview_image1:
                        editText_jiaoshi.setText(editText_jiaoshi.getText().toString() + xiaoxi);
                        editText_jiaoshi.requestFocus();
                        editText_jiaoshi.setSelection(editText_jiaoshi.getText().toString().length());
                        window.dismiss();
                        break;
                }

            }
        });
        // 实例化一个ColorDrawable颜色为半透明

        window.showAsDropDown(getActivity().findViewById(layout_id));
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                window.dismiss();
            }
        });

    }

    private void pop(View v) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop1, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        initWheel1(view);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);


//         设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
//         在底部显示
        window.showAtLocation(v.findViewById(R.id.dropview_image),
                Gravity.BOTTOM, 0, 0);
        window.showAsDropDown(v.findViewById(R.id.jiaoxuelou), 0, 0);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                window.dismiss();
            }
        });
        window.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !window.isFocusable()) {
                    window.dismiss();
                    System.out.println("点击外面了");
                    return true;
                }
                return false;
            }
        });
    }

    //默认快捷用语
    private void setMap() {
        list = new ArrayList<String>();
        list.add("教师正在走动");
        list.add("教师正在讲课");
        list.add("教师正在为学生解答");
        map.put("jiaoshi", list);
        list = new ArrayList<String>();
        list.add("迟到 人");
        list.add("缺勤 人");
        list.add("带食物 人");
        list.add("玩手机和游戏 人");
        list.add("睡觉 人");
        list.add("其他");
        map.put("xuesheng", list);
//        KuaiJieDatabase kuaiJieDatabase = new KuaiJieDatabase(getContext(), "KuaiJieDatabase.db", null, 2);
//        final SQLiteDatabase db = kuaiJieDatabase.getWritableDatabase();
//        Cursor cursor = db.query("KuaiJieDatabase", new String[]{"t1", "t2", "t3", "t4", "id", "status"}, "flag=? or flag=?", new String[]{"studet_true", "teacher_true"}, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                list = new ArrayList<String>();
//                list.add(cursor.getString(cursor.getColumnIndex("t1")));
//                list.add(cursor.getString(cursor.getColumnIndex("t2")));
//                list.add(cursor.getString(cursor.getColumnIndex("t3")));
//                list.add(cursor.getString(cursor.getColumnIndex("t4")));
//                String ss = cursor.getString(cursor.getColumnIndex("status"));
//                if (ss.equals("老师状态")) {
//                    map.put("jiaoshi", list);
//                } else if (ss.equals("学生状态")) {
//                    map.put("xuesheng", list);
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();

    }


    class LianJieWangLuo extends Thread {
        String teacher_name;
        String course_name;
        String jiaoxuelou;
        String jiaoshi;
        String xueshheng;
        String userId;
        String banji_name;
        ArrayList<String> uri = null;

        public LianJieWangLuo(String userId, String teacher_name, String course_name, String banji_name, String jiaoxuelou, String jiaoshi, String xueshheng, ArrayList<String> uri) {
            this.userId = userId;
            this.teacher_name = teacher_name;
            this.course_name = course_name;
            this.jiaoxuelou = jiaoxuelou;
            this.jiaoshi = jiaoshi;
            this.xueshheng = xueshheng;
            this.uri = uri;
            this.banji_name = banji_name;
        }

        @Override
        public void run() {
            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
            try {
                String str = userId + "," + teacher_name + "," + course_name + "," + banji_name + "," + jiaoxuelou + "," + jiaoshi + "," + xueshheng;
                if (uri.size() > 0)
                    socketFuWuQi.shangChuanTuPian(str, uri);
                else
                    socketFuWuQi.shangChuanTuPian1(str);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
