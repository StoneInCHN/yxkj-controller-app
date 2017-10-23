package com.yxkj.controller.beans;

/**
 *
 */

public class SearchHandleInfo {
    public boolean isContains;
    public SgByChannel sgByChannel;
    public boolean isCount;

    @Override
    public String toString() {
        return "SearchHandleInfo{" +
                "isContains=" + isContains +
                ", sgByChannel=" + sgByChannel +
                ", isCount=" + isCount +
                '}';
    }
}
