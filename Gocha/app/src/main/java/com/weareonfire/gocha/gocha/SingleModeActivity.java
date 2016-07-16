package com.weareonfire.gocha.gocha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
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
    List<Integer> generic_images = new ArrayList<>(2);
    int exempt_image_id; //id for the exempt pic
    int ink_image_id; //id for the ink pic
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
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private boolean exempt_on = false;
    private int exempt_val = 0;
    //private boolean reverse = false;

    //-- sound effect
    // private SoundPool soundPool;
    private int soundID_Coin;
    private int soundID_Bomb;
    private int soundID_Bubbles;
    private int soundID_Absorption;

    boolean loaded = false;

    Context context = this;
    Boolean soundOn;
    Boolean musicOn;

    private MediaPlayer mPlayer;

    // initilzie soundpool and set to the max volume
    AudioAttributes attributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();

    private SoundPool soundPool = new SoundPool.Builder().setAudioAttributes(attributes).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mode);

        sharedPref = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        soundOn = sharedPref.getBoolean("soundOn", true);
        musicOn = sharedPref.getBoolean("musicOn", true);
        editor = sharedPref.edit();
        mPlayer = MediaPlayer.create(this, R.raw.music1);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }
        if (musicOn) mPlayer.start();

        tracks.add ((RelativeLayout) findViewById(R.id.leftout));
        tracks.add ((RelativeLayout) findViewById(R.id.leftin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightin));
        tracks.add ((RelativeLayout) findViewById(R.id.rightout));

        generic_images.add(R.drawable.goodlittlefish1);
        generic_images.add(R.drawable.goodlittlefish2);
        exempt_image_id = R.drawable.pearlexempt; //assign preg woman to exempt_img_id
        ink_image_id = R.drawable.octopus; //assign rowing to ink_img_id
        //exempt_images.add(R.drawable.ic_rowing_black_24dp);
        //exempt_images_set.addAll(exempt_images);


        rHandler.sendEmptyMessage(random.nextInt(2)+2);
        lHandler.sendEmptyMessage(random.nextInt(2));

        RelativeLayout rightHalf = (RelativeLayout) findViewById(R.id.righthalf);
        RelativeLayout leftHalf = (RelativeLayout) findViewById(R.id.lefthalf);

