package com.ngandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ngandroid.demo.models.Input;
import com.ngandroid.lib.NgAndroid;


public class DemoActivity extends Activity {

    private Input input;

    private TextView really;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NgAndroid.setContentView(this, R.layout.activity_demo);

        input.setInput("Hello world");

        really = (TextView) findViewById(R.id.really);

        findViewById(R.id.really2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText(String.valueOf(input.getTest()));
            }
        });
    }

    private void clickReally(){
        really.setText(input.getInput());
    }
}
