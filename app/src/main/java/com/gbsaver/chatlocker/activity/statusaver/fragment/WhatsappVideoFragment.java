package com.gbsaver.chatlocker.activity.statusaver.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.adapter.WhatsappStatusAdapter;
import com.gbsaver.chatlocker.activity.statusaver.interfaces.FileListWhatsappClickInterface;
import com.gbsaver.chatlocker.activity.statusaver.model.WhatsappStatusModel;
import com.gbsaver.chatlocker.base.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import butterknife.BindView;


public class WhatsappVideoFragment extends BaseFragment {
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
    private FileListWhatsappClickInterface fileListClickInterface;

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
        WhatsappStatusModel whatsappStatusModel;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
        }
        File targetDirector = new File(targetPath);
        allfiles = targetDirector.listFiles();

        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses";
        }
        File targetDirectorBusiness = new File(targetPathBusiness);
        File[] allfilesBusiness = targetDirectorBusiness.listFiles();
        if (allfilesBusiness == null) {
            File targetDirectorBusinessNew = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses");
            allfilesBusiness = targetDirectorBusinessNew.listFiles();
        }
        try {
            Arrays.sort(allfiles, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfiles.length; i++) {
                File file = allfiles[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                            Uri.fromFile(file),
                            allfiles[i].getAbsolutePath(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Arrays.sort(allfilesBusiness, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfilesBusiness.length; i++) {
                File file = allfilesBusiness[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatusB: " + (i + 1),
                            Uri.fromFile(file),
                            allfilesBusiness[i].getAbsolutePath(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statusModelArrayList.size() != 0) {
            tvNoResult.setVisibility(View.GONE);
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
        }
//        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList);
        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList, fileListClickInterface);
        rvFileList.setAdapter(whatsappStatusAdapter);
    }
}
