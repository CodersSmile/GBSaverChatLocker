package com.gbsaver.chatlocker.activity.statusaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.activity.statusaver.apijava.CommonClassForAPI;
import com.gbsaver.chatlocker.activity.statusaver.responseData.TwitterResponse;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.AppLangSessionManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.RootDirectoryTwitter;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class TwitterActivity extends BaseActivity {
    TwitterActivity activity;
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
    protected int getLayoutId() {
        return R.layout.activity_chingari;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();
        AdUtils.showNativeAd(TwitterActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);

        TVTitle.setImageDrawable(getResources().getDrawable(R.drawable.twitter_icon));
        tvAppName.setText(getResources().getString(R.string.twitter_app_name));
        appName.setText(getResources().getString(R.string.twitter_app_name));

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
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


        loginBtn1.setOnClickListener(v -> {
            String LL = etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                Utils.showProgressDialog(activity);
                GetTwitterData();
            }
        });

        tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        TVTitle.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.twitter.android");
        });
    }

    private void GetTwitterData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            if (host.contains("twitter.com")) {
                Long id = getTweetId(etText.getText().toString());
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void PasteText() {
        try {
            etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("twitter.com")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("twitter.com")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("twitter.com")) {
                    etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetTwitterData(String id) {
        String URL = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(activity);
                    commonClassForAPI.callTwitterApi(observer, URL, id);
                }
            } else {
                Utils.setToast(activity, getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            Utils.hideProgressDialog(activity);
            try {
                VideoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")) {
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl, "image"));
                    etText.setText("");
                } else {
                    VideoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size() - 1).getUrl();
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl, "mp4"));
                    etText.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.setToast(activity, getResources().getString(R.string.no_media_on_tweet));
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


    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")) {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}