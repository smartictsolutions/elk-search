/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(CalendarHelper.class);
    private static final Random rand; // SecureRandom is preferred to Random

    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp getAfterTimeUnit(Timestamp startTime, int timeType, int additional) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(timeType, additional);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static Date getAfterYearNow(int amount) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();
    }

    public static Timestamp getAfterYearNowByTimestamp(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, amount);
        long time = calendar.getTime().getTime();
        return new Timestamp(time);
    }

    public static Date getDateAfterTimeUnit(int amount, int timeUnit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, amount);
        return calendar.getTime();
    }

    public static Date getDateAfterTimeUnit(Date date, int amount, int timeUnit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(timeUnit, amount);
        return calendar.getTime();
    }

    public static Date getFirstDayOfSpecifiedWeek(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        if (amount != 0)
            calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return calendar.getTime();
    }

    public static Date getLastDayOfSpecifiedWeek(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        if (amount != 0)
            calendar.add(Calendar.WEEK_OF_YEAR, amount);
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }

    public static Date getFirstDayOfSpecifiedMonth(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        if (amount != 0)
            calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }

    public static Date getLastDayOfSpecifiedMonth(int amount) {
        Calendar calendar = Calendar.getInstance();
        if (amount != 0)
            calendar.add(Calendar.MONTH, amount);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date getFirstDayOfYear(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        if (amount != 0)
            calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();
    }

    public static Date getFirstDayOfYear() {
        return getFirstDayOfYear(0);
    }

    public static Date getDate(Timestamp time) {
        if (time == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.getTime();
    }

    public static String getFormattedDate(Timestamp time) {
        if (time == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Date date = calendar.getTime();
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static Timestamp setYearMonthDateByZero(Timestamp time) {
        if (time == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(0, 0, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String getFormattedDate(Date date) {
        return getFormattedDate(date, "dd/MM/yyyy");
    }

    public static String getFormattedDate(Date date, String pattern) {
        if (date == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static Long getTimeDifference(Timestamp startDate, Timestamp endDate) {
        if (startDate == null || endDate == null) {
            return null;
        } else {
            return (getDate(endDate).getTime() - getDate(startDate).getTime()) / (1000 * 60 * 60 * 24);
        }
    }

    public static int getDayDifference(Timestamp startDate, Timestamp endDate) {
        if (startDate == null)
            return 0;
        long l;
        if (endDate == null) {
            Calendar cal = Calendar.getInstance();
            l = (cal.getTime().getTime() - getDate(startDate).getTime());
        } else {
            l = (getDate(endDate).getTime() - getDate(startDate).getTime());
        }
        long ret = l / (1000 * 60 * 60 * 24);
        return (int) ret;
    }

    public static Date getTime(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }

    public static Date generateRandomDate(Date startDate, Date endDate) {
        long random = rand.nextLong((endDate.getTime() - startDate.getTime()) - 1) + startDate.getTime();
        return new Date(random);
    }

    public static boolean nowIsBetween(Date startDate, Date endDate) {
        return startDate.before(Calendar.getInstance().getTime()) && endDate.after(Calendar.getInstance().getTime());
    }

    public static boolean dateIsBetween(Date startDate, Date endDate, Date date) {
        return startDate.before(date) && endDate.after(date);
    }

    public static boolean dateIsAfter(Date startDate, Date date) {
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            date = calendar.getTime();
        }
        return date.after(startDate);
    }

    public static Timestamp getTimeStamp() {
        Date date = new Date();
        long time = date.getTime();
        return new Timestamp(time);
    }

    public static Date getTodaysDate() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        return todayCal.getTime();
    }

    public static String convertDateToString(Timestamp date, String format) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(format);
            return formatDate.format(date);

        } catch (Exception ex) {
            LOGGER.error("Convert time to string failed!", ex);
        }
        return null;
    }

    public static void get() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
    }

    public static Integer getAge(Timestamp birthDate) {
        return (int) (getDayDifference(birthDate, getTimeStamp()) / 365.25);
    }

    public static boolean datesAreConsecutive(List<java.sql.Date> dates) {
        boolean hasConsecutiveDates = dates.stream().anyMatch(new Predicate<java.sql.Date>() {
            private java.sql.Date previous;

            @Override
            public boolean test(java.sql.Date date) {
                boolean consecutiveByDay = false;
                if (previous != null) {
                    consecutiveByDay = ChronoUnit.DAYS.between(previous.toLocalDate(), date.toLocalDate()) == 1;
                }
                previous = date;
                return consecutiveByDay;
            }
        });
        return false;
    }

    /**
     * başlangıç ve bitiş tarihleri eşitmi yada ardışıkmı diye bakar.
     *
     * @param  startDate
     * @param  endDate
     * @return
     */
    public static boolean datesAreEqualsOrConsecutive(Timestamp startDate, Timestamp endDate) {
        int dif = getDayDifference(startDate, endDate);
        return dif <= 1;
    }

    public static Date getDay(Long values) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
