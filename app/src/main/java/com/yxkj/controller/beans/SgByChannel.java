package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 根据货道编号查询商品
 */

public class SgByChannel implements Parcelable {
    public String gSpec;

    public String gName;

    public String gImg;

    public int price;

    public int count;

    public String cSn;

    public int cId;

    public int number;

    @Override
    public String toString() {
        return "SgByChannel{" +
                "gSpec='" + gSpec + '\'' +
                ", gName='" + gName + '\'' +
                ", gImg='" + gImg + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", cSn='" + cSn + '\'' +
                ", cId=" + cId +
                ", number=" + number +
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
        dest.writeInt(this.price);
        dest.writeInt(this.count);
        dest.writeString(this.cSn);
        dest.writeInt(this.cId);
    }

    public SgByChannel() {
    }

    protected SgByChannel(Parcel in) {
        this.gSpec = in.readString();
        this.gName = in.readString();
        this.gImg = in.readString();
        this.price = in.readInt();
        this.count = in.readInt();
        this.cSn = in.readString();
        this.cId = in.readInt();
    }

    public static final Parcelable.Creator<SgByChannel> CREATOR = new Parcelable.Creator<SgByChannel>() {
        @Override
        public SgByChannel createFromParcel(Parcel source) {
            return new SgByChannel(source);
        }

        @Override
        public SgByChannel[] newArray(int size) {
            return new SgByChannel[size];
        }
    };
}
