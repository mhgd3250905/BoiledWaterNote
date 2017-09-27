package com.skkk.boiledwaternote.Views.NoteImage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Modles.NoteModle;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends AppCompatActivity {

    @Bind(R.id.ivImagePreview)
    PhotoView ivImagePreview;
    @Bind(R.id.activity_image_preview)
    CoordinatorLayout activityImagePreview;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ImageModle imageModle;
    private int noteType;

    private NoteModle noteModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

//        WindowManager.LayoutParams attrs = getWindow().getAttributes();
//        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setAttributes(attrs);


        ButterKnife.bind(this);
        noteModle = new NoteModle(this);
        initData();
        initUI();
        initEvent();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        imageModle = (ImageModle) getIntent().getSerializableExtra(Configs.KEY_PREVIEW_IMAGE);
        noteType = getIntent().getIntExtra(Configs.KEY_NOTE_TYPE, 0);
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        //toolbar
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        //添加菜单
        toolbar.inflateMenu(R.menu.menu_image_preview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //设置图片
        if (imageModle.getnoteEditModel().getItemFlag() == NoteEditModel.Flag.IMAGE) {
            String imagePath = imageModle.getnoteEditModel().getImagePath();
            Glide.with(this)
                    .load(imagePath)
                    .into(ivImagePreview);
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.action_image_edit) {

                    return true;
                } else if (id == R.id.action_image_delete) {
                    DialogUtils.showDialog(ImagePreviewActivity.this, R.drawable.vector_drawable_notice,
                            "提醒", "是否删除该图片？",
                            "好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    noteModle.deleteImage(imageModle, noteType);
                                    Intent intent=new Intent();
                                    intent.putExtra(Configs.KEY_PREVIEW_IMAGE,imageModle);
                                    setResult(RESULT_OK,intent);
                                    onBackPressed();


                                }
                            }, "算了", null).show();
                    return true;
                } else if (id == android.R.id.home) {
                    onBackPressed();
                }
                return false;
            }
        });
    }

}
