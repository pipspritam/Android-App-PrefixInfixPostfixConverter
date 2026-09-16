package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_postfix_to_infix extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_postfix_to_infix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_infix_output = findViewById(R.id.infix_output_final);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String postfix_input_exp = bundle.getString("tag");
            try {
                assert postfix_input_exp != null;
                if (postfix_input_exp.trim().isEmpty()) {
                    textView_input.setText("No input string found. Enter Postfix String");
                    textView_solution.setText(null);
                }
                else {
                    int i;
                    textView_input.setText("Postfix: " + postfix_input_exp);
                    StringBuilder solutionBuilder = new StringBuilder();
                    Stack<String> s1 = new Stack<>();
                    for (i = 0; i < postfix_input_exp.length(); i++)
                    {
                        solutionBuilder.append("\n------------Step ").append(i + 1).append("------------\n");
                        char c=postfix_input_exp.charAt(i);
                        solutionBuilder.append("Character Scan: ").append(c).append("\n");
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
                        solutionBuilder.append("Infix Stack: ").append(s1).append("\n");
                    }
                    textView_solution.setText(solutionBuilder.toString());
                    String result_infix =  s1.peek();
                    textView_infix_output.setText("Infix: "+result_infix);
                }
            }catch(Exception e)
            {
                textView_input.setText(R.string.invalid_input);
                textView_solution.setText(null);
            }
        }
        else
        {
            textView_input.setText("No input string found. Enter Postfix String");
            textView_solution.setText(null);
        }
    }
}