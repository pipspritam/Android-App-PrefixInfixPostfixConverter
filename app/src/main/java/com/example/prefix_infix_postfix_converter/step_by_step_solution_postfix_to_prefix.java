package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_postfix_to_prefix extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_postfix_to_prefix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_prefix_output = findViewById(R.id.prefix_output_final);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String postfix_input_exp = bundle.getString("tag");
            try {
                if (postfix_input_exp.matches("")) {
                    textView_input.setText("No input string found.Enter Postfix String");
                }
                else {
                    textView_input.setText("Postfix: " + postfix_input_exp);
                    Stack<String> s = new Stack<>();
                    int length = postfix_input_exp.length();
                    int i;
                    for (i = 0; i < length; i++) {
                        textView_solution.append("\n------------Step "+(i+1)+"------------\n");
                        char c=postfix_input_exp.charAt(i);
                        textView_solution.append("Character Scan: "+c+"\n");
                        if (!Character.isLetterOrDigit(postfix_input_exp.charAt(i))) {
                            String op1 = s.peek();
                            s.pop();
                            String op2 = s.peek();
                            s.pop();
                            String temp = postfix_input_exp.charAt(i) + op2 + op1;
                            s.push(temp);
                        }
                        else {
                            s.push(postfix_input_exp.charAt(i) + "");
                        }
                        textView_solution.append("Stack: "+s+"\n");
                    }
                    StringBuilder ans = new StringBuilder();
                    for (String j : s)
                        ans.append(j);
                    textView_prefix_output.setText("Prefix: "+ans.toString());
                }
            }catch(Exception e)
            {
                textView_input.setText(R.string.invalid_input);
                textView_solution.setText(null);
            }
        }
        else
        {
            textView_input.setText("No input string found.Enter Postfix String");
            textView_solution.setText(null);
        }
    }
}