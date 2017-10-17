package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 验证商品库存数量
 */

public class VerifyStock implements Parcelable {
    public int count;
    public int cId;

    @Override
    public String toString() {
        return "VerifyStock{" +
                "count=" + count +
                ", cId=" + cId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeInt(this.cId);
    }

    public VerifyStock() {
    }

    protected VerifyStock(Parcel in) {
        this.count = in.readInt();
        this.cId = in.readInt();
    }

    public static final Parcelable.Creator<VerifyStock> CREATOR = new Parcelable.Creator<VerifyStock>() {
        @Override
        public VerifyStock createFromParcel(Parcel source) {
            return new VerifyStock(source);
        }

        @Override
        public VerifyStock[] newArray(int size) {
            return new VerifyStock[size];
        }
    };
}
