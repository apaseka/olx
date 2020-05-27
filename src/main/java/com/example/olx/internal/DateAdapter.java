package com.example.olx.internal;

import com.example.olx.internal.MonthEnum;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class DateAdapter {

    public static LocalDateTime adapt(String olxDate) {
        final Month month = MonthEnum.findMonthByText(olxDate);

        final String[] split = olxDate.split(" ");
        final String[] timeSplit = split[1].replace(",", "").split(":");

        return LocalDateTime.of(Integer.parseInt(split[4].replace(",", "")),
                month,
                Integer.parseInt(split[2]),
                Integer.parseInt(timeSplit[0]),
                Integer.parseInt(timeSplit[1]),
                59, 999_999_000);
    }

    public static String timeInOlx(LocalDateTime from, LocalDateTime to) {

        LocalDateTime fromTemp = LocalDateTime.from(from);
        long years = fromTemp.until(to, ChronoUnit.YEARS);
        fromTemp = fromTemp.plusYears(years);

        long months = fromTemp.until(to, ChronoUnit.MONTHS);
        fromTemp = fromTemp.plusMonths(months);

        long days = fromTemp.until(to, ChronoUnit.DAYS);
        fromTemp = fromTemp.plusDays(days);

        long hours = fromTemp.until(to, ChronoUnit.HOURS);
        fromTemp = fromTemp.plusHours(hours);

        long minutes = fromTemp.until(to, ChronoUnit.MINUTES);
        fromTemp = fromTemp.plusMinutes(minutes);

        long seconds = fromTemp.until(to, ChronoUnit.SECONDS);
        fromTemp = fromTemp.plusSeconds(seconds);

        long millis = fromTemp.until(to, ChronoUnit.MILLIS);

        return months + " " + (days < 10 ? "0" + days : days) + " " + (hours < 10 ? "0" + hours : hours);
    }
}
