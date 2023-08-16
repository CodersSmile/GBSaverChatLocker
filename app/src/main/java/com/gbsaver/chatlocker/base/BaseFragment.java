package com.gbsaver.chatlocker.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    public PrefManager prefmanager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = on_ViewCreated(inflater.inflate(getLayoutId(), container, false));
        prefmanager = new PrefManager(getContext());

        onViewCreated(view);
        return view;
    }

    private View on_ViewCreated(View view) {
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract View onViewCreated(View inflate);

    protected abstract int getLayoutId();

//    protected abstract View onViewCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void openActivity(Class destClass) {
        startActivity(new Intent(getContext(), destClass));
    }

    public void openTopActivity(Class destClass) {
        startActivity(new Intent(getContext(), destClass).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK));
    }

  /*  public void openTopActivity(Intent Intent) {

        startActivity(Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK));
    }

   */

    public static boolean isNullStr(String Val) {
        if (Val == null || Val.isEmpty() || Val.equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    public String checkNullStr(String Val) {
        if (Val == null || Val.isEmpty())
            return "";
        else
            return Val;
    }

    public int checkNullInt(String intVal) {
        if (intVal == null || intVal.isEmpty())
            return 0;
        else
            return Integer.parseInt(intVal);
    }

    public long checkNullLong(String intVal) {
        if (intVal == null || intVal.isEmpty())
            return 0;
        else
            return Long.parseLong(intVal);
    }



}