package com.monobogdan.monolaunch;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.*;

public class ListMenuView extends BaseMenuView {
    private int selectedItem = 0;

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
                selectedItem = (selectedItem+listView.getCount()-1) % listView.getCount();
                setSelection(selectedItem);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                selectedItem = (selectedItem+1) % listView.getCount();
                setSelection(selectedItem);
                break;
            default:
                throw new RuntimeException("Unknown keycode for processNav: " + keyCode);
        }
    }

    protected void setSelection(int i) {
        selectedItem = i;
        ListView listView = (ListView)getContentView();
        listView.setSelection(selectedItem);
    }

    @Override
    protected void processSelection(int item) {
        setSelection(item-1);
        processEnter();
    }

    @Override
    protected void processEnter() {
        ListView listView = (ListView)getContentView();
        listView.getChildAt(selectedItem - listView.getFirstVisiblePosition()).performClick();
    }

    public void setAdapter(BaseAdapter adapter) {
        ListView listView = (ListView)getContentView();
        listView.setAdapter(adapter);
    }
}
