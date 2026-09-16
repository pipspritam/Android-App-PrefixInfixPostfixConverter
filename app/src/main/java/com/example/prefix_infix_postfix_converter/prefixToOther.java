package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class prefixToOther extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_prefix_input;
    private TextView textView_infix_output, textView_postfix_output;
    private Button sbss_infix, sbss_postfix;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefix_to_other);

        View prefixLayout = findViewById(R.id.prefix_layout);
        if (prefixLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(prefixLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
                return windowInsets;
            });
        }

        editText_prefix_input=findViewById(R.id.prefix_input);
        Button convertButton = findViewById(R.id.button_convert);
        Button reset = findViewById(R.id.button_reset);
        textView_infix_output=findViewById(R.id.infix_output);
        textView_postfix_output=findViewById(R.id.postfix_output);
        sbss_infix=findViewById(R.id.infix_sbss);
        sbss_postfix=findViewById(R.id.postfix_sbss);
        convertButton.setOnClickListener(this);
        sbss_infix.setOnClickListener(this);
        sbss_postfix.setOnClickListener(this);
        reset.setOnClickListener(this);

        MobileAds.initialize(this, initializationStatus -> {
        });
        setAds();

    }
    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.button_convert)
            {
                String prefix_input_exp = editText_prefix_input.getText().toString();
                if(prefix_input_exp.trim().isEmpty())
                {
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_infix_output.setText(R.string.dot_line);
                    Toast toast_message_prefix_to_other = Toast.makeText(prefixToOther.this, R.string.please_enter_input, Toast.LENGTH_SHORT);
                    toast_message_prefix_to_other.setGravity(Gravity.BOTTOM,0,200);
                    toast_message_prefix_to_other.show();
                }
                else {
                    Stack<String> s= new Stack<>();
                    int length = prefix_input_exp.length();
                    for (int i = length - 1; i >= 0; i--)
                    {
                        if (!Character.isLetterOrDigit(prefix_input_exp.charAt(i)))
                        {
                            String op1 = s.peek(); s.pop();
                            String op2 = s.peek(); s.pop();
                            String temp = op1 + op2 + prefix_input_exp.charAt(i);
                            s.push(temp);
                        }
                        else
                        {
                            s.push(String.valueOf(prefix_input_exp.charAt(i)));
                        }
                    }
                    String result_postfix = s.peek();
                    textView_postfix_output.setText(result_postfix);
                    //postfix to infix
                    Stack<String> s1 = new Stack<>();
                    for (int i = 0; i < result_postfix.length(); i++)
                    {
                        if (Character.isLetterOrDigit(result_postfix.charAt(i)))
                        {
                            s1.push(String.valueOf(result_postfix.charAt(i)));
                        }
                        else
                        {
                            String op1 = s1.peek();
                            s1.pop();
                            String op2 = s1.peek();
                            s1.pop();
                            s1.push("(" + op2 + result_postfix.charAt(i) + op1 + ")");
                        }
                    }
                    String result_infix =  s1.peek();
                    textView_infix_output.setText(result_infix);
                    sbss_postfix.setVisibility(View.VISIBLE);
                    sbss_infix.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId() == R.id.infix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_prefix_to_infix.class);
            }
            if (v.getId() == R.id.postfix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_prefix_to_postfix.class);
            }
            if(v.getId()==R.id.button_reset)
            {
                editText_prefix_input.setText(null);
                textView_infix_output.setText(R.string.dot_line);
                textView_postfix_output.setText(R.string.dot_line);
                sbss_postfix.setVisibility(View.GONE);
                sbss_infix.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            editText_prefix_input.setText(null);
            textView_infix_output.setText(R.string.dot_line);
            textView_postfix_output.setText(R.string.dot_line);
            Toast toast_message_prefix_to_other = Toast.makeText(prefixToOther.this,R.string.invalid_input,Toast.LENGTH_SHORT);
            toast_message_prefix_to_other.setGravity(Gravity.BOTTOM,0,200);
            toast_message_prefix_to_other.show();
            sbss_postfix.setVisibility(View.GONE);
            sbss_infix.setVisibility(View.GONE);
        }
    }

    private void showStepByStepSolution(Class<?> targetActivity) {
        String prefix_input_exp = editText_prefix_input.getText().toString();
        Intent intent_sbss = new Intent(prefixToOther.this, targetActivity);
        intent_sbss.putExtra("tag", prefix_input_exp);

        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    setAds();
                    startActivity(intent_sbss);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mInterstitialAd = null;
                    setAds();
                    startActivity(intent_sbss);
                }
            });
            mInterstitialAd.show(prefixToOther.this);
        } else {
            startActivity(intent_sbss);
        }
    }

    public void setAds () {
        AdRequest adRequest = new AdRequest.Builder().build();

        //ca-app-pub-3940256099942544/1033173712 sample
        // ca-app-pub-7769405161583944/1245851684 original
        InterstitialAd.load(this, "ca-app-pub-7769405161583944/1245851684", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });

    }
}

