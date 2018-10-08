package com.example.a64929.mynote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * 长按主界面列表
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        p.width= dm.widthPixels;
        p.height= (int)(dm.heightPixels*0.4);
        getWindow().setAttributes(p);

    }

}
