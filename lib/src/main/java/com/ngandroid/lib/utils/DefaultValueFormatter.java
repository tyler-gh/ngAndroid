package com.ngandroid.lib.utils;

import android.util.Log;

/**
 * This is the default {@link ValueFormatter} used in the library. Can be extended and passed into
 * {@link com.ngandroid.lib.NgOptions.Builder#setValueFormatter(ValueFormatter)} in order to
 * customize the how values are translated and for adding custom class transformations.
 */
public class DefaultValueFormatter extends ValueFormatter {

    @Override
    protected Boolean toBoolean(String value, String previousValue) {
        return Boolean.parseBoolean(value);
    }

    @Override
    protected Double toDouble(String value, String previousValue) {
        if(value.isEmpty())
            return (double) 0;
        return Double.parseDouble(value);
    }

    @Override
    protected Integer toInt(String value, String previousValue) {
        if(value.isEmpty())
            return 0;
        return Integer.parseInt(value);
    }

    @Override
    protected Long toLong(String value, String previousValue) {
        if(value.isEmpty())
            return (long) 0;
        return Long.parseLong(value);
    }

    @Override
    protected Float toFloat(String value, String previousValue) {
        if(value.isEmpty())
            return (float) 0;
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

    /**
     * The default filter removes the display of '0' or '0.0' if the user has erased the text. This is
     * to prevent a number being shown when the user tries to erase it.
     * @param value the value that is being filtered
     * @param previousValue the previous value
     * @return
     */
    @Override
    public String filter(String value, String previousValue){
        if(previousValue != null && value.length() > previousValue.length())
            return value;
        return value.equals("0") || value.equals("0.0") ? "" : value;
    }

    @Override
    protected String formatBoolean(Boolean value) {
        return value.toString();
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
