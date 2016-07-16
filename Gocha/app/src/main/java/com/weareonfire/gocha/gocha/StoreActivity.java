package com.weareonfire.gocha.gocha;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StoreActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        final SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int coins = sharedPref.getInt("coins", 0);
    }
}
