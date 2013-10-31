package com.leaf8.zishao;

import android.app.Activity;
import android.os.Bundle;

import com.leaf8.zishao.widget.ZsConditionWidget;
import com.leaf8.zishao.widget.ZsConditionWidget.SaveBundle;

public class ConditionWidgetActivity extends Activity {

    private ZsConditionWidget mSelectWidget;
    private ZsConditionWidget.SaveBundle mSelectItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaf_condition_test);
        mSelectWidget = (ZsConditionWidget) findViewById(R.id.leaf_condition_widget);
        mSelectWidget.setItemSelectListener(new SelectWidgetListener());
    }
    
    protected void initView() {
        mSelectWidget.reset();
        mSelectWidget.initDefault();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mSelectWidget) {
            mSelectWidget.destory();
            mSelectWidget = null;
        }
    }
    
    /**
     * 筛选组件的事件监听
     * 
     * @author 紫韶
     * @date Jul 12, 2013
     **/
    private class SelectWidgetListener implements ZsConditionWidget.DataSourceListener {
        @Override
        public void onSelectListener(int type, Object info, SaveBundle data) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean isRejectClickEvent(int type, Object info) {
            // TODO Auto-generated method stub
            return false;
        }
    }
}
