package com.skkk.boiledwaternote;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skkk.boiledwaternote.Presenters.NoteEdit.NoteEditPresenter;
import com.skkk.boiledwaternote.Views.Home.NoteListFragment;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private Menu navigationMenu;
    private NoteEditPresenter presenter;

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
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
    }

    /**
     * 添加进入时候默认的Fragment
     */
    private void addDefaultFragment() {
        NoteListFragment noteListFragment = NoteListFragment.newInstance(NoteListFragment.NOTE_TYPE_NONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, noteListFragment)
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

        Fragment fragment = null;

        if(id==R.id.nav_all) {
            fragment = NoteListFragment.newInstance(NoteListFragment.NOTE_TYPE_NONE);
        }else if (id == R.id.nav_article) {//文章
            fragment = NoteListFragment.newInstance(NoteListFragment.NOTE_TYPE_ARTICLE);
        } else if (id == R.id.nav_note) {//笔记
            fragment = NoteListFragment.newInstance(NoteListFragment.NOTE_TYPE_NOTE);
        } else if (id == R.id.nav_image) {//图片

        } else if (id == R.id.nav_privacy) {//隐私
            fragment = NoteListFragment.newInstance(NoteListFragment.NOTE_TYPE_PRIVACY);
        } else if (id == R.id.nav_recycle) {//回收站

        } else if (id == R.id.nav_about) {//关于
            navigationMenu.findItem(id).setChecked(false);

        } else if (id == R.id.nav_setting) {//设置
            navigationMenu.findItem(id).setChecked(false);
        }

        if (fragment==null){
            return true;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
