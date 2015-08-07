package com.ngandroid.lib;

import com.ngandroid.lib.utils.Blur;
import com.ngandroid.lib.utils.DefaultBlur;
import com.ngandroid.lib.utils.DefaultValueFormatter;
import com.ngandroid.lib.utils.ValueFormatter;

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
         * builds the NgOptions instance
         * @return NgOptions
         */
        public NgOptions build(){

            if(valueFormatter == null)
                valueFormatter = new DefaultValueFormatter();

            if(blur == null)
                blur = new DefaultBlur(scaleRatio, blurRadius);

            return new NgOptions(valueFormatter, blur);
        }
    }
}
