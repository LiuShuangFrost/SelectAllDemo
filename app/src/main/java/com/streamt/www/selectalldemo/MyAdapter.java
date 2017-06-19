package com.streamt.www.selectalldemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * @author： LiuShuang
 * @time： 2017/6/16 9:18
 * @description：
 */

public class MyAdapter extends RecyclerView.Adapter {

    List<String> content;
    private OnItemClickListener mOnItemClickListener;

    private boolean isShow = false;

    public MyAdapter(List<String> content) {
        this.content = content;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
    //        private boolean[] flag = new boolean[100];//此处添加一个boolean类型的数组


    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

        void checkBoxClick(int position, boolean isChecked);


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.mOnItemClickListener = onItemClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.text.setText(content.get(position));

        myViewHolder.checkBox.setOnCheckedChangeListener(null);//先设置一次CheckBox的选中监听器，传入参数null
        myViewHolder.checkBox.setChecked(MyApp.flag[position]);//用数组中的值设置CheckBox的选中状态

        //再设置一次CheckBox的选中监听器，当CheckBox的选中状态发生改变时，把改变后的状态储存在数组中
//        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                MyApp.flag[position] = b;
//            }
//        });

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
//                    holder.cb_select.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            ((MyViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MyApp.flag[position] = isChecked;
                    mOnItemClickListener.checkBoxClick(position, isChecked);
                }
            });
        }

        if (isShow()) {//编辑状态
            ((MyViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (content != null) {
            return content.size();
        } else {
            return 0;
        }
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_select);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
