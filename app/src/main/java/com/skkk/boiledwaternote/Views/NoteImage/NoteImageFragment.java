package com.skkk.boiledwaternote.Views.NoteImage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skkk.boiledwaternote.Collect;
import com.skkk.boiledwaternote.Configs;
import com.skkk.boiledwaternote.CostomViews.VerticalRecyclerView;
import com.skkk.boiledwaternote.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建于 2017/9/24
 * 作者 admin
 */
/*
* 
* 描    述：显示图片的Fragment
* 作    者：ksheng
* 时    间：2017/9/24$ 16:48$.
*/
public class NoteImageFragment extends Fragment implements NoteImageImpl {
    private static final String NOTE_TYPE = "note_type";
    /*
    * 设置单例模式
    * */
    private volatile static NoteImageFragment instance;
    @Bind(R.id.rv_note_image)
    VerticalRecyclerView rvNoteImage;
    @Bind(R.id.fab_note_iamge)
    FloatingActionButton fabNoteIamge;
    @Bind(R.id.cl_image_container)
    CoordinatorLayout clImageContainer;
    private int noteType;
    private NoteImagePresent noteImagePresent;
    private NoteImageAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private int columnCount = 4;//4列

    public static NoteImageFragment getInstance(int noteType) {
        if (instance == null) {
            synchronized (NoteImageFragment.class) {
                if (instance == null) {
                    instance = newInstance(noteType);
                }
            }
        }
        instance.attachPresenter();
        instance.setNoteType(noteType);
        return instance;
    }

    public static NoteImageFragment newInstance(int noteType) {
        NoteImageFragment fragment = new NoteImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_note_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteImagePresent = new NoteImagePresent();
        noteImagePresent.attachView(this);

        fabNoteIamge.hide();

        gridLayoutManager = new GridLayoutManager(getContext(), columnCount);
        adapter = new NoteImageAdapter(getContext(), new ArrayList<ImageModle>());
        initEvent();
        rvNoteImage.setLayoutManager(gridLayoutManager);
        rvNoteImage.setAdapter(adapter);

        noteImagePresent.showImages(noteType);//显示指定类型的图片
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        adapter.setImageClickListener(new Collect.OnImageFragmentItemClickListener() {
            @Override
            public void onImagePreviewClickListener(NoteImageViewHolder holder) {
                noteImagePresent.startPreviewActivity(holder.getAdapterPosition());
            }

            @Override
            public void onBackgroundClickListener(NoteImageViewHolder holder) {
                noteImagePresent.changeItemDeleteStatus(holder,true);
            }


            @Override
            public void onImageCheckClickListener(NoteImageViewHolder holder) {
                noteImagePresent.changeItemDeleteStatus(holder,false);

            }

            @Override
            public void onImageLongClickListener(NoteImageViewHolder holder) {
                changeEditStatus(adapter.isEdit());
            }
        });
        /*
        * fab 点击事件
        * */
        fabNoteIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ImageModle> chooseImages = new ArrayList<ImageModle>();
                for (ImageModle imageModle : adapter.getmDataList()) {
                    if (imageModle.isNeedDelete()) {
                        chooseImages.add(imageModle);
                    }
                }
                noteImagePresent.deleteAllImage(chooseImages,noteType);
            }
        });
    }

    /**
     * 设置笔记类型
     *
     * @param noteType
     */
    public void setNoteType(int noteType) {
        this.noteType = noteType;
        if (noteImagePresent != null) {
            noteImagePresent.showImages(noteType);
        }
    }


    /**
     * 展示图片
     *
     * @param imageModleList
     */
    @Override
    public void showImages(List<ImageModle> imageModleList) {
        adapter.setmDataList(imageModleList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 跳转到图片查看界面
     *
     * @param modle
     */
    @Override
    public void startToPreviewActivity(ImageModle modle) {
        Intent intent=new Intent(getContext(),ImagePreviewActivity.class);
        intent.putExtra(Configs.KEY_PREVIEW_IMAGE,modle);
        getActivity().startActivity(intent);
    }

    /**
     * 删除图片
     *
     * @param pos
     */
    @Override
    public void deleteImage(int pos) {
        adapter.notifyItemRemoved(pos);
    }

    /**
     * 删除批量图片
     *
     * @param start
     * @param count
     */
    @Override
    public void deleteAllImage(int start, int count) {
        adapter.notifyItemRangeRemoved(start, count);
    }

    /**
     * 展示提示框
     *
     * @param strId
     */
    @Override
    public void showNotice(@StringRes int strId) {
        Toast.makeText(getContext(), strId, Toast.LENGTH_LONG).show();
    }

    /**
     * 根据当前界面状态来判断是否处于编辑状态
     * @param currentIsEdit
     */
    @Override
    public void changeEditStatus(boolean currentIsEdit) {
        adapter.setEdit(!currentIsEdit);
        noteImagePresent.showImages(noteType);
        if (currentIsEdit){
            fabNoteIamge.hide();
        }else {
            fabNoteIamge.show();
            showNotice(R.string.image_list_change_edit_status);
        }
    }

    /**
     * 设置Item删除状态
     * @param holder
     * @param needDelete
     */
    @Override
    public void changeItemDeleteStatus(NoteImageViewHolder holder, boolean needDelete) {
        holder.setNeedDelete(needDelete);
    }

    @Override
    public void onResume() {
        super.onResume();
        noteImagePresent.showImages(noteType);//显示指定类型的图片
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void attachPresenter() {
        if (noteImagePresent != null) {
            noteImagePresent.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noteImagePresent.detach();
    }
}
