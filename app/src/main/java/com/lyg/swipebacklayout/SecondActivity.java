package com.lyg.swipebacklayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lyg.SwipeBackActivity;

/**
 * android:windowIsTranslucent该属性设置true
 */
public class SecondActivity extends SwipeBackActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SecondActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void back(View v) {
        scrollToFinishActivity();
    }
}
