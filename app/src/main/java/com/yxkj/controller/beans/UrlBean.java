package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 上位机指令实体
 */

public class UrlBean implements Parcelable {
    public String key;
    public String url;
    public long record_id;

    @Override
    public String toString() {
        return "UrlBean{" +
                "key='" + key + '\'' +
                ", url='" + url + '\'' +
                ", record_id=" + record_id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.url);
        dest.writeLong(this.record_id);
    }

    public UrlBean() {
    }

    protected UrlBean(Parcel in) {
        this.key = in.readString();
        this.url = in.readString();
        this.record_id = in.readLong();
    }

    public static final Parcelable.Creator<UrlBean> CREATOR = new Parcelable.Creator<UrlBean>() {
        @Override
        public UrlBean createFromParcel(Parcel source) {
            return new UrlBean(source);
        }

        @Override
        public UrlBean[] newArray(int size) {
            return new UrlBean[size];
        }
    };
}
