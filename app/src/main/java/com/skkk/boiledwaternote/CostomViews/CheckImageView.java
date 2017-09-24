package com.skkk.boiledwaternote.CostomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skkk.boiledwaternote.Collect;
import com.skkk.boiledwaternote.R;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：可以长按选择的ImageView
* 作    者：ksheng
* 时    间：2017/9/24$ 18:14$.
*/
public class CheckImageView extends RelativeLayout{
    private ImageView ivImage;
    private ImageView ivImageChecked;
    private boolean isEdit;
    private int imageSrc,imageCheckedSrc;
    private int pos;
    private Collect.OnImageFragmentItemClickListener imageClickListener;

    public CheckImageView(Context context) {
        super(context);
        initUI();
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        initAttrs(attrs);//初始化自定义属性
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
        initAttrs(attrs);//初始化自定义属性
    }

    /**
     * 初始化自定义属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray ta=getContext().obtainStyledAttributes(attrs,R.styleable.CheckImageView);
        imageSrc=ta.getInteger(R.styleable.CheckImageView_imageSrc,0);
        imageCheckedSrc=ta.getInteger(R.styleable.CheckImageView_imageCheckedSrc,0);
        ta.recycle();
    }

    /*
    * 初始化界面
    * */
    private void initUI() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_check_image, this, true);
        ivImage = (ImageView) view.findViewById(R.id.iv_note_image);
        ivImageChecked = (ImageView) view.findViewById(R.id.iv_note_iamge_checked_bg);
        if (imageSrc!=0){
            ivImage.setImageResource(imageSrc);
        }
        if (imageCheckedSrc!=0){
            ivImageChecked.setImageResource(imageCheckedSrc);
        }
        setEdit(false);//正常状态
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        if (isEdit){
            ivImageChecked.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }


    /**
     * 获取编辑状态
     * @return
     */
    public boolean isEdit() {
        return isEdit;
    }


    /**
     * 设置编辑状态
     * @param isEdit
     */
    public void setEdit(boolean isEdit) {
        if (ivImageChecked==null){
            return;
        }
        this.isEdit=isEdit;
        ivImageChecked.setVisibility(isEdit?VISIBLE:GONE);
    }

    /**
     * 设置点击监听事件
     * @param imageClickListener
     */
    public void setImageClickListener(Collect.OnImageFragmentItemClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }
    
    
}
