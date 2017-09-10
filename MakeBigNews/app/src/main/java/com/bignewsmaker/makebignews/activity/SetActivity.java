package com.bignewsmaker.makebignews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.extra_class.Speaker;

/**
 * Created by liminyan on 06/09/2017.
 * 设置界面
 */

public class SetActivity extends AppCompatActivity {

    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private Speaker speaker = Speaker.getInstance();// 设置语音系统接口

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        //设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.fanhui);
        toolbar.setTitle("设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();


            }
        });
        setSupportActionBar(toolbar);

        Switch switchNight = (Switch) findViewById(R.id.switchnight);
        switchNight.setChecked(!const_data.getDay());
        switchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                const_data.setDay(!b);
                const_data.setSetChanged(true);

            }
        });

        Switch switchPicture = (Switch) findViewById(R.id.switchpicture);
        switchPicture.setChecked(!const_data.getShow_picture());
        switchPicture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                const_data.setShow_picture(!b);
                const_data.setSetChanged(true);

            }
        });


        AppCompatSeekBar sizeBar = (AppCompatSeekBar) findViewById(R.id.sizebar);
        final TextView textView = (TextView) findViewById(R.id.sizenumber);
        sizeBar.setProgress(Integer.parseInt(const_data.getCur_pageSize()));
        textView.setText(const_data.getCur_pageSize());
        sizeBar.setMax(50);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String p = String.valueOf(progress);
                textView.setText(p);
                const_data.setCur_pageSize(p);
                const_data.setSetChanged(true);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        //设置输入监控
        //设置更新函数

    }
}