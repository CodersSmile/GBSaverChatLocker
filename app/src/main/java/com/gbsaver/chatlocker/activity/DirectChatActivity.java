package com.gbsaver.chatlocker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gbsaver.chatlocker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.gbsaver.chatlocker.AdsUtils.Utils.Constants;
import com.gbsaver.chatlocker.R;
import com.gbsaver.chatlocker.base.BaseActivity;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import butterknife.BindView;
import butterknife.OnClick;

//import com.gbsaver.chatlocker.library.CountryCodePicker.CountryCodePicker;

public class DirectChatActivity extends BaseActivity {
    @BindView(R.id.spin)
    CountryCodePicker spin;
    @BindView(R.id.edit_number)
    EditText editNumber;
    @BindView(R.id.text_mess)
    EditText text_mess;
    String code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_direct_chat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        spin.setDefaultCountryUsingNameCode("in");
        AdUtils.showNativeAd(DirectChatActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), true);

    }

    @OnClick({R.id.back_btn, R.id.send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.send_btn:
                callbackCall();
                break;
        }
    }

    public void callbackCall() {
        code = spin.getSelectedCountryCode();

        if (code.isEmpty() || code.equals("")) {
            Toast.makeText(getApplicationContext(), "please select county code!", Toast.LENGTH_SHORT).show();
        } else if (editNumber.getText().toString().isEmpty() || editNumber.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "please enter number!", Toast.LENGTH_SHORT).show();
        } else if (text_mess.getText().toString().isEmpty() || text_mess.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "please select message!", Toast.LENGTH_SHORT).show();
        } else {
            spin.registerPhoneNumberTextView(editNumber);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + spin.getFullNumberWithPlus() + "&text=" + text_mess.getText().toString())));
        }
    }
}
