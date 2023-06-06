package com.monobogdan.monolaunch;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class GridMenuView extends BaseMenuView {
    View prevSelected = null;
    int selectedItem = 0;

    public GridMenuView(Context ctx) {
        super(ctx, new GridView(ctx));
        GridView gridView = (GridView)getContentView();

        gridView.setNumColumns(3);
        gridView.setColumnWidth(getWidth() / 3 - 10);
        gridView.setClickable(true);
        gridView.setSelector(R.drawable.none);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Toast.makeText(getContext(), "Scrolling is Disabled", Toast.LENGTH_SHORT).show();
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }

    public void setAdapter(BaseAdapter adapter) {
        GridView gridView = (GridView)getContentView();
        gridView.setAdapter(adapter);

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view != null) {
                    view.animate().scaleX(1.1f).scaleY(1.2f).setDuration(100);
                }
                if (prevSelected != view) {
                    if (prevSelected != null)
                        prevSelected.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
                    prevSelected = view;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSelection(0);
            }
        });
    }

    protected void setSelection(int i) {
        selectedItem = i;
        GridView gridView = (GridView)getContentView();
        gridView.setSelection(selectedItem);
    }

    @Override
    protected void processNav(int keyCode, KeyEvent event) {
        GridView gridView = (GridView)getContentView();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                selectedItem = (selectedItem+11) % 12;
                setSelection(selectedItem);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                selectedItem = (selectedItem+1) % 12;
                setSelection(selectedItem);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                selectedItem = (selectedItem+9) % 12;
                setSelection(selectedItem);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                selectedItem = (selectedItem+3) % 12;
                setSelection(selectedItem);
                break;
            default:
                throw new RuntimeException("Unknown keycode for processNav: " + keyCode);
        }

    }
}
