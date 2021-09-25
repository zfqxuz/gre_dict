package com.test.GDAS;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.RequiresApi;
import android.util.Log;


import java.util.Locale;


public class SpeechUtils {

    private TextToSpeech textToSpeech; // TTS对象

    public SpeechUtils(Context context) {
        textToSpeech = new TextToSpeech(context, (int i)-> {
            Log.d("voice",String.valueOf(i));
            if (i == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.UK);
                textToSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                textToSpeech.setSpeechRate(1.0f);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null,"UniqueID");
        }
    }

    public void ttsStop(){
        if (null != textToSpeech){
            textToSpeech.stop();
        }
    }

    public void ttsDestory(){
        if (null != textToSpeech){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}

