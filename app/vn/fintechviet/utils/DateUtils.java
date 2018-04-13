package vn.fintechviet.utils;

import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tungn on 9/1/2017.
 */
public class DateUtils {
    private static String FORMAT_DATE = "dd/MM/yyyy HH:mm:SS";
    private static String FORMAT_DATE_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static String FORMAT_DATE1 = "dd/MM/yyyy";
    private static String FORMAT_DATE_STR = "dd/MM/yyyy";
    private static String FORMAT_DATE2 = "yyyy-MM-dd HH:mm:ss";
    private static String FORMAT_DATE_NEWS = "dd/MM/yyyy - HH:mm";
    public static String EQUAL = "EQUAL";
    public static String BEFORE = "BEFORE";
    public static String AFTER = "AFTER";


    private static String convertEngDayToVn(String dayOfWeek) {
        String dayOfVn;
        switch (dayOfWeek) {
            case "Monday":
                dayOfVn = "Thứ hai";
                break;
            case "Tuesday":
                dayOfVn = "Thứ ba";
                break;
            case "Wednesday":
                dayOfVn = "Thứ tư";
                break;
            case "Thursday":
                dayOfVn = "Thứ năm";
                break;
            case "Friday":
                dayOfVn = "Thứ sáu";
                break;
            case "Saturday":
                dayOfVn = "Thứ bảy";
                break;
            default:
                dayOfVn = "Chủ nhật";
        }
        return dayOfVn;
    }

    public static String convertIntDayToString(int day) {
        String dayOfVn;
        switch (day) {
            case 0:
                dayOfVn = "Chủ nhật";
                break;
            case 1:
                dayOfVn = "Thứ hai";
                break;
            case 3:
                dayOfVn = "Thứ ba";
                break;
            case 4:
                dayOfVn = "Thứ tư";
                break;
            case 5:
                dayOfVn = "Thứ năm";
                break;
            case 6:
                dayOfVn = "Thứ sáu";
                break;
            default:
                dayOfVn = "Thứ 7";
        }
        return dayOfVn;
    }

    public static String replaceEnglishDay(String weekday) {
        String dayOfVn;
        if (weekday.contains("Monday")) {
            dayOfVn = weekday.replace("Monday", "Thứ hai");
        } else if (weekday.contains("Tuesday")) {
            dayOfVn = weekday.replace("Tuesday", "Thứ ba");
        } else if (weekday.contains("Wednesday")) {
            dayOfVn = weekday.replace("Wednesday", "Thứ tư");
        } else if (weekday.contains("Thursday")) {
            dayOfVn = weekday.replace("Thursday", "Thứ năm");
        } else if (weekday.contains("Friday")) {
            dayOfVn = weekday.replace("Friday", "Thứ sáu");
        } else if (weekday.contains("Saturday")) {
            dayOfVn = weekday.replace("Saturday", "Thứ bảy");
        } else {
            dayOfVn = weekday.replace("Sunday", "Chủ nhật");
        }
        dayOfVn = dayOfVn.replace("Closed", "Đóng cửa");
        return dayOfVn;
    }

    public static String convertDateToString(Date dateVal) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        String day = convertEngDayToVn(dayFormat.format(dateVal));
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_NEWS);
        return day + ", " + dateFormat.format(dateVal);
    }

    public static Date convertStringToDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
            return dateFormat.parse(dateStr);
        } catch (ParseException ex){
        }
        return null;
    }

    public static Date convertStringToOnlyDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE1);
            return dateFormat.parse(dateStr);
        } catch (ParseException ex){
        }
        return null;
    }

    public static String convertDateToStringUTC(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_UTC);
        return dateFormat.format(date);
    }

    public static Date convertStringToDate2(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE2);
            return dateFormat.parse(dateStr);
        } catch (ParseException ex){
        }
        return null;
    }

    public static String compare(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);

            if (cal1.after(cal2)) {
                return DateUtils.AFTER;
            }

            if (cal1.before(cal2)) {
                return DateUtils.BEFORE;
            }

            if (cal1.equals(cal2)) {
                return DateUtils.EQUAL;
            }
        } catch (ParseException ex){
        }
        return DateUtils.EQUAL;
    }

    public static Tuple2<Date, Date> getCurrentWeekInterval() {
        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);

        Calendar first, last;

        first = (Calendar) today.clone();
        int dayOfWeek = first.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                last = (Calendar) first.clone();
                first.add(Calendar.DAY_OF_WEEK, -6);

                break;
            default:
                first.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - dayOfWeek);

                last = (Calendar) first.clone();
                last.add(Calendar.DAY_OF_WEEK, 6);

                break;
        }

        return Tuple.of(first.getTime(), last.getTime());
    }
}
