package com.yxkj.controller.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yxkj.controller.R;
import com.yxkj.controller.constant.Constant;

/**
 * Glide工具类
 */

public class GlideUtil {
    public static void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(Constant.BASE_URL + url).centerCrop().error(R.mipmap.ic_launcher).into(imageView);
    }
}
