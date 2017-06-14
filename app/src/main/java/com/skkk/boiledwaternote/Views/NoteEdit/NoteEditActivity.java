package com.skkk.boiledwaternote.Views.NoteEdit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.MyItemTouchHelperCallback;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.OnStartDragListener;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Presenters.NoteEdit.NoteEditPresenter;
import com.skkk.boiledwaternote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.iv_format_align_center)
    ImageView ivFormatAlignCenter;
    @Bind(R.id.iv_format_blod)
    ImageView ivFormatBlod;
    @Bind(R.id.iv_format_italic)
    ImageView ivFormatItalic;
    @Bind(R.id.iv_format_list)
    ImageView ivFormatList;
    @Bind(R.id.iv_format_list_numbered)
    ImageView ivFormatListNumbered;
    @Bind(R.id.iv_format_quote)
    ImageView ivFormatQuote;
    @Bind(R.id.iv_format_size)
    ImageView ivFormatSize;
    @Bind(R.id.iv_format_underlined)
    ImageView ivFormatUnderlined;
    @Bind(R.id.iv_format_strike_through)
    ImageView ivFormatStrikeThrough;


    private String cameraPath;

    private List<NoteEditModel> mDataList;
    private MyItemTouchHelperCallback callback;
    private ItemTouchHelper itemTouchHelper;
    private NoteEditAdapter adapter;
    private LinearLayoutManager layoutManager;
    private NoteEditPresenter presenter;
    private InputMethodManager imm;

    private int currentPos;     //当前光标选择的item位置
    private boolean isNew;      //判断是否为新的笔记
    private Note updateNote;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        ButterKnife.bind(this);
        handler = new Handler();
        presenter = new NoteEditPresenter(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initSetting();      //获取传入数据
        initUI();           //初始化UI
        initEvent();        //初始化各种事件
    }

    /**
     * 获取传入数据
     */
    private void initSetting() {
        Intent intent = getIntent();
        isNew = (intent.getSerializableExtra(Configs.KEY_UPDATE_NOTE) == null);
        if (!isNew) {
            updateNote = (Note) intent.getSerializableExtra(Configs.KEY_UPDATE_NOTE);
        }
    }

    @Override
    protected void onStop() {
//        checkAndSyncItem(rvNoteEdit);       //同步数据
        super.onStop();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tbNoteEdit.setTitle("");
        tbNoteEdit.setNavigationIcon(R.drawable.back_arrow);
        if (isNew) {
            mDataList = loadData();
        } else {
            NoteEditModel[] models = new Gson().fromJson(updateNote.getContent(), NoteEditModel[].class);
            mDataList = new ArrayList<>();
            for (int i = 0; i < models.length; i++) {
                mDataList.add(models[i]);
            }
        }
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
     * 获取最后一个Item，如果是文本，那么就获取焦点呼出键盘
     *
     * @param rvNoteEdit
     * @param layoutManager
     */
    private void selectLastTextItem(RecyclerView rvNoteEdit, LinearLayoutManager layoutManager) {
        int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
        View lastItem = rvNoteEdit.getChildAt(lastPos);
        if (lastItem == null) {
            return;
        }
        if (null != rvNoteEdit.getChildViewHolder(lastItem)) {
            NoteEditAdapter.NoteEditViewHolder viewHolder = (NoteEditAdapter.NoteEditViewHolder) rvNoteEdit.getChildViewHolder(lastItem);
            if (viewHolder.etItem.getVisibility() == View.VISIBLE
                    && mDataList.get(mDataList.size() - 1).getItemFlag() == NoteEditModel.Flag.TEXT) {
                //首先判断文本是可见的,那么就获取焦点呼出键盘
                String lastText = viewHolder.etItem.getText().toString();
                viewHolder.etItem.setSelection(lastText.length());
                viewHolder.etItem.setFocusable(true);
                viewHolder.etItem.setFocusableInTouchMode(true);
                viewHolder.etItem.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 初始化各种事件
     */
    private void initEvent() {
        //设置RecyclerView触摸如果在最下方一个text光标之外 那么就定位到最后一个ItemText
        rvNoteEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        View childViewUnder = rvNoteEdit.findChildViewUnder(event.getX(), event.getY());
                        if (null == childViewUnder) {      //如果通过坐标获取的Item是null，说明我们点击了item的下方空白区域
                            //获取最后一个Item，如果是文本，那么就获取焦点呼出键盘
                            selectLastTextItem(rvNoteEdit, layoutManager);
                        }
                        break;
                }
                return false;
            }
        });

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

    @Override
    public void onBackPressed() {
        //获取我们写的笔记类
        if (isNew) {
            if (!presenter.saveNote(adapter.getmDataList())) {
                Toast.makeText(this, "添加笔记失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!presenter.updateNote(adapter.getmDataList(), updateNote)) {
                Toast.makeText(this, "更新笔记失败", Toast.LENGTH_SHORT).show();
            }
        }
        super.onBackPressed();
    }

    /**
     * 加载数据
     * 这里默认加载一个文本类型的空数据
     */
    private List<NoteEditModel> loadData() {
        List<NoteEditModel> dates = new ArrayList<>();
        dates.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
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
                adapter.setmDataList(mDataList);
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

    @OnClick({R.id.iv_format_align_center, R.id.iv_format_blod, R.id.iv_format_italic, R.id.iv_format_list, R.id.iv_format_list_numbered, R.id.iv_format_quote, R.id.iv_format_size, R.id.iv_format_underlined, R.id.iv_format_strike_through})
    public void onViewClicked(View v) {
        NoteEditAdapter.NoteEditViewHolder currentHolder=
                (NoteEditAdapter.NoteEditViewHolder) rvNoteEdit.findContainingViewHolder(getCurrentFocus());
        if (currentHolder==null){
            return;
        }
        boolean isSelected = currentHolder.etItem.hasSelection();
        int start = currentHolder.etItem.getSelectionStart();
        int end = currentHolder.etItem.getSelectionEnd();

        startBottomViewAnim(v);

        switch (v.getId()) {
            /*
            * 设置文字居中
            * */
            case R.id.iv_format_align_center:
                v.setBackgroundColor(adapter.isAlignCenterText()?Color.LTGRAY:Color.TRANSPARENT);
                break;
            case R.id.iv_format_blod:                //设置文字Blod
                if (!isSelected) {                   //如果没有选择
                    if (currentHolder.myItemTextChangeListener.isFormat_blod()) {
                        currentHolder.myItemTextChangeListener.setFormat_blod(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_blod(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {                             //如果选择
                    //获取选择区域内所有的StyleSpan
                    StyleSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StyleSpan.class);
                    StyleSpan hasSpan = null;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans[i].getStyle() == Typeface.BOLD) {
                            hasSpan = spans[i];
                        }
                    }
                    if (hasSpan != null) {   //如果有Bold则设置为正常
                        currentHolder.etItem.getText().removeSpan(hasSpan);
                    } else {                //如果不包含Bold那么就设置粗体
                        currentHolder.etItem.getText().setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_italic:             //设置文字斜体
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_italic()) {
                        currentHolder.myItemTextChangeListener.setFormat_italic(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_italic(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    StyleSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StyleSpan.class);
                    StyleSpan hasSpan = null;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans[i].getStyle() == Typeface.ITALIC) {
                            hasSpan = spans[i];
                        }
                    }
                    if (hasSpan != null) {   //如果有ITALIC则设置为正常
                        currentHolder.etItem.getText().removeSpan(hasSpan);
                    } else {               //如果不包含ITALIC那么就设置粗体
                        currentHolder.etItem.getText().setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_list:               //列表
                if (adapter.isItemFormatList()){
                    adapter.setItemFormatList(false);
                }else {
                    adapter.setItemFormatList(true);
                }
                v.setBackgroundColor(adapter.isItemFormatList()?Color.LTGRAY:Color.TRANSPARENT);
                break;
            case R.id.iv_format_list_numbered:
                break;
            case R.id.iv_format_quote:              //设置引用
                if (currentHolder.myItemTextChangeListener.isFormat_quote()){
                    currentHolder.setFormat_quote(false);
                }else {
                    currentHolder.setFormat_quote(true);
                }
//                v.setBackgroundColor(currentHolder.myItemTextChangeListener.isFormat_quote()?Color.LTGRAY:Color.TRANSPARENT);

                break;
            case R.id.iv_format_size:
                break;
            case R.id.iv_format_underlined:         //设置文字下划线
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_underlined()) {
                        currentHolder.myItemTextChangeListener.setFormat_underlined(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_underlined(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    UnderlineSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, UnderlineSpan.class);
                    boolean hasSpan = false;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans.length > 0) {
                            hasSpan = true;
                        }
                    }
                    //清除区域内所有的UnderLineSpan
                    for (int i = 0; i < spans.length; i++) {
                        currentHolder.etItem.getText().removeSpan(spans[i]);
                    }
                    //如果本身没有Span，这里需要设置
                    if (!hasSpan) {
                        currentHolder.etItem.getText().setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case R.id.iv_format_strike_through:     //设置文字删除线
                if (!isSelected) {
                    if (currentHolder.myItemTextChangeListener.isFormat_strike_through()) {
                        currentHolder.myItemTextChangeListener.setFormat_strike_through(false);
                        v.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        currentHolder.myItemTextChangeListener.setFormat_strike_through(true);
                        v.setBackgroundColor(Color.LTGRAY);
                    }
                } else {
                    //获取选择区域内所有的StyleSpan
                    StrikethroughSpan[] spans = currentHolder.etItem.getText().getSpans(start, end, StrikethroughSpan.class);
                    boolean hasSpan = false;
                    for (int i = 0; i < spans.length; i++) {
                        if (spans.length > 0) {
                            hasSpan = true;
                        }
                    }
                    //清除区域内所有的UnderLineSpan
                    for (int i = 0; i < spans.length; i++) {
                        currentHolder.etItem.getText().removeSpan(spans[i]);
                    }
                    //如果本身没有Span，这里需要设置
                    if (!hasSpan) {
                        currentHolder.etItem.getText().setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
        }
    }

    /**
     * bottomBar的按键动画
     * @param v
     */
    private void startBottomViewAnim(View v) {

    }
}
