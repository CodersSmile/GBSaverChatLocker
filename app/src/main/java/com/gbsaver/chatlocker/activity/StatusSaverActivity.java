package com.gbsaver.chatlocker.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Interfaces.AppInterfaces;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.WhatsAppStatusSaverActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.FacebookActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.InstagramActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.MojActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.ReposeActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.SharechatActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.TikTokActivity;
import com.gbsaver.chatlocker.activity.statusaver.activity.TwitterActivity;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.AppLangSessionManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import butterknife.OnClick;

import static com.gbsaver.chatlocker.AdsUtils.Utils.Global.isLatestVersion;

public class StatusSaverActivity extends BaseActivity {
    String CopyKey = "";
    String CopyValue = "";
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;
    StatusSaverActivity activity;
    String[] arrPermissionsCamera;

    public abstract class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status_saver;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        arrPermissionsCamera = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (isLatestVersion())
            arrPermissionsCamera = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};


        initViews();
        AdUtils.showNativeAd(StatusSaverActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);
        AdUtils.showNativeAd(StatusSaverActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big1), true);

        Dexter.withContext(this)
                .withPermissions(
                        arrPermissionsCamera
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    CopyValue = extractLinks(CopyValue);
                    Dexter.withContext(this)
                            .withPermissions(
                                    arrPermissionsCamera
                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                callText(value);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                    }).check();

                } else {
                    CopyValue = "";
                    Dexter.withContext(this)
                            .withPermissions(
                                    arrPermissionsCamera
                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                callText(value);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                    }).check();
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("instagram.com")) {
                callInstaActivity();
            } else if (CopiedText.contains("facebook.com") || CopiedText.contains("fb")) {
                callFacebookActivity();
            } else if (CopiedText.contains("tiktok.com")) {
//                callTikTokActivity();
            } else if (CopiedText.contains("twitter.com")) {
//                callTwitterActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String extractLinks(String text) {
        Matcher m = Patterns.WEB_URL.matcher(text);
        String url = "";
        while (m.find()) {
            url = m.group();
            Log.d("New URL", "URL extracted: " + url);

            break;
        }
        return url;
    }

    @OnClick({R.id.mIvBack, R.id.mIvwhatUp, R.id.mIvInsta, R.id.mIvShareChat, R.id.mIvRoposo, R.id.mIvFacebook, R.id.mIvMoj, R.id.mIvTwitter, R.id.mIvTiktok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvBack:
                finish();
                break;
            case R.id.mIvwhatUp:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
                                @Override
                                public void adLoadState(boolean isLoaded) {
                                    startActivity(new Intent(StatusSaverActivity.this, WhatsAppStatusSaverActivity.class));
                                }
                            });
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvInsta:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callInstaActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvShareChat:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callShareChatActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvRoposo:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callRoposoActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvFacebook:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callFacebookActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvMoj:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callMojActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvTwitter:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callTwitterActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
            case R.id.mIvTiktok:
                Dexter.withContext(StatusSaverActivity.this)
                        .withPermissions(
                                arrPermissionsCamera
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            callTikTokActivity();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                break;
        }
    }

    public void callMojActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, MojActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callRoposoActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, ReposeActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callShareChatActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, SharechatActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callTikTokActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, TikTokActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callTwitterActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, TwitterActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callFacebookActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, FacebookActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callInstaActivity() {
        AdUtils.showInterstitialAd(StatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                Intent i = new Intent(activity, InstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(activity, StatusSaverActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.blackTheme))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

}