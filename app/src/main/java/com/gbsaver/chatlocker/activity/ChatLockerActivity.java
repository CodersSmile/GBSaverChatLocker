package com.gbsaver.chatlocker.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.adapter.MyHomeRecyclerViewAdapter;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.gbsaver.chatlocker.databases.DatabaseHandler;
import com.gbsaver.chatlocker.listener.RecyclerItemClickListener;
import com.gbsaver.chatlocker.model.HomeModel;
import com.gbsaver.chatlocker.utils.UIHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ChatLockerActivity extends BaseActivity implements ActionMode.Callback, MyHomeRecyclerViewAdapter.deleteSingleItem {
    public static final Companion Companion = new Companion();
    private static boolean isFromActivity;
    private String fromService;
    public static ChatLockerActivity chatLockerActivity;
    private Dialog accessbillityDialog;
    private AlertDialog alert;
    private MyHomeRecyclerViewAdapter adapter;
    private LottieAnimationView anim;
    private DatabaseHandler f119db;
    private boolean isMultiSelect;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final String ARG_COLUMN_COUNT = "column-count";
    private ArrayList<HomeModel> ITEMS = new ArrayList<>();
    private int mColumnCount = 1;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private ActionMode actionMode;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(HomeModel homeModel);
    }

    public static ChatLockerActivity getInstance() {
        return chatLockerActivity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_locker;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        chatLockerActivity = this;
        if (getIntent() != null) {
            this.fromService = getIntent().getStringExtra("fromService");
            Log.e("TAG", "onCreate: " + this.fromService);
        }
        AdUtils.showNativeAd(ChatLockerActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), false);
        this.recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerview);

        this.anim = (LottieAnimationView) findViewById(R.id.ic);
        int i = this.mColumnCount;
        if (i <= 1) {
            RecyclerView recyclerView = this.recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(chatLockerActivity));
        } else {
            RecyclerView recyclerView2 = this.recyclerView;
            recyclerView2.setLayoutManager(new GridLayoutManager(chatLockerActivity, i));
        }
        this.ITEMS.clear();
        DatabaseHandler databaseHandler = new DatabaseHandler(chatLockerActivity);
        this.f119db = databaseHandler;
        this.ITEMS.addAll(databaseHandler.getAllChatLock());
        if(ITEMS.size()==0){
            anim.setVisibility(View.VISIBLE);
        }else {
            anim.setVisibility(View.GONE);
        }
        MyHomeRecyclerViewAdapter myHomeRecyclerViewAdapter = new MyHomeRecyclerViewAdapter(this.ITEMS, this.mListener);
        myHomeRecyclerViewAdapter.setdeleteSingleItemClickListener(this);
        this.adapter = myHomeRecyclerViewAdapter;
        RecyclerView recyclerView3 = this.recyclerView;
        recyclerView3.setAdapter(myHomeRecyclerViewAdapter);
        RecyclerView recyclerView4 = this.recyclerView;
        recyclerView4.addOnItemTouchListener(new RecyclerItemClickListener(chatLockerActivity, this.recyclerView, new RecyclerItemClickListener.OnItemClickListener() { // from class: com.gbsaver.chatlocker.ui.fragments.HomeFragment$onCreateView$1
            @Override
            // com.gbsaver.chatlocker.listener.RecyclerItemClickListener.OnItemClickListener
            public void onItemClick(View view, int i2) {
                if (isMultiSelect()) {
                    multiSelect(i2);
                }
            }

            @Override
            // com.gbsaver.chatlocker.listener.RecyclerItemClickListener.OnItemClickListener
            public void onItemLongClick(View view, int i2) {
                if (!isMultiSelect()) {
                    setSelectedIds(new ArrayList<>());
                    getSelectedIds();
                    setMultiSelect(true);
                    isMultiSelect();
                    if (getActionMode() == null) {
                        setActionMode(startActionMode(chatLockerActivity));
                        getActionMode();
                    }
                }
                multiSelect(i2);
            }
        }));
        ((ImageView) findViewById(R.id.floatingActionButton)).setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.HomeActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                try {
                    if (!chatLockerActivity.isAccessibilitySettingsOn(chatLockerActivity)) {
                        showAccessConsent();
                    } else {
                        isFromActivity = true;
                        Intent intent = new Intent();
                        intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                        intent.addFlags(268435456);
                        intent.addFlags(67108864);
                        startActivityForResult(intent, 9);
                    }
                } catch (Exception unused) {
                    Toast.makeText(chatLockerActivity, getResources().getString(R.string.not_installed), 0).show();
                }
            }
        });
    }

    public final ArrayList<Integer> getSelectedIds() {
        return this.selectedIds;
    }

    public final void setSelectedIds(ArrayList<Integer> arrayList) {
        this.selectedIds = arrayList;
    }

    public final void setMultiSelect(boolean z) {
        this.isMultiSelect = z;
    }

    public final boolean isMultiSelect() {
        return this.isMultiSelect;
    }

    public final ActionMode getActionMode() {
        return this.actionMode;
    }

    public final void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public static final class Companion {

        private Companion() {
        }

        public final boolean isFromActivity() {
            return isFromActivity;
        }

        public final void setFromActivity(boolean z) {
            isFromActivity = z;
        }
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//        actionMode.getMenuInflater().inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
       /* if (menuItem.getItemId() != R.id.action_delete) {
            return false;
        }
        AskOption().show();*/
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        this.isMultiSelect = false;
        this.selectedIds = new ArrayList<>();
        MyHomeRecyclerViewAdapter myHomeRecyclerViewAdapter = this.adapter;
        myHomeRecyclerViewAdapter.setChats(new ArrayList());
    }

    @Override // com.gbsaver.chatlocker.adapter.MyHomeRecyclerViewAdapter.deleteSingleItem
    public void deleteSingleItemOnClick(HomeModel homeModel) {
        deleteDialog(homeModel);
    }

    private final void deleteDialog(HomeModel homeModel) {
        Dialog dialog = new Dialog(chatLockerActivity);
        dialog.requestWindowFeature(1);
//        DeleteDialogueBinding inflate = DeleteDialogueBinding.inflate(getLayoutInflater());
        dialog.setContentView(R.layout.delete_dialogue);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;
        layoutParams.gravity = 17;
        Window window2 = dialog.getWindow();
        window2.setAttributes(layoutParams);
        MaterialButton textView = dialog.findViewById(R.id.mMbtnCancel);
        dialog.show();
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.fragments.HomeFragment$$ExternalSyntheticLambda3

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                dialog.dismiss();
                ;
            }
        });
        ((MaterialButton) dialog.findViewById(R.id.mMbtnDiscard)).setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.fragments.HomeFragment$$ExternalSyntheticLambda4

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DatabaseHandler databaseHandler = f119db;
                databaseHandler.deleteChatInfo(homeModel);
                notifyNewData();
                dialog.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.fragments.HomeFragment$$ExternalSyntheticLambda2

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private final void notifyNewData() {
        this.ITEMS.clear();
        ArrayList<HomeModel> arrayList = this.ITEMS;
        DatabaseHandler databaseHandler = this.f119db;
        arrayList.addAll(databaseHandler.getAllChatLock());
        MyHomeRecyclerViewAdapter myHomeRecyclerViewAdapter = this.adapter;
        myHomeRecyclerViewAdapter.notifyDataSetChanged();
        if (this.ITEMS.isEmpty()) {
            LottieAnimationView lottieAnimationView = this.anim;
            lottieAnimationView.setVisibility(0);
            return;
        }
        LottieAnimationView lottieAnimationView2 = this.anim;
        lottieAnimationView2.setVisibility(8);
    }

    public final void multiSelect(int i) {
        MyHomeRecyclerViewAdapter myHomeRecyclerViewAdapter = this.adapter;
        HomeModel item = myHomeRecyclerViewAdapter.getItem(i);
        if (item != null && this.actionMode != null) {
            if (this.selectedIds.contains(Integer.valueOf(item.getId()))) {
                this.selectedIds.remove(Integer.valueOf(item.getId()));
            } else {
                this.selectedIds.add(Integer.valueOf(item.getId()));
            }
            if (this.selectedIds.size() > 0) {
                ActionMode actionMode = this.actionMode;
                actionMode.setTitle(String.valueOf(this.selectedIds.size()));
            } else {
                ActionMode actionMode2 = this.actionMode;
                actionMode2.setTitle("");
                ActionMode actionMode3 = this.actionMode;
                actionMode3.finish();
            }
            MyHomeRecyclerViewAdapter myHomeRecyclerViewAdapter2 = this.adapter;
            myHomeRecyclerViewAdapter2.setChats(this.selectedIds);
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRunTimerPermission();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishAffinity();
    }

    private final void checkRunTimerPermission() {
        if (!isAccessibilitySettingsOn(chatLockerActivity)) {
            showAccessConsent();
        }
        if (isAccessibilitySettingsOn(chatLockerActivity) && UIHelper.isMiUi() && Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(chatLockerActivity) && this.alert == null) {
            showPopupPermissionSetting();
        }
    }

    private final void showPopupPermissionSetting() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.popup_service_des)).setIcon(R.drawable.ic_splash).setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.HomeActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                UIHelper.goToXiaomiPermissions(chatLockerActivity);
            }
        }).create();
        this.alert = create;
        if (!create.isShowing()) {
            AlertDialog alertDialog = this.alert;
            alertDialog.show();
        }
    }

    private final boolean isAccessibilitySettingsOn(Context context) {
        int i;
        Settings.SettingNotFoundException e;
        String str = context.getPackageName() + "/com.gbsaver.chatlocker.services.AccessibilityServiceHandler";
        try {
            i = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), "accessibility_enabled");
            Log.v("TAG", "accessibilityEnabled = " + i);
        } catch (Settings.SettingNotFoundException e3) {
            e = e3;
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter2 = new TextUtils.SimpleStringSplitter(':');
        if (i == 1) {
            String string = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "enabled_accessibility_services");
            if (!TextUtils.isEmpty(string)) {
                simpleStringSplitter2.setString(string);
                while (simpleStringSplitter2.hasNext()) {
                    if (simpleStringSplitter2.next().equals(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private final void showAccessConsent() {
        Dialog dialog = new Dialog(this);
        this.accessbillityDialog = dialog;
        dialog.requestWindowFeature(1);
        Dialog dialog2 = this.accessbillityDialog;
        dialog2.setContentView(R.layout.accessibility_dialog_layout);
        Dialog dialog3 = this.accessbillityDialog;
        Window window = dialog3.getWindow();
        window.setBackgroundDrawableResource(17170445);
        Dialog dialog4 = this.accessbillityDialog;
        Window window2 = dialog4.getWindow();
        window2.setLayout(-1, -2);
        Dialog dialog5 = this.accessbillityDialog;
        dialog5.setCancelable(false);
        Dialog dialog6 = this.accessbillityDialog;
        View findViewById = dialog6.findViewById(R.id.checkBox);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.widget.CheckBox");
        CheckBox checkBox = (CheckBox) findViewById;
        Dialog dialog7 = this.accessbillityDialog;
        View findViewById2 = dialog7.findViewById(R.id.checkBoxWarning);
        Objects.requireNonNull(findViewById2, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) findViewById2;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    textView.setVisibility(8);
                }
            }
        });
        Dialog dialog8 = this.accessbillityDialog;
        View findViewById3 = dialog8.findViewById(R.id.privacyButton);
        Objects.requireNonNull(findViewById3, "null cannot be cast to non-null type android.widget.TextView");
        ((TextView) findViewById3).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://logisticwave.blogspot.com/p/privacy-policy.html")));
            }
        });
        Dialog dialog9 = this.accessbillityDialog;
        View findViewById4 = dialog9.findViewById(R.id.confirmButton);
        Objects.requireNonNull(findViewById4, "null cannot be cast to non-null type androidx.cardview.widget.CardView");
        ((CardView) findViewById4).setOnClickListener(new View.OnClickListener() { // from class: com.gbsaver.chatlocker.ui.HomeActivity$showAccessConsent$3

            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    textView.setVisibility(0);
                    return;
                }
                Dialog accessbillityDialog = getAccessbillityDialog();
                accessbillityDialog.dismiss();
                getPackageName();
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(268435456);
                startActivity(intent);
            }
        });
        ((CardView) dialog9.findViewById(R.id.anullerButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getAccessbillityDialog().dismiss();
            }
        });
        Dialog dialog10 = this.accessbillityDialog;
        if (!dialog10.isShowing()) {
            Dialog dialog11 = this.accessbillityDialog;
            dialog11.show();
        }
    }

    public final Dialog getAccessbillityDialog() {
        return this.accessbillityDialog;
    }

    public void mOnBackClick(View view) {
       onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(StartActivity.getInstance()!=null){
            StartActivity.getInstance().finish();
        }
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }
}