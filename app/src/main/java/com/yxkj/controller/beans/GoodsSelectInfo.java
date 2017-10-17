package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 搜索列表选择商品信息
 */

public class GoodsSelectInfo implements Parcelable {
    public double total_price;
    public int size;

    public GoodsSelectInfo(double total_price, int size) {
        this.total_price = total_price;
        this.size = size;
    }

    @Override
    public String toString() {
        return "GoodsSelectInfo{" +
                "total_price=" + total_price +
                ", size=" + size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.total_price);
        dest.writeInt(this.size);
    }

    public GoodsSelectInfo() {
    }

    protected GoodsSelectInfo(Parcel in) {
        this.total_price = in.readDouble();
        this.size = in.readInt();
    }

    public static final Parcelable.Creator<GoodsSelectInfo> CREATOR = new Parcelable.Creator<GoodsSelectInfo>() {
        @Override
        public GoodsSelectInfo createFromParcel(Parcel source) {
            return new GoodsSelectInfo(source);
        }

        @Override
        public GoodsSelectInfo[] newArray(int size) {
            return new GoodsSelectInfo[size];
        }
    };
}
