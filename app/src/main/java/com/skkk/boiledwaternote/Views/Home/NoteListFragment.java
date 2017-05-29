package com.skkk.boiledwaternote.Views.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skkk.boiledwaternote.CostomViews.RefreshLayout.RefreshLayout;
import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Presenters.NoteList.NoteListPresenter;
import com.skkk.boiledwaternote.R;
import com.skkk.boiledwaternote.Views.NoteEdit.NoteEditActivity;

import java.util.ArrayList;
import java.util.List;


public class NoteListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NoteListPresenter noteListPresenter;

    private String mParam1;
    private String mParam2;
    private RefreshLayout refreshLayout;
    private RecyclerView rvNoteList;
    private List<Note> mDataList;
    private HeaderAdapter adapter;


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
        noteListPresenter=new NoteListPresenter(getContext());      //获取操作类
        initUI(view);       //初始化UI
        initEvent();        //设置各种事件
    }


    /**
     * 初始化UI
     */
    private void initUI(View view) {
        refreshLayout = (RefreshLayout) view.findViewById(R.id.rl_note_list);
        rvNoteList = (RecyclerView) view.findViewById(R.id.rv_note_list);
        rvNoteList.setItemAnimator(new DefaultItemAnimator());
        rvNoteList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataList = getDefaultData();
        adapter = new HeaderAdapter(getContext(), mDataList);
        rvNoteList.setAdapter(adapter);
    }

    /**
     * 获取数据
     * @return
     */
    public List<Note> getDefaultData() {
        List<Note> notes=new ArrayList<>();
        if (noteListPresenter!=null){
            notes = noteListPresenter.getNotes();
        }
        return notes;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //设置下拉刷新事件
        refreshLayout.setOnHeaderRefreshListener(new RefreshLayout.OnHeaderRefreshListener() {
            @Override
            public void onRefreshListener() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        refreshLayout.cancelRefresh();
                    }
                }).start();
            }
        });

        //设置Item点击事件以及拖拽是阿金
        adapter.setOnItemClickListener(new HeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                getContext().startActivity(new Intent(getContext(),NoteEditActivity.class));
            }

            @Override
            public void onDragButtonClickListener(View view, int pos) {
                mDataList.remove(pos);
                adapter.notifyItemRemoved(pos);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
