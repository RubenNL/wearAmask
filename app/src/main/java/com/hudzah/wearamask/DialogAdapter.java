package com.hudzah.wearamask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public enum DialogAdapter {
    ADAPTER;

    private Activity activity;
    private AlertDialog dialog;
    private AlertDialog errorDialog;
    LayoutInflater inflater;

    String name = "";




    public void initDialogAdapter(Activity myActivity){
        activity = myActivity;
        inflater = activity.getLayoutInflater();


    }

    public void loadingDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(false);

        Log.d("Tag", "loadingDialog: coming from " + activity);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismissLoadingDialog(){

        dialog.dismiss();
        Log.d("Tag", "dismissLoadingDialog: called");
    }

    public void locationFindingDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        builder.setView(inflater.inflate(R.layout.dialog_location_finding, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void dismissLocationDialog(){
        Log.d("Tag", "dismissLocationDialog: dismiss location dialog");
        dialog.dismiss();
    }

    public void displayErrorDialog(final String error, final String buttonText){


        if(errorDialog != null && errorDialog.isShowing()){
            Log.d("Tag", "displayErrorDialog: already showing");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(inflater.inflate(R.layout.dialog_error, null));
        errorDialog = builder.create();
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.show();
        Log.d("Tag", "displayErrorDialog: dialog id at start, " + errorDialog.toString());
        TextView errorTextView = (TextView) errorDialog.findViewById(R.id.errorTextView);
        errorTextView.setText(error);

        Button actionButton = (Button) errorDialog.findViewById(R.id.actionButton);
        ImageView closeDialogButton = (ImageView) errorDialog.findViewById(R.id.closeDialogButton);
        if(!buttonText.equals("")) actionButton.setText(buttonText);

        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag", "onClick: close error dialog, " + errorDialog.toString());
                try {
                    dismissErrorDialog();
                } catch (Exception e){
                    Log.d("Tag", "onClick: error is + " + e.getMessage());
                }
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(buttonText.equals("")) {
                dismissErrorDialog();
            }
            else if(buttonText.equals(activity.getApplicationContext().getResources().getString(R.string.dialog_enable_location_button))){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getApplicationContext().startActivity(intent);
                dismissErrorDialog();
            }
        }
    });

    }

    public void dismissErrorDialog(){
        if(errorDialog != null) {
            errorDialog.cancel();
        }
    }

    public void displaySafeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        builder.setView(inflater.inflate(R.layout.dialog_safe, null));

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button closeDialogButton = (Button) dialog.findViewById(R.id.closeDialogButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSafeAndWarningDialog();
            }
        });
    }

    public void displayWarningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        builder.setView(inflater.inflate(R.layout.dialog_warning, null));

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageView closeDialogButton = (ImageView) dialog.findViewById(R.id.closeDialogButton);
        Button actionButton = (Button) dialog.findViewById(R.id.actionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(activity, R.id.fragment);
                navController.navigate(R.id.precautionsFragment);
                dismissSafeAndWarningDialog();

            }
        });

        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSafeAndWarningDialog();
            }
        });
    }

    public void dismissSafeAndWarningDialog(){
        dialog.dismiss();
    }

}
