package com.example.prefix_infix_postfix_converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdManager.initialize(this);

        View mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            int sidePad = (int) (16 * getResources().getDisplayMetrics().density);
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left + sidePad, insets.top, insets.right + sidePad, insets.bottom);
                return windowInsets;
            });
        }

        Button prefixToOtherButton = findViewById(R.id.prefix_button);
        Button infixToOtherButton = findViewById(R.id.infix_button);
        Button postfixToOtherButton = findViewById(R.id.postfix_button);
        prefixToOtherButton.setOnClickListener(this);
        postfixToOtherButton.setOnClickListener(this);
        infixToOtherButton.setOnClickListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });

        requestInAppReview();
    }

    private void requestInAppReview() {
        SharedPreferences prefs = getSharedPreferences("app_review_prefs", MODE_PRIVATE);
        int launchCount = prefs.getInt("launch_count", 0) + 1;
        boolean reviewPrompted = prefs.getBoolean("review_prompted", false);
        prefs.edit().putInt("launch_count", launchCount).apply();

        if (!reviewPrompted && launchCount >= 3) {
            ReviewManager manager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                    flow.addOnCompleteListener(flowTask -> {
                        prefs.edit().putBoolean("review_prompted", true).apply();
                    });
                }
            });
        }
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

    private void showExitDialog() {
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogbuilder.setTitle(R.string.alert_title);
        alertDialogbuilder.setMessage(R.string.alert_message);
        alertDialogbuilder.setCancelable(false);
        alertDialogbuilder.setPositiveButton("Yes", (dialog, which) -> finish());
        alertDialogbuilder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.show();
    }
}