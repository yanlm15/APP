package com.example.liminyan.myapplication;

/**
 * Created by liminyan on 01/09/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.*;



public class AddActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);//打开指定的xml 网页 注意以下的widget必须在xml中指定id
        Button commit = (Button)findViewById(R.id.commit); //获取按钮
        Button cancle = (Button)findViewById(R.id.cancle);


        commit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                EditText et = (EditText)findViewById(R.id.editTextans);//获取文本内容
                EditText et1 = (EditText)findViewById(R.id.editText1);
                EditText et2 = (EditText)findViewById(R.id.editText2);
                String first =  et1.getText().toString();
                String second = et2.getText().toString();
                String ans ="";

                try {
                    int ans1 =  Integer.parseInt(first)+ Integer.parseInt(second); //排除非法输入
                    ans = String.valueOf(ans1);


                }catch (Exception e)
                {
                    ans = "False";//如果错误结果显示False
                }

                et.setText(ans); //设置文本内容
                et1.setText(first);
                et2.setText(second);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                EditText et1 = (EditText)findViewById(R.id.editText1);
                et1.setText("0");
                EditText et2 = (EditText)findViewById(R.id.editText2);
                et2.setText("0");
                EditText et = (EditText)findViewById(R.id.editTextans);//获取文本内容
                et.setText("0");
            }
        });








    }
}
