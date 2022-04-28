package com.zhonghezhihui.iorg.util;

import org.springframework.util.DigestUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServerUtils {

    public static Boolean isEmpty(String val) {
        return val == null || val == "";
    }
    /**
     * 获取两个日期之间的所有日期
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<String> getDays(String startTime, String endTime) throws ParseException {

        // 返回的日期集合
        List<String> days = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startTime);
        Date end = dateFormat.parse(endTime);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    /**
     * 获取当前天的前day天
     * @return
     */
    public static String getBeforeDay(){
        //当前2022-01-23，返回2022-01-22
        Calendar   cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        String time = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return time;

    }
    public static String getBeforeYear(Integer year,String pattern){
        //当前2022-01-23，返回2021-12
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_YEAR,1);
        String s = formatDate(c.getTime(), pattern);
        return s;

    }


    /**
     * 获取时间戳，精确到秒
     *
     * @return
     */
    public static Long getTimestampSeconds() {
        long milliseconds = System.currentTimeMillis();
        long seconds = milliseconds / 1000;
        return seconds;
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    public static String getRandomString(int length) {
//        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    public static String getRandomNumber(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     *
     * @param number
     * @param length hex字符串长度
     * @return
     */
    public static String intToString(Integer number, Integer length) {
        String src = number.toString();
        Integer len = src.length();
        StringBuilder sb = new StringBuilder(4);
        for (int i=len; i < length; i++) {
            sb.append("0");
        }
        sb.append(src);
        return sb.toString().toUpperCase();
    }

    /**
     * 计算签名
     *
     * @param base
     * @return
     */
    public static String computeMd5Sign(String base) {
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 校验签名
     *
     * @param base
     * @param sign
     * @return
     */
    public static Boolean checkSign(String base, String sign) {
        System.out.println("------------------------------------------校验签名------------------------------------------");
        System.out.println(base);
        String md5 = computeMd5Sign(base);
        System.out.println(md5);
        System.out.println(sign);
        System.out.println("------------------------------------------------------------------------------------------");
        if (md5.equals(sign)) return true;
        return false;
    }

    /**
     * 获取时间字符串
     *
     * @return
     */
    public static String getTimeStr(String pattern) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String timeStr = sdf.format(now);
        return timeStr;
    }


    /**
     * base64生成图片
     *
     * @param base64Str
     * @param imgFilePath
     * @return
     */
    public static boolean GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) // 图像数据为空
            return false;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decode(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            //====
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 生成uuid
     *
     * @return
     */
    public static String getUuid() {
        String id = UUID.randomUUID().toString();
        return id;
    }

    /**
     * 生成自定义uuid
     *
     * @return
     */
    public static String getCustomerUuid() {
        String id = UUID.randomUUID().toString();
        String uid = id.replaceAll("-", "");

        return uid;
    }

    /**
     * 计算金额，单位分转化为元保留两位小数
     *
     * @param amount
     * @return
     */
    public static String computeMoney(Integer amount) {
        float f = (float) amount / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(f);
        return format;
    }

    /**
     * @param date
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = ft.format(date);
        return format;
    }

    /**
     * @param date
     * @param num
     * @param symbol
     * @return
     */
    public static Date computeTime(Date date, Integer num, int symbol) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(symbol, num); //把日期往后增加,整数  往后推,负数往前移动
        date = calendar.getTime();
        return date;
    }

    /**
     * date
     *
     * @return
     */
    public static Date getStartDateTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * date
     *
     * @return
     */
    public static Date getStartMonthTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取6位随机数
     *
     * @return
     */
    public static String getSixNum() {
        String str = "0123456789";
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 6; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 根据开始和结束日期统计区间
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static List getDateRange(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> lDate = new ArrayList();
        lDate.add(begin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        List<String> timeStringList = new ArrayList<>();
        for (Date datePes : lDate) {
            String format = sdf.format(datePes);
            timeStringList.add(format);
        }

        return timeStringList;
    }

    public static List getTodayEveryHour() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, i);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
            hours.add(df.format(calendar.getTime()));
        }

        return hours;
    }

    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    public static long getDaysOfTwoDate(String startDate, String endDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date before = simpleDateFormat.parse(startDate);
        Date after = simpleDateFormat.parse(endDate);
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24) + 1;
    }
    /**
     * 获取当月开始时间和结束日期
     *
     * @return
     */
    public static Map getThisMonthStartAndEndDate(Boolean endToday) {
        Long startTime = getMonthStartTime();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = "";
        if (endToday) {
            endTimeStr = getTodayDate();
        } else {
            Long endTime = getMonthEndTime();
            endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        }


        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }

    /**
     * 获取当月开始时间和结束时间
     *
     * @return
     */
    public static Map getThisMonthStartAndEndTime() {
        Long startTime = getMonthStartTime();
        Long endTime = getMonthEndTime();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startTime", startTimeStr);
        map.put("endTime", endTimeStr);
        return map;
    }

    /**
     * 获取当月开始时间戳
     */
    public static Long getMonthStartTime() {

        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的结束时间戳
     */
    public static Long getMonthEndTime() {
        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }
    public static List<String> getYear2(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//格式化为年月
        int calendarType = Calendar.YEAR;
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.add(calendarType, 0);
        max.setTime(sdf.parse(maxDate));
        max.add(calendarType, 1);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(min.getTime()));
            curr.add(calendarType, 1);
        }

        return result;
    }
    public static Date formatString(String dataS) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataS);
    }


    /**
     * 获取当前月的前month个月第一天
     * @return
     */
    public static String getBeforeMonth(Integer month,String pattern){
        //当前2022-01-23，返回2021-12-01
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH,1);
        String s = formatDate(c.getTime(), pattern);
        return s;

    }
    public static String getLastDayOfYear(final String date) throws ParseException {

        final Calendar cal = Calendar.getInstance();


        SimpleDateFormat fo = new SimpleDateFormat("yyyy");
        cal.setTime(fo.parse(date));

        final int last = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

        cal.set(Calendar.DAY_OF_YEAR, last);

        return formatDate(cal.getTime(), "yyyy-MM-dd");

    }
    public static String getFirstDayDateOfYear(final String date) throws ParseException {

        final Calendar cal = Calendar.getInstance();

        SimpleDateFormat fo = new SimpleDateFormat("yyyy");
        cal.setTime(fo.parse(date));

        final int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);

        cal.set(Calendar.DAY_OF_YEAR, last);

        return formatDate(cal.getTime(), "yyyy-MM-dd");

    }
