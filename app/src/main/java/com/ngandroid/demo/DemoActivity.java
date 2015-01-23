package com.ngandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ngandroid.demo.models.Input;
import com.ngandroid.lib.NgAndroid;


public class DemoActivity extends Activity {

    public class Model {
        private Input input;
    }
    private Model model = new Model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NgAndroid.setContentView(this, R.layout.activity_demo, model);

        model.input.setInput("Hello world");

        findViewById(R.id.really).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText(model.input.getInput());
            }
        });

        findViewById(R.id.really2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText(String.valueOf(model.input.getTest()));
            }
        });
    }
}
