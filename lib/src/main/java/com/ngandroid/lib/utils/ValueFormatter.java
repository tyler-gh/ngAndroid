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
     * @param type the class that the value should be converted to
     * @param value the String representation of the type
     * @param previousValue the previous String representation
     * @return the translated value
     * @throws Exception generally a syntax exception
     */
    public <T> T getValue(Class<T> type, String value, String previousValue) {
        try {
            if (int.class.equals(type) || Integer.class.equals(type))
                return (T) toInt(value, previousValue);
            if (long.class.equals(type) || Long.class.equals(type))
                return (T) toLong(value, previousValue);
            if (String.class.equals(type))
                return (T) toString(value, previousValue);
            if (double.class.equals(type) || Double.class.equals(type))
                return (T) toDouble(value, previousValue);
            if (float.class.equals(type) || Float.class.equals(type))
                return (T) toFloat(value, previousValue);
            if (boolean.class.equals(type) || Boolean.class.equals(type))
                return (T) toBoolean(value, previousValue);
        } catch (NumberFormatException ex) {
            if(previousValue != null) {
                return getValue(type, previousValue, null);
            } else {
                return null;
            }
        }

        return (T) toObject(type, value, previousValue);
    }

    protected abstract Boolean toBoolean(String value, String previousValue);

    /**
     * used to 'filter' representations of objects that you wouldn't want displayed to the user.
     * @param value the value that is being filtered
     * @param previousValue the previous value
     * @return the filtered String
     */
    public abstract String filter(String value, String previousValue);

    /**
     * converts a String into a double
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract Double toDouble(String value, String previousValue);

    /**
     * converts a String into a integer
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract Integer toInt(String value, String previousValue);

    /**
     * converts a String into a long
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract Long toLong(String value, String previousValue);

    /**
     * converts a String into a double
     * @param value the value to be converted
     * @param previousValue the last successfully converted value
     * @return the converted value
     */
    protected abstract Float toFloat(String value, String previousValue);

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
    public String format(Object value){
        return format(value, null);
    }

    public String format(Object value, String currentValue){
        if(value == null)
            return "";
        return filter(performFormat(value), currentValue);
    }

    protected String performFormat(Object value) {
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
        if(value instanceof Boolean){
            return formatBoolean((Boolean) value);
        }
        return formatObject(value);
    }

    protected abstract String formatBoolean(Boolean value);

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
