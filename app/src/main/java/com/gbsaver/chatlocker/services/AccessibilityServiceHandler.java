package com.gbsaver.chatlocker.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.gbsaver.chatlocker.activity.PinLockActivity;
import com.gbsaver.chatlocker.activity.ChatLockerActivity;
import com.gbsaver.chatlocker.databases.DatabaseHandler;
import com.gbsaver.chatlocker.model.HomeModel;

import java.util.ArrayList;
import java.util.Iterator;

public class AccessibilityServiceHandler extends AccessibilityService {
    Context context;
    public String currentAccessibilityPackage;

    /* renamed from: db */
    private DatabaseHandler f118db;
    public ArrayList<HomeModel> ITEMS = new ArrayList<>();
    public boolean isWhatsappUnLockedLocked = false;
    public boolean isChatScreen = true;

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected() {
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (accessibilityEvent != null && accessibilityEvent.getPackageName() != null) {
            try {
//                Log.e("TAG", "data: " + accessibilityEvent.getSource().getViewIdResourceName());
                if (accessibilityEvent.getSource().getViewIdResourceName().equalsIgnoreCase("com.whatsapp:id/contact_row_container")) {
                    this.isChatScreen = true;
                } else {
                    this.isChatScreen = false;
                }
                String charSequence = accessibilityEvent.getPackageName().toString();
                this.currentAccessibilityPackage = charSequence;
                if (charSequence.equalsIgnoreCase("com.whatsapp") && this.isChatScreen && accessibilityEvent.getClassName().toString().equalsIgnoreCase("android.widget.RelativeLayout")) {
                    String obj = accessibilityEvent.getText().toString();
                    if (obj.contains("[")) {
                        obj = obj.replace("[", "");
                    }
                    if (obj.contains(",")) {
                        String[] split = obj.split(",");
                        Log.e("SHAH", "size " + split.length);
                        StringBuilder sb = new StringBuilder();
                        sb.append(split[0]);
                        this.ITEMS.clear();
                        this.ITEMS.addAll(this.f118db.getAllChatLock());
                        if (ChatLockerActivity.Companion.isFromActivity()) {
                            if (!contains(this.ITEMS, sb.toString())) {
                                HomeModel homeModel = new HomeModel();
                                homeModel.setUsername(sb.toString());
                                homeModel.setIsToCheckLock(0);
                                homeModel.setIsLock(1);
                                this.f118db.addChatLockInfo(homeModel);
                                Toast.makeText(this.context, "Chat/Group added successfully.", 0).show();
                                startActivity(new Intent(this.context, ChatLockerActivity.class).putExtra("fromService", "Chat").addFlags(67108864).addFlags(268435456));
                                ChatLockerActivity.Companion.setFromActivity(false);
                            } else {
                                Toast.makeText(this.context, "Already exist", 0).show();
                                startActivity(new Intent(this.context, ChatLockerActivity.class).putExtra("fromService", "Chat").addFlags(67108864).addFlags(268435456));
                                ChatLockerActivity.Companion.setFromActivity(false);
                            }
                        } else if (contains(this.ITEMS, sb.toString()) && isLocked(this.ITEMS, sb.toString()) == 1) {
                            startActivity(new Intent(this.context, PinLockActivity.class).putExtra("fromService", "Chat").addFlags(67108864).addFlags(268435456));
                        }
                    }
                }
                if (accessibilityEvent.getEventType() == 32 && this.currentAccessibilityPackage.equalsIgnoreCase("com.whatsapp")) {
                    if (defaultSharedPreferences.getBoolean("app_lock", false) && !this.isWhatsappUnLockedLocked) {
                        this.isWhatsappUnLockedLocked = true;
                        startActivity(new Intent(this.context, PinLockActivity.class).putExtra("fromService", "Whatsapp").addFlags(335577088));
                        return;
                    }
                    return;
                }
                if (!this.currentAccessibilityPackage.equalsIgnoreCase(this.context.getPackageName()) && !this.currentAccessibilityPackage.equalsIgnoreCase("com.whatsapp")) {
                    this.isWhatsappUnLockedLocked = false;
                }
            } catch (Exception unused) {
                unused.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        this.f118db = new DatabaseHandler(getApplicationContext());
    }

    public boolean contains(ArrayList<HomeModel> arrayList, String str) {
        Iterator<HomeModel> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getUsername().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public int isLocked(ArrayList<HomeModel> arrayList, String str) {
        Iterator<HomeModel> it = arrayList.iterator();
        while (it.hasNext()) {
            HomeModel next = it.next();
            if (next.getUsername().equals(str)) {
                return next.getIsLock();
            }
        }
        return 0;
    }
}
