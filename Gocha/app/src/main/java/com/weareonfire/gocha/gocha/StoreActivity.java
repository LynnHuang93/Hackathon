package com.weareonfire.gocha.gocha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        final SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final int pearls = sharedPref.getInt("pearls", 0 ); //currently owned pearls
        final int octs = sharedPref.getInt("octs", 0); //currently owned octs
        final int coins = sharedPref.getInt("coins", 0); //real coin number retrieved from the sharedPref
        //final int coins = 10;
        final TextView balance = (TextView) findViewById(R.id.coin_num); //current balance
        balance.setText(Integer.toString(coins));
        final int pearl_num[] = {0};
        final int oct_num[] = {0};
        final int coin_num[] = {coins}; //coin number for the current buying session
        final TextView show_pearls = (TextView) findViewById(R.id.exemptNum);
        final TextView show_octs = (TextView) findViewById(R.id.octNum);
        final TextView remain_pearls = (TextView) findViewById(R.id.exemptRestNum);
        final TextView remain_octs = (TextView) findViewById(R.id.octRestNum);
        remain_pearls.setText(String.format("remain: %d", pearls));
        remain_octs.setText(String.format("remain: %d", octs));
        ImageView pearlMinus = (ImageView) findViewById(R.id.exemptMinus);
        pearlMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pearl_num[0]>0){
                    pearl_num[0]--;
                    coin_num[0]+=5;
                    show_pearls.setText(Integer.toString(pearl_num[0]));
                    balance.setText(Integer.toString(coin_num[0]));
                }
            }
        });
        ImageView pearlPlus = (ImageView) findViewById(R.id.exemptPlus);
        pearlPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coin_num[0]>=5){
                    pearl_num[0]++;
                    coin_num[0]-=5;
                    show_pearls.setText(Integer.toString(pearl_num[0]));
                    balance.setText(Integer.toString(coin_num[0]));
                }
            }
        });
        ImageView octMinus = (ImageView) findViewById(R.id.octMinus);
        octMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oct_num[0]>0){
                    oct_num[0]--;
                    coin_num[0]+=5;
                    show_octs.setText(Integer.toString(oct_num[0]));
                    balance.setText(Integer.toString(coin_num[0]));
                }
            }
        });
        ImageView octPlus = (ImageView) findViewById(R.id.octPlus);
        octPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coin_num[0]>=5){
                    oct_num[0]++;
                    coin_num[0]-=5;
                    show_octs.setText(Integer.toString(oct_num[0]));
                    balance.setText(Integer.toString(coin_num[0]));
                }
            }
        });

        Button buy_button = (Button) findViewById(R.id.button);
        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("coins",coin_num[0]);
                int cur_pearls = pearls + pearl_num[0];
                int cur_octs = octs + oct_num[0];
                editor.putInt("pearls",cur_pearls);
                editor.putInt("octs",cur_octs);
                remain_pearls.setText(String.format("remain: %d",cur_pearls));
                remain_octs.setText(String.format("remain: %d", cur_octs));
            }
        });
    }
}
