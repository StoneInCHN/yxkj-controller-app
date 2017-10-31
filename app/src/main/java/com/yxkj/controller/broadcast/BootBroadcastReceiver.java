package com.yxkj.controller.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yxkj.controller.activity.MainActivity;
import com.yxkj.controller.http.HttpFactory;
import com.yxkj.controller.share.SharePrefreceHelper;

/**
 *
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    /**
     * 可以实现开机自动打开软件并运行。
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //MainActivity就是开机显示的界面
            long record_id = SharePrefreceHelper.getInstence(context).getRestart();
            if (record_id != -1) {
                HttpFactory.updateCmdStatus(record_id, true);
                SharePrefreceHelper.getInstence(context).setRestart(-1);
            }
            Intent mBootIntent = new Intent(context, MainActivity.class);
            //下面这句话必须加上才能开机自动运行app的界面
            mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        }
    }
}
