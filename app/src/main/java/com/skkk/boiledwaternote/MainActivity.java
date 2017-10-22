package com.skkk.boiledwaternote;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Utils.Utils.DialogUtils;
import com.skkk.boiledwaternote.Utils.Utils.PermissionsUtils;
import com.skkk.boiledwaternote.Utils.Utils.Toasts;
import com.skkk.boiledwaternote.Views.Home.NoteListFragment;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditPresenter;
import com.skkk.boiledwaternote.Views.NoteImage.NoteImageFragment;
import com.skkk.boiledwaternote.Views.PrivacyProtect.TouchIdActivity;
import com.skkk.boiledwaternote.Views.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String EXTRA_PERMISSIONS = "com.skkk.boiledwaternote.permission.extra_permission"; // 权限参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    @Bind(R.id.content_main)
    RelativeLayout contentMain;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.ll_edit_container)
    LinearLayout llEditContainer;
    @Bind(R.id.activity_main_container)
    CoordinatorLayout activityMainContainer;
    private Menu navigationMenu;
    private NoteEditPresenter presenter;
    private Fragment fragment;
    private Menu menu;

    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private ArrayList<String> needRequestPermissions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new NoteEditPresenter(this);
        initUI();               //初始化UI
        initEvent();            //初始化各种事件
        addDefaultFragment();   //添加进入时候默认的Fragment
    }


    @Override
    protected void onStart() {
        super.onStart();
        initPermissions();
    }

    /**
     * 检测权限
     */
    private void initPermissions() {
        if (PermissionsUtils.lacksPermissions(MainActivity.this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS);
        }
    }

    // 请求权限兼容低版本
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions(String... permissions) {
        needRequestPermissions.clear();
        for (int i = 0; i < PERMISSIONS.length; i++) {
            if (PermissionsUtils.lacksPermission(this, PERMISSIONS[i])) {
                needRequestPermissions.add(PERMISSIONS[i]);
            }
        }
        String[] permissionArr = new String[needRequestPermissions.size()];
        needRequestPermissions.toArray(permissionArr);
        requestPermissions(permissionArr, PERMISSION_REQUEST_CODE);
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {

        } else {
            DialogUtils.showDialog(MainActivity.this, R.drawable.vector_drawable_notice,
                    "提醒", "当前应用缺少必要权限，\n请点击\"设置\"-\"权限\"打开所需要的权限。",
                    "设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                            startActivity(intent);
                        }
                    }, "算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    }).show();
        }
    }


    /**
     * 判断是否包含所有的权限
     *
     * @param grantResults
     * @return
     */

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationMenu = navigationView.getMenu();
        navigationMenu.findItem(R.id.nav_all).setChecked(true);


    }

    /**
     * 初始化各种点击事件
     */
    private void initEvent() {

    }

    /**
     * 返回值处理啊
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //抛转到NoteList界面
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        currentFragment.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Configs.REQUEST_PRIVACY_CHECK:
                if (resultCode == Configs.RESULT_PRIVACY_CHECK_OK) {
                    fragment = NoteListFragment.getInstance(Note.NoteType.PRIVACY_NOTE.getValue());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, fragment)
                            .commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (resultCode == Configs.RESULT_PRIVACY_CHECK_FAILED) {
                    if (fragment != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_container, fragment)
                                .commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                break;
        }
    }

    /**
     * 添加进入时候默认的Fragment
     */
    private void addDefaultFragment() {
        fragment = NoteListFragment.getInstance(Note.NoteType.ALL_NOTE.getValue());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }

    /**
     * 加入双击退出提示
     *
     * @param keyCode
     * @param event
     * @return
     */
    /* @描述 菜单、返回键响应 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                exitBy2Click(); //调用双击退出函数
            }
        }
        return false;
    }

    /* @描述 双击返回函数 */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }


    /**
     * 创建toolbar右侧目录
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    /**
     * toolbar右侧菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(MainActivity.this, NoteEditActivity.class), Configs.REQUEST_START_NEW_NOTE);
            return true;
        } else if (id == R.id.action_delete) {
            //删除回收站全部笔记
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单点击事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //设置菜单点击事件
        navigationMenu.findItem(id).setChecked(true);
        //显示添加按钮
        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(false);

        if (id == R.id.nav_all) {
            fragment = NoteListFragment.getInstance(Note.NoteType.ALL_NOTE.getValue());
        } else if (id == R.id.nav_article) {//文章
            fragment = NoteListFragment.getInstance(Note.NoteType.ARTICLE_NOTE.getValue());
        } else if (id == R.id.nav_note) {//笔记
            fragment = NoteListFragment.getInstance(Note.NoteType.NOTE_NOTE.getValue());
        } else if (id == R.id.nav_image) {//图片
            fragment = NoteImageFragment.getInstance(Note.NoteType.ALL_NOTE.getValue());
        } else if (id == R.id.nav_privacy) {//隐私
            /*
            * 跳转到验证界面
            * */
            if (fragment instanceof NoteListFragment) {
                NoteListFragment noteListFragment = (NoteListFragment) fragment;
                if (noteListFragment.getNoteType() != Note.NoteType.PRIVACY_NOTE.getValue()) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, TouchIdActivity.class);
                    startActivityForResult(intent, Configs.REQUEST_PRIVACY_CHECK);
                    return true;
                } else {
                    fragment = NoteListFragment.getInstance(Note.NoteType.PRIVACY_NOTE.getValue());
                }
            }
        } else if (id == R.id.nav_recycle) {//回收站
            menu.findItem(R.id.action_add).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            fragment = NoteListFragment.getInstance(Note.NoteType.RECYCLE_NOTE.getValue());
        } else if (id == R.id.nav_about) {//关于
            navigationMenu.findItem(id).setChecked(false);
            Toasts.costom(this, "您点击了关于按钮！", R.drawable.vector_drawable_pen_blue, Color.WHITE, 10f, Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_setting) {//设置:跳转到设置界面
            navigationMenu.findItem(id).setChecked(false);
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (fragment == null) {
            return true;
        }



        //切换笔记类型
//        fragment.changNoteType();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        //17621750601
    }


}
