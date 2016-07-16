package com.weareonfire.gocha.gocha;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class FrontPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button singleModeButton = (Button) findViewById(R.id.single_mode);
        singleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent toLoginActivityIntent = new Intent(FrontPageActivity.this, SingleModeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean("reverse", false);
                toLoginActivityIntent.putExtras(bundle);
                startActivity(toLoginActivityIntent);
            }
        });

        Button hardModeButton = (Button) findViewById(R.id.hard_mode);
        hardModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivityIntent = new Intent(FrontPageActivity.this, SingleModeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean("reverse", true);
                toLoginActivityIntent.putExtras(bundle);
                startActivity(toLoginActivityIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent toLoginActivityIntent = new Intent(FrontPageActivity.this, LoginActivity.class);
//                startActivity(toLoginActivityIntent);
                FloatingActionButton store = (FloatingActionButton)findViewById(R.id.store);
                FloatingActionButton setting = (FloatingActionButton)findViewById(R.id.setting);
                if (store.getVisibility() == View.GONE) {
                    setting.setVisibility(View.VISIBLE);
                    store.setVisibility(View.VISIBLE);
                    setting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FrontPageActivity.this, SettingActivity.class);
                            startActivity(intent);
                        }
                    });
                    store.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FrontPageActivity.this, StoreActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    setting.setVisibility(View.GONE);
                    store.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_front_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
