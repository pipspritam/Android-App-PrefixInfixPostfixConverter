package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_prefix_to_postfix extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_prefix_to_postfix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_postfix_output = findViewById(R.id.postfix_output_final);
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
                    Stack<String> s= new Stack<>();
                    int length = prefix_input_exp.length();
                    int j=0;
                    for (int i = length - 1; i >= 0; i--)
                    {
                        textView_solution.append("\n------------Step "+(j+1)+"------------\n");
                        j++;
                        char c=prefix_input_exp.charAt(i);
                        textView_solution.append("Character Scan: "+c+"\n");
                        if (!Character.isLetterOrDigit(prefix_input_exp.charAt(i)))
                        {
                            String op1 = s.peek(); s.pop();
                            String op2 = s.peek(); s.pop();
                            String temp = op1 + op2 + prefix_input_exp.charAt(i);
                            s.push(temp);
                        }
                        else
                        {
                            s.push( prefix_input_exp.charAt(i)+"");
                        }
                        textView_solution.append("Postfix Stack: "+s+"\n");
                    }
                    String result_postfix = s.peek();
                    textView_postfix_output.setText("Postfix: "+result_postfix);
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