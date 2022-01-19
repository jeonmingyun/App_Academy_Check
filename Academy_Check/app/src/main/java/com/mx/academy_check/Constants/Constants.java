package com.mx.academy_check.Constants;

public class Constants {
    /*test용 서버 주소*/
//    private final static String SERVER_IP = "192.168.0.230";
//    private final static String SERVER_HOST = "8080";
//    public final static String BASE_URL = "http://" + SERVER_IP + ":" + SERVER_HOST + "/Hawon/Hawon/api/";

    private final static String SERVER_IP = "106.240.7.189";
    private final static String SERVER_HOST = "4812";
    public final static String BASE_URL = "http://" + SERVER_IP + ":" + SERVER_HOST + "/Hawon/Hawon/api/";

    public final static String USER_CLASS_STUDENT = "0";
    public final static String USER_CLASS_TEACHER = "1";
    public final static String USER_CLASS_PARENT = "2";
    public final static String USER_CLASS_ACADEMY = "3";

    public final static String FCM_TYPE_TIME_CHECK_START = "1"; // 등원 FCM type
    public final static String FCM_TYPE_TIME_CHECK_END = "2"; // 하원 FCM type
    public final static String FCM_TYPE_TIME_CHECK_TABLET = "3"; // 테블릿 FCM type
}
