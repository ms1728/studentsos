package com.studentsos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.studentsos.R;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;

public class LoadActivity extends Activity {

    private static final int SHOW_TIME_MIN = 1000;
    ACache mCache;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.load);
        mCache = ACache.get(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mCache.getAsObject("user") != null) {
                    Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                    LoadActivity.this.startActivity(mainIntent);
                    LoadActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(LoadActivity.this, LoginActivity.class);
                    LoadActivity.this.startActivity(mainIntent);
                    LoadActivity.this.finish();
                }

            }
        }, SHOW_TIME_MIN); // 1500 for release*/

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

}
