package com.example.prefix_infix_postfix_converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button prefixToOtherButton = findViewById(R.id.prefix_button);
        Button infixToOtherButton = findViewById(R.id.infix_button);
        Button postfixToOtherButton = findViewById(R.id.postfix_button);
        prefixToOtherButton.setOnClickListener(this);
        postfixToOtherButton.setOnClickListener(this);
        infixToOtherButton.setOnClickListener(this);

        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.prefix_button)
        {
            Intent intent_prefix = new Intent(MainActivity.this,prefixToOther.class);
            startActivity(intent_prefix);
        }
        if(v.getId()==R.id.infix_button)
        {
            Intent intent_infix = new Intent(MainActivity.this,infixToOther.class);
            startActivity(intent_infix);
        }
        if(v.getId()==R.id.postfix_button)
        {
            Intent intent_postfix = new Intent(MainActivity.this,postfixToOther.class);
            startActivity(intent_postfix);
        }
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialogbuilder;
        alertDialogbuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogbuilder.setTitle(R.string.alert_title);
        alertDialogbuilder.setMessage(R.string.alert_massage);
        alertDialogbuilder.setCancelable(false);
        alertDialogbuilder.setPositiveButton("Yes", (dialog, which) -> finish());
        alertDialogbuilder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.show();
    }
}