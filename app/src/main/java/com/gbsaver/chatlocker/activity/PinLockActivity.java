package com.gbsaver.chatlocker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.adapter.PinButtonAdapter;
import com.gbsaver.chatlocker.utils.SharedPrefUtils;
import com.gbsaver.chatlocker.utils.Util;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.Gravity.CENTER;

public final class PinLockActivity extends AppCompatActivity implements Util {
    private boolean closeChat;
    private String fromService;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private String pin = "";
    TextView mTxtNext;

    private final int getMaxPinSize() {
        return 4;
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
    }

    @Override // com.gbsaver.chatlocker.utils.Util
    public float dp2px(Context context, float f) {
        return Util.DefaultImpls.dp2px(this, context, f);
    }

    @Override // com.gbsaver.chatlocker.utils.Util
    public float px2dp(Context context, float f) {
        return Util.DefaultImpls.px2dp(this, context, f);
    }

    public final boolean getCloseChat() {
        return this.closeChat;
    }

    public final void setCloseChat(boolean z) {
        this.closeChat = z;
    }

    public final String getFromService() {
        return this.fromService;
    }

    public final void setFromService(String str) {
        this.fromService = str;
    }

    @Override
    public void onCreate(Bundle bundle) {
        AppCompatDelegate.setDefaultNightMode(1);
        super.onCreate(bundle);
        setContentView(R.layout.activity_pin_lock);
        AdUtils.showNativeAd(PinLockActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), false);
        if (getIntent() != null) {
            this.fromService = getIntent().getStringExtra("fromService");
        }
        PinLockActivity pinLockActivity = this;
        if (SharedPrefUtils.getStringData(pinLockActivity, SharedPrefUtils.keylockPasscode) != null) {
            ((TextView) _$_findCachedViewById(R.id.forgotButton)).setVisibility(0);
            ((TextView) findViewById(R.id.messageTextView)).setText(getString(R.string.simplepinlock_request_pin));
        }
        if (fromService == null) {
            ((TextView) _$_findCachedViewById(R.id.forgotButton)).setVisibility(View.GONE);
        }
        mTxtNext = findViewById(R.id.mTxtNext);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter((ListAdapter) new PinButtonAdapter(pinLockActivity));
        gridView.setNumColumns(3);
        reloadPinView();
        ((TextView) findViewById(R.id.forgotButton)).setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.PinLockActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                startActivity(new Intent(pinLockActivity, ForgotPinActivity.class));
                onBackPressed();
            }
        });
        mTxtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringData = SharedPrefUtils.getStringData(pinLockActivity, SharedPrefUtils.keylockPasscode);
                if (stringData == null) {
                    ((TextView) findViewById(R.id.messageTextView)).setText(getString(R.string.confirm));
                    Intent intent = new Intent(pinLockActivity, ConfirmPinLockActivity.class);
                    intent.putExtra("pin", pin);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                } else if (pin.equals(stringData)) {
//                if (this.label == 0) {
//                    ResultKt.throwOnFailure(obj);
                    setCloseChat(true);
                    ((TextView) findViewById(R.id.messageTextView)).setText(getResources().getString(R.string.simplepinlock_pin_match));
                    showMainFragment();
                    finish();
//                }
                } else {
                    mTxtNext.setVisibility(View.GONE);
                    closeChat = false;
                    clearPin();
                    ((TextView) findViewById(R.id.messageTextView)).setText(getString(R.string.simplepinlock_invalid_pin));
                }
            }
        });
    }
    /*
     *//* renamed from: onCreate$lambda-0 *//*
    public static final void m185onCreate$lambda0(PinLockActivity pinLockActivity, View view) {
        Intent intent = new Intent(pinLockActivity, SecurityQuestionActivity.class);
        intent.putExtra(TypedValues.TransitionType.S_FROM, "FORGOT");
        pinLockActivity.startActivity(intent);
    }*/

    private final void reloadPinView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pinView);
        PinLockActivity pinLockActivity = this;
        int dp2px = (int) dp2px(pinLockActivity, 41.0f);
        int dp2pxh = (int) dp2px(pinLockActivity, 50.0f);
        int dp2px2 = (int) dp2px(pinLockActivity, 10.0f);
        linearLayout.removeAllViews();
        for (int i = 0; i < getMaxPinSize(); i++) {
            int nextInt = i;
            TextView imageView = new TextView(pinLockActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px, dp2pxh, 0.0f);
            layoutParams.setMargins(dp2px2, dp2px2, dp2px2, dp2px2);
            layoutParams.gravity = CENTER;
            imageView.setTextSize(20);
            imageView.setTextColor(Color.BLACK);
            imageView.setLayoutParams(layoutParams);
            imageView.setGravity(CENTER);
            imageView.setBackground(ContextCompat.getDrawable(pinLockActivity, R.drawable.bg_stroke));
            if (nextInt >= this.pin.length()) {
                imageView.setText("");
            } else {
                String pinword = String.valueOf(pin.charAt(i));
                imageView.setText("" + pinword);
            }
            linearLayout.addView(imageView);
        }
    }

    public final void onPinButtonClicked(String str) {
        if (this.pin.length() < getMaxPinSize()) {
            this.pin += str;
            reloadPinView();
        }
        mSetNextVisibility();
    }

    public final void clearPin() {
        this.pin = "";
        reloadPinView();
    }

    public final void onDeleteButtonClicked() {
        if (this.pin.length() > 0) {
            String str = this.pin;
            String substring = str.substring(0, str.length() - 1);
            this.pin = substring;
            reloadPinView();
            mSetNextVisibility();
        }
    }

    private void mSetNextVisibility() {
        if (this.pin.length() == getMaxPinSize()) {
            mTxtNext.setVisibility(View.VISIBLE);
        } else {
            mTxtNext.setVisibility(View.GONE);
        }
    }

    public final void showMainFragment() {
        String str = this.fromService;
        if (str == null) {
            startActivity(new Intent(this, ChatLockerActivity.class));
            finish();
        } else if (str.equals("Whatsapp")) {
            Intent intent = new Intent();
            intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
            intent.addFlags(268435456);
            intent.addFlags(67108864);
            startActivityForResult(intent, 9);
            finish();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        try {
            String str = this.fromService;
            if (str != null && "Chat".equalsIgnoreCase(str) && !this.closeChat) {
                Intent intent = new Intent();
                intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                intent.addFlags(268435456);
                intent.addFlags(67108864);
                startActivityForResult(intent, 9);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void mOnBackClick(View view) {
        onBackPressed();
    }
}
