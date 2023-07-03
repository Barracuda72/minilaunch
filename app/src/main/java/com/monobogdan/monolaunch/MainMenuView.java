package com.monobogdan.monolaunch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainMenuView extends GridMenuView {
    public MainMenuView(Context ctx) {
        super(ctx);

        List<View> widgetList = new ArrayList<>();

        final String[] appIds = new String [] {
          // Phonebook
          "com.android.contacts",
          // Calls
          "com.android.dialer",
          // Browser
          "com.android.browser",

          // Camera - there's none
          "com.sprd.sprdcalculator",
          // Messages
          "com.android.mms",
          // Calendar
          "com.android.calendar",

          // Misc stuff
          "com.android.music",
          // Files
          "com.android.documentsui",
          // Settings
          "com.android.settings",

          // Extras
          "com.android.soundrecorder",
          "com.android.deskclock",
          "com.duoqin.manager",
        };

        PackageManager pm = getContext().getPackageManager();

        for (int i = 0; i < appIds.length; i++) {
            try {
                ApplicationInfo info = pm.getApplicationInfo(appIds[i], 0);
                String name = info.loadLabel(pm).toString();
                Drawable icon = info.loadIcon(pm).getConstantState().newDrawable();
                Intent intent = pm.getLaunchIntentForPackage(info.packageName);

                ImageButton button = new ImageButton(getContext());
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setFocusable(true);

                button.setImageBitmap(Extension.getScaledBitmapFromDrawable(icon, 100, 100));
                button.setTag(name);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getContext().startActivity(intent);
                    }
                });
                widgetList.add(button);
            } catch (PackageManager.NameNotFoundException e) {
                Log.i("POOK", "App info fetch FAILED: " + appIds[i]);
            }
        }

        setSelection(5);

        /*final String[] catNames = new String[] {
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                "Китти", "Масяня", "Симба"
        };

        for(int i = 0; i < catNames.length; i++)
        {
            Button button = new Button(getContext());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setFocusable(true);

            button.setBackground(getContext().getResources().getDrawable(android.R.drawable.ic_menu_add + i));

            button.setText(catNames[i]);
            widgetList.add(button);
        }*/

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
