package p03.example.li.xuncha;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
/**
 * Created by li on 2017/12/13.
 */

public class ImgGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imgList;
    private int maxImgCount;
    private LayoutInflater inflater;
    private FreshImgCallBack freshImgCallBack;//针对三种操作逻辑所自定义的回调

    public ImgGridAdapter(Context context, ArrayList<String> imgList, int maxImgCount, FreshImgCallBack freshImgCallBack) {
        this.context = context;
        this.imgList = imgList;
        this.maxImgCount = maxImgCount;
        this.freshImgCallBack = freshImgCallBack;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 设置回调
     *
     * @param callBack freshImgCallBack
     */
    public void setImgShowFresh(FreshImgCallBack callBack) {
        freshImgCallBack = callBack;
    }

    @Override
    public int getCount() {
        if (imgList.size() < maxImgCount) {
            return imgList.size() + 1;
        } else {
            return maxImgCount;
        }
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.itme_gridview, null);
            holder.sdvItemShowImg = (SimpleDraweeView) convertView.findViewById(R.id.sdvItemShowImg);
            holder.ivDeleteImg =  convertView.findViewById(R.id.ivDeleteImg);
            holder.ivItemAdd =  convertView.findViewById(R.id.ivItemAdd);
            holder.ivItemAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    freshImgCallBack.openGallery();//打开相册放在里面即可
                }
            });
            holder.rlItemShow = (RelativeLayout) convertView.findViewById(R.id.rlItemShow);
        }
        //——————————————————————————————设置图片逻辑——————————————————————————————
        holder.ivItemAdd.setVisibility(View.GONE);
        holder.rlItemShow.setVisibility(View.GONE);
        if (imgList.size() < 5) {
            if (position == getCount() - 1) {
                holder.ivItemAdd.setVisibility(View.VISIBLE);
            } else {
                if (getCount() > 1) {
                    showImg(position, holder);
                }
            }
        } else {
            showImg(position, holder);
        }

        //放在外面用于更新position
        holder.ivDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freshImgCallBack.updateGvImgShow(position);//更新数据
            }
        });
        return convertView;
    }

    //显示图片
    private void showImg(int position, ViewHolder holder) {
        holder.ivItemAdd.setVisibility(View.GONE);
        holder.rlItemShow.setVisibility(View.VISIBLE);
        //设置图片
        System.out.println("imgList: " + imgList);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + imgList.get(position)))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(100, 100))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .setOldController(holder.sdvItemShowImg.getController())
                .build();
        holder.sdvItemShowImg.setController(controller);
    }

    class ViewHolder {
        SimpleDraweeView sdvItemShowImg;
        ImageView ivDeleteImg;
        ImageView ivItemAdd;
        RelativeLayout rlItemShow;
    }

}