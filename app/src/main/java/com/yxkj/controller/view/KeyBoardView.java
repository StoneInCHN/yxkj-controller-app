package com.yxkj.controller.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.yxkj.controller.R;


/**
 * 键盘
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
    /*输入结束监听*/
    private InputEndListener listener;
    /*判断输入框是否是三个字*/
    private boolean isEnd;

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

    public void setListener(InputEndListener listener) {
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

    /**
     * 输入结束监听
     */
    public interface InputEndListener {
        void onEnd(String param);
    }
}
