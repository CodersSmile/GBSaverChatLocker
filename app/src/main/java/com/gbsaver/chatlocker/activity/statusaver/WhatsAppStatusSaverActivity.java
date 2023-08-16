package com.gbsaver.chatlocker.activity.statusaver;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.PinLockActivity;
import com.gbsaver.chatlocker.activity.statusaver.fragment.WhatsappImageFragment;
import com.gbsaver.chatlocker.activity.statusaver.fragment.WhatsappQImageFragment;
import com.gbsaver.chatlocker.activity.statusaver.fragment.WhatsappQVideoFragment;
import com.gbsaver.chatlocker.activity.statusaver.fragment.WhatsappVideoFragment;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;

public class WhatsAppStatusSaverActivity extends BaseActivity {
    @BindView(R.id.imBack)
    ImageView imBack;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tvAllowAccess)
    TextView tvAllowAccess;
    private WhatsAppStatusSaverActivity activity;

    private File[] allfiles;
    private ArrayList<Uri> fileArrayList;
    ProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_whats_app_status_saver;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = this;
        createFileFolder();
        initViews();
        AdUtils.showNativeAd(WhatsAppStatusSaverActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    private void initViews() {

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AdUtils.showInterstitialAd(WhatsAppStatusSaverActivity.this, new AppInterfaces.InterStitialADInterface() {
//                    @Override
//                    public void adLoadState(boolean isLoaded) {
                finish();
//                    }
//                });
            }
        });


//        LLOpenWhatsapp.setOnClickListener(v -> {
//            Utils.OpenApp(activity,"com.whatsapp");
//        });
        fileArrayList = new ArrayList<>();
        initProgress();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            if (getContentResolver().getPersistedUriPermissions().size() > 0) {
                progressDialog.show();
                new LoadAllFiles().execute();
                tvAllowAccess.setVisibility(View.GONE);
            } else {
                tvAllowAccess.setVisibility(View.VISIBLE);
            }
        } else {
            setupViewPager(viewpager);
            tabs.setupWithViewPager(viewpager);

            for (int i = 0; i < tabs.getTabCount(); i++) {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
                tabs.getTabAt(i).setCustomView(tv);
            }
        }
        tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(activity, R.style.SheetDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_whatsapp_permission, null);
                dialog.setContentView(dialogView);
                ((TextView)dialogView.findViewById(R.id.tvAllow)).setOnClickListener(v -> {
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            StorageManager sm = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);
                            Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                            String startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
                            Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
                            String scheme = uri.toString();
                            scheme = scheme.replace("/root/", "/document/");
                            scheme += "%3A" + startDir;
                            uri = Uri.parse(scheme);
                            intent.putExtra("android.provider.extra.INITIAL_URI", uri);
                            startActivityForResult(intent, 2001);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
    }

    public void initProgress() {
        progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Status. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new WhatsappImageFragment(), getResources().getString(R.string.images));
        adapter.addFragment(new WhatsappVideoFragment(), getResources().getString(R.string.videos));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2001 && resultCode == RESULT_OK) {
                Uri dataUri = data.getData();
                if (dataUri.toString().contains(".Statuses")) {
                    getContentResolver().takePersistableUriPermission(dataUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    progressDialog.show();
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        new LoadAllFiles().execute();
                    }
                } else {
                    Utils.infoDialog(activity, activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_folder));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class LoadAllFiles extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... furl) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(activity, activity.getContentResolver().getPersistedUriPermissions().get(0).getUri());
            for (DocumentFile file : documentFile.listFiles()) {
                if (file.isDirectory()) {

                } else {
                    if (!file.getName().equals(".nomedia")) {
                        fileArrayList.add(file.getUri());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String fileUrl) {
            progressDialog.dismiss();
            ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            adapter.addFragment(new WhatsappQImageFragment(fileArrayList), getResources().getString(R.string.images));
            adapter.addFragment(new WhatsappQVideoFragment(fileArrayList), getResources().getString(R.string.videos));
            viewpager.setAdapter(adapter);
            viewpager.setOffscreenPageLimit(1);
            tabs.setupWithViewPager(viewpager);
            tvAllowAccess.setVisibility(View.GONE);
            for (int i = 0; i < tabs.getTabCount(); i++) {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
                tabs.getTabAt(i).setCustomView(tv);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
        }
    }


}
