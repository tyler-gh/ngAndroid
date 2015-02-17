package com.ngandroid.demo.models;

/**
 * Created by davityle on 1/12/15.
 */
public interface Input {
    public String getInput();
    public void setInput(String input);
    public int getInteger();
    public void setInteger(int input);
    public boolean getDisabled();
    public void setDisabled(boolean disabled);
    public boolean getGone();
    public void setGone(boolean disabled);
    public boolean getInvisible();
    public void setInvisible(boolean disabled);
    public void setBlur(boolean blur);
    public boolean getBlur();
    public void setFocus(boolean focus);
    public boolean getFocus();
}
