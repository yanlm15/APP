package com.bignewsmaker.makebignews.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.extra_class.Speaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static com.bignewsmaker.makebignews.activity.MainActivity.setStatusBarColor;

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

        LinearLayout linedeladd=(LinearLayout)findViewById(R.id.linedeladd);
        linedeladd.setOnClickListener(new LinearLayout.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("请选择您喜好的类别");
                final String[] category = {"推荐","科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
                //    设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉多选框的数据集合
                 * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
                 * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
                 * 第三个参数给每一个多选项绑定一个监听器
                 */
                final boolean[] isSeleted = new boolean[13];
                for (int i = 0; i < isSeleted.length; i++)
                    isSeleted[i] = const_data.getIstagSelected(i + 1);

                builder.setMultiChoiceItems(category, isSeleted, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isSeleted[which] = isChecked;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < isSeleted.length; i++)
                            const_data.setIstagSelected(i + 1, isSeleted[i]);
                        const_data.setSetChanged(true);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SetActivity.this, "设置未保存", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });

        LinearLayout lineclear=(LinearLayout)findViewById(R.id.lineclear);
        lineclear.setOnClickListener(new LinearLayout.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("您确定要清除吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        const_data.setDislike(new HashSet<String>() );
                        const_data.setHaveRead(new HashMap<String, News>());
                        const_data.setFiltered(new HashSet<String>());
                        const_data.setLike(new TreeMap<String,Integer>());
                        Toast.makeText(SetActivity.this, "已成功清除", Toast.LENGTH_SHORT).show();
                        const_data.setSetChanged(true);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });

        if (!const_data.getDay()) {
            toolbar.setBackgroundColor(Color.rgb(66, 66, 66));
            setStatusBarColor(SetActivity.this, Color.rgb(66, 66, 66));
        }
        //设置输入监控
        //设置更新函数

    }
}