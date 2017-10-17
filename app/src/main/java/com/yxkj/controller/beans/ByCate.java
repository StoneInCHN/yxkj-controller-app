package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 根据类别查询商品
 */

public class ByCate implements Parcelable {
    public String gSpec;

    public String gName;

    public String gImg;

    public double price;

    public int count;

    public int cTemp;

    public String cId;

    public String cSn;

    public int select = 1;

    @Override
    public String toString() {
        return "ByCate{" +
                "gSpec='" + gSpec + '\'' +
                ", gName='" + gName + '\'' +
                ", gImg='" + gImg + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", cTemp=" + cTemp +
                ", cId=" + cId +
                ", cSn='" + cSn + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gSpec);
        dest.writeString(this.gName);
        dest.writeString(this.gImg);
        dest.writeDouble(this.price);
        dest.writeInt(this.count);
        dest.writeInt(this.cTemp);
        dest.writeString(this.cId);
        dest.writeString(this.cSn);
        dest.writeInt(this.select);
    }

    public ByCate() {
    }

    protected ByCate(Parcel in) {
        this.gSpec = in.readString();
        this.gName = in.readString();
        this.gImg = in.readString();
        this.price = in.readDouble();
        this.count = in.readInt();
        this.cTemp = in.readInt();
        this.cId = in.readString();
        this.cSn = in.readString();
        this.select = in.readInt();
    }

    public static final Parcelable.Creator<ByCate> CREATOR = new Parcelable.Creator<ByCate>() {
        @Override
        public ByCate createFromParcel(Parcel source) {
            return new ByCate(source);
        }

        @Override
        public ByCate[] newArray(int size) {
            return new ByCate[size];
        }
    };
}