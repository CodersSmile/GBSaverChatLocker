package com.gbsaver.chatlocker.broadcast;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            context.startService(new Intent(context, AccessibilityService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
