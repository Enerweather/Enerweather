package org.ulpgc.dacd.enerweather.businessunit.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String formatBasicDate(String basicDate) {
        if (basicDate == null || basicDate.length() != 8) {
            return basicDate;
        }

        return basicDate.substring(0, 4) + "-" +
                basicDate.substring(4, 6) + "-" +
                basicDate.substring(6, 8);
    }

    public static String toBasicDate(String isoDate) {
        if (isoDate == null || isoDate.length() < 10) {
            return isoDate;
        }

        return LocalDate.parse(isoDate.substring(0, 10))
                .format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
