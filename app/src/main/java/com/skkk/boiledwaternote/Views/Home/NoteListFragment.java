package com.skkk.boiledwaternote.Views.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MLinearLayoutManager;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MStaggeredGridLayoutManager;
import com.skkk.boiledwaternote.CostomViews.VerticalRecyclerView;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.MyApplication;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Utils.Utils.DialogUtils;
import com.skkk.boiledwaternote.Utils.Utils.SpUtils;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NoteListFragment extends Fragment implements NoteListImpl {

    private static final String NOTE_TYPE = "note_type";
    @Bind(R.id.rv_note_list)
    VerticalRecyclerView rvNoteList;
    @Bind(R.id.et_note_edit)
    EditText etNoteEdit;
    @Bind(R.id.iv_note_edit_save)
    ImageView ivNoteEditSave;
    @Bind(R.id.rl_note_edit)
    RelativeLayout rlNoteEdit;

    private NoteListPresenter noteListPresenter;

    private int noteType;//传入的笔记类型

    //    private RefreshLayout refreshLayout;
    private NoteListAdapter adapter;

    /*
    * 设置单例模式
    * */
    private volatile static NoteListFragment instance;
    private RecyclerView.LayoutManager layoutManager;

    public static NoteListFragment getInstance(int noteType) {
        if (instance == null) {
            synchronized (NoteListFragment.class) {
                if (instance == null) {
                    instance = newInstance(noteType);
                }
            }
        }
        instance.attachPresenter();
        instance.setNoteType(noteType);
        return instance;
    }

    public static NoteListFragment newInstance(int noteType) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putInt(NOTE_TYPE, noteType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteType = getArguments().getInt(NOTE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteListPresenter = new NoteListPresenter();
        noteListPresenter.attachView(this);
        initUI();           //初始化UI
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        rvNoteList.setItemAnimator(new DefaultItemAnimator());
        rvNoteList.post(new Runnable() {
            @Override
            public void run() {
                int editScopeWidth = rvNoteList.getMeasuredWidth() - rvNoteList.getPaddingLeft() - rvNoteList.getPaddingRight();
                MyApplication.setEditScopeWidth(editScopeWidth);
            }
        });
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        //设置Item点击事件以及拖拽事件
        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            //Item点击事件
            @Override
            public void onItemClickListener(View view, int pos) {
                boolean haveItemOpen = adapter.isHaveItemMenuOpen();
                if (haveItemOpen) {
                    adapter.resetMenuStatus();
                    return;
                }
                noteListPresenter.startEditActivity(pos);
            }

            //隐藏菜单删除按钮点击事件
            @Override
            public void onItemDeleteClickListener(View view, int pos) {
                noteListPresenter.deleteNote(pos);
            }

            //隐藏菜单上锁点击事件
            @Override
            public void onItemLockClickListener(View view, int pos) {
                final int notePos = pos;
                DialogUtils.showDialog(getContext(), R.drawable.vector_drawable_notice,
                        getString(R.string.note_list_notice_title), getString(R.string.note_list_notice_move_to_privacy),
                        getString(R.string.note_list_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteListPresenter.updateNoteToPrivacy(notePos);
                            }
                        }, getString(R.string.note_list_cancel), null).show();
            }

            @Override
            public void onItemUnlockClickListener(View view, int pos) {
                final int notePos = pos;
                DialogUtils.showDialog(getContext(), R.drawable.vector_drawable_notice,
                        getString(R.string.note_list_notice_title), getString(R.string.note_list_notice_out_from_privacy),
                        getString(R.string.note_list_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteListPresenter.updateNoteFromPrivacy(notePos);
                            }
                        }, getString(R.string.note_list_cancel), null).show();
            }

            @Override
            public void onItemRecycleClickListener(View view, int pos) {
                final int notePos = pos;
                DialogUtils.showDialog(getContext(), R.drawable.vector_drawable_notice,
                        getString(R.string.note_list_notice_title), getString(R.string.note_list_notice_recycle),
                        getString(R.string.note_list_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteListPresenter.updateNoteFromPrivacy(notePos);
                            }
                        }, getString(R.string.note_list_cancel), null).show();
            }

            //便签类型长按点击事件
            @Override
            public void onNoteItemLongClickListener(View view, int pos) {
                final int notePos = pos;
                DialogUtils.showDialog(getContext(), R.drawable.vector_drawable_notice,
                        getString(R.string.note_list_notice_title), getString(R.string.note_list_notice_delete),
                        getString(R.string.note_list_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteListPresenter.deleteNote(notePos);
                            }
                        }, getString(R.string.note_list_cancel), null).show();


            }
        });

        /**
         * 下方笔记类别添加按钮事件
         */
        ivNoteEditSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etNoteEdit.length() != 0) {
                    noteListPresenter.saveNote(etNoteEdit.getText().toString());
                } else {
                    Toast.makeText(getContext(), R.string.note_list_notice_empty, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //请求码是新建一个Note
        if (requestCode == Configs.REQUEST_START_NEW_NOTE) {
            noteListPresenter.showNotes(noteType);
        }
        if (requestCode == Configs.REQUEST_UPDATE_NOTE) {
            noteListPresenter.showNotes(noteType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initLayoutManager();
        initEvent();
        noteListPresenter.showNotes(noteType);
    }


    /**
     * 初始化布局管理器
     */
    private void initLayoutManager() {
        int layoutStyle = SpUtils.getInt(getContext(), Configs.SP_KEY_NOTE_LIST_LAYOUT_STYLE);
        if (layoutStyle == -1) {
            layoutStyle = 0;//默认为线性列表
        }
        layoutManager = null;
        switch (layoutStyle) {
            case Configs.NOTE_LIST_LAYOUT_STYLE_LINEAR:
                layoutManager =new MLinearLayoutManager(getContext());
                break;
            case Configs.NOTE_LIST_LAYOUT_STYLE_STAGGER:
                layoutManager =new MStaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        adapter = new NoteListAdapter(getContext(), new ArrayList<Note>(), noteType,layoutStyle);
        rvNoteList.setAdapter(adapter);
        rvNoteList.setLayoutManager(layoutManager);
    }


    /**
     * 展示所有的笔记
     *
     * @param noteList
     */
    @Override
    public void showList(List<Note> noteList) {
        adapter.setDataList(noteList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除指定的Note
     *
     * @param pos
     */
    @Override
    public void deleteNote(int pos) {
        adapter.notifyItemRemoved(pos);
    }

    /**
     * 删除批量Notes
     *
     * @param noteList
     */
    @Override
    public void deletelist(List<Note> noteList) {

    }

    /**
     * 插入指定位置的Note
     *
     * @param pos
     */
    @Override
    public void insertNote(int pos) {
        adapter.notifyItemInserted(0);
        layoutManager.scrollToPosition(0);
    }


    /**
     * 清除便签编辑框的内容
     */
    @Override
    public void clearNoteEditText() {
        etNoteEdit.setText("");
    }

    /**
     * 重置适配器中的内容
     *
     * @param dataList
     */
    @Override
    public void resetAdapterData(List<Note> dataList) {
        if (adapter != null) {
            adapter.setDataList(dataList);
        }
    }

    /**
     * 显示提示内容
     *
     * @param strId
     */
    @Override
    public void showNotice(@StringRes int strId) {
        Toast.makeText(getContext(), strId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转到编辑笔记内容界面
     *
     * @param note
     */
    @Override
    public void startActivity(Note note) {
        Intent intent = new Intent();
        intent.setClass(getContext(), NoteEditActivity.class);
        intent.putExtra(Configs.KEY_UPDATE_NOTE, note);
        intent.putExtra(Configs.KEY_NOTE_TYPE, noteType);
        getActivity().startActivityForResult(intent, Configs.REQUEST_UPDATE_NOTE);
    }

    /**
     * 切换笔记类型
     */
    @Override
    public void changNoteType() {
        noteListPresenter.showSpecialTypeNotes(noteType);
    }


    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
        if (noteListPresenter != null) {
            noteListPresenter.showNotes(noteType);
        }
    }

    public void attachPresenter() {
        if (noteListPresenter != null) {
            noteListPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        noteListPresenter.detach();
    }
}
