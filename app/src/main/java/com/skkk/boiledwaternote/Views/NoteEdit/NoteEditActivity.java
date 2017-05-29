package com.skkk.boiledwaternote.Views.NoteEdit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.MyItemTouchHelperCallback;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Presenters.NoteEdit.NoteEditPresenter;
import com.skkk.boiledwaternote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteEditActivity extends AppCompatActivity {

    private final String TAG = NoteEditActivity.class.getSimpleName();

    @Bind(R.id.tb_note_edit)
    Toolbar tbNoteEdit;
    @Bind(R.id.rv_note_edit)
    RecyclerView rvNoteEdit;
    @Bind(R.id.btn_insert)
    Button btnInsert;

    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CROP_REQUEST = 2;
    public final static int CAMERA_REQUEST_CODE = 3;

    //文件拍照保存的位置
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/SKRecyclerViewDemo/camera/";// 拍照路径
    private String cameraPath;

    private List<NoteEditModel> mDataList;
    private MyItemTouchHelperCallback callback;
    private ItemTouchHelper itemTouchHelper;
    private NoteEditAdapter adapter;
    private LinearLayoutManager layoutManager;
    private NoteEditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        ButterKnife.bind(this);
        presenter = new NoteEditPresenter(this);
        initUI();           //初始化UI
        initEvent();        //初始化各种事件
    }

    @Override
    protected void onStop() {
        checkAndSyncItem(rvNoteEdit);       //同步数据
        super.onStop();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tbNoteEdit.setTitle("");
        tbNoteEdit.setNavigationIcon(R.drawable.back_arrow);

        mDataList = loadData();
        //设置RecyclerView...
        layoutManager = new LinearLayoutManager(this);
        rvNoteEdit.setLayoutManager(layoutManager);
        adapter = new NoteEditAdapter(this, mDataList);
        rvNoteEdit.setAdapter(adapter);
        rvNoteEdit.setItemAnimator(new DefaultItemAnimator());


        //设置ItemTouchHelper
        callback = new MyItemTouchHelperCallback(this, adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvNoteEdit);

    }

    /**
     * 初始化各种事件
     */
    private void initEvent() {
        //设置Item拖拽监听
        adapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDragListener(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });

        //设置返回按钮点击事件
        tbNoteEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出之前我们需要判断一下时候有最后一行文字没有保存的数据列表中
//                checkAndSyncItem(rvNoteEdit);
                presenter.saveNote(presenter.analysisData2NoteStr(mDataList));
                onBackPressed();
            }
        });

        //设置添加照片点击事件
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();        //进入相机界面
            }
        });


    }

    /**
     * 加载数据
     */
    private List<NoteEditModel> loadData() {
        List<NoteEditModel> dates = new ArrayList<>();
        dates.add(new NoteEditModel("这里是第" + 1 + "条item", NoteEditModel.Flag.TEXT, null));
        return dates;
    }


    /**
     * 返回值
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {       //保存图片样式的DataModle
                mDataList.add(new NoteEditModel("", NoteEditModel.Flag.IMAGE, cameraPath));
                mDataList.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 进入相机界面
     */
    private void takePhoto() {
        // 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 把文件地址转换成Uri格式
            Uri uri;
            if (Build.VERSION.SDK_INT < 24) {
                // 从文件中创建uri
                uri = Uri.fromFile(new File(cameraPath));
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", new File(cameraPath));
            }
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查所有的Text Item中的文本并同步
     */
    private void checkAndSyncItem(RecyclerView rvNoteEdit) {
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            View lastItem = rvNoteEdit.getChildAt(i);
            if (lastItem == null) {
                return;
            }
            if (null != rvNoteEdit.getChildViewHolder(lastItem)) {
                NoteEditViewHolder viewHolder = (NoteEditViewHolder) rvNoteEdit.getChildViewHolder(lastItem);
                if (viewHolder.etItem.getVisibility() == View.VISIBLE
                        && mDataList.get(mDataList.size() - 1).getItemFlag() == NoteEditModel.Flag.TEXT) {
                    //首先判断文本是可见的，然后判断这个位置是否在已有的列表中
                    String lastText = viewHolder.etItem.getText().toString();
                    mDataList.get(i).setContent(lastText);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
