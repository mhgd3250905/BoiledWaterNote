package com.skkk.boiledwaternote.Views.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.DragItemView.DragItemView;
import com.skkk.boiledwaternote.CostomViews.DragItemView.MyLinearLayoutManager;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Presenters.NoteList.NoteListPresenter;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class NoteListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NoteListPresenter noteListPresenter;

    private String mParam1;
    private String mParam2;
    //    private RefreshLayout refreshLayout;
    private RecyclerView rvNoteList;
    private MyLinearLayoutManager linearLayoutManager;
    private List<Note> mDataList;
    private NoteListAdapter adapter;

    private DragItemView lastDragItem;


    public static NoteListFragment newInstance(String param1, String param2) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteListPresenter = new NoteListPresenter(getContext());      //获取操作类
        initUI(view);       //初始化UI
        initEvent();        //设置各种事件
    }

    /**
     * 初始化UI
     */
    private void initUI(View view) {
//      refreshLayout = (RefreshLayout) view.findViewById(R.id.rl_note_list);
        rvNoteList = (RecyclerView) view.findViewById(R.id.rv_note_list);
        linearLayoutManager=new MyLinearLayoutManager(getContext());
        rvNoteList.setLayoutManager(linearLayoutManager);
        rvNoteList.setItemAnimator(new DefaultItemAnimator());
        mDataList = getDefaultData();
        adapter = new NoteListAdapter(getContext(), mDataList);
        rvNoteList.setAdapter(adapter);
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<Note> getDefaultData() {
        List<Note> notes = new ArrayList<>();
        if (noteListPresenter != null) {
            notes = noteListPresenter.getNotes();
        }
        return notes;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        //设置Item点击事件以及拖拽事件
        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Note note = mDataList.get(pos);
                Intent intent = new Intent();
                intent.setClass(getContext(), NoteEditActivity.class);
                intent.putExtra(Configs.KEY_UPDATE_NOTE, note);
                getActivity().startActivityForResult(intent, Configs.REQUEST_UPDATE_NOTE);
            }

            @Override
            public void onDragButtonClickListener(View view, int pos) {

            }

            @Override
            public void onItemDeleteClickListener(View view, int pos) {
                Note note = mDataList.get(pos);
                if (noteListPresenter.deleteNote(note)) {
                    mDataList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    adapter.notifyItemRangeRemoved(pos,adapter.getItemCount());
                }else {
                    Toast.makeText(getContext(), "删除笔记失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setOnDragItemStatusChange(new NoteListAdapter.OnDragItemStatusChange() {
            @Override
            public void onDragingListener(int pos, DragItemView item, View changedView, int left, int top, int dx, int dy) {
                linearLayoutManager.setScroll(false);
                if (lastDragItem!=null){
                    lastDragItem.resetItemAnim();
                    lastDragItem=null;
                }
//               Log.i(TAG, "onDragingListener: 第"+pos+"个Item正在拖拽！left--->"+left);
            }

            @Override
            public void onDragClose(int pos, DragItemView item, View changedView, int left, int top, int dx, int dy) {
                linearLayoutManager.setScroll(true);
                lastDragItem=item;
                Log.i(TAG, "onDragingListener: 第"+pos+"个Item结束！left--->"+left);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //请求码是新建一个Note
        if (requestCode == Configs.REQUEST_START_NEW_NOTE) {
            mDataList = getDefaultData();
            adapter.setDataList(mDataList);
            adapter.notifyDataSetChanged();
        }
        if (requestCode == Configs.REQUEST_UPDATE_NOTE) {
            mDataList = getDefaultData();
            adapter.setDataList(mDataList);
            adapter.notifyDataSetChanged();
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

}
