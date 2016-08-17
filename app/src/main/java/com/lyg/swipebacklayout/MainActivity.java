package com.lyg.swipebacklayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 使用注意事项：
 * 主Activity不能设置侧滑返回，不能设置背景透明
 * android:windowIsTranslucent该属性不能设置true
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump(View view) {
        SecondActivity.startActivity(this);
    }
}
