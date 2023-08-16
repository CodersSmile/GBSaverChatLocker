package com.gbsaver.chatlocker.activity.statusaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
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
import com.gbsaver.chatlocker.activity.StatusSaverActivity;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.activity.statusaver.adapter.FBStoriesListAdapter;
import com.gbsaver.chatlocker.activity.statusaver.adapter.FbStoryUserListAdapter;
import com.gbsaver.chatlocker.activity.statusaver.apijava.CommonClassForAPI;
import com.gbsaver.chatlocker.activity.statusaver.model.NodeModel;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.utils.AppLangSessionManager;
import com.gbsaver.chatlocker.utils.SharePrefs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.RootDirectoryFacebook;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class FacebookActivity extends BaseActivity {
    FacebookActivity activity;
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
    private String videoUrl;
    private ClipboardManager clipBoard;
    ArrayList<NodeModel> edgeModelList;
    TextView app_name;

    AppLangSessionManager appLangSessionManager;
    private String strName = "facebook";
    private String strNameSecond = "fb";
    FbStoryUserListAdapter fbStoryUserListAdapter;
    FBStoriesListAdapter fbStoriesListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_instagram;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = this;
        app_name = findViewById(R.id.app_name);
        AdUtils.showNativeAd(FacebookActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads_big), true);

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        pasteText();
    }


    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        tvAppName.setText(activity.getResources().getString(R.string.facebook_app_name));
        appName.setText(activity.getResources().getString(R.string.facebook_app_name));
        TVTitle.setImageResource(R.drawable.facebook_icon);
        tvLoginInstagram.setText(getResources().getString(R.string.download_stories));
        imBack.setOnClickListener(view -> onBackPressed());

        tvPaste.setOnClickListener(view -> {
            pasteText();
        });

        tvAppName.setText(activity.getResources().getString(R.string.facebook_app_name));
        appName.setText(activity.getResources().getString(R.string.facebook_app_name));

        loginBtn1.setOnClickListener(v -> {
            String ll = etText.getText().toString();
            if (ll.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(ll).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                getFacebookData();
            }

        });
        TVTitle.setOnClickListener(v -> Utils.OpenApp(activity, "com.facebook.katana"));

        edgeModelList = new ArrayList<>();
//        fbStoryUserListAdapter = new FbStoryUserListAdapter(activity, edgeModelList, FacebookActivity.this);

        if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISFBLOGIN)) {
            layoutCondition();
            SwitchLogin.setChecked(true);
        } else {
            SwitchLogin.setChecked(false);
        }

    }

    public void layoutCondition() {
    }


    private void getFacebookData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            if (host.contains(strName) || host.contains(strNameSecond)) {
                Utils.showProgressDialog(activity);
                new CallGetFacebookData().execute(etText.getText().toString());
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pasteText() {
        try {
            etText.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            copyIntent = StatusSaverActivity.extractLinks(copyIntent);
            if (copyIntent == null || copyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {
                    Log.d(TAG, "PasteText");
                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strName)) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    } else if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(strNameSecond)) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains(strName)) {
                        etText.setText(item.getText().toString());
                    } else if (item.getText().toString().contains(strNameSecond)) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains(strName)) {
                    etText.setText(copyIntent);
                } else if (copyIntent.contains(strNameSecond)) {
                    etText.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CallGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return facebookDoc;
        }

        @Override
        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                videoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                if (!videoUrl.equals("")) {
                    startDownload(videoUrl, RootDirectoryFacebook, activity, "facebook_" + System.currentTimeMillis() + ".mp4");
                    videoUrl = "";
                    etText.setText("");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100 && resultCode == RESULT_OK) {
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISFBLOGIN)) {
                    SwitchLogin.setChecked(true);
                    layoutCondition();
                } else {
                    SwitchLogin.setChecked(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}