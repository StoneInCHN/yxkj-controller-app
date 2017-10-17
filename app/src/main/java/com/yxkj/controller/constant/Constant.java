package com.yxkj.controller.constant;

import com.yxkj.controller.application.MyApplication;

import java.io.File;

/**
 * 常量类
 */

public class Constant {

    public static final String SQLITE_NAME = "_db";//数据库名称

    public static final int SQLITE_VERSION = 1;//数据库版本

    //数据库所有的表
    public static String[] TABLES;
//            = new String[]{UserModel.class.getName()};

    public static final String BASE_URL = "http://shelf.ybjcq.com:8080";

    public static final long TIMEOUT = 30;

    public static String url_sougou = "http://wap.dl.pinyin.sogou.com/";
    public static String VIDEO = "http://tb-video.bdstatic.com/tieba-smallvideo-spider/8391224_e8b06bccccb0867cd70cc2f32d942c7c/";
    public static String VIDEO_URL = "http://tb-video.bdstatic.com/tieba-smallvideo-spider/8391224_e8b06bccccb0867cd70cc2f32d942c7c.mp4";

    public static final String CONFIGURE_FILE_PATH = "/controller";

    /**
     * 两个视频本地地址
     */
    public static String VIDEO_TOP_ADDRESS= MyApplication.getMyApplication().getExternalFilesDir(null) + File.separator + "video_top.mp4";
    public static String VIDEO_BOTTOM_ADDRESS= MyApplication.getMyApplication().getExternalFilesDir(null) + File.separator + "video_bottom.mp4";
    /**
     * 上位机指令
     */
    public static final String VIDEO_TOP = "video_top";
    public static final String VIDEO_BOTTOM = "video_bottom";
    public static final String IMG_LEFT = "img_left";
    public static final String IMG_CENTER = "img_center";
    public static final String IMG_RIGHT = "img_right";
    /**
     * 中控接口
     */

    public static final String GETSGBYCHANNEL = "/goods/getSgByChannel";//1.7.1	根据货道编号查询商品
    public static final String GETCATEGORY = "/goods/getCategory";//1.7.2	查询商品类别
    public static final String GETBYCATE = "/goods/getByCate";//1.7.3	根据类别查询商品
    public static final String VERIFYSTOCK = "/goods/verifyStock";//1.7.4	验证商品库存数量

}
