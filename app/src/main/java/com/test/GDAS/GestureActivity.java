package com.test.GDAS;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class GestureActivity extends Activity implements GestureDetector.OnGestureListener {
    ViewFlipper flipper;
    GestureDetector detector;
    Animation[] animations = new Animation[4];
    final int FLIP_DISTANCE = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//
//        detector = new GestureDetector(this);
//
//        flipper = (ViewFlipper)findViewById(R.id.flipper);
//
//
//        //初始化Animation数组
//        animations[0] = AnimationUtils.loadAnimation(this,R.anim.left_in);
//        animations[1] = AnimationUtils.loadAnimation(this,R.anim.left_out);
//        animations[2] = AnimationUtils.loadAnimation(this,R.anim.right_in);
//        animations[3] = AnimationUtils.loadAnimation(this,R.anim.right_in);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}