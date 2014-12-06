package com.cyou.mrd.pengyou.widget;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;

public class CYMenuDialog {

    public static void show(Context context, List<DialogItem> items) {
        LinearLayout dialogView = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.menu_dialog_layout, null);
        final Dialog menuDialog = new Dialog(context, R.style.CYMenuDialog);
        LinearLayout itemView;
        for (DialogItem item : items) {
            itemView = (LinearLayout) LayoutInflater.from(context).inflate(
                    item.getViewId(), null);
            dialogView.addView(itemView);
        }

        WindowManager.LayoutParams localLayoutParams = menuDialog.getWindow()
                .getAttributes();
        localLayoutParams.x = 0;
        localLayoutParams.y = -1000;
        localLayoutParams.gravity = 80;
        dialogView.setMinimumWidth(10000);

        menuDialog.onWindowAttributesChanged(localLayoutParams);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.setCancelable(true);
        menuDialog.setContentView(dialogView);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                menuDialog.show();
            }
        }
    }
}

