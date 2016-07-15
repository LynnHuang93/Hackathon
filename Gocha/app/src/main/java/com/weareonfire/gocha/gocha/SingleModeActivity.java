package com.weareonfire.gocha.gocha;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleModeActivity extends AppCompatActivity {
    List<RelativeLayout> tracks = new ArrayList<>(4);
    List<Integer> images = new ArrayList<>(3);
    private Handler rHandler = new Handler(new rightHandlerCallBack());
    private Handler lHandler = new Handler(new leftHandlerCallBack());
    Random random = new Random();
    private int duration = 2000;
    private int time_gap = 600;
    private int rightCurrent = R.id.rightoutimage;
    private int leftCurrent = R.id.leftoutimage;
    private int rightNext = R.id.rightinimage;
    private int leftNext = R.id.leftinimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mode);

        tracks.add ((RelativeLayout) findViewById(R.id.leftout));
        tracks.add ((RelativeLayout) findViewById(R.id.leftin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightout));

        images.add(R.drawable.ic_grade_black_24dp);
        images.add(R.drawable.ic_invert_colors_black_24dp);
        images.add(R.drawable.ic_report_problem_black_24dp);

        rHandler.sendEmptyMessage(random.nextInt(4));
        lHandler.sendEmptyMessage(random.nextInt(4));

        RelativeLayout rightHalf = (RelativeLayout) findViewById(R.id.righthalf);
        RelativeLayout leftHalf = (RelativeLayout) findViewById(R.id.lefthalf);

        rightHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView randomImage = new ImageView(SingleModeActivity.this);
                randomImage.setImageResource(R.drawable.ic_record_voice_over_black_24dp);
                RelativeLayout rightCurrentView = (RelativeLayout)findViewById(rightCurrent);
                rightCurrentView.removeAllViews();
                RelativeLayout rightNextView = (RelativeLayout)findViewById(rightNext);
                rightNextView.addView(randomImage);
                int tmp = rightCurrent;
                rightCurrent = rightNext;
                rightNext = tmp;
            }
        });

        leftHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView randomImage = new ImageView(SingleModeActivity.this);
                randomImage.setImageResource(R.drawable.ic_record_voice_over_black_24dp);
                RelativeLayout leftCurrentView = (RelativeLayout)findViewById(leftCurrent);
                leftCurrentView.removeAllViews();
                RelativeLayout rightNextView = (RelativeLayout)findViewById(leftNext);
                rightNextView.addView(randomImage);
                int tmp = leftCurrent;
                leftCurrent = leftNext;
                leftNext = tmp;
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        lHandler.removeMessages(0);
        lHandler.removeMessages(1);
        rHandler.removeMessages(2);
        rHandler.removeMessages(3);
        return super.onTouchEvent(event);
    }

    private class rightHandlerCallBack implements Handler.Callback {
        public boolean handleMessage(Message m) {
            ImageView randomImage = new ImageView(SingleModeActivity.this);
            randomImage.setImageResource(images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);
            randomImage.animate().y(1600f).setDuration(duration).start();
            rHandler.sendEmptyMessageDelayed(random.nextInt(2)+2, time_gap-100+random.nextInt(200));
            return true;
        }
    }

    private class leftHandlerCallBack implements Handler.Callback {
        public boolean handleMessage(Message m) {
            ImageView randomImage = new ImageView(SingleModeActivity.this);
            randomImage.setImageResource(images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);
            randomImage.animate().y(1600f).setDuration(duration).start();
            lHandler.sendEmptyMessageDelayed(random.nextInt(2), time_gap-200+random.nextInt(400));
            return true;
        }
    }
}