//        // Set the hardware buttons to control the music
//        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        // Load the sound
//        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                loaded = true;
//            }
//        });
//        soundID_Coin = soundPool.load(this, R.raw.coin, 1);
//        soundID_Absorption = soundPool.load(this, R.raw.absorption, 1);
//        soundID_Bomb = soundPool.load(this, R.raw.bomb, 1);
//        soundID_Bubbles = soundPool.load(this, R.raw.bubbles, 1);
//        // Getting the user sound settings
//        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        float actualVolume = (float) audioManager
//                .getStreamVolume(AudioManager.STREAM_MUSIC);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

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
                RelativeLayout leftNextView = (RelativeLayout)findViewById(leftNext);
                leftNextView.addView(randomImage, layoutParams);
                int tmp = leftCurrent;
                leftCurrent = leftNext;
                leftNext = tmp;
            }
        });
    }

    private class rightHandlerCallBack implements Handler.Callback {
        public boolean handleMessage(Message m) {
            final ImageView randomImage = new ImageView(SingleModeActivity.this);
            float randf = random.nextFloat();
            final int currentImgId;
            if (randf<=0.8){
                currentImgId = generic_images.get(random.nextInt(2));
                randomImage.setImageResource(currentImgId);

            }
            else if (randf>0.8 && randf<=0.9){
                currentImgId = exempt_image_id;
                randomImage.setImageResource(currentImgId);
            }
            else{
                currentImgId = ink_image_id;
                randomImage.setImageResource(currentImgId);
            }

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);

            Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,Animation.RELATIVE_TO_PARENT, 0,Animation.RELATIVE_TO_PARENT, - 0.5f, Animation.RELATIVE_TO_PARENT, 0.45f);

            //Animation animation = new TranslateAnimation(0, 0, -500, 900);

            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(6000);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentLayout.getId() == rightCurrent) {
                        if (!gameEnd){
//                            Toast.makeText(getApplicationContext(), String.valueOf(currentImgId),
//                                    Toast.LENGTH_SHORT).show();
                            points += 1;
                            if (currentImgId==exempt_image_id){ //current image is the exempt pic
//                                Toast.makeText(getApplicationContext(), "found an exempt image",
//                                        Toast.LENGTH_SHORT).show();
                                if (!exempt_on){
                                    exempt_on = true;
                                }
                                exempt_val += 1;
                                TextView exempt = (TextView) findViewById(R.id.ExemptNum);
                                exempt.setText(Integer.toString(exempt_val));


                            }
                            else if (currentImgId==ink_image_id){ //current image is the ink trigger
                                final ImageView inkImage = (ImageView) findViewById(R.id.ink);
                                inkImage.setVisibility(View.VISIBLE);
                                Animation fadeOut = new AlphaAnimation(1, 0);
                                fadeOut.setInterpolator(new AccelerateInterpolator());
                                fadeOut.setDuration(2000);
                                fadeOut.setAnimationListener(new Animation.AnimationListener()
                                {
                                    public void onAnimationEnd(Animation animation)
                                    {
                                        inkImage.setVisibility(View.GONE);
                                    }
                                    public void onAnimationRepeat(Animation animation) {}
                                    public void onAnimationStart(Animation animation) {}
                                });
                                inkImage.startAnimation(fadeOut);

                            }

                        }

                            TextView score = (TextView) findViewById(R.id.ScoreNum);
                            score.setText(Integer.toString(points));


                        currentLayout.removeView(randomImage);
                    }
                    else if (exempt_on){ //exempt is on
                        currentLayout.removeView(randomImage);
                        exempt_val --;
                        TextView exempt = (TextView) findViewById(R.id.ExemptNum);
                        exempt.setText(Integer.toString(exempt_val));
                        if (exempt_val==0){
                            exempt_on = false;
                        }
                    }
                    else {
                        currentLayout.removeView(randomImage);
                        endGame();
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
            final int currentImgId;
            if (randf<=0.8){ //control the prob for different imgs
                currentImgId = generic_images.get(random.nextInt(2));
                randomImage.setImageResource(currentImgId);
            }
            else if (randf>0.8 && randf<=0.9){
                currentImgId = exempt_image_id;
                randomImage.setImageResource(currentImgId);
            }
            else{
                currentImgId = ink_image_id;
                randomImage.setImageResource(currentImgId);
            }
            //randomImage.setImageResource(generic_images.get(random.nextInt(3)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
            layoutParams.setMargins(0, - randomImage.getHeight(), 0, randomImage.getHeight());
            final RelativeLayout currentLayout = tracks.get(m.what);
            currentLayout.addView(randomImage,layoutParams);

//            randomImage.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            final int imageheight = randomImage.getMeasuredHeight();
            Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,Animation.RELATIVE_TO_PARENT, 0,Animation.RELATIVE_TO_PARENT, - 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);



            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(6000);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    if (loaded) {
//                        soundPool.play(soundID_Bomb, 1, 1, 1, 0, 1f);
//                        Log.e("Test", "Played sound");
//                    }
                    if (currentLayout.getId() == leftCurrent) {
                        if (!gameEnd){
//                            Toast.makeText(getApplicationContext(), String.valueOf(currentImgId),
//                                    Toast.LENGTH_SHORT).show();
                            points += 1;
                            if (currentImgId==exempt_image_id){ //current image in exempt set
//                                Toast.makeText(getApplicationContext(), "found an exempt Img",
//                                        Toast.LENGTH_SHORT).show();
                                if (!exempt_on){
                                    exempt_on = true;
                                }
                                exempt_val += 1;
                                TextView exempt = (TextView) findViewById(R.id.ExemptNum);
                                exempt.setText(Integer.toString(exempt_val));

                            }
                            else if (currentImgId==ink_image_id){ //current image is the ink trigger
                                final ImageView inkImage = (ImageView) findViewById(R.id.ink);
                                inkImage.setVisibility(View.VISIBLE);
                                Animation fadeOut = new AlphaAnimation(1, 0);
                                fadeOut.setInterpolator(new AccelerateInterpolator());
                                fadeOut.setDuration(2000);
                                fadeOut.setAnimationListener(new Animation.AnimationListener()
                                {
                                    public void onAnimationEnd(Animation animation)
                                    {
                                        inkImage.setVisibility(View.GONE);
                                    }
                                    public void onAnimationRepeat(Animation animation) {}
                                    public void onAnimationStart(Animation animation) {}
                                });
                                inkImage.startAnimation(fadeOut);

                            }

                            TextView score = (TextView) findViewById(R.id.ScoreNum);
                            score.setText(Integer.toString(points));

                        }
                        currentLayout.removeView(randomImage);
                    }
                    else if (exempt_on){ //exempt is on
                        currentLayout.removeView(randomImage);
                        exempt_val --;
                        TextView exempt = (TextView) findViewById(R.id.ExemptNum);
                        exempt.setText(Integer.toString(exempt_val));
                        if (exempt_val==0){
                            exempt_on = false;
                        }
                    }
                    else {
                        currentLayout.removeView(randomImage);
                        endGame();
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

    @Override
    protected void onStop(){
        super.onStop();
        if (musicOn) mPlayer.stop();
//        mServ.stopMusic();
//        doUnbindService();
    }

    private void endGame() {
        gameEnd = true;
        RelativeLayout rightHalf = (RelativeLayout) findViewById(R.id.righthalf);
        rightHalf.setOnClickListener(null);
        RelativeLayout leftHalf = (RelativeLayout) findViewById(R.id.lefthalf);
        leftHalf.setOnClickListener(null);
        LinearLayout gameOver = (LinearLayout) findViewById(R.id.gameover);
        TextView gameOverText = (TextView)findViewById(R.id.gameoverpoints);
        gameOverText.setText("Points: " + points + " Coins Gain: " + (points / 10) );
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

        int coins = sharedPref.getInt("coins", 0 );
        editor.putInt("coins",coins + points / 10);
        coins = sharedPref.getInt("coins",0);
        //Toast.makeText(getApplicationContext(), String.valueOf(coins), Toast.LENGTH_SHORT).show();
        lHandler.removeMessages(0);
        lHandler.removeMessages(1);
        rHandler.removeMessages(2);
        rHandler.removeMessages(3);
    }
}
