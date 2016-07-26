package com.nodomain.callinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class CallReceiver extends BroadcastReceiver {

    private WindowManager windowManager;
    private ViewGroup windowLayout;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                showPhoneNumberInfo(context, phoneNumber);
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                closeInfo();
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                closeInfo();
            }
        }
    }

    private void showPhoneNumberInfo(Context context, String phoneNumber) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.RGBA_8888);

        params.gravity = Gravity.TOP;

        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.call_info, null);

        windowLayout.findViewById(R.id.yasnenko_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeInfo();
            }
        });

        TextView tvInfo = (TextView) windowLayout.findViewById(R.id.tv_info);

        windowManager.addView(windowLayout, params);

        new LoadCallInfoTask(tvInfo).execute(phoneNumber);
    }

    private void closeInfo() {
        if (windowManager != null) {
            windowManager.removeView(windowLayout);
            windowLayout = null;
        }
    }

}
