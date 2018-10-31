package p03.example.li.xuncha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ZoomImageView zoomImageView;
    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;
    private List<Integer> paths = new ArrayList<Integer>();
    List<Bitmap> aBitmap = new ArrayList<Bitmap>();
    int imagePosition;
    private ViewPagerAdapter adapter;
    private Boolean isNo = false;

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
        Bundle bundle = this.getIntent().getExtras();
        imagePosition = getIntent().getIntExtra("image_position", 1);
        if (bundle.getIntegerArrayList("imags") == null)
        {
            isNo = true;
            String url = bundle.getString("bitmap");
            ImageCacheUtil imageMemoryCache = new ImageCacheUtil(getApplicationContext());
            aBitmap = imageMemoryCache.getBitmap(url);
            adapter = new ViewPagerAdapter(aBitmap,ImageDetailsActivity.this,"a");
            pageText = (TextView) findViewById(R.id.page_text);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(imagePosition);
            viewPager.addOnPageChangeListener(ImageDetailsActivity.this);
            // 设定当前的页数和总页数
            pageText.setText((imagePosition + 1) + "/" + aBitmap.size());
            pageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            isNo = false;
            paths = bundle.getIntegerArrayList("imags");
            adapter = new ViewPagerAdapter(paths,ImageDetailsActivity.this);
            pageText = (TextView) findViewById(R.id.page_text);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(imagePosition);
            viewPager.addOnPageChangeListener(ImageDetailsActivity.this);
            // 设定当前的页数和总页数
            pageText.setText((imagePosition + 1) + "/" + paths.size());
            pageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

//        Toast.makeText(this, paths.get(imagePosition).get("name"),
//                Toast.LENGTH_SHORT).show();


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
        if (isNo){
            pageText.setText((currentPage + 1) + "/" + aBitmap.size());

        }else {
            pageText.setText((currentPage + 1) + "/" + paths.size());
        }
//            Toast.makeText(this, paths.get(currentPage).get("name"),
//                    Toast.LENGTH_SHORT).show();
    }

}
