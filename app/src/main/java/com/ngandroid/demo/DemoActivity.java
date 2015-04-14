package com.ngandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ngandroid.demo.models.Input;
import com.ngandroid.lib.NgAndroid;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

@NgScope
public class DemoActivity extends Activity {
    @NgModel
    Input input, output;

    private TextView stringClickEvent, really2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NgAndroid.getInstance().setContentView(this, R.layout.activity_demo);

        input.setInput("Hello world");

        stringClickEvent = (TextView) findViewById(R.id.stringClickEvent);
        really2 = (TextView) findViewById(R.id.really2);

        input.setDisabled(true);
    }
// TODO private functions should throw erro
    void multiply(float num1, int num2){
        Toast.makeText(this, String.valueOf(num1 * num2), Toast.LENGTH_SHORT).show();
    }

    void multiply(int num1, int num2){
        Toast.makeText(this, String.valueOf(num1 * num2), Toast.LENGTH_SHORT).show();
    }


    void stringClickEvent(){
        stringClickEvent.setText(input.getInput());
    }


    void intClickEvent(int num){
        really2.setText(String.valueOf(num));
    }

    void onChange(){
        Toast.makeText(this, "Text Changed", Toast.LENGTH_SHORT).show();
    }
}
