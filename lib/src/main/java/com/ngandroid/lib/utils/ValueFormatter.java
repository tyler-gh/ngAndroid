package com.ngandroid.lib.utils;

/**
 * Created by tyler on 4/24/15.
 */
public abstract class ValueFormatter {

    public final Object toValue(int intType, Class<?> type, String value, String previousValue) throws Exception{
        switch (intType){
            case TypeUtils.STRING:
                return toString(value, previousValue);
            case TypeUtils.DOUBLE:
                return toDouble(value, previousValue);
            case TypeUtils.INTEGER:
                return toInt(value, previousValue);
            case TypeUtils.LONG:
                return toLong(value, previousValue);
            case TypeUtils.FLOAT:
                return toFloat(value, previousValue);
            case TypeUtils.OBJECT:
            default:
                return toObject(type, value, previousValue);
        }
    }

    public String filter(String value, String previousValue){
        if(value.length() > previousValue.length())
            return value;
        return value.equals("0") || value.equals("0.0") ? "" : value;
    }

    protected abstract double toDouble(String value, String previousValue);
    protected abstract int toInt(String value, String previousValue);
    protected abstract long toLong(String value, String previousValue);
    protected abstract float toFloat(String value, String previousValue);
    protected abstract String toString(String value, String previousValue);
    protected abstract Object toObject(Class<?> type, String value, String previousValue);


    public final String format(Object value){
        if(value instanceof Double){
            return formatDouble((Double) value);
        }
        if(value instanceof Integer){
            return formatInt((Integer) value);
        }
        if(value instanceof Long){
            return formatLong((Long) value);
        }
        if(value instanceof Float){
            return formatFloat((Float) value);
        }
        if(value instanceof String){
            return formatString((String) value);
        }
        return formatObject(value);
    }

    protected abstract String formatDouble(double value);
    protected abstract String formatInt(int value);
    protected abstract String formatLong(long value);
    protected abstract String formatFloat(float value);
    protected abstract String formatString(String value);
    protected abstract String formatObject(Object value);
}
