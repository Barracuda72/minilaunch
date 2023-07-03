package com.monobogdan.monolaunch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class NewAppListView extends ListMenuView {
    public NewAppListView(Context ctx) {
        super(ctx);

        List<View> widgetList = new ArrayList<>();

        Intent filter = new Intent();
        filter.setAction(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getContext().getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(0);

        for (PackageInfo info: apps) {
            Button button = new Button(getContext());
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setFocusable(true);

            String name = info.applicationInfo.loadLabel(pm).toString();
            Drawable icon = info.applicationInfo.loadIcon(pm);
            Intent intent = pm.getLaunchIntentForPackage(info.packageName);

            if(intent == null) {
                Log.i("Test", "fetchAppList: skipping " + String.format("%s", name));
                continue;
            }

            Log.i("Test", "fetchAppList: " + String.format("%s %s", name, intent));

            button.setText(name);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    getContext().startActivity(intent);
                }
            });
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
