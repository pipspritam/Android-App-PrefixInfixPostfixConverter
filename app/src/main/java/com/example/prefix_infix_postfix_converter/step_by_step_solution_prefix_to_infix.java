package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_prefix_to_infix extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_prefix_to_infix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_infix_output = findViewById(R.id.infix_output_final);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String prefix_input_exp = bundle.getString("tag");
            try {
                assert prefix_input_exp != null;
                if (prefix_input_exp.trim().isEmpty()) {
                    textView_input.setText("No input string found. Enter Prefix String");
                    textView_solution.setText(null);
                }
                else {
                    textView_input.setText("Prefix: "+prefix_input_exp);
                    StringBuilder solutionBuilder = new StringBuilder();
                    Stack<String> stack = new Stack<>();
                    int l = prefix_input_exp.length();
                    int j=0;
                    for(int i = l - 1; i >= 0; i--)
                    {
                        solutionBuilder.append("\n------------Step ").append(j + 1).append("------------\n");
                        j++;
                        char c=prefix_input_exp.charAt(i);
                        solutionBuilder.append("Character Scan: ").append(c).append("\n");
                        if (!Character.isLetterOrDigit(c))
                        {
                            String op1 = stack.pop();
                            String op2 = stack.pop();
                            String temp = "(" + op1 + c + op2 + ")";
                            stack.push(temp);
                        }
                        else
                        {
                            stack.push(String.valueOf(c));
                        }
                        solutionBuilder.append("Stack: ").append(stack).append("\n");
                    }
                    textView_solution.setText(solutionBuilder.toString());
                    String infix_output = stack.pop();
                    textView_infix_output.setText("Infix: "+infix_output);
                }
            }catch(Exception e)
            {
                textView_input.setText(R.string.invalid_input);
                textView_solution.setText(null);
            }
        }
        else
        {
            textView_input.setText("No input string found. Enter Prefix String");
            textView_solution.setText(null);
        }
    }
}