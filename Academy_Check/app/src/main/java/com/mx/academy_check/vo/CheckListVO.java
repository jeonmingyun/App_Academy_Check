package com.mx.academy_check.vo;

import com.mx.academy_check.util.JsonConvertible;

public class CheckListVO extends JsonConvertible {
    private String checkListDate;
    private String checkListDay;
    private String start_time;
    private String end_time;

    public CheckListVO() {
    }

    public CheckListVO(String checkListDate, String checkListDay, String startedTime, String endedTime) {
        this.start_time = startedTime;
        this.end_time = endedTime;
        this.checkListDate = checkListDateFormat(checkListDate);
        this.checkListDay = checkListDay;
    }

    public String getCheckListDate() {
        return checkListDate;
    }

    public void setCheckListDate(String checkListDate) {
        this.checkListDate = checkListDateFormat(checkListDate);
    }

    public String getCheckListDay() {
        return checkListDay;
    }

    public void setCheckListDay(String checkListDay) {
        this.checkListDay = checkListDay;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }


    private String checkListDateFormat(String checkListDate) {
        return checkListDate + "일";
    }
}
