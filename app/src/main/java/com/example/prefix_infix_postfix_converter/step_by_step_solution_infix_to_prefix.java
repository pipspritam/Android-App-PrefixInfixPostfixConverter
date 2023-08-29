package com.example.prefix_infix_postfix_converter;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Stack;
public class step_by_step_solution_infix_to_prefix extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_solution_infix_to_prefix);
        TextView textView_input = findViewById(R.id.input);
        TextView textView_solution = findViewById(R.id.solution);
        TextView textView_prefix_output = findViewById(R.id.prefix_output_final);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            try {
                String infix_input_exp = bundle.getString("tag");
                assert infix_input_exp != null;
                if (infix_input_exp.matches("")) {
                    textView_input.setText("No input string found.Enter Infix String");
                    textView_solution.setText(null);
                } else if (!Character.isLetterOrDigit(infix_input_exp.charAt(0))  && !(infix_input_exp.charAt(0)=='(')) {
                    textView_input.setText(R.string.invalid_input);
                    textView_solution.setText(null);
                } else {
                    int i;
                    textView_input.setText("Infix: " + infix_input_exp);
                    Stack<Character> operators = new Stack<>();
                    Stack<String> operands = new Stack<>();
                    for (i = 0; i < infix_input_exp.length(); i++)
                    {
                        textView_solution.append("\n------------Step "+(i+1)+"------------\n");
                        char c = infix_input_exp.charAt(i);
                        textView_solution.append("Character Scan: "+c+"\n");
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
                            operands.push(infix_input_exp.charAt(i) + "");
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
                        textView_solution.append("Target Stack: "+operands+"\n");
                        textView_solution.append("Operator Stack: "+operators+"\n");
                    }
                    while (!operators.empty())
                    {
                        textView_solution.append("\n------------Step "+(i+1)+"------------\n");
                        String op1 = operands.peek();
                        operands.pop();
                        String op2 = operands.peek();
                        operands.pop();
                        char op = operators.peek();
                        operators.pop();
                        String tmp = op + op2 + op1;
                        operands.push(tmp);
                        i++;
                        textView_solution.append("Target Stack: "+operands+"\n");
                        textView_solution.append("Operator Stack: "+operators+"\n");
                    }
                    String result_prefix= operands.peek();
                    textView_prefix_output.setText("Prefix: "+result_prefix);
                }
            } catch (Exception e) {
                textView_input.setText(R.string.invalid_input);
                textView_solution.setText(null);
            }
        }
        else
        {
            textView_input.setText("No input string found.Enter Infix String");
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