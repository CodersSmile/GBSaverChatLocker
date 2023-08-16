package com.gbsaver.chatlocker.utils;

import android.view.View;

public final class DoubleClickEvent {
    public final void preventTwoClick(View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {

            @Override // java.lang.Runnable
            public final void run() {
                DoubleClickEvent.m194preventTwoClick$lambda0(view);
            }
        }, 500);
    }

    public static final void m194preventTwoClick$lambda0(View view) {
        view.setEnabled(true);
    }
}
