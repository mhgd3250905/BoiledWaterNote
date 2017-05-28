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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skkk.boiledwaternote.R;

import java.util.List;

/**
 * RecyclerView数据适配器
 */
public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder>{
    private Context context;
    private List<String> dataList;
    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClickListener(View view, int pos);
        void onDragButtonClickListener(View view,int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HeaderAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder= new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvItem.setText(dataList.get(position));
        if (onItemClickListener!=null){
            holder.llShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=position;
                    onItemClickListener.onItemClickListener(v,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvItem;
        private LinearLayout llShow;
        private LinearLayout llHide;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItem= (TextView) itemView.findViewById(R.id.tv_item);
            llShow= (LinearLayout) itemView.findViewById(R.id.ll_show);
            llHide= (LinearLayout) itemView.findViewById(R.id.ll_hide);
        }
    }
}
