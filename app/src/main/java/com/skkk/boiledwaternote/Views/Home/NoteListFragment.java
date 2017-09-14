package com.skkk.boiledwaternote.Views.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.DragItemView.DragItemCircleView;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MyLinearLayoutManager;
import com.skkk.boiledwaternote.CostomViews.VerticalRecyclerView;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Modles.NoteEditModel;
import com.skkk.boiledwaternote.Presenters.NoteEdit.NoteEditPresenter;
import com.skkk.boiledwaternote.Presenters.NoteList.NoteListPresenter;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NoteListFragment extends Fragment implements NoteListImpl {
    public static final String NOTE_TYPE_ARTICLE = "note_type_article";
    public static final String NOTE_TYPE_NOTE = "note_type_note";
    public static final String NOTE_TYPE_PRIVACY = "note_type_privacy";

    private static final String NOTE_TYPE="note_type";
    @Bind(R.id.rv_note_list)
    VerticalRecyclerView rvNoteList;
    @Bind(R.id.et_note_edit)
    EditText etNoteEdit;
    @Bind(R.id.iv_note_edit_save)
    ImageView ivNoteEditSave;
    @Bind(R.id.rl_note_edit)
    RelativeLayout rlNoteEdit;

    private NoteListPresenter noteListPresenter;
    private NoteEditPresenter noteEditPresenter;

    private String noteType;

    //    private RefreshLayout refreshLayout;
    private MyLinearLayoutManager linearLayoutManager;
    private List<Note> mDataList;
    private NoteListAdapter adapter;

    private DragItemCircleView lastDragItem;


    public static NoteListFragment newInstance(String noteType) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_TYPE, noteType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteType = getArguments().getString(NOTE_TYPE);
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
        noteListPresenter=new NoteListPresenter(this);
        noteEditPresenter=new NoteEditPresenter(getContext());
        mDataList = new ArrayList<>();                                  //初始化数据集
        initUI(view);       //初始化UI
        initEvent();        //设置各种事件
        noteListPresenter.showNotes(noteType);
    }

    /**
     * 初始化UI
     */
    private void initUI(View view) {
        linearLayoutManager = new MyLinearLayoutManager(getContext());
        rvNoteList.setLayoutManager(linearLayoutManager);
        rvNoteList.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoteListAdapter(getContext(), mDataList);
        rvNoteList.setAdapter(adapter);
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
                Note note = mDataList.get(pos);
                Intent intent = new Intent();
                intent.setClass(getContext(), NoteEditActivity.class);
                intent.putExtra(Configs.KEY_UPDATE_NOTE, note);
                getActivity().startActivityForResult(intent, Configs.REQUEST_UPDATE_NOTE);
            }

            //隐藏菜单删除按钮点击事件
            @Override
            public void onItemDeleteClickListener(View view, int pos) {
                Note note = mDataList.get(pos);
                if (noteListPresenter.deleteNote(note)) {
                    mDataList.remove(pos);
                    adapter.setDataList(mDataList);
                    adapter.notifyItemRemoved(pos);
                } else {
                    Toast.makeText(getContext(), R.string.note_list_article_item_delete, Toast.LENGTH_SHORT).show();
                }
            }

            //隐藏菜单上锁点击事件
            @Override
            public void onItemLockClickListener(View view, int pos) {

            }

            //便签类型长按点击事件
            @Override
            public void onNoteItemLongClickListener(View view, int pos) {
                Note note = mDataList.get(pos);
                if (noteListPresenter.deleteNote(note)) {
                    mDataList.remove(pos);
                    adapter.setDataList(mDataList);
                    adapter.notifyItemRemoved(pos);
                } else {
                    Toast.makeText(getContext(), R.string.note_list_note_item_delete, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 下方笔记类别添加按钮事件
         */
        ivNoteEditSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etNoteEdit.length()!=0){
                    NoteEditModel note=new NoteEditModel(etNoteEdit.getText().toString(), NoteEditModel.Flag.TEXT,null);
                    if (noteEditPresenter.saveNote(2,true,note)){
                        noteListPresenter.showLatestNote();
                        etNoteEdit.setText("");
                    }else{
                        Toast.makeText(getContext(), "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "内容为空", Toast.LENGTH_SHORT).show();
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void showList(List<Note> noteList) {
        mDataList = noteList;
        adapter.setDataList(mDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteNote(int pos) {
        mDataList.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, adapter.getItemCount() - pos);
    }

    @Override
    public void deletelist(List<Note> noteList) {

    }

    @Override
    public void showLatestOne(Note note) {
        mDataList.add(0,note);
        adapter.notifyItemInserted(0);
        linearLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
//        noteListPresenter.detach();
    }
}
