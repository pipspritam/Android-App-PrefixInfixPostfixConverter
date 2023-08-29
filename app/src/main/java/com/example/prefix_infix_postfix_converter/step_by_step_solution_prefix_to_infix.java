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
                if (prefix_input_exp.matches("")) {
                    textView_input.setText("No input string found.Enter Prefix String");
                }
                else {
                    textView_input.setText("Prefix: "+prefix_input_exp);
                    Stack<String> stack = new Stack<>();
                    int l = prefix_input_exp.length();
                    int j=0;
                    for(int i = l - 1; i >= 0; i--)
                    {
                        textView_solution.append("\n------------Step "+(j+1)+"------------\n");
                        j++;
                        char c=prefix_input_exp.charAt(i);
                        textView_solution.append("Character Scan: "+c+"\n");
                        if (!Character.isLetterOrDigit(c))
                        {
                            String op1 = stack.pop();
                            String op2 = stack.pop();
                            String temp = "(" + op1 + c + op2 + ")";
                            stack.push(temp);
                        }
                        else
                        {
                            stack.push(c + "");
                        }
                        textView_solution.append("Stack: "+stack+"\n");
                    }
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
            textView_input.setText("No input string found.Enter Prefix String");
            textView_solution.setText(null);
        }
    }
}