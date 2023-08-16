package com.gbsaver.chatlocker.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.widget.TextView;

import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.FirebaseUtils;
import com.gbsaver.chatlocker.AdsUtils.Interfaces.AppInterfaces;
import com.gbsaver.chatlocker.AdsUtils.PreferencesManager.AppPreferencesManger;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.AdsUtils.Utils.Global;
import com.gbsaver.chatlocker.AdsUtils.Utils.StringUtils;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.vpnService.LoginListener;
import com.gbsaver.chatlocker.vpnService.VPNInitiator_Handler;
import com.gbsaver.chatlocker.vpnService.VpnConnectListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;

public class SplashActivity extends AppCompatActivity {
    Activity activity;
    private RelativeLayout rltest;
    private AdsJsonPOJO adsJsonPOJO;
    Thread timer;
    private SharedPreferences sharedPreferences;

    AppPreferencesManger appPreferences;
    static FirebaseRemoteConfig mFirebaseRemoteConfig;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(512, 512);
        getWindow().setStatusBarColor(0);
        setContentView((int) R.layout.activity_splash);
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(Constants.PACKAGE_NAME, 0);
        this.sharedPreferences = sharedPreferences2;
        if (!sharedPreferences2.getString(Constants.CURRENCY, "null").equals("null")) {
            Constants.CURRENCY_STORED = this.sharedPreferences.getString(Constants.CURRENCY, (String) null);
        }
        activity = SplashActivity.this;
        appPreferences = new AppPreferencesManger(activity);
        AppPreferencesManger appPreferencesManger = new AppPreferencesManger(this);

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ADSJSON);

        Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
     /*   new VPNInitiator_Handler(activity, new VPNInitiator_Handler.VPNInterface() {
            @Override
            public void handlePostAd() {
                nextActivity();
            }
        });*/

        if (Constants.adsJsonPOJO != null && !StringUtils.isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
            AdUtils.showAppOpenAd(activity, new AppInterfaces.AppOpenADInterface() {
                @Override
                public void appOpenAdState(boolean state_load) {
                    nextActivity();
                }
            });


        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(activity, new AppInterfaces.AdDataInterface() {
                @Override
                public void getAdData(AdsJsonPOJO adsJsonPOJO) {
                    //Need to call this only once per
                    appPreferencesManger.setAdsModel(adsJsonPOJO);
                    Constants.adsJsonPOJO = adsJsonPOJO;
                    Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
                    AdUtils.showAppOpenAd(activity, new AppInterfaces.AppOpenADInterface() {
                        @Override
                        public void appOpenAdState(boolean state_load) {
                            nextActivity();
                        }
                    });
                }
            });
//
        }

    }

    private void AppOpenLoad() {
        AppPreferencesManger appPreferencesManger = new AppPreferencesManger(this);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ADSJSON);
        Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
        if (Constants.adsJsonPOJO != null && !StringUtils.isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
            AdUtils.showAppOpenAd(SplashActivity.this, new AppInterfaces.AppOpenADInterface() {
                @Override
                public void appOpenAdState(boolean state_load) {
                    nextActivity();
                }
            });


        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(SplashActivity.this, new AppInterfaces.AdDataInterface() {
                @Override
                public void getAdData(AdsJsonPOJO adsJsonPOJO) {
                    appPreferencesManger.setAdsModel(adsJsonPOJO);
                    Constants.adsJsonPOJO = adsJsonPOJO;
                    Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
                    AdUtils.showAppOpenAd(SplashActivity.this, new AppInterfaces.AppOpenADInterface() {
                        @Override
                        public void appOpenAdState(boolean state_load) {
                            nextActivity();
                        }
                    });
                }
            });
//
        }
    }

    public void nextActivity() {
        timer = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                    finish();
                }
            }
        };
        timer.start();

    }

    public void onDestroy() {
        super.onDestroy();
//        stopVpn();
    }
}