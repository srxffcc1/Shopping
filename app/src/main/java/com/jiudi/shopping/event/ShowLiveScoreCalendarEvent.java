package com.jiudi.shopping.event;

/**
 * Created by zjj on 2018/1/5 <br>
 * Email:1260968684@qq.com <p>
 *     比分直播是篮球的时候有一个选择日期的图标
 */

public class ShowLiveScoreCalendarEvent {
    private boolean mShowCalendar = false;

    public boolean isShowCalendar() {
        return mShowCalendar;
    }

    public void setShowCalendar(boolean showCalendar) {
        mShowCalendar = showCalendar;
    }
}
