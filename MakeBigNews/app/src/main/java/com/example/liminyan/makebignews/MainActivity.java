package com.example.liminyan.makebignews;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech mTextToSpeech = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS) {
                    //设置朗读语言
                    int supported=mTextToSpeech.setLanguage(Locale.CHINA);

                    if ((supported!=TextToSpeech.LANG_AVAILABLE)&&(supported!= TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        Toast.makeText(MainActivity.this, "不支持当前语言！", 1).show();
                    }

                    mTextToSpeech.speak("世界 你好", TextToSpeech.QUEUE_FLUSH, null);
                }

            }


        });
    }
}
