package com.bignewsmaker.makebignews.extra_class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.Toast;
import android.os.Environment;

import com.bignewsmaker.makebignews.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by liminyan on 06/09/2017.
 * 只是一个全局功能类
 * 通过setText 设置朗读的内容
 * setcur 设置朗读的界面
 * 调用start 进行朗读 朗读失败会反回"不支持当前语言"
 * 语言当前默认为中文
 */

public class Speaker {

    private String text = null;
    private AppCompatActivity cur;
    private TextToSpeech mTextToSpeech;

    private SpeechSynthesizer mTts ;
    //set
    private String voicer = "xiaofen";
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SharedPreferences mSharedPreferences;
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void showTip(String text, int code)
    {
        Toast.makeText(cur, "初始化失败,错误码!"+code, 1).show();

    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override

        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码!",code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    static Speaker now = null;

    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            mTts.setParameter(SpeechConstant.VOLUME,"50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCur(AppCompatActivity cur) {
        this.cur = cur;
    }

    public void start()
    {

        String _text = this.text;
        mTts = SpeechSynthesizer.createSynthesizer(cur.getApplicationContext(), null);


        if (mTts == null)
        {
            System.out.println("wo cao ni ma");
        }
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);

    }

    public void stop() {
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }

    }

    public static Speaker getInstance() {

        if (now == null)
            now = new Speaker();
        return now;
    }

}
