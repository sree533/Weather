package com.app.weatherproject.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date Utility class
 */
public class DateUtil {

    /**
     * From epoch to date format "Today, June 10"
     *
     * @param epoch epoch
     * @return date
     */
    public static String convertEpochToString(long epoch) {
        Date date = new Date(epoch * 1000);
        DateFormat convertFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
        return convertFormat.format(date);
    }
}
