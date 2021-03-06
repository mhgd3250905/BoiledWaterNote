package com.skkk.boiledwaternote.Views.NoteEdit;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.ClickableEdit.OnRegularClickListener;
import com.skkk.boiledwaternote.CostomViews.ClickableEdit.RegexParser;
import com.skkk.boiledwaternote.CostomViews.RecyclerEditView.MyItemTouchHelperCallback;
import com.skkk.boiledwaternote.CostomViews.RichEdit.NoteEditAdapter;
import com.skkk.boiledwaternote.CostomViews.RichEdit.RichEditView;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.AnimatorUtils;
import com.skkk.boiledwaternote.Utils.Utils.ImageUtils;
import com.skkk.boiledwaternote.Views.NoteImage.ImageModle;
import com.skkk.boiledwaternote.Views.NoteImage.ImagePreviewActivity;
import com.skkk.boiledwaternote.Views.PrivacyProtect.GraphyUnlockActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteEditActivity extends AppCompatActivity {

    private final String TAG = NoteEditActivity.class.getSimpleName();

    @Bind(R.id.tb_note_edit)
    Toolbar tbNoteEdit;
    @Bind(R.id.rev_note_edit)
    RichEditView revEdit;
    @Bind(R.id.activity_edit_container)
    CoordinatorLayout activityEditContainer;
    @Bind(R.id.ll_edit_container)
    LinearLayout llEditContainer;

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
    private LinearLayoutManager layoutManager;
    private NoteEditPresenter presenter;
    private InputMethodManager imm;

    private int currentPos;     //当前光标选择的item位置
    private boolean isNew;      //判断是否为新的笔记
    private Note updateNote;
    private View popView;
    private WindowManager.LayoutParams lp;
    private TextView tvDialogItemCamera;
    private TextView tvDialogItemAlbum;
    private PopupWindow popupWindow;
    private int noteType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        ButterKnife.bind(this);

        presenter = new NoteEditPresenter(this);
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

//        WindowManager manager = this.getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int width2 = outMetrics.widthPixels;

        initData();      //获取传入数据
        initUI();           //初始化UI
        initEvent();        //初始化各种事件

        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取传入数据
     */
    private void initData() {
        Intent intent = getIntent();
        isNew = (intent.getSerializableExtra(Configs.KEY_UPDATE_NOTE) == null);
        if (!isNew) {
            updateNote = (Note) intent.getSerializableExtra(Configs.KEY_UPDATE_NOTE);
            noteType = intent.getIntExtra(Configs.KEY_NOTE_TYPE, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        tbNoteEdit.setTitle("");
        tbNoteEdit.setNavigationIcon(R.drawable.back_arrow);
        //添加菜单
        tbNoteEdit.inflateMenu(R.menu.menu_edit_activity);

        //弹出PopWindow布局
        popView = View.inflate(NoteEditActivity.this, R.layout.view_popwindow_container, null);
        tvDialogItemCamera = (TextView) popView.findViewById(R.id.tv_dialog_item_camera);
        tvDialogItemAlbum = (TextView) popView.findViewById(R.id.tv_dialog_item_album);

        if (isNew) {
            revEdit.startNewEdit(true, null);
        } else {
            NoteEditModel[] models = new Gson().fromJson(updateNote.getContent(), NoteEditModel[].class);
            mDataList = new ArrayList<>();
            for (int i = 0; i < models.length; i++) {
                mDataList.add(models[i]);
            }
            revEdit.startNewEdit(false, mDataList);
        }
    }


    /**
     * 初始化各种事件
     */
    private void initEvent() {
        //设置返回按钮点击事件
        tbNoteEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tbNoteEdit.getMenu().findItem(R.id.menu_edit_perious).setVisible(false);
        tbNoteEdit.getMenu().findItem(R.id.menu_edit_next).setVisible(false);

        tbNoteEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit_insert_img:
                        //第一步，强制隐藏软键盘
                        imm.hideSoftInputFromWindow(activityEditContainer.getWindowToken(), 0);
                        //第二步，弹出dialog
                        popupWindow = new PopupWindow(popView,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
                        backgroundAlpha(1f, 0.5f, 200);
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                backgroundAlpha(0.5f, 1f, 200);
                            }
                        });
                        popupWindow.showAtLocation(llEditContainer, Gravity.BOTTOM, 0, 300);
                        break;

                    case R.id.menu_edit_perious:
                        List<NoteEditModel> historyNote = revEdit.getHistoryNote(revEdit.getRichText());
                        if (historyNote==null){
                            return false;
                        }
                        revEdit.loadRichText(historyNote,true);
                        break;
                    case R.id.menu_edit_next:
                        List<NoteEditModel> previewNote = revEdit.getPreviewNote(revEdit.getRichText());
                        if (previewNote==null){
                            return false;
                        }
                        revEdit.loadRichText(previewNote,true);
                        break;

                    case R.id.menu_edit_theme:
                        Intent intent=new Intent(NoteEditActivity.this, GraphyUnlockActivity.class);
                        intent.putExtra(Configs.KEY_GRAPHY_PURPOSE,Configs.GRAPHY_SET_PASSWORD);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        //插入图片dialog点击事件监听
        tvDialogItemCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        tvDialogItemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeGallery();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });

        /**
         * 设置图片点击事件
         */
        revEdit.setOnImageItemClickListener(new NoteEditAdapter.OnImageItemClickListener() {
            @Override
            public void onImageClickListener(int pos, View v, NoteEditModel model) {
                Intent intent = new Intent(NoteEditActivity.this, ImagePreviewActivity.class);
                intent.putExtra(Configs.KEY_PREVIEW_IMAGE, new ImageModle(model, false));
                intent.putExtra(Configs.KEY_NOTE_TYPE, noteType);
                startActivityForResult(intent, Configs.REQUEST_DELETE_IMAGE);
            }
        });

        /**
         * 设置正则特殊文本点击
         */
        revEdit.setOnRegularClickListener(new OnRegularClickListener() {

            @Override
            public void onPhoneClickListener(View view, String regexMatcher, int type) {
                showSnakeBarNotice(type,regexMatcher);
            }

            @Override
            public void onUrlClickListener(View view, String regexMatcher, int type) {
                showSnakeBarNotice(type,regexMatcher);
            }

            @Override
            public void onEmailClickListener(View view, String regexMatcher, int type) {
                showSnakeBarNotice(type,regexMatcher);
            }
        });


        /*
        * 设置事件监听
        * */
