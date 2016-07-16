package com.weareonfire.gocha.gocha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class StoreActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        final SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final int coins = sharedPref.getInt("coins", 0);
        ImageView pearlMinus = (ImageView) findViewById(R.id.exemptMinus);
        pearlMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins>=5){
                    editor.putInt("coins",coins-5);
                }
            }
        });
        ImageView pearlPlus = (ImageView) findViewById(R.id.exemptPlus);
        pearlMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins>=5){
                    editor.putInt("coins",coins-5);
                }
            }
        });
    }
}
