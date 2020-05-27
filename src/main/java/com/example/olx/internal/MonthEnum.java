package com.example.olx.internal;

import java.time.Month;
import java.util.Arrays;

public enum MonthEnum {

    JAN(Month.JANUARY, "янв"),
    FEB(Month.FEBRUARY, "фев"),
    MAR(Month.MARCH, "мар"),
    APR(Month.APRIL, "апр"),
    MAY(Month.MAY, "мая"),
    JUN(Month.JUNE, "июн"),
    JUL(Month.JULY, "июл"),
    AUG(Month.AUGUST, "авг"),
    SEP(Month.SEPTEMBER, "сен"),
    OCT(Month.OCTOBER, "окт"),
    NOV(Month.NOVEMBER, "ноя"),
    DEC(Month.DECEMBER, "дек");

    private String text;
    private Month month;

    MonthEnum(Month month, String text) {
        this.month = month;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Month getMonth() {
        return month;
    }

    public static Month findMonthByText(String text) {
        final MonthEnum monthEnum = Arrays.stream(MonthEnum.values()).filter(e -> text.contains(e.getText())).findAny().get();
        return monthEnum.getMonth();
    }
}
