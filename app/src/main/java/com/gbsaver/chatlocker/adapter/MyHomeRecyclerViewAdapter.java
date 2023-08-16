package com.gbsaver.chatlocker.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.activity.ChatLockerActivity;
import com.gbsaver.chatlocker.databases.DatabaseHandler;
import com.gbsaver.chatlocker.model.HomeModel;
import com.gbsaver.chatlocker.utils.DoubleClickEvent;

import java.util.ArrayList;
import java.util.List;

public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.MViewHolder> {
    private deleteSingleItem callback;
    Context context;
    DatabaseHandler databaseHandler;
    public final ChatLockerActivity.OnListFragmentInteractionListener mListener;
    public List<HomeModel> mValues;
    private List<Integer> selectedIds = new ArrayList();

    /* loaded from: classes.dex */
    public interface deleteSingleItem {
        void deleteSingleItemOnClick(HomeModel homeModel);
    }

    public void setdeleteSingleItemClickListener(deleteSingleItem deletesingleitem) {
        this.callback = deletesingleitem;
    }

    public MyHomeRecyclerViewAdapter(List<HomeModel> list, ChatLockerActivity.OnListFragmentInteractionListener onListFragmentInteractionListener) {
        this.mValues = list;
        this.mListener = onListFragmentInteractionListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public MViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        this.databaseHandler = new DatabaseHandler(viewGroup.getContext());
        return new MViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_locker, viewGroup, false));
    }

    public void setChats(List<Integer> list) {
        this.selectedIds = list;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(final MViewHolder mViewHolder, int i) {
        mViewHolder.mItem = this.mValues.get(i);
        boolean z = false;
        if (this.mValues.get(i).getUsername().substring(0, 1) != null) {
            mViewHolder.apphabet_text.setText(this.mValues.get(i).getUsername().substring(0, 1));
        }
        if (this.mValues.get(i).getUsername() != null) {
            mViewHolder.textView_name.setText(this.mValues.get(i).getUsername());
        }
        CheckBox checkBox = mViewHolder.lock;
        if (this.mValues.get(i).getIsLock() <= 0) {
            z = true;
        }
        checkBox.setChecked(z);
        mViewHolder.lock.setTag(Integer.valueOf(i));
        mViewHolder.lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.gbsaver.chatlocker.adapter.MyHomeRecyclerViewAdapter$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                MyHomeRecyclerViewAdapter.this.m53xe2a1ddac(i, compoundButton, z2);
            }
        });
        mViewHolder.delete.setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.adapter.MyHomeRecyclerViewAdapter$$ExternalSyntheticLambda0

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MyHomeRecyclerViewAdapter.this.m52xe8a5a90b(mViewHolder, view);
            }
        });
        if (this.selectedIds.contains(Integer.valueOf(this.mValues.get(i).getId()))) {
            if (Build.VERSION.SDK_INT >= 23) {
                mViewHolder.rlMain.setForeground(new ColorDrawable(ContextCompat.getColor(this.context, R.color.hint_color)));
            }
        } else if (Build.VERSION.SDK_INT >= 23) {
            mViewHolder.rlMain.setForeground(new ColorDrawable(ContextCompat.getColor(this.context, R.color.transpherent)));
        }
        mViewHolder.mView.setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.adapter.MyHomeRecyclerViewAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MyHomeRecyclerViewAdapter.this.mListener != null) {
                    MyHomeRecyclerViewAdapter.this.mListener.onListFragmentInteraction(mViewHolder.mItem);
                }
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-example-chatlockerwa-adapter-MyHomeRecyclerViewAdapter */
    public /* synthetic */ void m53xe2a1ddac(int i, CompoundButton compoundButton, boolean z) {
        HomeModel homeModel = new HomeModel();
        homeModel.setId(this.mValues.get(i).getId());
        if (z) {
            homeModel.setIsLock(0);
        } else {
            homeModel.setIsLock(1);
        }
        homeModel.setUsername(this.mValues.get(i).getUsername());
        homeModel.setIsToCheckLock(this.mValues.get(i).getIsToCheckLock());
        this.databaseHandler.updateChatLock(homeModel);
    }

    /* renamed from: lambda$onBindViewHolder$1$com-example-chatlockerwa-adapter-MyHomeRecyclerViewAdapter */
    public /* synthetic */ void m52xe8a5a90b(MViewHolder mViewHolder, View view) {
        new DoubleClickEvent().preventTwoClick(view);
        deleteSingleItem deletesingleitem = this.callback;
        if (deletesingleitem != null) {
            deletesingleitem.deleteSingleItemOnClick(mViewHolder.mItem);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mValues.size();
    }

    public HomeModel getItem(int i) {
        return this.mValues.get(i);
    }

    /* loaded from: classes.dex */
    public class MViewHolder extends RecyclerView.ViewHolder {
        public final TextView apphabet_text;
        public final ImageView delete;
        public final CheckBox lock;
        public HomeModel mItem;
        public final View mView;
        public LinearLayout rlMain;
        public final TextView textView_name;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MViewHolder(View view) {
            super(view);
            this.mView = view;
            this.apphabet_text = (TextView) view.findViewById(R.id.apphabet_text);
            this.textView_name = (TextView) view.findViewById(R.id.textView_name);
            this.lock = (CheckBox) view.findViewById(R.id.lock);
            this.rlMain = (LinearLayout) view.findViewById(R.id.llMain);
            this.delete = (ImageView) view.findViewById(R.id.delete);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
        public String toString() {
            return super.toString() + " '" + ((Object) this.textView_name.getText()) + "'";
        }
    }
}
