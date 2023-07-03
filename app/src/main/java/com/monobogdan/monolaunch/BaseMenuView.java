package com.monobogdan.monolaunch;

import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

class BottomNavBar extends View {
    private Paint defaultPaint;
    private Paint fontPaint;

    private String leftText;
    private String rightText;

    Bitmap checkmark;

    public BottomNavBar(Context ctx) {
        super(ctx);

        defaultPaint = new Paint();
        defaultPaint.setColor(Color.WHITE);

        fontPaint = new Paint();
        fontPaint.setColor(Color.WHITE);
        fontPaint.setAntiAlias(true);
        fontPaint.setTextSize(24);

        Drawable checkmarkDrawable = getContext().getResources().getDrawable(android.R.drawable.checkbox_on_background);
        checkmark = Extension.getScaledBitmapFromDrawable(checkmarkDrawable, 32, 32);
    }

    public void setText(String left, String right) {
        leftText = left;
        rightText = right;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float metrics = fontPaint.getFontMetrics().bottom;
        float bottomLine = getHeight() - metrics - 3;
        float rightLine = getWidth() - fontPaint.measureText(rightText) - 5.0f;

        canvas.drawText(leftText, 5.0f, bottomLine, fontPaint);
        canvas.drawText(rightText, rightLine, bottomLine, fontPaint);

        float centerLine = getWidth() / 2 - (checkmark.getWidth() / 2);

        canvas.drawLine(0, 0, getWidth(), 0, defaultPaint);

        canvas.drawBitmap(checkmark, centerLine, getHeight() - checkmark.getHeight() - 3, defaultPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = checkmark.getHeight() + 5;
        height = Math.max(height, (int)(fontPaint.getFontMetrics().bottom+5));
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }
}

class TopNavBar extends View {
    private Paint defaultPaint;
    private Paint fontPaint;

    private Bitmap headerImage;
    private String headerText;
    private String headerNumber;

    public TopNavBar(Context ctx) {
        super(ctx);

        defaultPaint = new Paint();
        defaultPaint.setColor(Color.WHITE);

        fontPaint = new Paint();
        fontPaint.setColor(Color.WHITE);
        fontPaint.setAntiAlias(true);
        fontPaint.setTextSize(24);
    }

    public void setText(Bitmap image, String text, int number) {
        headerImage = image;
        headerText = text;
        headerNumber = number + "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float metrics = fontPaint.getFontMetrics().bottom;
        float textLen = fontPaint.measureText(headerNumber);

        float imageWidth = 0.0f;
        float imageHeight = 0.0f;

        if (headerImage != null) {
            canvas.drawBitmap(headerImage, 5.0f, 5.0f, defaultPaint);
            imageWidth = 5.0f + headerImage.getWidth();
            imageHeight = 5.0f + headerImage.getHeight();
        }

        canvas.drawText(headerText, 5.0f + imageWidth, 5.0f + imageHeight / 2, fontPaint);
        canvas.drawText(headerNumber, getWidth() - textLen - 5.0f, 5.0f + imageHeight / 2, fontPaint);

        canvas.drawLine(0, getHeight()-1, getWidth(), getHeight()-1, defaultPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (headerImage != null) {
            height = Math.max(height, headerImage.getHeight() + 10);
        }
        height = Math.max(height, (int)(fontPaint.getFontMetrics().bottom+10));
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }
}

public abstract class BaseMenuView extends LinearLayout {
    private View parent;

    final String TAG = "BaseMenuView";

    private TopNavBar topBar;
    private BottomNavBar bottomBar;
    private View contentView;

    public BaseMenuView(Context ctx, View content) {
        super(ctx);

        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOrientation(LinearLayout.VERTICAL);

        topBar = new TopNavBar(ctx);
        contentView = content;
        bottomBar = new BottomNavBar(ctx);

        LinearLayout.LayoutParams paramsTop = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        paramsTop.gravity = Gravity.TOP;
        LinearLayout.LayoutParams paramsMiddle = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 999.0f);
        LinearLayout.LayoutParams paramsBottom = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        paramsBottom.gravity = Gravity.BOTTOM;

        addView(topBar, paramsTop);
        addView(contentView, paramsMiddle);
        addView(bottomBar, paramsBottom);

        Resources res = getContext().getResources();
        Drawable image = res.getDrawable(android.R.drawable.btn_star);
        setHeader(Extension.getScaledBitmapFromDrawable(image,32 ,32), "Hello world", 42);

        setFooter(res.getString(R.string.options), res.getString(R.string.back));
    }

    protected View getContentView() {
        return contentView;
    }

    public void setParent(View parentView) {
        parent = parentView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + keyCode);

        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_UP ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                keyCode == KeyEvent.KEYCODE_DPAD_DOWN
        )
        {
            processNav(keyCode, event);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp: " + keyCode);

        if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            Log.i(TAG, "Element select!");
            processEnter();
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.i(TAG, "BACK PRESSED!!!!!");
            processBack();
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_MENU)
        {
            //contentView.processMenu();
            return true;
        }

        if(keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9 || keyCode == KeyEvent.KEYCODE_STAR || keyCode == KeyEvent.KEYCODE_POUND)
        {
            Log.i(TAG, "Key pressed: " + keyCode);
            // *, 0 and # doesn't correspond to 10, 11 and 12 items respectively; fix that
            int selected_item = -1;

            switch (keyCode) {
                case KeyEvent.KEYCODE_STAR:
                    selected_item = 10;
                    break;
                case KeyEvent.KEYCODE_0:
                    selected_item = 11;
                    break;
                case KeyEvent.KEYCODE_POUND:
                    selected_item = 12;
                    break;
                default:
                    selected_item = keyCode - KeyEvent.KEYCODE_0;
            };
            processSelection(selected_item);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    protected void setHeader(Bitmap image, String text, int number) {
        topBar.setText(image, text, number);
    }

    protected void setFooter(String left, String right) {
        bottomBar.setText(left, right);
    }

    protected void processBack() {
        Launcher.switchTo(parent);
    }

    protected abstract void processEnter();

    protected void refresh() {
        topBar.invalidate();
        bottomBar.invalidate();
    }

    protected abstract void processSelection(int item);

    protected abstract void processNav(int keyCode, KeyEvent event);
}