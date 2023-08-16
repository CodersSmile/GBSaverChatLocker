package com.gbsaver.chatlocker.activity.statusaver.interfaces;

import com.gbsaver.chatlocker.activity.statusaver.model.WhatsappStatusModel;

import java.util.ArrayList;

public class AppInterface {

    public interface WhatsAppAdapterInterface {
        void onSelectAll(ArrayList<WhatsappStatusModel> statusModelArrayList);

        void onSingleDownload(WhatsappStatusModel singleModel);

        void showAllDownload(boolean show);

        void getPosition(int position);

    }
    public interface DownloadFileTask {
        void downloadComplete();
    }


}
