package com.monobogdan.monolaunch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class TestMenuView extends GridMenuView {
    public TestMenuView (Context ctx) {
        super(ctx);
        Drawable image = getContext().getResources().getDrawable(android.R.drawable.btn_radio);
        setHeader(Extension.getScaledBitmapFromDrawable(image,36 ,36), "Hello world", 42);
        setFooter("Menu", "Back");

        final String[] catNames = new String[] {
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                "Китти", "Масяня", "Симба"
        };
        //setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, catNames));

        List<View> widgetList = new ArrayList<>();

        for(int i = 0; i < catNames.length; i++)
        {
            Button button = new Button(getContext());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setFocusable(true);

            button.setBackground(getContext().getResources().getDrawable(android.R.drawable.ic_menu_add + i));

            button.setText(catNames[i]);
            widgetList.add(button);
        }

        setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return widgetList.size();
            }

            @Override
            public Object getItem(int position) {
                return widgetList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return widgetList.get(position);
            }
        });
    }
}
