package com.gbsaver.chatlocker.activity.statusaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.activity.statusaver.apijava.CommonClassForAPI;
import com.gbsaver.chatlocker.activity.statusaver.model.TiktokModel;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.AppLangSessionManager;

import java.util.Locale;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.ROOTDIRECTORYMOJ;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class MojActivity extends BaseActivity {
    MojActivity activity;
    CommonClassForAPI commonClassForAPI;
    @BindView(R.id.imBack)
    ImageView imBack;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.TVTitle)
    ImageView TVTitle;
    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.tvLoginInstagram)
    TextView tvLoginInstagram;
    @BindView(R.id.SwitchLogin)
    Switch SwitchLogin;
    @BindView(R.id.RLLoginInstagram)
    RelativeLayout RLLoginInstagram;
    @BindView(R.id.LLOpenInstagram)
    RelativeLayout LLOpenInstagram;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.only)
    TextView only;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.tv_paste)
    RadioButton tvPaste;
    @BindView(R.id.login_btn1)
    RadioButton loginBtn1;
    @BindView(R.id.RLDownloadLayout)
    LinearLayout RLDownloadLayout;
    @BindView(R.id.RLEdittextLayout)
    RelativeLayout RLEdittextLayout;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        AdUtils.showNativeAd(MojActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);
        TVTitle.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_moj));
        tvAppName.setText(getResources().getString(R.string.moj_app_name));
        appName.setText(getResources().getString(R.string.moj_app_name));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chingari;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        loginBtn1.setOnClickListener(v -> {
            String LL = etText.getText().toString().trim();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetMojData();
            }
        });

        TVTitle.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("in.mohalla.video");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("in.mohalla.video");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, getResources().getString(R.string.app_not_available));
            }

        });
    }

    private void GetMojData() {
        try {
            createFileFolder();
            String host = etText.getText().toString().trim();
            if (host.contains("moj")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(etText.getText().toString().trim());


            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVideoDownload(String Url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTiktokVideo(mojObserver, Url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<TiktokModel> mojObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Utils.hideProgressDialog(activity);
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    startDownload(tiktokModel.getData().getMainvideo(),
                            ROOTDIRECTORYMOJ, activity, "moj_" + System.currentTimeMillis() + ".mp4");
                    etText.setText("");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    private void PasteText() {
        try {
            etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("moj")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("moj")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("moj")) {
                    etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }


}