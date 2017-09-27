package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.yxkj.controller.R;
import com.yxkj.controller.adapter.KeyBoardAdapter;
import com.yxkj.controller.callback.InputEndListener;


/**
 * 键盘
 */

public class KeyBoardView extends FrameLayout {
    /*顶部输入框*/
    private EditText editText;
    /*列表*/
    private RecyclerView gridView;
    /*键盘内容集合*/
    private String[] contents = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "X", "重 置"};
    /*提示文字是否为初始化*/
    private boolean isInit = true;
    /*输入结束监听*/
    private InputEndListener<String> listener;
    /*判断输入框是否是三个字*/
    private boolean isEnd;
    private KeyBoardAdapter adapter;

    public KeyBoardView(@NonNull Context context) {
        this(context, null);
    }

    public KeyBoardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyBoardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        initData();
    }

    public void setListener(InputEndListener<String> listener) {
        this.listener = listener;
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        View view = View.inflate(getContext(), R.layout.view_keyboard, null);
        editText = view.findViewById(R.id.et_content);
        gridView = view.findViewById(R.id.gridView);
        this.addView(view);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        editText.setEnabled(false);
        adapter = new KeyBoardAdapter(getContext(), contents);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 8);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 37) {
                    return 2;
                } else
                    return 1;
            }
        });
        gridView.setLayoutManager(manager);
        gridView.setAdapter(adapter);
        adapter.setOnItemClickListener(new KeyBoardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String data) {
                switch (position) {
                    case 37:
                        editText.setText("");
                        break;
                    case 36:
                        Editable editable = editText.getText();
                        editText.setText(editable.length() > 0 ? editable.delete(editable.length() - 1, editable.length()) : "");
                        break;
                    default:
                        if (!isInit) {
                            initGoods();
                        }
                        editText.setText(editText.getText().append(contents[position]));
                        break;
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 3) {
                    if (isEnd) {
                        return;
                    }
                    if (!isEnd) {
                        isEnd = true;
                    }
                    if (listener != null) {
                        listener.onEnd(editable.toString());
                    }
                } else {
                    isEnd = false;
                }
            }
        });
    }

    /**
     * 设置没有该商品
     */
    public void setNoGoods() {
        editText.setHint(R.string.no_select_goods);
        isInit = false;
    }

    /**
     * 初始化提示
     */
    public void initGoods() {
        editText.setHint(R.string.please_input_num);
        isInit = true;
    }

    /**
     * 初始化
     */
    public void clear() {
        editText.setText("");
    }

}
