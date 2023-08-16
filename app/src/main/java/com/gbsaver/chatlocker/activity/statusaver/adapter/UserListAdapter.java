package com.gbsaver.chatlocker.activity.statusaver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.statusaver.interfaces.UserListInterface;
import com.gbsaver.chatlocker.activity.statusaver.model.TrayModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TrayModel> trayModelArrayList;
    private UserListInterface userListInterface;

    public UserListAdapter(Context context, ArrayList<TrayModel> list, UserListInterface listInterface) {
        this.context = context;
        this.trayModelArrayList = list;
        this.userListInterface = listInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.real_name.setText(trayModelArrayList.get(position).getUser().getFull_name());
        Glide.with(context).load(trayModelArrayList.get(position).getUser().getProfile_pic_url())
                .thumbnail(0.2f).into(viewHolder.story_pc);

        viewHolder.RLStoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListInterface.userListClick(position, trayModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return trayModelArrayList == null ? 0 : trayModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView real_name;
        CircleImageView story_pc;
        RelativeLayout RLStoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            real_name = itemView.findViewById(R.id.real_name);
            ;
            story_pc = itemView.findViewById(R.id.story_pc);
            ;
            RLStoryLayout = itemView.findViewById(R.id.RLStoryLayout);
            ;
        }
    }
}