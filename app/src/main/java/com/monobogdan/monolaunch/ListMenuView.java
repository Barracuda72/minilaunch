package com.monobogdan.monolaunch;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListMenuView extends BaseMenuView {
    private int current_position = -1;

    public ListMenuView(Context ctx) {
        super(ctx, new ListView(ctx));
        ListView listView = (ListView)getContentView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setSelector(android.R.color.holo_blue_light);
    }

    @Override
    protected void processNav(int keyCode, KeyEvent event) {
        ListView listView = (ListView)getContentView();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                processBack();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                processEnter();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                listView.onKeyDown(keyCode, event);
                break;
            default:
                throw new RuntimeException("Unknown keycode for processNav: " + keyCode);
        }
    }

    @Override
    protected void processSelection(int item) {
        //setSelection(item);
        throw new RuntimeException("Unimplemented!");
    }

    @Override
    protected void processEnter() {
        throw new RuntimeException("Unimplemented!");
    }

    public void setAdapter(BaseAdapter adapter) {
        ListView listView = (ListView)getContentView();
        listView.setAdapter(adapter);
    }
}
