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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SingleModeActivity extends AppCompatActivity {
    List<RelativeLayout> tracks = new ArrayList<>(4);
    List<Integer> generic_images = new ArrayList<>(3);
    List<Integer> exempt_images = new ArrayList<>(2);
    Set<Integer> exempt_images_set = new HashSet<>();
    private Handler rHandler = new Handler(new rightHandlerCallBack());
    private Handler lHandler = new Handler(new leftHandlerCallBack());
    Random random = new Random();
    private int duration = 4000;
    private int time_gap = 2000;
    private int rightCurrent = R.id.rightout;
    private int leftCurrent = R.id.leftout;
    private int rightNext = R.id.rightin;
    private int leftNext = R.id.leftin;
    private boolean gameEnd = false;
    private int points = 0;

    private boolean exempt_on = false;
    private int exempt_val = 0;
    private boolean reverse = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mode);

        Intent intentstream = getIntent();
        reverse =intentstream.getBooleanExtra("reverse",false);

        tracks.add ((RelativeLayout) findViewById(R.id.leftout));
        tracks.add ((RelativeLayout) findViewById(R.id.leftin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightout));

        generic_images.add(R.drawable.ic_grade_black_24dp);
        generic_images.add(R.drawable.ic_invert_colors_black_24dp);
        generic_images.add(R.drawable.ic_report_problem_black_24dp);
        exempt_images.add(R.drawable.ic_pregnant_woman_black_24dp);
        exempt_images.add(R.drawable.ic_rowing_black_24dp);

        exempt_images_set.addAll(exempt_images);


        rHandler.sendEmptyMessage(random.nextInt(2)+2);
        lHandler.sendEmptyMessage(random.nextInt(2));

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
            float randf = random.nextFloat();
            if (randf<=0.8){
                randomImage.setImageResource(generic_images.get(random.nextInt(3)));

            }
            else{
                randomImage.setImageResource(exempt_images.get(random.nextInt(2)));
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);

            Animation animation = new TranslateAnimation(0, 0, -500, 900);

            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(4000);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentLayout.getId() == rightCurrent ^ reverse) {
                        if (!gameEnd){
                            points += 1;
                            if (exempt_images_set.contains(randomImage.getId())){ //current image in exempt set
                                if (!exempt_on){
                                    exempt_on = true;
                                }
                                exempt_val += 3;

                            }

                        }

                            TextView score = (TextView) findViewById(R.id.ScoreNum);
                            score.setText(Integer.toString(points));


                        currentLayout.removeView(randomImage);
                    }
                    else if (exempt_on){ //exempt is on
                        currentLayout.removeView(randomImage);
                        exempt_val --;
                        if (exempt_val==0){
                            exempt_on = false;
                        }

                    }

                    else {
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
            float randf = random.nextFloat();
            if (randf<=0.8){ //control the prob for different imgs
                randomImage.setImageResource(generic_images.get(random.nextInt(3)));
            }
            else{
                randomImage.setImageResource(exempt_images.get(random.nextInt(2)));
            }
            //randomImage.setImageResource(generic_images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);

            Animation animation = new TranslateAnimation(0, 0, -500, 900);


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
                            points += 1;
                            if (exempt_images_set.contains(randomImage.getId())){ //current image in exempt set
                                if (!exempt_on){
                                    exempt_on = true;
                                }
                                exempt_val += 3;

                            }
                            TextView score = (TextView) findViewById(R.id.ScoreNum);
                            score.setText(Integer.toString(points));

                        }
                        currentLayout.removeView(randomImage);
                    }
                    else if (exempt_on){ //exempt is on
                        currentLayout.removeView(randomImage);
                        exempt_val --;
                        if (exempt_val==0){
                            exempt_on = false;
                        }

                    }

                    else {
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
