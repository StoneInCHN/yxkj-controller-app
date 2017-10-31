package com.yxkj.controller.tools;

import android.content.Context;
import android.media.AudioManager;

import com.yxkj.controller.application.MyApplication;

/**
 * 声音管理器
 */

public class ControlAudioManager {
    private AudioManager audioManager;
    private int maxVolume;
    private static ControlAudioManager controlAudioManager = null;

    private ControlAudioManager() {
        audioManager = (AudioManager) MyApplication.getMyApplication().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static ControlAudioManager newInstance() {
        if (controlAudioManager == null) {
            controlAudioManager = new ControlAudioManager();
        }
        return controlAudioManager;
    }

    /**
     * 设置当前音量百分比
     *
     * @param curentVolume
     */
    public void setVolume(float curentVolume) {
        if (curentVolume > 100) curentVolume = 100;
        float percent = curentVolume / 100;
        int curVolume = (int) (maxVolume * percent);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
    }

    /**
     * 获取当前音量
     */
    public int getCurrentStreamVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
