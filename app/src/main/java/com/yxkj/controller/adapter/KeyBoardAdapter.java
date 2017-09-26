package com.yxkj.controller.adapter;

import android.content.Context;

import com.yxkj.controller.R;
import com.yxkj.controller.base.BaseRecyclerViewAdapter;
import com.yxkj.controller.base.BaseViewHolder;

/**
 *
 */

public class KeyBoardAdapter extends BaseRecyclerViewAdapter<String> {

    public KeyBoardAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_keyboard;
    }

    @Override
    public void onCorvert(BaseViewHolder holder, int position, String bean) {
        if (position < tList.size() - 1) {
            if (position == tList.size() - 2) {
                holder.setVisible(R.id.img_delete,true);
                holder.setVisible(R.id.textView, false);
                holder.setVisible(R.id.text_img, false);
            } else {
                holder.setText(R.id.textView, bean);
                holder.setVisible(R.id.textView, true);
                holder.setVisible(R.id.text_img, false);
                holder.setVisible(R.id.img_delete,false);
            }
        } else {
            holder.setText(R.id.text_img, bean);
            holder.setVisible(R.id.textView, false);
            holder.setVisible(R.id.text_img, true);
            holder.setVisible(R.id.img_delete,false);
        }
    }
}
