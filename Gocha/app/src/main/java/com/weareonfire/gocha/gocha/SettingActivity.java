package com.weareonfire.gocha.gocha;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;


public class SettingActivity extends AppCompatActivity {
    Context context = this;
    Boolean soundOn;
    Boolean musicOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final SharedPreferences sharedPref = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        soundOn = sharedPref.getBoolean("soundOn", true);
        musicOn = sharedPref.getBoolean("musicOn", true);
        final Switch switchSound = (Switch) findViewById(R.id.soundOn);
        final Switch switchMusic = (Switch) findViewById(R.id.musicOn);
        final SharedPreferences.Editor editor = sharedPref.edit();
        switchSound.setChecked(soundOn);
        switchMusic.setChecked(musicOn);
        switchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundOn = !soundOn;
                editor.putBoolean("soundOn", soundOn);
                switchSound.setChecked(soundOn);
                editor.commit();
            }
        });

        switchMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicOn = !musicOn;
                editor.putBoolean("musicOn", musicOn);
                switchSound.setChecked(musicOn);
                editor.commit();
            }
        });


    }
}
