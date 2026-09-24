package com.example.prefix_infix_postfix_converter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_postfix_to_prefix extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_postfix_to_prefix);
        View sbssLayout = findViewById(R.id.sbss_layout);
        if (sbssLayout != null) {
            int sidePad = (int) (16 * getResources().getDisplayMetrics().density);
            ViewCompat.setOnApplyWindowInsetsListener(sbssLayout, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(insets.left + sidePad, insets.top, insets.right + sidePad, insets.bottom);
                return windowInsets;
            });
        }
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_prefix_output = findViewById(R.id.prefix_output_final);
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
                    textView_input.setText("Postfix: " + postfix_input_exp);
                    StringBuilder solutionBuilder = new StringBuilder();
                    Stack<String> s = new Stack<>();
                    int length = postfix_input_exp.length();
                    int i;
                    for (i = 0; i < length; i++) {
                        solutionBuilder.append("\n------------Step ").append(i + 1).append("------------\n");
                        char c=postfix_input_exp.charAt(i);
                        solutionBuilder.append("Character Scan: ").append(c).append("\n");
                        if (!Character.isLetterOrDigit(postfix_input_exp.charAt(i))) {
                            String op1 = s.peek();
                            s.pop();
                            String op2 = s.peek();
                            s.pop();
                            String temp = postfix_input_exp.charAt(i) + op2 + op1;
                            s.push(temp);
                        }
                        else {
                            s.push(String.valueOf(postfix_input_exp.charAt(i)));
                        }
                        solutionBuilder.append("Stack: ").append(s).append("\n");
                    }
                    textView_solution.setText(solutionBuilder.toString());
                    StringBuilder ans = new StringBuilder();
                    for (String j : s)
                        ans.append(j);
                    textView_prefix_output.setText("Prefix: "+ ans);
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