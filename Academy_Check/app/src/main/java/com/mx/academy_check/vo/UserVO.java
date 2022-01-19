package com.mx.academy_check.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.mx.academy_check.util.JsonConvertible;

import java.io.Serializable;
import java.util.ArrayList;

public class UserVO extends JsonConvertible implements Parcelable, Serializable {

    private int itemPosition;

    private String user_class;
    private String user_name;
    private String user_code;
    private String user_phone_num;
    private String user_gender;
    private String parent_code;
    private String academy_code;
    private String check_date; // 2021-08
    private String start_time; // 2021-04-03 12:04
    private String start_day;
    private String end_time; // 2021-04-03 12:04
    private String end_day;

    /*when response parameter from server*/
    private String student_idx; // user_code와 동일
    private String teacher_code; // user_code와 동일
    private String teacher_name; // user_name와 동일
    private String phone_number; // user_phone_num와 동일
    private String gender; // user_gender와 동일
    private String day; // start_day와 동일

    public UserVO() {
    }

    public UserVO(String name, String code, String phoneNum, String gender) {
        this.user_name = name;
        this.user_code = code;
        this.user_phone_num = phoneNum;
        this.user_gender = gender;
    }

    protected UserVO(Parcel in) {
        user_class = in.readString();
        user_name = in.readString();
        user_code = in.readString();
        user_phone_num = in.readString();
        user_gender = in.readString();
        parent_code = in.readString();

        start_time = in.readString();
        start_day = in.readString();
        end_time = in.readString();
        end_day = in.readString();

        gender = in.readString();
    }

    public static final Creator<UserVO> CREATOR = new Creator<UserVO>() {
        @Override
        public UserVO createFromParcel(Parcel in) {
            return new UserVO(in);
        }

        @Override
        public UserVO[] newArray(int size) {
            return new UserVO[size];
        }
    };

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public String getUser_class() {
        return user_class;
    }

    public void setUser_class(String user_class) {
        this.user_class = user_class;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_phone_num() {
        return user_phone_num;
    }

    public void setUser_phone_num(String user_phone_num) {
        this.user_phone_num = user_phone_num;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getStart_time() {
        return start_time;
    }

    /**
     * @param start_time yyyy-MM-ss HH:mm
     * */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public void setStart_day(int start_day) {
        this.start_day = apiDayFormat(start_day);
    }

    public String getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time yyyy-MM-ss HH:mm
     * */
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
}

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public void setEnd_day(int end_day) {
        this.end_day = apiDayFormat(end_day);
    }

    public String getCheck_date() {
        return check_date;
    }

    /**
     * @param check_date yyyy-MM
     * */
    public void setCheck_date(String check_date) {
        this.check_date = check_date;
    }

    public String apiDayFormat(int day) {
        String formattedDay = "";

        switch (day) {
            case 0:
                formattedDay = "토";
                break;
            case 1:
                formattedDay = "일";
                break;
            case 2:
                formattedDay = "월";
                break;
            case 3:
                formattedDay = "화";
                break;
            case 4:
                formattedDay = "수";
                break;
            case 5:
                formattedDay = "목";
                break;
            case 6:
                formattedDay = "금";
                break;
            default:
                formattedDay = "";
        }

        return formattedDay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_class);
        dest.writeString(user_name);
        dest.writeString(user_code);
        dest.writeString(user_phone_num);
        dest.writeString(user_gender);
        dest.writeString(parent_code);
        dest.writeString(start_time);
        dest.writeString(start_day);
        dest.writeString(end_time);
        dest.writeString(end_day);

        dest.writeString(gender);
    }

    /*when response parameter from server*/
    public String getStudent_idx() {
        return student_idx;
    }

    public void setStudent_idx(String student_idx) {
        this.student_idx = student_idx;
        this.user_code = student_idx;
    }

    public String getTeacher_code() {
        return teacher_code;
    }

    public void setTeacher_code(String teacher_code) {
        this.teacher_code = teacher_code;
        this.user_code = teacher_code;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
        this.user_name = teacher_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        this.user_phone_num = phone_number;
    }

    public String getAcademy_code() {
        return academy_code;
    }

    public void setAcademy_code(String academy_code) {
        this.academy_code = academy_code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.user_gender = gender;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
        this.start_day = day;
    }

    public ArrayList<UserVO> getTestList() {
        ArrayList<UserVO> list = new ArrayList<>();
        UserVO vo;
        for(int i = 0 ; i < 10; i++) {
            vo = new UserVO(i+"name", i+"code", i+"phoneNum", "남");
            list.add(vo);
        }
        return list;
    }
}
