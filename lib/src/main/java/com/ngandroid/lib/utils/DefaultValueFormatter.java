package com.ngandroid.lib.utils;

import android.util.Log;

import java.util.Map;

/**
 * This is the default {@link ValueFormatter} used in the library. Can be extended and passed into
 * {@link com.ngandroid.lib.NgOptions.Builder#setValueFormatter(ValueFormatter)} in order to
 * customize the how values are translated and for adding custom class transformations.
 */
public class DefaultValueFormatter extends ValueFormatter {

    private final Map<Class<?>, Formatter<?>> formatters;

    public DefaultValueFormatter(Map<Class<?>, Formatter<?>> formatters) {
        this.formatters = formatters;
    }

    @Override
    public Boolean getValue(Boolean current, String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public Double getValue(Double current, String value) {
        if(value == null || value.isEmpty())
            return (double) 0;
        try {
            return Double.parseDouble(value);
        } catch(NumberFormatException e) {
            return current;
        }
    }

    @Override
    public Integer getValue(Integer current, String value) {
        if(value == null || value.isEmpty())
            return 0;
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return current;
        }
    }

    @Override
    public Long getValue(Long current, String value) {
        if(value == null || value.isEmpty())
            return (long) 0;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return current;
        }
    }

    @Override
    public Float getValue(Float current, String value) {
        if(value == null || value.isEmpty())
            return (float) 0;
        try {
            return Float.parseFloat(value);
        } catch(NumberFormatException e) {
            return current;
        }
    }

    @Override
    public String getValue(String current, String value) {
        return value;
    }

    @Override
    public <T> T getValue(T current, String value) {
        if(formatters != null) {
            Formatter<T> formatter = (Formatter<T>) formatters.get(current.getClass());
            if(formatter != null) {
                return formatter.getValue(current, value);
            }
        }
        Log.w("DefaultValueFormatter", "DefaultValueFormatter does not know how to convert a String to '" +
                current.getClass().getSimpleName() +
                "'. Either build NgOptions with a custom ValueFormatter or call NgOptions.addFormatter("+
                current.getClass().getSimpleName() +
                ".class, ValueFormatter<" +
                current.getClass().getSimpleName() +
                ">)");
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
    protected String format(Boolean value) {
        return value.toString();
    }

    @Override
    protected String format(double value) {
        return fmt(value);
    }

    @Override
    protected String format(int value) {
        return Integer.toString(value);
    }

    @Override
    protected String format(long value) {
        return Long.toString(value);
    }

    @Override
    protected String format(float value) {
        return fmt(value);
    }

    @Override
    protected String format(String value) {
        return value;
    }

    @Override
    protected <T> String format(T value) {
        if(formatters != null ) {
            Formatter<T> formatter = (Formatter<T>) formatters.get(value.getClass());
            if(formatter != null) {
                return formatter.format(value);
            }
        }
        return String.valueOf(value);
    }

    protected static String fmt(double d) {
        long ld = (long) d;
        if(d == ld)
            return String.format("%d",ld);
        else
            return String.format("%s",d);
    }

    public interface Formatter<T> {
        T getValue(T current, String value);
        String format(T value);
    }

}
