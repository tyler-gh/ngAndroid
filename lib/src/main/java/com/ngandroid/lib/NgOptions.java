package com.ngandroid.lib;

import com.ngandroid.lib.utils.Blur;
import com.ngandroid.lib.utils.DefaultBlur;
import com.ngandroid.lib.utils.DefaultValueFormatter;
import com.ngandroid.lib.utils.ValueFormatter;

import java.util.HashMap;
import java.util.Map;

public class NgOptions {

    private final ValueFormatter valueFormatter;
    private final Blur blur;

    public NgOptions(ValueFormatter valueFormatter, Blur blur) {
        this.valueFormatter = valueFormatter;
        this.blur = blur;
    }

    public ValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    public Blur getBlur() {
        return blur;
    }

    /**
     * Customizes and Builds a NgOptions
     */
    public static final class Builder {

        private ValueFormatter valueFormatter;
        private Map<Class<?>, DefaultValueFormatter.Formatter<?>> formatters;
        private Blur blur;
        private float scaleRatio = 5;
        private float blurRadius = 5;

        /**
         * sets the value formatter that will be used to format and convert values
         * @param valueFormatter the value formatter
         * @return this
         */
        public Builder setValueFormatter(ValueFormatter valueFormatter){
            this.valueFormatter = valueFormatter;
            return this;
        }

        /**
         * sets the blur that will blur views
         * @param blur
         * @return
         */
        public Builder setBlur(Blur blur) {
            this.blur = blur;
            return this;
        }

        /**
         * sets the scale ratio that will be used by {@link DefaultBlur}, only works if #setBlur is
         * not used
         * @param scaleRatio
         * @return
         */
        public Builder setDefaultBlurScaleRatio(float scaleRatio){
            this.scaleRatio = scaleRatio;
            return this;
        }

        /**
         * sets the blur radius that will be used by {@link DefaultBlur}, only works if #setBlur is
         * not used
         * @param blurRadius
         * @return
         */
        public Builder setDefaultBlurRadius(float blurRadius){
            this.blurRadius = blurRadius;
            return this;
        }

        /**
         * adds a formatter to the {@link DefaultValueFormatter} only works if {@link #setValueFormatter(ValueFormatter)}
         * is not called
         * @param clss
         * @param formatter
         * @param <T>
         */
        public <T> void addFormatter(Class<T> clss, DefaultValueFormatter.Formatter<T> formatter) {
            if(formatters == null) {
                formatters = new HashMap<>();
            }
            formatters.put(clss, formatter);
        }

        /**
         * builds the NgOptions instance
         * @return NgOptions
         */
        public NgOptions build(){

            if(valueFormatter == null)
                valueFormatter = new DefaultValueFormatter(formatters);

            if(blur == null)
                blur = new DefaultBlur(scaleRatio, blurRadius);

            return new NgOptions(valueFormatter, blur);
        }
    }
}
