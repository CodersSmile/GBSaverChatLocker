package com.gbsaver.chatlocker.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.PinLockActivity;
import com.gbsaver.chatlocker.utils.Util;

import java.lang.reflect.Method;
import java.util.Objects;

public final class PinButtonAdapter extends BaseAdapter implements Util {
    private final Context context;
    private final String[] list = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "-2", "0", "-1"};

    /* renamed from: getView$lambda-0 */
    public static final void m166getView$lambda0(View view) {
    }

    public PinButtonAdapter(Context context) {
        this.context = context;
    }

    @Override // com.gbsaver.chatlocker.utils.Util
    public float dp2px(Context context, float f) {
        return Util.DefaultImpls.dp2px(this, context, f);
    }

    public final Context getContext() {
        return this.context;
    }

    @Override // com.gbsaver.chatlocker.utils.Util
    public float px2dp(Context context, float f) {
        return Util.DefaultImpls.px2dp(this, context, f);
    }

    public final String[] getList() {
        return this.list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.length;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.list[i];
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return (long) Integer.parseInt(this.list[i]);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        Object systemService = this.context.getSystemService("layout_inflater");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.view.LayoutInflater");
        View inflate = ((LayoutInflater) systemService).inflate(R.layout.pin_button, viewGroup, false);
        if (inflate != null) {
            float px2dp = px2dp(this.context, (float) getRealSize().x);
            int dp2px = (int) dp2px(this.context, px2dp >= 390.0f ? 65.0f : (px2dp - ((float) 165)) / 3.0f);
            String str = (String) getItem(i);
            TextView textView = inflate.findViewById(R.id.button);
            ImageView mIvErase = inflate.findViewById(R.id.mIvErase);
            textView.setVisibility(View.VISIBLE);
            mIvErase.setVisibility(View.GONE);
            if (str.equals("-2")) {
                textView.setVisibility(4);
                textView.setEnabled(false);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PinButtonAdapter.m166getView$lambda0(view);
                    }
                });
            } else if (str.equals("-1")) {
                textView.setVisibility(View.GONE);
                mIvErase.setVisibility(View.VISIBLE);
                mIvErase.setOnClickListener(new View.OnClickListener() {
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        PinButtonAdapter.m167getView$lambda1(PinButtonAdapter.this, view2);
                    }
                });
            } else {
//                textView.setLayoutParams(new LinearLayout.LayoutParams(dp2px, dp2px));
                textView.setText(str);
                textView.setOnClickListener(new View.OnClickListener() {

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        PinButtonAdapter.m168getView$lambda2(PinButtonAdapter.this, str, view2);
                    }
                });
            }
        }
        return inflate;
    }

    /* renamed from: getView$lambda-1 */
    public static final void m167getView$lambda1(PinButtonAdapter pinButtonAdapter, View view) {
        ((PinLockActivity) pinButtonAdapter.context).onDeleteButtonClicked();
    }

    /* renamed from: getView$lambda-2 */
    public static final void m168getView$lambda2(PinButtonAdapter pinButtonAdapter, String str, View view) {
        ((PinLockActivity) pinButtonAdapter.context).onPinButtonClicked(str);
    }

    public final void setMargins(View view, int i, int i2, int i3, int i4) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(i, i2, i3, i4);
            view.requestLayout();
        }
    }

    public final Point getRealSize() {
        Object systemService = this.context.getSystemService("window");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.view.WindowManager");
        Display defaultDisplay = ((WindowManager) systemService).getDefaultDisplay();
        Point point = new Point(0, 0);
        if (Build.VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
            return point;
        }
        if (Build.VERSION.SDK_INT >= 13) {
            try {
                Method method = Display.class.getMethod("getRawWidth", new Class[0]);
                Method method2 = Display.class.getMethod("getRawHeight", new Class[0]);
                Object invoke = method.invoke(defaultDisplay, new Object[0]);
                if (invoke != null) {
                    int intValue = ((Integer) invoke).intValue();
                    Object invoke2 = method2.invoke(defaultDisplay, new Object[0]);
                    if (invoke2 != null) {
                        point.set(intValue, ((Integer) invoke2).intValue());
                        return point;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return point;
    }
}
