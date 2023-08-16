package com.gbsaver.chatlocker.activity.statusaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.VideoPlayerActivity;
import com.gbsaver.chatlocker.activity.statusaver.model.NodeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gbsaver.chatlocker.activity.statusaver.Utils.RootDirectoryFacebook;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class FBStoriesListAdapter extends RecyclerView.Adapter<FBStoriesListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NodeModel> nodeModels;

    public FBStoriesListAdapter(Context context, ArrayList<NodeModel> list) {
        this.context = context;
        this.nodeModels = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_whatsapp_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        NodeModel nodeModel = nodeModels.get(position);
        try {
            if (nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().get__typename().equalsIgnoreCase("Video")) {
                viewHolder.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivPlay.setVisibility(View.GONE);
            }

            viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("PathVideo", nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().getPlayable_url_quality_hd());
                    context.startActivity(intent);
                }
            });
            Glide.with(context)
                    .load(nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().getPreviewImage().get("uri").getAsString())
                    .thumbnail(0.2f).into(viewHolder.pcw);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        viewHolder.tvDownload.setOnClickListener(v -> {
            if (nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().get__typename().equalsIgnoreCase("Video")) {
                startDownload(nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().getPlayable_url_quality_hd(),
                        RootDirectoryFacebook, context, "fbstory_" + System.currentTimeMillis() + ".mp4");
            } else {
                startDownload(nodeModel.getNodeDataModel().getAttachmentsList().get(0).getMediaDataModel().getPreviewImage().get("uri").getAsString(),
                        RootDirectoryFacebook, context, "fbstory_" + System.currentTimeMillis() + ".png");

            }
        });


    }

    @Override
    public int getItemCount() {
        return nodeModels == null ? 0 : nodeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pcw)
        ImageView pcw;
        @BindView(R.id.iv_play)
        ImageView ivPlay;
        @BindView(R.id.checkbox_fav)
        CheckBox checkboxFav;
        @BindView(R.id.dowload)
        ImageView dowload;
        @BindView(R.id.RLM)
        RelativeLayout RLM;
        @BindView(R.id.tv_download)
        TextView tvDownload;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
