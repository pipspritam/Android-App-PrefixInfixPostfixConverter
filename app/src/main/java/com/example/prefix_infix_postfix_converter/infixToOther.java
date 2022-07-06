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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Stack;

//ADs


public class infixToOther extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_infix_input;
    private TextView textView_prefix_output, textView_postfix_output;
    private Button sbss_postfix, sbss_prefix;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infix_to_other);
        editText_infix_input=findViewById(R.id.infix_input);
        Button convertButton = findViewById(R.id.button_convert);
        Button reset = findViewById(R.id.button_reset);
        sbss_postfix = findViewById(R.id.postfix_sbss);
        sbss_prefix=findViewById(R.id.prefix_sbss);
        textView_prefix_output=findViewById(R.id.prefix_output);
        textView_postfix_output=findViewById(R.id.postfix_output);
        convertButton.setOnClickListener(this);
        reset.setOnClickListener(this);
        sbss_postfix.setOnClickListener(this);
        sbss_prefix.setOnClickListener(this);


        MobileAds.initialize(this, initializationStatus -> {
        });
        setAds();

    }
    static int Priority(char ch)
    {
        switch (ch)
        {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
            case '$':
                return 3;
        }
        return -1;
    }
    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.button_convert)
            {
                String infix_input_exp = editText_infix_input.getText().toString();
                if(infix_input_exp.matches(""))
                {
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_prefix_output.setText(R.string.dot_line);
                    Toast toast_massage_infix_to_other = Toast.makeText(infixToOther.this,"Please Enter Input",Toast.LENGTH_SHORT);
                    toast_massage_infix_to_other.setGravity(Gravity.BOTTOM,0,200);
                    toast_massage_infix_to_other.show();
                }
                else if(!Character.isLetterOrDigit(infix_input_exp.charAt(0)) && !(infix_input_exp.charAt(0)=='('))
                {
                    editText_infix_input.setText(null);
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_prefix_output.setText(R.string.dot_line);
                    Toast toast_massage_infix_to_other = Toast.makeText(infixToOther.this,R.string.invalid_input,Toast.LENGTH_SHORT);
                    toast_massage_infix_to_other.setGravity(Gravity.BOTTOM,0,200);
                    toast_massage_infix_to_other.show();
                }
                else {
                    StringBuilder result_postfix = new StringBuilder();
                    Stack<Character> stack = new Stack<>();
                    for (int i = 0; i<infix_input_exp.length(); ++i)
                    {
                        char c = infix_input_exp.charAt(i);
                        if (Character.isLetterOrDigit(c))
                            result_postfix.append(c);
                        else if (c == '(')
                            stack.push(c);
                        else if (c == ')')
                        {
                            while (!stack.isEmpty() && stack.peek() != '(')
                                result_postfix.append(stack.pop());
                            stack.pop();
                        }
                        else
                        {
                            while(!stack.isEmpty() && (Priority(c)< Priority(stack.peek()) || (Priority(c)==Priority(stack.peek())&& (c!='^'&&c!='$'))))
                            {
                                result_postfix.append(stack.pop());
                            }
                            stack.push(c);
                        }
                    }
                    while (!stack.isEmpty()){
                        result_postfix.append(stack.pop());
                    }
                    textView_postfix_output.setText(result_postfix.toString());
                    //infix to prefix
                    Stack<Character> operators = new Stack<>();
                    Stack<String> operands = new Stack<>();
                    for (int i = 0; i < infix_input_exp.length(); i++)
                    {
                        if (infix_input_exp.charAt(i) == '(')
                        {
                            operators.push(infix_input_exp.charAt(i));
                        }
                        else if (infix_input_exp.charAt(i) == ')')
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
                        else if (Character.isLetterOrDigit(infix_input_exp.charAt(i)))
                        {
                            operands.push(infix_input_exp.charAt(i) + "");
                        }
                        else
                        {
                            while (!operators.empty() && (Priority(infix_input_exp.charAt(i))< Priority(operators.peek()) || (Priority(infix_input_exp.charAt(i))==Priority(operators.peek())&& (infix_input_exp.charAt(i)!='^'&&infix_input_exp.charAt(i)!='$'))))
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
                            operators.push(infix_input_exp.charAt(i));
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
                    sbss_postfix.setVisibility(View.VISIBLE);
                    sbss_prefix.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId()==R.id.postfix_sbss)
            {

                if(mInterstitialAd!=null) {
                    mInterstitialAd.show(infixToOther.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            String infix_input_exp = editText_infix_input.getText().toString();
                            Intent intent_sbss = new Intent(infixToOther.this, step_by_step_solution_infix_to_postfix.class);
                            intent_sbss.putExtra("tag",infix_input_exp);
                            startActivity(intent_sbss);
                            mInterstitialAd =null;
                            setAds();
                        }
                    });
                }
                else {

                    String infix_input_exp = editText_infix_input.getText().toString();
                    Intent intent_sbss = new Intent(infixToOther.this, step_by_step_solution_infix_to_postfix.class);
                    intent_sbss.putExtra("tag",infix_input_exp);
                    startActivity(intent_sbss);
                }


            }
            if (v.getId()==R.id.prefix_sbss)
            {

                if(mInterstitialAd!=null) {
                    mInterstitialAd.show(infixToOther.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            String infix_input_exp = editText_infix_input.getText().toString();
                            Intent intent_sbss = new Intent(infixToOther.this, step_by_step_solution_infix_to_prefix.class);
                            intent_sbss.putExtra("tag",infix_input_exp);
                            startActivity(intent_sbss);
                            mInterstitialAd =null;
                            setAds();
                        }
                    });
                }

                else  {
                    String infix_input_exp = editText_infix_input.getText().toString();
                    Intent intent_sbss = new Intent(infixToOther.this, step_by_step_solution_infix_to_prefix.class);
                    intent_sbss.putExtra("tag",infix_input_exp);
                    startActivity(intent_sbss);
                }



            }
            if(v.getId()==R.id.button_reset)
            {
                sbss_postfix.setVisibility(View.GONE);
                sbss_prefix.setVisibility(View.GONE);
                editText_infix_input.setText(null);
                textView_prefix_output.setText(R.string.dot_line);
                textView_postfix_output.setText(R.string.dot_line);
            }
        }
        catch (Exception e)
        {
            editText_infix_input.setText(null);
            textView_prefix_output.setText(R.string.dot_line);
            textView_postfix_output.setText(R.string.dot_line);
            Toast toast_massage_infix_to_other = Toast.makeText(infixToOther.this,R.string.invalid_input,Toast.LENGTH_SHORT);
            toast_massage_infix_to_other.setGravity(Gravity.BOTTOM,0,200);
            toast_massage_infix_to_other.show();
            sbss_postfix.setVisibility(View.GONE);
            sbss_prefix.setVisibility(View.GONE);
        }
    }


    public void setAds () {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-7769405161583944/1245851684", adRequest,
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