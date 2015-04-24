package com.ngandroid.lib.utils;

import android.util.Log;

/**
 * Created by tyler on 4/24/15.
 */
public class DefaultValueFormatter extends ValueFormatter {

    @Override
    protected double toDouble(String value, String previousValue) {
        if(value.isEmpty())
            return 0;
        return Double.parseDouble(value);
    }

    @Override
    protected int toInt(String value, String previousValue) {
        if(value.isEmpty())
            return 0;
        return Integer.parseInt(value);
    }

    @Override
    protected long toLong(String value, String previousValue) {
        if(value.isEmpty())
            return 0;
        return Long.parseLong(value);
    }

    @Override
    protected float toFloat(String value, String previousValue) {
        if(value.isEmpty())
            return 0;
        return Float.parseFloat(value);
    }

    @Override
    protected String toString(String value, String previousValue) {
        return value;
    }

    @Override
    protected Object toObject(Class<?> type, String value, String previousValue) {
        Log.w("DefaultValueFormatter", "NgAndroid does not know how to convert a String to '" + type.getSimpleName() + "'. Please build NgAndroid with a custom ValueFormatter. You can override toObject() in DefaultValueFormatter.");
        return null;
    }

    @Override
    protected String formatDouble(double value) {
        return fmt(value);
    }

    @Override
    protected String formatInt(int value) {
        return Integer.toString(value);
    }

    @Override
    protected String formatLong(long value) {
        return Long.toString(value);
    }

    @Override
    protected String formatFloat(float value) {
        return fmt(value);
    }

    @Override
    protected String formatString(String value) {
        return value;
    }

    @Override
    protected String formatObject(Object value) {
        return String.valueOf(value);
    }

    protected static String fmt(double d) {
        long ld = (long) d;
        if(d == ld)
            return String.format("%d",ld);
        else
            return String.format("%s",d);
    }
}
