package com.example.samsung01.exampleformultiplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by samsung01 on 2017/9/2.
 */

public class MultiActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);//打开指定的xml网页(布局) 注意以下的widget必须在xml中指定id

        Button commit = findViewById(R.id.commit); //获取按钮
        Button cancel = findViewById(R.id.cancel);


        commit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = findViewById(R.id.editText3);//获取文本内容
                EditText et1 = findViewById(R.id.editText1);
                EditText et2 = findViewById(R.id.editText2);
                String first = et1.getText().toString();
                String second = et2.getText().toString();
                String ans;

                try {
                    int ans1 = Integer.parseInt(first) * Integer.parseInt(second);
                    ans = String.valueOf(ans1);


                } catch (Exception e) {
                    ans = "NAN";//如果错误结果显示False
                }

                et.setText(ans); //设置文本内容
                et1.setText(first);
                et2.setText(second);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et1 = findViewById(R.id.editText1);
                et1.setText("0");
                EditText et2 = findViewById(R.id.editText2);
                et2.setText("0");
                EditText et = findViewById(R.id.editText3);//获取文本内容
                et.setText("0");
            }
        });
    }
}
