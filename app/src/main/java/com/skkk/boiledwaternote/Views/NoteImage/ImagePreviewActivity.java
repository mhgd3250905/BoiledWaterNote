package com.skkk.boiledwaternote.Views.NoteImage;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);
        initData();
        initUI();
        initEvent();
    }



    /**
     * 初始化数据
     */
    private void initData() {
        imageModle = (ImageModle) getIntent().getSerializableExtra(Configs.KEY_PREVIEW_IMAGE);
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        //toolbar
//        setSupportActionBar(toolbar);
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

                    return true;
                }else if (id==android.R.id.home){
                    onBackPressed();
                }
                return false;
            }
        });
    }

}
