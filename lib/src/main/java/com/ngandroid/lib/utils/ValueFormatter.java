package com.ngandroid.lib.utils;

/**
 * Used by {@link com.ngandroid.lib.ngattributes.NgModel} and
 * {@link com.ngandroid.lib.ngattributes.NgText} to translate values to and from Strings.
 * Can be extended and passed into
 * {@link com.ngandroid.lib.NgAndroid.Builder#setValueFormatter(ValueFormatter)} in order to
 * customize the how values are translated and for adding custom class transformations.
 */
public abstract class ValueFormatter {

    /**
     * This translates a string into a value. It must translate the String into the class that is
     * passed in
     * @param intType the integer representation of the class
     * @param type the class that the value should be converted to
     * @param value the String representation of the type
     * @param previousValue the previous String representation
     * @return the translated value
     * @throws Exception generally a syntax exception
     */
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

    /**
     * used to 'filter' representations of objects that you wouldn't want displayed to the user. The
     * default function removes the display of '0' or '0.0' if the user has erased the text. This is
     * to prevent a number being shown when the user tries to erase it.
     * @param value the value that is being filtered
     * @param previousValue the previous value
     * @return the filtered String
     */
    public String filter(String value, String previousValue){
        if(value.length() > previousValue.length())
            return value;
        return value.equals("0") || value.equals("0.0") ? "" : value;
    }

    /**
     * converts a String into a double
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract double toDouble(String value, String previousValue);

    /**
     * converts a String into a integer
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract int toInt(String value, String previousValue);

    /**
     * converts a String into a long
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract long toLong(String value, String previousValue);

    /**
     * converts a String into a double
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract float toFloat(String value, String previousValue);

    /**
     * converts a String into a String, generally not useful...
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract String toString(String value, String previousValue);

    /**
     * converts a String into a Object. Useful for custom classes
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract Object toObject(Class<?> type, String value, String previousValue);

    /**
     * determins the type of the object and calls on of : {@link #formatDouble(double)},
     * {@link #formatFloat(float)}, {@link #formatInt(int)}, {@link #formatLong(long)},
     * {@link #formatString(String)}, {@link #formatObject(Object)}
     * @param value
     * @return
     */
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

    /**
     * formats a double to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatDouble(double value);

    /**
     * formats an int to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatInt(int value);

    /**
     * formats a long to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatLong(long value);

    /**
     * formats a float to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatFloat(float value);

    /**
     * formats a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatString(String value);

    /**
     * formats an object to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String formatObject(Object value);
}
