package com.gbsaver.chatlocker.activity.statusaver.fragment;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.SettingsManager;
import com.gbsaver.chatlocker.activity.statusaver.Utils;
import com.gbsaver.chatlocker.activity.statusaver.adapter.WhatsappStatusAdapter;
import com.gbsaver.chatlocker.activity.statusaver.interfaces.AppInterface;
import com.gbsaver.chatlocker.activity.statusaver.interfaces.FileListWhatsappClickInterface;
import com.gbsaver.chatlocker.activity.statusaver.model.WhatsappStatusModel;
import com.gbsaver.chatlocker.base.BaseFragment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;

import static com.gbsaver.chatlocker.activity.statusaver.Utils.createFileFolder;


public class WhatsappQVideoFragment extends BaseFragment implements FileListWhatsappClickInterface {
    @BindView(R.id.iv_empty)
    LottieAnimationView ivEmpty;
    @BindView(R.id.tv_NoResult)
    TextView tvNoResult;
    @BindView(R.id.rl_notfound)
    RelativeLayout rlNotfound;
    @BindView(R.id.cb_selectAll)
    CheckBox cbSelectAll;
    @BindView(R.id.downloadall)
    ImageView downloadall;
    @BindView(R.id.hello)
    LinearLayout hello;
    @BindView(R.id.rv_fileList)
    RecyclerView rvFileList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    private File[] allfiles;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    private WhatsappStatusAdapter whatsappStatusAdapter;
    private boolean isAllSelected = true;
    private ArrayList<Uri> fileArrayList;
    String fileName = "";
    public String saveFilePath = SettingsManager.DOWNLOAD_FOLDER_VIDEO + File.separator;

    public WhatsappQVideoFragment(ArrayList<Uri> fileArrayList) {
        this.fileArrayList = fileArrayList;
    }

    @Override
    protected View onViewCreated(View inflate) {
        initViews();
        return inflate;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_whatsapp_image;
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        getData();
        swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
            getData();
            swiperefresh.setRefreshing(false);
        });

    }

    private void getData() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                for (int i = 0; i < fileArrayList.size(); i++) {
                    WhatsappStatusModel whatsappStatusModel;
                    Uri uri = fileArrayList.get(i);
                    if (uri.toString().endsWith(".mp4")) {
                        whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                                uri,
                                new File(uri.toString()).getAbsolutePath(),
                                new File(uri.toString()).getName());
                        statusModelArrayList.add(whatsappStatusModel);
                    }
                }
                if (statusModelArrayList.size() != 0) {
                    tvNoResult.setVisibility(View.GONE);
                } else {
                    tvNoResult.setVisibility(View.VISIBLE);
                }
                whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList, new AppInterface.WhatsAppAdapterInterface() {
                    @Override
                    public void onSelectAll(ArrayList<WhatsappStatusModel> statusModelArrayList) {

                    }

                    @Override
                    public void onSingleDownload(WhatsappStatusModel singleModel) {
                        if (singleModel != null) {
//                            AdUtils.showInterstitialAd(getActivity(), new AppInterfaces.InterStitialADInterface() {
//                                @Override
//                                public void adLoadState(boolean isLoaded) {
                            saveFile(singleModel.getUri(), singleModel.getPath());
//                                }
//                            });

                        }
                    }

                    @Override
                    public void showAllDownload(boolean show) {

                    }

                    @Override
                    public void getPosition(int position) {

                    }

                });
                RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvFileList.setLayoutManager(layoutManager);
                rvFileList.setAdapter(whatsappStatusAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void saveFile(Uri singleFileURI, String singleFilePath) {
        createFileFolder();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            try {
                if (singleFileURI.toString().endsWith(".mp4")) {
                    fileName = "status_" + System.currentTimeMillis() + ".mp4";
                    new DownloadFileTask().execute(singleFileURI.toString());
                } else {
                    fileName = "status_" + System.currentTimeMillis() + ".png";
                    new DownloadFileTask().execute(singleFileURI.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            final String path = singleFilePath;
            String filename = path.substring(path.lastIndexOf("/") + 1);
            final File file = new File(path);
            File destFile = new File(singleFilePath);
            try {
                FileUtils.copyFileToDirectory(file, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileNameChange = filename.substring(12);
            File newFile = new File(saveFilePath + fileNameChange);
            String contentType = "image/*";
            if (singleFileURI.toString().endsWith(".mp4")) {
                contentType = "video/*";
            } else {
                contentType = "image/*";
            }
            MediaScannerConnection.scanFile(getActivity(), new String[]{newFile.getAbsolutePath()}, new String[]{contentType}, new MediaScannerConnection.MediaScannerConnectionClient() {
                public void onMediaScannerConnected() {
                    //NA
                }

                public void onScanCompleted(String path, Uri uri) {
                    //NA
                }
            });

            File from = new File(saveFilePath, filename);
            File to = new File(saveFilePath, fileNameChange);
            from.renameTo(to);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.saved_to) + saveFilePath + fileNameChange, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getPosition(int position) {

    }


    public class DownloadFileTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... furl) {
            try {
                InputStream in = getActivity().getContentResolver().openInputStream(Uri.parse(furl[0]));
                File f = null;
                f = new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO + File.separator + fileName);
                f.setWritable(true, false);
                OutputStream outputStream = new FileOutputStream(f);
                byte buffer[] = new byte[1024];
                int length = 0;

                while ((length = in.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                in.close();
            } catch (IOException e) {
                System.out.println("error in creating a file");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String fileUrl) {
            Utils.setToast(getActivity(), getActivity().getResources().getString(R.string.download_complete));
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaScannerConnection.scanFile(getActivity(), new String[]{new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO + File.separator + fileName).getAbsolutePath()}, null, (path, uri) -> {
                        //no action
                    });
                } else {
                    getActivity().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO + File.separator + fileName))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
