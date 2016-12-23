package com.yuyh.library.imgsel.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;
import com.yuyh.library.imgsel.ImgSelConfig;
import com.yuyh.library.imgsel.R;
import com.yuyh.library.imgsel.bean.Image;
import com.yuyh.library.imgsel.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public class ImageListAdapter extends EasyRVAdapter<Image> {

    private boolean showCamera;
    private boolean mutiSelect;

    private ImgSelConfig config;
    private Context context;

    private List<Image> selectedImageList = new ArrayList<>();
    private OnItemClickListener listener;

    public ImageListAdapter(Context context, List<Image> list, ImgSelConfig config) {
        super(context, list, R.layout.item_img_sel, R.layout.item_img_sel_take_photo);
        this.context = context;
        this.config = config;
    }

    @Override
    protected void onBindData(EasyRVHolder viewHolder, final int position, final Image item) {

        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(position, item);
            }
        });

        //允许拍照，第一个位置是拍照图片
        if (position == 0 && showCamera) {
            ImageView iv = viewHolder.getView(R.id.ivTakePhoto);
            iv.setImageResource(R.drawable.ic_take_photo);
            return;
        }

        final ImageView iv = viewHolder.getView(R.id.ivImage);
        final FrameLayout frameLayout = viewHolder.getView(R.id.pi_picture_choose_item_select);
        config.loader.displayImage(context, item.path, iv);

        //我们知道在oncreate中View.getWidth和View.getHeight无法获得一个view的高度和宽度，这是因为View组件布局要在onResume回调后完成。
        //所以现在需要使用getViewTreeObserver().addOnGlobalLayoutListener()来获得宽度或者高度。这是获得一个view的宽度和高度的方法之一。
        //具体见http://blog.csdn.net/linghu_java/article/details/46544811
        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                //设置图片为正方形
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                params.height = params.width;
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        //多选情况下，点击确定选中图标切换
        if (mutiSelect) {
            viewHolder.setVisible(R.id.ivPhotoCheaked, true);
            if (selectedImageList.contains(item)) {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_checked);
                viewHolder.setVisible(R.id.pi_picture_choose_item_select, View.VISIBLE);
            } else {
                viewHolder.setImageResource(R.id.ivPhotoCheaked, R.drawable.ic_uncheck);
                viewHolder.setVisible(R.id.pi_picture_choose_item_select, View.GONE);
            }
        } else {
            viewHolder.setVisible(R.id.ivPhotoCheaked, false);
        }
    }

    //设置是否可以拍照
    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    //设置是否可以多选
    public void setMutiSelect(boolean mutiSelect) {
        this.mutiSelect = mutiSelect;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && showCamera) {
            return 1;
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void select(Image image, int position) {
        if (selectedImageList.contains(image)) {
            selectedImageList.remove(image);
        } else {
            selectedImageList.add(image);
        }
        notifyItemChanged(position); //选取后刷新此位置的ViewHolder
    }
}
