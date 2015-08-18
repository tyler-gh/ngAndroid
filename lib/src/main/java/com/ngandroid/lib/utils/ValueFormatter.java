package com.ngandroid.lib.utils;

/**
 * Used by bindings to translate values to and from Strings.
 * Can be extended and passed into
 * {@link com.ngandroid.lib.NgOptions.Builder#setValueFormatter(ValueFormatter)} in order to
 * customize the how values are translated and for adding custom class transformations.
 */
public abstract class ValueFormatter {

    public abstract Boolean getValue(Boolean current, String value);

    public abstract Double getValue(Double current, String value);

    public abstract Float getValue(Float current, String value);

    public abstract Long getValue(Long current, String value);

    public abstract Integer getValue(Integer current, String value);

    public abstract String getValue(String current, String value);

    public abstract <T> T getValue(T current, String value);

    /**
     * used to 'filter' representations of objects that you wouldn't want displayed to the user.
     * @param value the value that is being filtered
     * @param previousValue the previous value
     * @return the filtered String
     */
    public abstract String filter(String value, String previousValue);


    public String format(Object value, String currentValue){
        if(value == null)
            return "";
        return filter(format(value), currentValue);
    }

    public String format(Boolean value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }

    /**
     * formats a double to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    public String format(Double value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }

    /**
     * formats an int to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    public String format(Integer value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }

    /**
     * formats a long to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    public String format(Long value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }

    /**
     * formats a float to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    public String format(Float value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }

    /**
     * formats a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    public String format(String value, String currentValue) {
        if(value == null) {
            return "";
        }
        return filter(format(value), currentValue);
    }


    protected abstract String format(Boolean value);

    /**
     * formats a double to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String format(double value);

    /**
     * formats an int to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String format(int value);

    /**
     * formats a long to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String format(long value);

    /**
     * formats a float to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String format(float value);

    /**
     * formats a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract String format(String value);

    /**
     * formats an object to a String
     * @param value the value to be formatted
     * @return the formatted value
     */
    protected abstract <T> String format(T value);
}
