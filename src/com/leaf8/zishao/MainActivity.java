package com.leaf8.zishao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 首页
 * 
 * @author 紫韶 
 * @date Oct 27, 2013
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.leaf_main);
    }
    
    public void goTo(View view) {
        switch (view.getId()) {
        case R.id.condition_widget:
            Intent intent = new Intent(this, ConditionWidgetActivity.class);
            startActivity(intent);
            break;
        }
    }
}
