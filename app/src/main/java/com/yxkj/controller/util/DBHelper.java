package com.yxkj.controller.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yxkj.controller.application.MyApplication;
import com.yxkj.controller.base.BaseModel;
import com.yxkj.controller.constant.Constant;


/**
 * SQLiteOpenHelper辅助类
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    private DBHelper(Context context) {
        super(context, Constant.SQLITE_NAME, null, Constant.SQLITE_VERSION);
    }

    public static DBHelper getInstance() {
        if (dbHelper == null)
            dbHelper = new DBHelper(MyApplication.getMyApplication());
        return dbHelper;
    }


    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //第一次安装时运行创建
        try {
            for (int i = 0; i < Constant.TABLES.length; i++) {
                Class<BaseModel> baseModleClass = (Class<BaseModel>) Class.forName(Constant.TABLES[i]);
                BaseModel baseModle = baseModleClass.newInstance();
                sqLiteDatabase.execSQL(baseModle.getCreateTableSql());
                LogUtil.e(baseModle.getCreateTableSql());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
