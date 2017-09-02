package com.example.samsung01.exampleformultiplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button Multi = (Button)findViewById(R.id.button);//访问button

        Multi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View e)
            {
                Intent intent=new Intent(MainActivity.this,MultiActivity.class);// 新建一个界面
                startActivity(intent);//跳转界面
            }
        });

    }


}
