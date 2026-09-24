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


public class infixToOther extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_infix_input;
    private TextView textView_prefix_output, textView_postfix_output;
    private Button sbss_postfix, sbss_prefix;
    private String lastConvertedInput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infix_to_other);

        View infixLayout = findViewById(R.id.infix_layout);
        if (infixLayout != null) {
            int sidePad = (int) (16 * getResources().getDisplayMetrics().density);
            ViewCompat.setOnApplyWindowInsetsListener(infixLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left + sidePad, insets.top, insets.right + sidePad, insets.bottom);
                return windowInsets;
            });
        }

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

        AdManager.loadInterstitial(this);

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
                String infix_input_exp = editText_infix_input.getText().toString().trim();
                if(infix_input_exp.isEmpty())
                {
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_prefix_output.setText(R.string.dot_line);
                    sbss_postfix.setVisibility(View.GONE);
                    sbss_prefix.setVisibility(View.GONE);
                    Toast.makeText(infixToOther.this, R.string.please_enter_input, Toast.LENGTH_SHORT).show();
                }
                else if(!Character.isLetterOrDigit(infix_input_exp.charAt(0)) && !(infix_input_exp.charAt(0)=='('))
                {
                    editText_infix_input.setText(null);
                    textView_postfix_output.setText(R.string.dot_line);
                    textView_prefix_output.setText(R.string.dot_line);
                    sbss_postfix.setVisibility(View.GONE);
                    sbss_prefix.setVisibility(View.GONE);
                    Toast.makeText(infixToOther.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
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
                        char popped = stack.pop();
                        if (popped == '(') {
                            throw new IllegalArgumentException("Unmatched parenthesis");
                        }
                        result_postfix.append(popped);
                    }
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
                            operands.push(String.valueOf(infix_input_exp.charAt(i)));
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
                    if (operands.size() != 1) {
                        throw new IllegalArgumentException("Invalid infix expression");
                    }
                    String result_prefix= operands.peek();
                    lastConvertedInput = infix_input_exp;
                    textView_postfix_output.setText(result_postfix.toString());
                    textView_prefix_output.setText(result_prefix);
                    sbss_postfix.setVisibility(View.VISIBLE);
                    sbss_prefix.setVisibility(View.VISIBLE);
                }
            }
            if (v.getId() == R.id.postfix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_infix_to_postfix.class);
            }
            if (v.getId() == R.id.prefix_sbss)
            {
                showStepByStepSolution(step_by_step_solution_infix_to_prefix.class);
            }
            if(v.getId()==R.id.button_reset)
            {
                hideKeyboard();
                lastConvertedInput = "";
                sbss_postfix.setVisibility(View.GONE);
                sbss_prefix.setVisibility(View.GONE);
                editText_infix_input.setText(null);
                textView_prefix_output.setText(R.string.dot_line);
                textView_postfix_output.setText(R.string.dot_line);
            }
        }
        catch (Exception e)
        {
            lastConvertedInput = "";
            editText_infix_input.setText(null);
            textView_prefix_output.setText(R.string.dot_line);
            textView_postfix_output.setText(R.string.dot_line);
            Toast.makeText(infixToOther.this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            sbss_postfix.setVisibility(View.GONE);
            sbss_prefix.setVisibility(View.GONE);
        }
    }

    private void showStepByStepSolution(Class<?> targetActivity) {
        String infix_input_exp = lastConvertedInput.isEmpty() ? editText_infix_input.getText().toString().trim() : lastConvertedInput;
        Intent intent_sbss = new Intent(infixToOther.this, targetActivity);
        intent_sbss.putExtra("tag", infix_input_exp);
        AdManager.showInterstitialIfReady(infixToOther.this, () -> startActivity(intent_sbss));
    }
}