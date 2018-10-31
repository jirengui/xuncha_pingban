package p03.example.li.xuncha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XunChuaJiLu extends AppCompatActivity {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<SortModel> SourceDateList = new ArrayList<SortModel>();

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private String[] jilu = new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xun_chua_ji_lu);
        initViews();

    }

    private void initViews() {
        ButterKnife.bind(this);
        Toolbar tb = (Toolbar) findViewById(R.id.xuncha_toolbar);
        tb.setBackgroundColor(Color.parseColor("#32CD32"));
        setSupportActionBar(tb);
        ImageView imageView = findViewById(R.id.fanhui_xuncha);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        jilu = intent.getStringArrayExtra("jilu");
        final String userId = intent.getStringExtra("userId");
        final TextView tv_qiehuan = findViewById(R.id.weigui);
        final Blank2 myFragment1 = new Blank2();
        final BlankFragment myFragment = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("jiLu",jilu);//这里的values就是我们要传的值
        bundle.putString("userId", userId);
        myFragment.setArguments(bundle);
        myFragment1.setArguments(bundle);
        tabLayout = findViewById(R.id.tab);
        viewpager = findViewById(R.id.viewpager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(myFragment1);
        fragments.add(myFragment);
        tv_qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_qiehuan.getText().equals("异常")) {
                    myFragment.update(1);
                    myFragment1.update(1);
                    tv_qiehuan.setText("全部");
                }else if (tv_qiehuan.getText().equals("全部")){
                    myFragment.update(2);
                    myFragment1.update(2);
                    tv_qiehuan.setText("异常");
                }
            }
        });
        TitleFragmentPagerAdapter adapter1 = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"按时间排序", "按姓名排序"});
        viewpager.setAdapter(adapter1);
        tabLayout.setupWithViewPager(viewpager);

    }
}