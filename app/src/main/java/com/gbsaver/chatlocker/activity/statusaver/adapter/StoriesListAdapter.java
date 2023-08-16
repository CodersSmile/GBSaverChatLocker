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
import com.gbsaver.chatlocker.activity.statusaver.model.ItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gbsaver.chatlocker.activity.statusaver.Utils.RootDirectoryInsta;
import static com.gbsaver.chatlocker.activity.statusaver.Utils.startDownload;

public class StoriesListAdapter extends RecyclerView.Adapter<StoriesListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemModel> storyItemModelList;

    public StoriesListAdapter(Context context, ArrayList<ItemModel> list) {
        this.context = context;
        this.storyItemModelList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_whatsapp_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemModel itemModel = storyItemModelList.get(position);
        try {
            if (itemModel.getMedia_type() == 2) {
                viewHolder.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(itemModel.getImage_versions2().getCandidates().get(0).getUrl())
                    .into(viewHolder.pcw);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("PathVideo", itemModel.getVideo_versions().get(0).getUrl());
                context.startActivity(intent);

            }
        });


        viewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemModel.getMedia_type() == 2) {
                    startDownload(itemModel.getVideo_versions().get(0).getUrl(),
                            RootDirectoryInsta, context, "story_" + itemModel.getId() + ".mp4");
                } else {
                    startDownload(itemModel.getImage_versions2().getCandidates().get(0).getUrl(),
                            RootDirectoryInsta, context, "story_" + itemModel.getId() + ".png");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return storyItemModelList == null ? 0 : storyItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_play)
        ImageView ivPlay;
        @BindView(R.id.checkbox_fav)
        CheckBox checkboxFav;
        @BindView(R.id.dowload)
        ImageView dowload;
        @BindView(R.id.tv_download)
        TextView tvDownload;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;
        @BindView(R.id.pcw)
        ImageView pcw;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}