package com.yxkj.controller.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询商品类别
 */

public class Category implements Parcelable {
    public int id;

    public int cateOrder;

    public String cateName;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", cateOrder=" + cateOrder +
                ", cateName='" + cateName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.cateOrder);
        dest.writeString(this.cateName);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.cateOrder = in.readInt();
        this.cateName = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
