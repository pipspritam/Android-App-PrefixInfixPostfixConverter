package com.example.prefix_infix_postfix_converter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Stack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class prefixToOther extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_prefix_input;
    private TextView textView_infix_output, textView_postfix_output;
    private Button sbss_infix, sbss_postfix;
    private String lastConvertedInput = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefix_to_other);

        View prefixLayout = findViewById(R.id.prefix_layout);
        if (prefixLayout != null) {
            int sidePad = (int) (16 * getResources().getDisplayMetrics().density);
            ViewCompat.setOnApplyWindowInsetsListener(prefixLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left + sidePad, insets.top, insets.right + sidePad, insets.bottom);
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

        AdManager.loadInterstitial(this);

    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if(v.getId()==R.id.button_convert)
            {
                hideKeyboard();
                String prefix_input_exp = editText_prefix_input.getText().toString().trim();
                if(prefix_input_exp.isEmpty())
                {
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_infix_output.setText(R.string.dot_line);
                    sbss_postfix.setVisibility(View.GONE);
                    sbss_infix.setVisibility(View.GONE);
                    Toast.makeText(prefixToOther.this, R.string.please_enter_input, Toast.LENGTH_SHORT).show();
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
                    if (s.size() != 1) {
                        throw new IllegalArgumentException("Invalid prefix expression");
                    }
                    String result_postfix = s.peek();
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
                    if (s1.size() != 1) {
                        throw new IllegalArgumentException("Invalid prefix expression");
                    }
                    String result_infix =  s1.peek();
                    lastConvertedInput = prefix_input_exp;
                    textView_postfix_output.setText(result_postfix);
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
                hideKeyboard();
                lastConvertedInput = "";
                editText_prefix_input.setText(null);
                textView_infix_output.setText(R.string.dot_line);
                textView_postfix_output.setText(R.string.dot_line);
                sbss_postfix.setVisibility(View.GONE);
                sbss_infix.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            lastConvertedInput = "";
            editText_prefix_input.setText(null);
            textView_infix_output.setText(R.string.dot_line);
            textView_postfix_output.setText(R.string.dot_line);
            Toast.makeText(prefixToOther.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            sbss_postfix.setVisibility(View.GONE);
            sbss_infix.setVisibility(View.GONE);
        }
    }

    private void showStepByStepSolution(Class<?> targetActivity) {
        String prefix_input_exp = lastConvertedInput.isEmpty() ? editText_prefix_input.getText().toString().trim() : lastConvertedInput;
        Intent intent_sbss = new Intent(prefixToOther.this, targetActivity);
        intent_sbss.putExtra("tag", prefix_input_exp);
        AdManager.showInterstitialIfReady(prefixToOther.this, () -> startActivity(intent_sbss));
    }
}

