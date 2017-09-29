package com.yxkj.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxkj.controller.R;

/**
 *
 */
public class KeyBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private String list[];
    private int DELETE = 0x110;
    public int RESET = 0x112;
    public int NORMAL = 0x113;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public KeyBoardAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType) {
            case 0x110:
                view = LayoutInflater.from(context).inflate(R.layout.item_keyboard_delete, parent, false);
                viewHolder = new DeleteViewHolder(view);
                break;
            case 0x112:
                view = LayoutInflater.from(context).inflate(R.layout.item_keyboard_reset, parent, false);
                viewHolder = new ResetViewHolder(view);
                break;
            case 0x113:
                view = LayoutInflater.from(context).inflate(R.layout.item_keyboard_normal, parent, false);
                viewHolder = new NormalViewHolder(view);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).textView.setText(list[position]);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position, list[position]);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 37:
                return RESET;
            case 36:
                return DELETE;
        }
        return NORMAL;
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder {

        public DeleteViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ResetViewHolder extends RecyclerView.ViewHolder {

        public ResetViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onClick(int pos, String data);
    }
}
