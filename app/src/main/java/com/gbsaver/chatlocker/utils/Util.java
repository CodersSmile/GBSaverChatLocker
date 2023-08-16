package com.gbsaver.chatlocker.utils;

import android.content.Context;

public interface Util {
    float dp2px(Context context, float f);

    float px2dp(Context context, float f);

    public static final class DefaultImpls {
        public static float dp2px(Util util, Context context, float f) {
            return f * context.getResources().getDisplayMetrics().density;
        }

        public static float px2dp(Util util, Context context, float f) {
            return f / context.getResources().getDisplayMetrics().density;
        }
    }
}
