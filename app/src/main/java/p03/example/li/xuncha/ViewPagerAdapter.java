package p03.example.li.xuncha;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by li on 2018/1/17.
 */

public class ViewPagerAdapter extends PagerAdapter{
    private List<Integer> paths = new ArrayList<Integer>();
    private Activity activity;
    private List<Bitmap> aBitmap = null;

    public ViewPagerAdapter(List<Integer> paths,Activity activity){
        this.paths = paths;
        this.activity = activity;
    }
    public ViewPagerAdapter(List<Bitmap> aBitmap,Activity activity, String a){
        this.aBitmap = aBitmap;
        this.activity = activity;
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
//            String imagePath = paths.get(position);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap bitmap = null;
        if (aBitmap == null) {
            bitmap = BitmapFactory.decodeResource(container.getResources(),
                    paths.get(position));
        }else {
                bitmap = aBitmap.get(position);

        }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(container.getResources(),
                        R.drawable.zhengzaijiazai);
            }
            View view = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.zoom_image_layout, null);
            ZoomImageView zoomImageView = (ZoomImageView) view
                    .findViewById(R.id.zoom_image_view);
            zoomImageView.setImageBitmap(bitmap);
            zoomImageView.fin(activity);
            container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (aBitmap != null)
        {
            return aBitmap.size();
        }
        return paths.size();
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
