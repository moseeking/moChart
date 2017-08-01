package com.youdy.mo.mochart.utils;

/**
 * Created by mo on 2017/6/19.
 */


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    //获取当前日期

    public static String getNow_MMDD() {
        return getMMdd(0, 0, 0);
    }
    public static String getMMdd(int year, int month, int day) {
        return getMMdd(year, month, day, 0);
    }

   // 获取以指定年月日为基础向前或向后推几天的日期

    public static String getMMdd(int year, int month, int day, int amount) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0 && month != 0 && day != 0)
            calendar.set(year, month - 1, day);
        //向前或向后推daysLater天数
        calendar.add(Calendar.DAY_OF_MONTH, amount);

        Date mDate = calendar.getTime();
        return new SimpleDateFormat("MM-dd").format(mDate);
    }


    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    public static int getYear() {
        return getYear(new Date());
    }
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1);
    }
    public static int getMonth() {
        return getMonth(new Date());
    }
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static int getDay() {
        return getDay(new Date());
    }

    public static int getFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH)-getDaynumOfWeek(date)+1;
    }


    //获取当前星期
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = getDaynumOfWeek(date);
        return weekDays[w];
    }
    public static int getDaynumOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }
    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws \
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int getWeekNum(Date smdate,Date bdate){
        int week  = 1;
        try {
            int day = daysBetween(smdate,bdate);
            Log.d("day：  ", day+"");
            week = day / 7;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(week > 20){
            week = 20;
        }
        return week;
    }

}