//        RxBus.getInstance().toFlowable(Integer.class)
//                .toObservable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.i(TAG, "accept: "+integer);
//                        revEdit.refreshRichText();
//                    }
//                });
    }

    private void sendEmail(String regexMatcher) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {regexMatcher});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT   , "");
        startActivity(Intent.createChooser(email, "请选择邮箱："));
    }

    private void jump2Url(String regexMatcher) {
        Uri uri = Uri.parse("http://" + regexMatcher);
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(urlIntent);
    }

    private void callPhone(String regexMatcher) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + regexMatcher);
        intent.setData(data);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //获取我们写的笔记类
        if (isNew) {
            if (!presenter.saveNote(Note.NoteType.ARTICLE_NOTE.getValue(), false, revEdit.getRichText())) {
                Toast.makeText(this, "添加笔记失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!presenter.updateNote(revEdit.getRichText(), updateNote)) {
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
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:    //拍照返回插入图片到编辑框
                    List<NoteEditModel> cameraItems = new ArrayList<>();
                    cameraItems.add(new NoteEditModel("", NoteEditModel.Flag.IMAGE, cameraPath));
                    if (revEdit.getCurrentHolderPosition() + 1 >= revEdit.getCurrentDataList().size()) {
                        cameraItems.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
                    }
                    revEdit.insertItems(cameraItems, revEdit.getCurrentHolderPosition() + 1);

                    break;
                case ALBUM_REQUEST_CODE:    //相册选取返回并插入图片到编辑框
                    if (data != null) {
                        Uri uriImageFromGallery = data.getData();
                        String albumImagePath = ImageUtils.getAbsoluteImagePath(NoteEditActivity.this,
                                uriImageFromGallery);
                        List<NoteEditModel> albumItems = new ArrayList<>();
                        albumItems.add(new NoteEditModel("", NoteEditModel.Flag.IMAGE, albumImagePath));
                        if (revEdit.getCurrentHolderPosition() + 1 >= revEdit.getCurrentDataList().size()) {
                            albumItems.add(new NoteEditModel("", NoteEditModel.Flag.TEXT, null));
                        }

                        revEdit.insertItems(albumItems, revEdit.getCurrentHolderPosition() + 1);
                    }
                    break;
                case Configs.REQUEST_DELETE_IMAGE:
                    ImageModle imaegModle = (ImageModle) data.getSerializableExtra(Configs.KEY_PREVIEW_IMAGE);
                    revEdit.deleteImage(imaegModle.getnoteEditModel().getImagePath());
                    break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        * 在进入编辑界面的时候判断并隐藏软键盘
        * */
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
     * 进入相册选取图片界面
     */
    private void takeGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }


    /**
     * 在指定时间内改变透明度
     *
     * @param start    起始透明度
     * @param end      结束透明度
     * @param duration 耗时
     */
    private void backgroundAlpha(float start, float end, int duration) {
        lp = getWindow().getAttributes();
        AnimatorUtils.backgroundAlpha(start, end, duration, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                getWindow().setAttributes(lp);
            }
        });
    }

    private void showSnakeBarNotice(int type, final String matcherResult){
        String title="";
        String actionStr="";
        View.OnClickListener actionClick=null;
        switch (type){
            case RegexParser.phoneType:
                title=String.format(getString(R.string.note_edit_phone_text_title),matcherResult);
                actionStr=getString(R.string.note_edit_phone_text_action);
                actionClick=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPhone(matcherResult);
                    }
                };
                break;
            case RegexParser.urlType:
                title=String.format(getString(R.string.note_edit_url_text_title),matcherResult);
                actionStr=getString(R.string.note_edit_url_text_action);
                actionClick=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jump2Url(matcherResult);
                    }
                };
                break;
            case RegexParser.emailType:
                title=String.format(getString(R.string.note_edit_email_text_title),matcherResult);
                actionStr=getString(R.string.note_edit_email_text_action);
                actionClick=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmail(matcherResult);
                    }
                };
                break;
        }
        Snackbar.make(activityEditContainer,title,Snackbar.LENGTH_LONG)
        .setAction(actionStr,actionClick)
        .show();
    }


}
