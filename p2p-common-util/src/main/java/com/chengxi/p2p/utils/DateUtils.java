package com.chengxi.p2p.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author CHENGXI
 * @date 2019/8/16
 */
public class DateUtils {


    /**
     * 通过指定日期添加天数，返回一个日期值
     * @param date
     * @param days
     * @return
     */
    public static Date getDateByAddDays(Date date, Integer days) {
        //创建日期处理类对象
        Calendar calendar = Calendar.getInstance();

        //设置日期处理对象的时间
        calendar.setTime(date);
        //在日期处理类对象上添加天数
        calendar.add(Calendar.DAY_OF_MONTH,days);

        return calendar.getTime();
    }

    /**
     * 通过指定日期添加月份，返回一个日期值
     * @param date
     * @param month
     * @return
     */
    public static Date getDateByAddMonth(Date date, Integer month) {
        //创建日期处理类对象
        Calendar calendar = Calendar.getInstance();

        //设置日期处理对象的时间
        calendar.setTime(date);

        //在日期处理类对象上添加天数
        calendar.add(Calendar.MONTH,month);

        return calendar.getTime();
    }



    /**
     * 获取指定年份的天数
     * @param year
     * @return
     */
    public static int getDaysByYear(int year) {
        //默认为365天，平年
        int days = 365;

        //闰年：能被4整除且不能被100整除或者能被400整除
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            days = 366;
        }

        return days;
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
        System.out.println(getDaysByYear(Calendar.getInstance().get(Calendar.YEAR)));
    }

    public static int getDistanceBetweenDate(Date endDate, Date startDate) {
        int distance = (int) ((endDate.getTime() - startDate.getTime())/ (24*60*60*1000));
        int mod = (int) ((endDate.getTime() - startDate.getTime())% (24*60*60*1000));
        if (mod > 0) {
            distance = distance + 1;
        }
        return distance;
    }

    /**
     * 获取时间戳
     * @return
     */
    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
