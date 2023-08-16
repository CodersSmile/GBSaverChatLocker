package com.gbsaver.chatlocker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.PointerIconCompat;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Interfaces.AppInterfaces;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.adapter.ConfirmPinButtonAdapter;
import com.gbsaver.chatlocker.utils.SharedPrefUtils;
import com.gbsaver.chatlocker.utils.Util;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.Gravity.CENTER;

public final class ConfirmPinLockActivity extends AppCompatActivity implements Util {
    private String Newpin;
    public static final Companion Companion = new Companion();
    private static final int CANCELLED = PointerIconCompat.TYPE_CONTEXT_MENU;
    private static final int PIN_REGISTERED = PointerIconCompat.TYPE_HAND;
    private static final int PIN_CONFIRMED = PointerIconCompat.TYPE_HELP;
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

    @Override // com.gbsaver.chatlocker.utils.Util
    public float dp2px(Context context, float f) {
        return Util.DefaultImpls.dp2px(this, context, f);
    }

    @Override // com.gbsaver.chatlocker.utils.Util
    public float px2dp(Context context, float f) {
        return Util.DefaultImpls.px2dp(this, context, f);
    }

    public void mOnBackClick(View view) {
        onBackPressed();
    }

    public static final class Companion {
        private Companion() {
        }

        public final int getCANCELLED() {
            return ConfirmPinLockActivity.CANCELLED;
        }

        public final int getPIN_REGISTERED() {
            return ConfirmPinLockActivity.PIN_REGISTERED;
        }

        public final int getPIN_CONFIRMED() {
            return ConfirmPinLockActivity.PIN_CONFIRMED;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        AppCompatDelegate.setDefaultNightMode(1);
        super.onCreate(bundle);
        setContentView(R.layout.activity_confirm_pin_lock);
        AdUtils.showNativeAd(ConfirmPinLockActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), false);
        if (getIntent() != null) {
            this.Newpin = getIntent().getStringExtra("pin");
        }
        mTxtNext = findViewById(R.id.mTxtNext);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter((ListAdapter) new ConfirmPinButtonAdapter(this));
        gridView.setNumColumns(3);
        reloadPinView();
        mTxtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPinInputFinished();
            }
        });
    }


    private final void reloadPinView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pinView);
        ConfirmPinLockActivity confirmPinLockActivity = this;
        int dp2px = (int) dp2px(confirmPinLockActivity, 41.0f);
        int dp2pxh = (int) dp2px(confirmPinLockActivity, 50.0f);
        int dp2px2 = (int) dp2px(confirmPinLockActivity, 10.0f);
        linearLayout.removeAllViews();

        for (int i = 0; i < getMaxPinSize(); i++) {
            int nextInt = i;
            TextView imageView = new TextView(confirmPinLockActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px, dp2pxh, 0.0f);
            layoutParams.setMargins(dp2px2, dp2px2, dp2px2, dp2px2);
            layoutParams.gravity = CENTER;
            imageView.setTextSize(20);
            imageView.setTextColor(Color.BLACK);
            imageView.setLayoutParams(layoutParams);
            imageView.setGravity(CENTER);
            imageView.setBackground(ContextCompat.getDrawable(confirmPinLockActivity, R.drawable.bg_stroke));
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
        if (this.pin.length() == getMaxPinSize()) {
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

    private final void onPinInputFinished() {
        if (this.Newpin == null) {
            return;
        }
        if (isPinCorrect(getPin())) {
            Toast.makeText(this, "Pin Created..!", Toast.LENGTH_SHORT).show();
            AdUtils.showInterstitialAd(ConfirmPinLockActivity.this, new AppInterfaces.InterStitialADInterface() {
                @Override
                public void adLoadState(boolean isLoaded) {
                    ((TextView) _$_findCachedViewById(R.id.messageTextView)).setText("");
                    SharedPrefUtils.saveData(ConfirmPinLockActivity.this, SharedPrefUtils.keylockPasscode, Newpin);
                    Intent intent = new Intent(ConfirmPinLockActivity.this, ChatLockerActivity.class);
                    intent.putExtra("pin", pin);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return;
                }
            });

        }
        clearPin();
        mSetNextVisibility();
        ((TextView) findViewById(R.id.messageTextView)).setText(getString(R.string.simplepinlock_invalid_pin));
    }

    private final boolean isPinCorrect(String str) {
        return Newpin.equals(str);
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

    public final String getPin() {
        return this.pin;
    }

    public final void clearPin() {
        this.pin = "";
        reloadPinView();
    }

    public final boolean onCancelButtonPressed() {
        setResult(CANCELLED);
        finish();
        return true;
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (keyEvent == null || i != 4 || keyEvent.getAction() != 0) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.pin.length() > 0) {
            onDeleteButtonClicked();
            return true;
        }
        setResult(CANCELLED);
        finish();
        return true;
    }
}
