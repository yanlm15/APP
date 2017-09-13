package com.bignewsmaker.makebignews.extra_class;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

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
    private String voicer = "xiaofeng";
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
//        Toast.makeText(cur, "初始化失败,错误码!"+code, 1).show();

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
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
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
//        mTts = SpeechSynthesizer.createSynthesizer(cur, null);
        mTts = SpeechSynthesizer.createSynthesizer(cur.getApplicationContext(), null);


        if (mTts == null)
        {
            System.out.println("wo cao ni ma");
        }
//        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);


//        mTextToSpeech=new TextToSpeech(cur, new TextToSpeech.OnInitListener() {
//
//            @Override
//            public void onInit(int status) {
//                if (status==TextToSpeech.SUCCESS) {
//                    //设置朗读语言
//                    int supported=mTextToSpeech.setLanguage(Locale.CHINESE);
//
//                    if ((supported!=TextToSpeech.LANG_AVAILABLE)&&(supported!= TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
//                        Toast.makeText(cur, "不支持当前语言！", 1).show();
//                    }
//
//                    mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//                }
//
//            }
//
//        });
    }

    public void stop() {
//        if (mTextToSpeech != null) {
//            mTextToSpeech.stop();
//            mTextToSpeech.shutdown();
//        }
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
