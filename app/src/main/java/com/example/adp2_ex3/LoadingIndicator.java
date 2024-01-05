package com.example.adp2_ex3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class LoadingIndicator {
    private final Dialog dialog;
    public LoadingIndicator(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progress_indicator);
        dialog=builder.create();
    }
    public void setDialog(boolean show){
        if (show)dialog.show();
        else dialog.dismiss();
    }
}