//getMonthBetween
    public static List<String> getMonthBetween1(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * 获取当前月的前month个月最后一天
     * @return
     */
    public static String getBeforeMonthLast(Integer month,String pattern){
        //当前2022-01-23，返回2021-12-31
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 0);
        String s = formatDate(c.getTime(), pattern);
        return s;

    }

    public static List<String> getYearBetween1(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//格式化为年月
        int calendarType = Calendar.YEAR;
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.add(calendarType, 0);
        max.setTime(sdf.parse(maxDate));
        max.add(calendarType, 1);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(min.getTime()));
            curr.add(calendarType, 1);
        }

        return result;
    }
    /**
     * 获取当前年
     *
     * @return
     */
    public static Integer getYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static Integer getMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH);
    }
    /*public static List<String> getMonth3(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }*/

    /**
     * 根据开始和结束日期统计区间
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static List getDteRange(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        List<Date> lDate = new ArrayList();
        lDate.add(begin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        List<String> timeStringList = new ArrayList<>();
        for (Date datePes : lDate) {
            String format = sdf.format(datePes);
            timeStringList.add(format);
        }

        return timeStringList;
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static Integer getDay() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /***
     * 获取两个时间段的年份/年第一天/年最后一天
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Map> getYearBetween(String startTime, String endTime) {
        List<Map> res = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.YEAR, 1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                String year = dateFormat.format(tempStart.getTime());
                String first = year + "-01-01 00:00:00";
                String last = year + "-12-31 23:59:59";
                String previousYear = (Integer.parseInt(year) - 1) + "";
                String previousFirst = previousYear + "-01-01 00:00:00";
                String previousLast = previousYear + "-12-31 23:59:59";
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("key", year);
                map.put("startTime", first);
                map.put("endTime", last);
                map.put("previousKey", previousYear);
                map.put("previousStartTime", previousFirst);
                map.put("previousEndTime", previousLast);
                res.add(map);
                tempStart.add(Calendar.YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Double formatNum(Double num) {
        return Double.valueOf(String.format("%.2f", num ));
    }


    /***
     * 获取两个时间段的年份-月份/月第一天/月最后一天
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Map> getMonthBetween(String startTime, String endTime) {
        List<Map> res = new ArrayList<Map>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.MONTH, 1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                //当月
                String month = dateFormat.format(tempStart.getTime());
                tempStart.set(Calendar.DAY_OF_MONTH, 1);
                String first = dateFormat3.format(tempStart.getTime());
                tempStart.set(Calendar.DAY_OF_MONTH, tempStart.getActualMaximum(Calendar.DAY_OF_MONTH));
                String last = dateFormat3.format(tempStart.getTime());

                //上月
                Calendar previousC = Calendar.getInstance();
                previousC.setTime(dateFormat.parse(month));
                previousC.add(Calendar.MONTH, -1);
                String previousMonth = dateFormat.format(previousC.getTime());
                String previousFirst = dateFormat3.format(previousC.getTime());
                previousC.set(Calendar.DAY_OF_MONTH, previousC.getActualMaximum(Calendar.DAY_OF_MONTH));
                String previousLast = dateFormat3.format(previousC.getTime());

                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("key", month);
                map.put("startTime", first + " 00:00:00");
                map.put("endTime", last + " 23:59:59");
                map.put("previousKey", previousMonth);
                map.put("previousStartTime", previousFirst + " 00:00:00");
                map.put("previousEndTime", previousLast + " 23:59:59");

                res.add(map);
                tempStart.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatLocalDateTime(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return dtf.format(time);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatLocalDateTime(LocalDateTime time, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        try {
            return dtf.format(time);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parseLocalDateTime(String dateStr) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(dateStr, dtf);
        } catch (Exception e) {
            return null;
        }
    }



    public static List<String> getMonths(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    public static String getFirstDayDateOfMonth(final String date) throws ParseException {

        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM");
        cal.setTime(fo.parse(date));
        final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);

        return   formatDate(cal.getTime(), "yyyy-MM-dd");

    }

    public static String getLastDayOfMonth(final String date) throws ParseException {

        final Calendar cal = Calendar.getInstance();

        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM");
        cal.setTime(fo.parse(date));
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return  formatDate(cal.getTime(), "yyyy-MM-dd");

    }
}
