package com.example.prefix_infix_postfix_converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Stack;

public class postfixToOther extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_postfix_input;
    private TextView textView_infix_output, textView_prefix_output;
    private Button sbss_infix, sbss_prefix;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postfix_to_other);

        View postfixLayout = findViewById(R.id.postfix_layout);
        if (postfixLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(postfixLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
                return windowInsets;
            });
        }

        editText_postfix_input=findViewById(R.id.postfix_input);
        Button convertButton = findViewById(R.id.button_convert);
        Button reset = findViewById(R.id.button_reset);
        textView_infix_output=findViewById(R.id.infix_output);
        textView_prefix_output=findViewById(R.id.prefix_output);
        sbss_infix=findViewById(R.id.infix_sbss);
        sbss_prefix=findViewById(R.id.prefix_sbss);
        convertButton.setOnClickListener(this);
        reset.setOnClickListener(this);
        sbss_infix.setOnClickListener(this);
        sbss_prefix.setOnClickListener(this);

        MobileAds.initialize(this, initializationStatus -> {
        });
        setAds();

    }

    static int getPriority(char C)
    {
        if (C == '-' || C == '+')
            return 1;
        else if (C == '*' || C == '/' || C == '%')
            return 2;
        else if (C == '^' || C=='$')
            return 3;
        return 0;
    }

    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.button_convert)
            {
                String postfix_input_exp = editText_postfix_input.getText().toString();
                if(postfix_input_exp.trim().isEmpty()) {
                    textView_prefix_output.setText(R.string.dot_line);
                    textView_infix_output.setText(R.string.dot_line);
                    Toast toast_message_postfix_to_other = Toast.makeText(postfixToOther.this, R.string.please_enter_input, Toast.LENGTH_SHORT);
                    toast_message_postfix_to_other.setGravity(Gravity.BOTTOM, 0, 200);
                    toast_message_postfix_to_other.show();
                }
                else
                {
                    Stack<String> s1 = new Stack<>();
                    for (int i = 0; i < postfix_input_exp.length(); i++)
                    {
                        if (Character.isLetterOrDigit(postfix_input_exp.charAt(i)))
                        {
                            s1.push(String.valueOf(postfix_input_exp.charAt(i)));
                        }
                        else {
                            String op1 = s1.peek();
                            s1.pop();
                            String op2 = s1.peek();
                            s1.pop();
                            s1.push("(" + op2 + postfix_input_exp.charAt(i) + op1 + ")");
                        }
                    }
                    String result_infix =  s1.peek();
                    textView_infix_output.setText(result_infix);
                    sbss_infix.setVisibility(View.VISIBLE);
                    //infix to prefix
                    Stack<Character> operators = new Stack<>();
                    Stack<String> operands = new Stack<>();
                    for (int i = 0; i < result_infix.length(); i++)
                    {
                        if (result_infix.charAt(i) == '(')
                        {
                            operators.push(result_infix.charAt(i));
                        }
                        else if (result_infix.charAt(i) == ')')
                        {
                            while (!operators.empty() &&
                                    operators.peek() != '(')
                            {
                                String op1 = operands.peek();
                                operands.pop();
                                String op2 = operands.peek();
                                operands.pop();
                                char op = operators.peek();
                                operators.pop();
                                String tmp = op + op2 + op1;
                                operands.push(tmp);
                            }
                            operators.pop();
                        }
                        else if (Character.isLetterOrDigit(result_infix.charAt(i)))
                        {
                            operands.push(String.valueOf(result_infix.charAt(i)));
                        }
                        else
                        {
                            while (!operators.empty() && getPriority(result_infix.charAt(i)) <= getPriority(operators.peek()))
                            {
                                String op1 = operands.peek();
                                operands.pop();
                                String op2 = operands.peek();
                                operands.pop();
                                char op = operators.peek();
                                operators.pop();
                                String tmp = op + op2 + op1;
                                operands.push(tmp);
                            }
                            operators.push(result_infix.charAt(i));
                        }
                    }
                    while (!operators.empty())
                    {
                        String op1 = operands.peek();
                        operands.pop();
                        String op2 = operands.peek();
                        operands.pop();
                        char op = operators.peek();
                        operators.pop();
                        String tmp = op + op2 + op1;
                        operands.push(tmp);
                    }
                    String result_prefix= operands.peek();
                    textView_prefix_output.setText(result_prefix);
                    sbss_prefix.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId() == R.id.infix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_postfix_to_infix.class);
            }
            if (v.getId() == R.id.prefix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_postfix_to_prefix.class);
            }
            if(v.getId()==R.id.button_reset)
            {
                editText_postfix_input.setText(null);
                textView_infix_output.setText(R.string.dot_line);
                textView_prefix_output.setText(R.string.dot_line);
                sbss_infix.setVisibility(View.GONE);
                sbss_prefix.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            editText_postfix_input.setText(null);
            textView_infix_output.setText(R.string.dot_line);
            textView_prefix_output.setText(R.string.dot_line);
            Toast toast_message_postfix_to_other = Toast.makeText(postfixToOther.this,R.string.invalid_input,Toast.LENGTH_SHORT);
            toast_message_postfix_to_other.setGravity(Gravity.BOTTOM,0,200);
            toast_message_postfix_to_other.show();
            sbss_infix.setVisibility(View.GONE);
            sbss_prefix.setVisibility(View.GONE);
        }
    }

    private void showStepByStepSolution(Class<?> targetActivity) {
        String postfix_input_exp = editText_postfix_input.getText().toString();
        Intent intent_sbss = new Intent(postfixToOther.this, targetActivity);
        intent_sbss.putExtra("tag", postfix_input_exp);

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
            mInterstitialAd.show(postfixToOther.this);
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
