package com.gbsaver.chatlocker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public final class UIHelper {
    public static final void hideKeyboard(Fragment fragment) {
        FragmentActivity activity;
        View view = fragment.getView();
        if (view != null && (activity = fragment.getActivity()) != null) {
            hideKeyboard(activity, view);
        }
    }

    public static final void hideKeyboard(Activity activity) {
        Activity activity2 = activity;
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(activity2);
        }
        hideKeyboard(activity2, currentFocus);
    }

    public static final void hideKeyboard(Context context, View view) {
        Object systemService = context.getSystemService("input_method");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
        ((InputMethodManager) systemService).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static final boolean isMiUi() {
        String systemProperty = getSystemProperty("ro.miui.ui.version.name");
        return systemProperty != null && (systemProperty.equalsIgnoreCase("") ^ true);
    }

    public static final boolean isMiuiWithApi28OrMore() {
        return isMiUi() && Build.VERSION.SDK_INT >= 28;
    }

    public static final void goToXiaomiPermissions(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        context.startActivity(intent);
    }

    private static final String getSystemProperty(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream()), 1024);
            try {
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return readLine;
            } catch (IOException unused) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException unused2) {
            bufferedReader = null;
        } catch (Throwable th3) {
            th = th3;
        }
        return str;
    }
}
