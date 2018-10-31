package p03.example.li.xuncha;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XunchaDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    /**
     * 用于管理图片的滑动
     */
    List<Bitmap> aBitmap = null;
    private ViewPager viewPager;
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();;
    int imagePosition;
    private TextView tv_jiaoshi;
    private TextView tv_banji;
    private TextView tv_jiaoshi_lou;
    private TextView tv_jiaoshizhuangtai;
    private TextView tv_xueshengzhuangtai;
    private TextView tv_date;
    private TextView tv_kecheng;
    private TextView tv_xuncharen;
    private String userId;
    /**
     * 显示当前图片的页数
     */
    private TextView pageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_details);
        Intent bundle = getIntent();
        userId = bundle.getStringExtra("userId");
        imagePosition = bundle.getIntExtra("positiion1", 1);
        SourceDateList = new ArrayList<SortModel>();
        SourceDateList = (List<SortModel>) bundle.getSerializableExtra("xx");
        pageText = (TextView) findViewById(R.id.page_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);//设置缓存个数
        viewPager.setCurrentItem(imagePosition);
        viewPager.setOnPageChangeListener(XunchaDetailsActivity.this);
        // 设定当前的页数和总页数
        pageText.setText((imagePosition + 1) + "/" + SourceDateList.size());
//        Toast.makeText(this, paths.get(imagePosition).get("name"),
//                Toast.LENGTH_SHORT).show();
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    class ViewPagerAdapter extends PagerAdapter {
        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String xx = SourceDateList.get(position).getVolid();
            View view = LayoutInflater.from(XunchaDetailsActivity.this).inflate(
                    R.layout.activity_ji_lu_xiang_qing, null);
            Toolbar tb = view.findViewById(R.id.xiangqin_toolbar);
            tb.setBackgroundColor(0xff0082c8);
            setSupportActionBar(tb);
            final ImageView imageView = view.findViewById(R.id.fanhui_jiluxiangqing);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            final String[] xinxi = xx.split(",");
            //0为日期1为teacher_name 2为course_name 3为班级 4为教学楼 5为教师状态  6为学生状态
            tv_date = view.findViewById(R.id.date);
            tv_jiaoshi = view.findViewById(R.id.jiaoshi);
            tv_kecheng = view.findViewById(R.id.kecheng);
            tv_banji = view.findViewById(R.id.banji);
            tv_jiaoshi_lou = view.findViewById(R.id.jiaoshi_lou);
            tv_jiaoshizhuangtai = view.findViewById(R.id.jiaoshi_zhuangtai);
            tv_xueshengzhuangtai = view.findViewById(R.id.xuesheng_zhuangtai);
            tv_xuncharen = view.findViewById(R.id.xuncharen);
            tv_date.setText(xinxi[0]);
            tv_jiaoshi.setText(xinxi[1]);
            tv_kecheng.setText(xinxi[2]);
            tv_banji.setText(xinxi[3]);
            tv_jiaoshi_lou.setText(xinxi[4]);
            tv_jiaoshizhuangtai.setText(xinxi[5]);
            tv_xueshengzhuangtai.setText(xinxi[6]);
            tv_xuncharen.setText(userId);


            final GridView gv = view.findViewById(R.id.x8);
            //应该硬盘缓存，内存缓存不能用。
            final ImageCacheUtil imageCacheUtil = new ImageCacheUtil(getApplicationContext());
            System.out.println("缓存图片为:  " + imageCacheUtil.getBitmap(xinxi[0] + xinxi[1]));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (imageCacheUtil.getBitmap(xinxi[0] + xinxi[1]) == null || imageCacheUtil.getBitmap(xinxi[0] + xinxi[1]).isEmpty()) {
                            SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                            final int length = socketFuWuQi.getLenth(xinxi[0], xinxi[1]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (length != 0) {
                                        final List<Integer> imags = new ArrayList<Integer>();
                                        for (int i = 0; i < length; i++) {
                                            imags.add(R.drawable.zhengzaijiazai);
                                        }
                                        Integer[] imagess = new Integer[imags.size()];
                                        for (int i = 0; i < imags.size(); i++) {
                                            imagess[i] = imags.get(i);
                                        }
                                        gv.setAdapter(new MyAdapter1(XunchaDetailsActivity.this, imagess));
//                                注册监听事件
                                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                Toast.makeText(XunchaDetailsActivity.this, "pic" + position, Toast.LENGTH_SHORT).show();
                                                Intent it = new Intent(XunchaDetailsActivity.this, ImageDetailsActivity.class);
//                it.putExtra("image_position", position);
                                                Bundle bundle = new Bundle();
                                                bundle.putIntegerArrayList("imags", (ArrayList<Integer>) imags);
                                                bundle.putInt("image_position", position);
                                                it.putExtras(bundle);
                                                startActivityForResult(it, 1);
                                            }
                                        });
                                    }
                                }
                            });
                            final List<Bitmap> bitmap = socketFuWuQi.huoQuTuPian(xinxi[0], xinxi[1]);
                            for (int i = 0; i < bitmap.size(); i++) {
                                imageCacheUtil.putBitmap(xinxi[0] + xinxi[1], bitmap.get(i));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gv.setAdapter(new MyAdapter1(XunchaDetailsActivity.this, bitmap));

                                    //注册监听事件
                                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                            Toast.makeText(XunchaDetailsActivity.this, "pic" + position, Toast.LENGTH_SHORT).show();
                                            Intent it = new Intent(XunchaDetailsActivity.this, ImageDetailsActivity.class);
//                it.putExtra("image_position", position);
                                            final List<Integer> integers = null;
                                            Bundle bundle = new Bundle();
                                            bundle.putIntegerArrayList("imags", (ArrayList<Integer>) integers);
                                            bundle.putString("bitmap", xinxi[0] + xinxi[1]);
                                            bundle.putInt("image_position", position);
                                            it.putExtras(bundle);
                                            startActivityForResult(it, 1);
                                        }
                                    });
                                }
                            });
                        } else {
                            System.out.println("是用的是缓存图片");
                            final List<Bitmap> bitmap = imageCacheUtil.getBitmap(xinxi[0] + xinxi[1]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gv.setAdapter(new MyAdapter1(XunchaDetailsActivity.this, bitmap));
                                    //注册监听事件
                                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                            Toast.makeText(XunchaDetailsActivity.this, "pic" + position, Toast.LENGTH_SHORT).show();
                                            Intent it = new Intent(XunchaDetailsActivity.this, ImageDetailsActivity.class);
//                it.putExtra("image_position", position);
                                            final List<Integer> integers = null;
                                            Bundle bundle = new Bundle();
                                            bundle.putIntegerArrayList("imags", (ArrayList<Integer>) integers);
                                            bundle.putString("bitmap", xinxi[0] + xinxi[1]);
                                            bundle.putInt("image_position", position);
                                            it.putExtras(bundle);
                                            startActivityForResult(it, 1);
                                        }
                                    });
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            container.addView(view);
            return view;
        }


        @Override
        public int getCount() {
            return SourceDateList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int currentPage) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        imagePosition = currentPage + 1;
        pageText.setText((currentPage + 1) + "/" + SourceDateList.size());
//            Toast.makeText(this, paths.get(currentPage).get("name"),
//                    Toast.LENGTH_SHORT).show();
    }

}

class MyAdapter1 extends BaseAdapter {
    //上下文对象
    private Context context;
    //图片数组
    private Integer[] imgs;
    private List<Bitmap> bitmap = null;

    MyAdapter1(Context context, Integer[] imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    MyAdapter1(Context context, List<Bitmap> bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    public int getCount() {
        if (bitmap != null) {
            return bitmap.size();
        }else {
            return imgs.length;
        }
    }

    public Object getItem(int item) {
        return item;
    }
    public long getItemId(int id) {
        return id;
    }
    //创建View方法
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));//设置ImageView对象布局
            imageView.setAdjustViewBounds(true);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(1, 1, 1, 1);//设置间距
        } else {
            imageView = (ImageView) convertView;
        }
        if (bitmap == null) {
            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
        } else {
            imageView.setImageBitmap(bitmap.get(position));
        }
        return imageView;
    }
}

