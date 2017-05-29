package com.skkk.boiledwaternote.Views.Home;

/**
 * Created by admin on 2017/5/28.
 */
/*
* 
* 描    述：
* 作    者：ksheng
* 时    间：2017/5/28$ 20:39$.
*/

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.R;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

/**
 * RecyclerView数据适配器
 */
public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder>{
    private Context context;
    private List<Note> dataList;
    public OnItemClickListener onItemClickListener;

    private String SEPARATED_FLAG="&";      //用来分隔不同条目传入的不同数据
    private String SEPARATED_TEXT_FLAG="$|TEXT|$";      //用来分隔不同条目传入的不同数据
    private String SEPARATED_IMAGE_FLAG="$|IMAGE|$";      //用来分隔不同条目传入的不同数据

    public interface OnItemClickListener{
        void onItemClickListener(View view, int pos);
        void onDragButtonClickListener(View view,int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HeaderAdapter(Context context, List<Note> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder= new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_list,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //显示距离此刻模式的时间显示方式
        CharSequence relativeDateTimeString = DateUtils
                .getRelativeDateTimeString(context, dataList.get(position).getCreateTime().getTime(),
                DateUtils.MINUTE_IN_MILLIS, DateUtils.HOUR_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME);
        holder.tvNoteListTime.setText(relativeDateTimeString);

        //获取第一段文字自动作为标题
        String content = dataList.get(position).getContent();
        String title = null;       //显示标题
        String imagePath=null;   //图片路径

        //设置图片初始化隐藏
        holder.ivNoteListImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(content)){
            String[] split = content.split(SEPARATED_FLAG);
            //获取标题
            for (int i = 0; i < split.length; i++) {
                if (split[i].startsWith(SEPARATED_TEXT_FLAG)){
                    title=split[i].substring(SEPARATED_TEXT_FLAG.length(),split[i].length());
                    break;
                }
            }

            holder.tvNoteListTitle.setText(title);
            if (onItemClickListener!=null){
                holder.llShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=position;
                        onItemClickListener.onItemClickListener(v,pos);
                    }
                });
            }

            //获取图片：目前仅获取第一张图片
            for (int i = 0; i < split.length; i++) {
                if (split[i].startsWith(SEPARATED_IMAGE_FLAG)){
                    imagePath=split[i].substring(SEPARATED_IMAGE_FLAG.length(),split[i].length());
                    break;
                }
            }
            if (!TextUtils.isEmpty(imagePath)){
                holder.ivNoteListImage.setVisibility(View.VISIBLE);

                //压缩图片
                Bitmap bitmapCompress = new Compressor.Builder(context)
                        .setMaxWidth(300)
                        .setMaxHeight(300)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .build()
                        .compressToBitmap(new File(imagePath));

                holder.ivNoteListImage.setImageBitmap(bitmapCompress);
            }
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_note_list_title)
        public TextView tvNoteListTitle;
        @Bind(R.id.tv_note_list_time)
        public TextView tvNoteListTime;
        @Bind(R.id.iv_note_list_image)
        public ImageView ivNoteListImage;
        @Bind(R.id.ll_show)
        public LinearLayout llShow;
        @Bind(R.id.ll_hide)
        public LinearLayout llHide;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
