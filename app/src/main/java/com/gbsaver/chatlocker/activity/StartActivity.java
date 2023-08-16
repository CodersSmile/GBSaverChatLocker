package com.gbsaver.chatlocker.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Interfaces.AppInterfaces;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.BuildConfig;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.UIHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
//import static com.gbsaver.chatlocker.vpnService.VpnConnected.stopVpn;
import static com.gbsaver.chatlocker.AdsUtils.Utils.Global.isLatestVersion;

public class StartActivity extends BaseActivity {
    @BindView(R.id.mDrawerlayout)
    DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle drawerToggle;

    private AlertDialog alert;
    private Dialog accessbillityDialog;

    public static StartActivity startActivity;

    public static StartActivity getInstance() {
        return startActivity;
    }

    String[] arrPermissionsCamera;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        startActivity = this;
        AdUtils.showNativeAd(StartActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.draweropen, R.string.drawerclose);

        mDrawerlayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        arrPermissionsCamera = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (isLatestVersion())
            arrPermissionsCamera = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};

        Dexter.withContext(startActivity)
                .withPermissions(
                        arrPermissionsCamera
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @OnClick({R.id.mIVMenu, R.id.mIVChatLocker, R.id.mIVDirectChat, R.id.mIVstatusSaver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIVMenu:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;
            case R.id.mIVChatLocker:
                mOpenChatLockerAct();
                break;
            case R.id.mIVDirectChat:
                mOpenDirectChat();
                break;
            case R.id.mIVstatusSaver:
                mOpenstatusSaverAct();
                break;
        }
    }

    private void mOpenstatusSaverAct() {
        if (isChecked()) {
            AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
                @Override
                public void adLoadState(boolean isLoaded) {
                    startActivity(new Intent(StartActivity.this, StatusSaverActivity.class));
                }
            });
        }
      /*  Dexter.withContext(startActivity)
                .withPermissions(
                        arrPermissionsCamera
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
                        @Override
                        public void adLoadState(boolean isLoaded) {
                            startActivity(new Intent(StartActivity.this, StatusSaverActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {*//* ... *//*}
        }).check();*/
    }
    public boolean isChecked() {
        String[] strings;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isLatestVersion()) {
                boolean z2 = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0;
                if (Build.VERSION.SDK_INT < 23 || !z2) {
                    return true;
                }
                strings = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            } else {
                boolean z = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0;
                boolean z2 = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0;
                if (Build.VERSION.SDK_INT < 23 || (!z && !z2)) {
                    return true;
                }
                strings = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            }
            ActivityCompat.requestPermissions(this, strings, 201);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 201) {
            if (iArr.length == 1) {
                if (iArr.length > 0 && iArr[0] == 0) {
                    AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
                        @Override
                        public void adLoadState(boolean isLoaded) {
                            startActivity(new Intent(StartActivity.this, StatusSaverActivity.class));
                        }
                    });
                    return;
                }
                Toast.makeText(this, "Premission not granted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                if (iArr.length > 0 && iArr[0] == 0 && iArr[1] == 0) {
                    AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
                        @Override
                        public void adLoadState(boolean isLoaded) {
                            startActivity(new Intent(StartActivity.this, StatusSaverActivity.class));
                        }
                    });
                    return;
                }
                Toast.makeText(this, "Premission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void mOpenDirectChat() {
        AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent intent = new Intent(StartActivity.this, DirectChatActivity.class);
                startActivity(intent);
            }
        });

    }

    private void mOpenChatLockerAct() {
        AdUtils.showInterstitialAd(StartActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(startActivity, PinLockActivity.class));
            }
        });
    }

    public void mOnNavigationClick(View view) {
        switch (view.getId()) {
            case R.id.mTxtchatLocker:
                mOpenChatLockerAct();
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.mTxtDirectChat:
                mOpenDirectChat();
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.mTxtStatus:
                mOpenstatusSaverAct();
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.mTxtRate:
                mRateApp();
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.mTxtShare:
                mShareApp();
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.mTxtPrivacy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://logisticwave.blogspot.com/p/privacy-policy.html"));
                startActivity(browserIntent);
                mDrawerlayout.closeDrawer(GravityCompat.START);
                break;

        }
    }

    private void mShareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mRateApp() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(getPackageName());
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkRunTimerPermission();
    }

    private final void checkRunTimerPermission() {
        if (!isAccessibilitySettingsOn(startActivity)) {
            showAccessConsent();
        }
        if (isAccessibilitySettingsOn(startActivity) && UIHelper.isMiUi() && Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(startActivity) && this.alert == null) {
            showPopupPermissionSetting();
        }
    }

    private final void showPopupPermissionSetting() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.popup_service_des)).setIcon(R.drawable.ic_splash).setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.HomeActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                UIHelper.goToXiaomiPermissions(startActivity);
            }
        }).create();
        this.alert = create;
        if (!create.isShowing()) {
            AlertDialog alertDialog = this.alert;
            alertDialog.show();
        }
    }

    private final boolean isAccessibilitySettingsOn(Context context) {
        int i;
        Settings.SettingNotFoundException e;
        String str = context.getPackageName() + "/com.gbsaver.chatlocker.services.AccessibilityServiceHandler";
        try {
            i = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), "accessibility_enabled");
            Log.v("TAG", "accessibilityEnabled = " + i);
        } catch (Settings.SettingNotFoundException e3) {
            e = e3;
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter2 = new TextUtils.SimpleStringSplitter(':');
        if (i == 1) {
            String string = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "enabled_accessibility_services");
            if (!TextUtils.isEmpty(string)) {
                simpleStringSplitter2.setString(string);
                while (simpleStringSplitter2.hasNext()) {
                    if (simpleStringSplitter2.next().equals(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private final void showAccessConsent() {
        Dialog dialog = new Dialog(this);
        this.accessbillityDialog = dialog;
        dialog.requestWindowFeature(1);
        Dialog dialog2 = this.accessbillityDialog;
        dialog2.setContentView(R.layout.accessibility_dialog_layout);
        Dialog dialog3 = this.accessbillityDialog;
        Window window = dialog3.getWindow();
        window.setBackgroundDrawableResource(17170445);
        Dialog dialog4 = this.accessbillityDialog;
        Window window2 = dialog4.getWindow();
        window2.setLayout(-1, -2);
        Dialog dialog5 = this.accessbillityDialog;
        dialog5.setCancelable(false);
        Dialog dialog6 = this.accessbillityDialog;
        View findViewById = dialog6.findViewById(R.id.checkBox);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.widget.CheckBox");
        CheckBox checkBox = (CheckBox) findViewById;
        Dialog dialog7 = this.accessbillityDialog;
        View findViewById2 = dialog7.findViewById(R.id.checkBoxWarning);
        Objects.requireNonNull(findViewById2, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) findViewById2;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    textView.setVisibility(8);
                }
            }
        });
        Dialog dialog8 = this.accessbillityDialog;
        View findViewById3 = dialog8.findViewById(R.id.privacyButton);
        Objects.requireNonNull(findViewById3, "null cannot be cast to non-null type android.widget.TextView");
        ((TextView) findViewById3).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://climaxtechnologyapps.blogspot.com/2021/09/privacy-policy-of-climax-technology-apps.html")));
            }
        });
        Dialog dialog9 = this.accessbillityDialog;
        View findViewById4 = dialog9.findViewById(R.id.confirmButton);
        Objects.requireNonNull(findViewById4, "null cannot be cast to non-null type androidx.cardview.widget.CardView");
        ((CardView) findViewById4).setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.HomeActivity$showAccessConsent$3

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    textView.setVisibility(0);
                    return;
                }
                Dialog accessbillityDialog = getAccessbillityDialog();
                accessbillityDialog.dismiss();
                getPackageName();
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(268435456);
                startActivity(intent);
            }
        });
        ((CardView) dialog9.findViewById(R.id.anullerButton)).setOnClickListener(new View.OnClickListener() {

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                getAccessbillityDialog().dismiss();
            }
        });
        Dialog dialog10 = this.accessbillityDialog;
        if (!dialog10.isShowing()) {
            Dialog dialog11 = this.accessbillityDialog;
            dialog11.show();
        }
    }

    public final Dialog getAccessbillityDialog() {
        return this.accessbillityDialog;
    }
    public void onDestroy() {
        super.onDestroy();
//        stopVpn();
    }
}