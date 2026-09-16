package com.example.prefix_infix_postfix_converter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;
public class step_by_step_solution_infix_to_postfix extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_infix_to_postfix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_postfix_output = findViewById(R.id.postfix_output_final);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            try {
                String infix_input_exp = bundle.getString("tag");
                assert infix_input_exp != null;
                if (infix_input_exp.trim().isEmpty()) {
                    textView_input.setText("No input string found. Enter Infix String");
                    textView_solution.setText(null);
                } else if (!Character.isLetterOrDigit(infix_input_exp.charAt(0))  && !(infix_input_exp.charAt(0)=='(')) {
                    textView_input.setText(R.string.invalid_input);
                    textView_solution.setText(null);
                } else {
                    int i;
                    textView_input.setText("Infix: " + infix_input_exp);
                    StringBuilder result_postfix = new StringBuilder();
                    StringBuilder solutionBuilder = new StringBuilder();
                    Stack<Character> stack = new Stack<>();
                    for (i = 0; i<infix_input_exp.length(); ++i)
                    {
                        solutionBuilder.append("\n------------Step ").append(i + 1).append("------------\n");
                        char c = infix_input_exp.charAt(i);
                        solutionBuilder.append("Character Scan: ").append(c).append("\n");

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
                        solutionBuilder.append("Stack: ").append(stack).append("\n");
                        solutionBuilder.append("Target String: ").append(result_postfix).append("\n");
                    }
                    while (!stack.isEmpty()){
                        solutionBuilder.append("\n------------Step ").append(i + 1).append("------------\n");
                        result_postfix.append(stack.pop());
                        solutionBuilder.append("Stack: ").append(stack).append("\n");
                        solutionBuilder.append("Target String: ").append(result_postfix).append("\n");
                        i++;
                    }
                    textView_solution.setText(solutionBuilder.toString());
                    textView_postfix_output.setText("Postfix: "+result_postfix);
                }
            } catch (Exception e) {
                textView_input.setText(R.string.invalid_input);
                textView_solution.setText(null);
            }
        }
        else
        {
            textView_input.setText("No input string found. Enter Infix String");
            textView_solution.setText(null);
        }
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
}