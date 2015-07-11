package com.ngandroid.demo.model.test;

/**
 * Created by davityle on 1/12/15.
 */
public interface Input {
    String getInput();
    void setInput(String input);
    int getInteger();
    void setInteger(int input);
    boolean getDisabled();
    void setDisabled(boolean disabled);
    boolean getGone();
    void setGone(boolean disabled);
    boolean getInvisible();
    void setInvisible(boolean disabled);
    void setBlur(boolean blur);
    boolean getBlur();
    void setFocus(boolean focus);
    boolean getFocus();
}
