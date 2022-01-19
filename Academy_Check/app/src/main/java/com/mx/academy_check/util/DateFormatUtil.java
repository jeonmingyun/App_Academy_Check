package com.mx.academy_check.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {
    private static final String TAG = "DateUtils";
    private static Calendar mCalendar;

    public static final String DATE_FORMAT_1 = "MM.dd(E)"; // 12.12(금)
    public static final String DATE_FORMAT_2 = "HH:mm"; //10:37
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd"; // 2020-04-02
    public static final String DATE_FORMAT_4 = "yyyy년 MM월 dd일"; // 2020년 04월 02일
    public static final String DATE_FORMAT_5 = "yyyy-MM"; // 2020-04
    public static final String DATE_FORMAT_6 = "yyyy-MM-dd HH:mm"; // 2020-04-02 10:37
    public static final String DATE_FORMAT_7 = "E"; // 금
    public static final String DATE_FORMAT_8 = "yyyy-MM-dd(E)"; // 12.12(금)

//    public static final String DATE_FORMAT_1 = "HH:mm:ss"; //10:37 am
//    public static final String DATE_FORMAT_2 = "h:mm a"; // 10:37 am
//    public static final String DATE_FORMAT_3 = "yyyy-MM-dd"; // 2020-02-24
//    public static final String DATE_FORMAT_4 = "dd-MMMM-yyyy"; // 05-December-2018
//    public static final String DATE_FORMAT_5 = "dd MMMM yyyy"; // 05 December 2018
//    public static final String DATE_FORMAT_6 = "dd MMMM yyyy zzzz"; // 05 December 2018 UTC
//    public static final String DATE_FORMAT_7 = "EEE, MMM d, ''yy"; // Wed,Dec 5,'18
//    public static final String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss"; // 2018-12-05 10:37:43
//    public static final String DATE_FORMAT_8 = "h:mm a dd MMMM yyyy"; // 10:37 am 05 December 2018
//    public static final String DATE_FORMAT_10 = "K:mm a, z"; // 10:37 am, UTC
//    public static final String DATE_FORMAT_11 = "hh 'o''clock' a, zzzz"; // 10 o'clock am, UTC
//    public static final String DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // 2018-12-05 T10:37:43.937 Z
//    public static final String DATE_FORMAT_13 = "E, dd MMM yyyy HH:mm:ss z"; // Wed,05 Dec 2018 10:37:43 UTC
//    public static final String DATE_FORMAT_14 = "yyyy.MM.dd G 'at' HH:mm:ss z"; // 2018.12.05 AD at 10:37:43 UTC
//    public static final String DATE_FORMAT_15 = "yyyyy.MMMMM.dd GGG hh:mm aaa"; // 02018.D.05AD 10:37 am
//    public static final String DATE_FORMAT_16 = "EEE, d MMM yyyy HH:mm:ss Z"; // Wed,5 Dec 2018 10:37:43+0000
//    public static final String DATE_FORMAT_17 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // 2018-12-05 T10:37:43.946+0000
//    public static final String DATE_FORMAT_18 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // 2018-12-05 T10:37:43.949 Z
//    public static final String DATE_FORMAT_19 = "dd-MMM-yyyy"; // 05-Dec-2018

    public static String getCurrentDate(String formatType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatType, java.util.Locale.getDefault());
        Date today = Calendar.getInstance().getTime();

        return dateFormat.format(today);
    }

    public static String getCurrentTime(String formatType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatType, java.util.Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    /**
     * @param time in milliseconds (Timestamp)
     * @param mDateFormat SimpleDateFormat
     * @return
     */
    public static String getDateTimeFromTimeStamp(Long time, String mDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat, java.util.Locale.getDefault());
        Date dateTime = new Date(time);
        return dateFormat.format(dateTime);
    }

    /**
     * Get Timestamp from date and time
     * @param mDateTime datetime String
     * @param mDateFormat Date Format
     * @return
     * @throws ParseException
     */
    public static long getTimeStampFromDateTime(String mDateTime, String mDateFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat, java.util.Locale.getDefault());
        Date date = dateFormat.parse(mDateTime);
        return date.getTime();
    }

    public static Date convertStringToDate(String mDateTime, String mDateFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(mDateFormat);
        Date date = formatter.parse(mDateTime);

        return date;
    }

    /**
     * Return datetime String from date object
     * @param mDateFormat format of date
     * @param date date object that you want to parse
     * @return
     */
    public static String convertDateToString(String mDateFormat, Date date) {
        if (date == null) {
            return null;
        }

        return DateFormat.format(mDateFormat, date).toString();
    }

    /**
     * Convert one date format string to another date format string in android
     * @param inputDate input Date String
     * @param inputDateFormat Input SimpleDateFormat
     * @param outputDateFormat Output SimpleDateFormat
     * @return
     * @throws ParseException
     */
    public static String formatChangeDateString(String inputDate, String inputDateFormat, String outputDateFormat) throws ParseException {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputDateFormat, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputDateFormat, java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;
    }

}
