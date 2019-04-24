package com.jiudi.shopping.event;

/**
 * Created by zjj on 2018/1/24 0024.
 */

public class UpdateOrderStatusEvent {
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }
}
