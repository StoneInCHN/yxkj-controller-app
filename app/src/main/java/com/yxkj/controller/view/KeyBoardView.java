package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.yxkj.controller.R;


/**
 *
 */

public class KeyBoardView extends FrameLayout {
    /*顶部输入框*/
    private EditText editText;
    /*列表*/
    private GridView gridView;
    /*键盘内容集合*/
    private String[] contents = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "<-", "清空"};
    /*提示文字是否为初始化*/
    private boolean isInit = true;

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
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.view_text, contents);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            switch (i) {
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
                    editText.setText(editText.getText().append(contents[i]));
                    break;
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
}
