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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

public class postfixToOther extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_postfix_input;
    private TextView textView_infix_output, textView_prefix_output;
    private Button sbss_infix, sbss_prefix;
    private String lastConvertedInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postfix_to_other);

        View postfixLayout = findViewById(R.id.postfix_layout);
        if (postfixLayout != null) {
            int sidePad = (int) (16 * getResources().getDisplayMetrics().density);
            ViewCompat.setOnApplyWindowInsetsListener(postfixLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left + sidePad, insets.top, insets.right + sidePad, insets.bottom);
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

        AdManager.loadInterstitial(this);

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
                String postfix_input_exp = editText_postfix_input.getText().toString().trim();
                if(postfix_input_exp.isEmpty()) {
                    textView_prefix_output.setText(R.string.dot_line);
                    textView_infix_output.setText(R.string.dot_line);
                    sbss_infix.setVisibility(View.GONE);
                    sbss_prefix.setVisibility(View.GONE);
                    Toast.makeText(postfixToOther.this, R.string.please_enter_input, Toast.LENGTH_SHORT).show();
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
                    if (s1.size() != 1) {
                        throw new IllegalArgumentException("Invalid postfix expression");
                    }
                    String result_infix =  s1.peek();
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
                    if (operands.size() != 1) {
                        throw new IllegalArgumentException("Invalid postfix expression");
                    }
                    String result_prefix= operands.peek();
                    lastConvertedInput = postfix_input_exp;
                    textView_infix_output.setText(result_infix);
                    textView_prefix_output.setText(result_prefix);
                    sbss_infix.setVisibility(View.VISIBLE);
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
                hideKeyboard();
                lastConvertedInput = "";
                editText_postfix_input.setText(null);
                textView_infix_output.setText(R.string.dot_line);
                textView_prefix_output.setText(R.string.dot_line);
                sbss_infix.setVisibility(View.GONE);
                sbss_prefix.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            lastConvertedInput = "";
            editText_postfix_input.setText(null);
            textView_infix_output.setText(R.string.dot_line);
            textView_prefix_output.setText(R.string.dot_line);
            Toast.makeText(postfixToOther.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            sbss_infix.setVisibility(View.GONE);
            sbss_prefix.setVisibility(View.GONE);
        }
    }

    private void showStepByStepSolution(Class<?> targetActivity) {
        String postfix_input_exp = lastConvertedInput.isEmpty() ? editText_postfix_input.getText().toString().trim() : lastConvertedInput;
        Intent intent_sbss = new Intent(postfixToOther.this, targetActivity);
        intent_sbss.putExtra("tag", postfix_input_exp);
        AdManager.showInterstitialIfReady(postfixToOther.this, () -> startActivity(intent_sbss));
    }
}
