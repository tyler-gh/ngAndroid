package com.ngandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ngandroid.demo.models.Input;
import com.ngandroid.lib.NgAndroid;


public class DemoActivity extends Activity {

    private Input input;

    private TextView stringClickEvent, really2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NgAndroid.setContentView(this, R.layout.activity_demo);

        input.setInput("Hello world");

        stringClickEvent = (TextView) findViewById(R.id.stringClickEvent);
        really2 = (TextView) findViewById(R.id.really2);
    }

    private void multiply(int num1, int num2){
        Toast.makeText(this, String.valueOf(num1 * num2), Toast.LENGTH_SHORT).show();
    }


    private void clickReally(){
        stringClickEvent.setText(input.getInput());
    }


    private void clickWithParameter(int num){
        really2.setText(String.valueOf(num));
    }
}
