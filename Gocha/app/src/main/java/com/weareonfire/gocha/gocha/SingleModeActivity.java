package com.weareonfire.gocha.gocha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private int rightCurrent = R.id.rightout;
    private int leftCurrent = R.id.leftout;
    private int rightNext = R.id.rightin;
    private int leftNext = R.id.leftin;
    private boolean gameEnd = false;
    private int points = 0;

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
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                ImageView randomImage = new ImageView(SingleModeActivity.this);
                randomImage.setImageResource(R.drawable.ic_record_voice_over_black_24dp);
                RelativeLayout rightCurrentView = (RelativeLayout)findViewById(rightCurrent);
                rightCurrentView.removeAllViews();
                RelativeLayout rightNextView = (RelativeLayout)findViewById(rightNext);
                rightNextView.addView(randomImage,layoutParams);
                int tmp = rightCurrent;
                rightCurrent = rightNext;
                rightNext = tmp;
            }
        });

        leftHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                ImageView randomImage = new ImageView(SingleModeActivity.this);
                randomImage.setImageResource(R.drawable.ic_record_voice_over_black_24dp);
                RelativeLayout leftCurrentView = (RelativeLayout)findViewById(leftCurrent);
                leftCurrentView.removeAllViews();
                RelativeLayout rightNextView = (RelativeLayout)findViewById(leftNext);
                rightNextView.addView(randomImage, layoutParams);
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
            final ImageView randomImage = new ImageView(SingleModeActivity.this);
            randomImage.setImageResource(images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);
            Animation animation = new TranslateAnimation(0, 0, -500, 1200);
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(4000);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentLayout.getId() == rightCurrent) {
                        if (!gameEnd){
                            points += 1;}
                        currentLayout.removeView(randomImage);
                    } else {
                        gameEnd = true;
                        RelativeLayout rightHalf = (RelativeLayout) findViewById(R.id.righthalf);
                        rightHalf.setOnClickListener(null);
                        LinearLayout gameOver = (LinearLayout) findViewById(R.id.gameover);
                        TextView gameOverText = (TextView)findViewById(R.id.gameoverpoints);
                        gameOverText.setText("Points: " + points);
                        Button restart = (Button) findViewById(R.id.restart);
                        Button quit = (Button) findViewById(R.id.quit);
                        restart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                        quit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SingleModeActivity.this, FrontPageActivity.class);
                                startActivity(intent);
                            }
                        });
                        gameOver.setVisibility(View.VISIBLE);
                        currentLayout.removeView(randomImage);
                        lHandler.removeMessages(0);
                        lHandler.removeMessages(1);
                        rHandler.removeMessages(2);
                        rHandler.removeMessages(3);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            randomImage.startAnimation(animation);
            rHandler.sendEmptyMessageDelayed(random.nextInt(2)+2, time_gap-100+random.nextInt(200));
            return true;
        }
    }

    private class leftHandlerCallBack implements Handler.Callback {
        public boolean handleMessage(Message m) {
            final ImageView randomImage = new ImageView(SingleModeActivity.this);
            randomImage.setImageResource(images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);
            Animation animation = new TranslateAnimation(0, 0, -500, 1200);
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(4000);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentLayout.getId() == leftCurrent) {
                        if (!gameEnd){
                        points += 1;}
                        currentLayout.removeView(randomImage);
                    } else {
                        gameEnd = true;
                        RelativeLayout leftHalf = (RelativeLayout) findViewById(R.id.lefthalf);
                        leftHalf.setOnClickListener(null);
                        LinearLayout gameOver = (LinearLayout) findViewById(R.id.gameover);
                        TextView gameOverText = (TextView)findViewById(R.id.gameoverpoints);
                        gameOverText.setText("Points: " + points);
                        Button restart = (Button) findViewById(R.id.restart);
                        Button quit = (Button) findViewById(R.id.quit);
                        restart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                        quit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SingleModeActivity.this, FrontPageActivity.class);
                                startActivity(intent);
                            }
                        });
                        gameOver.setVisibility(View.VISIBLE);
                        currentLayout.removeView(randomImage);
                        lHandler.removeMessages(0);
                        lHandler.removeMessages(1);
                        rHandler.removeMessages(2);
                        rHandler.removeMessages(3);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            randomImage.startAnimation(animation);
            lHandler.sendEmptyMessageDelayed(random.nextInt(2), time_gap-200+random.nextInt(400));
            return true;
        }
    }
}
