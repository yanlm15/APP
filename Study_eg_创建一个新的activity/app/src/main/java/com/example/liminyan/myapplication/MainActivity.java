package com.example.liminyan.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button; // 添加 button 类


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//显示activity_main界面

        Button add = (Button)findViewById(R.id.add);//访问button

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,AddActivity.class);// 新建一个界面
                startActivity(intent);//跳转界面
            }
        });

    }



}