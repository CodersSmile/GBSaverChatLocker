package com.gbsaver.chatlocker.activity.statusaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import com.bumptech.glide.Glide;
import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.activity.statusaver.apijava.CommonClassForAPI;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.AppLangSessionManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.RootDirectoryRoposo;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class ReposeActivity extends BaseActivity {
    ReposeActivity activity;
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
        AdUtils.showNativeAd(ReposeActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);

        TVTitle.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_roposo));
        tvAppName.setText(getResources().getString(R.string.roposo_app_name));
        appName.setText(getResources().getString(R.string.roposo_app_name));

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

        tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        TVTitle.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.roposo.android");
        });
    }

    private void GetRoposoData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            if (host.contains("roposo")) {
                Utils.showProgressDialog(activity);
                new callGetRoposoData().execute(etText.getText().toString());
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("roposo")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("roposo")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("roposo")) {
                    etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                RoposoDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return RoposoDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                VideoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                if (VideoUrl == null || VideoUrl.equals("")) {
                    VideoUrl = result.select("meta[property=\"og:video:url\"]").last().attr("content");
                }
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        startDownload(VideoUrl, RootDirectoryRoposo, activity, "roposo_" + System.currentTimeMillis() + ".mp4");
                        VideoUrl = "";
                        etText.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
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


}