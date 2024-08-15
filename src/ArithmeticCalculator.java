package ArithmeticCal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ArithmeticCalculator
{
    private char[] operators = new char[10];
    private double[] operands = new double[10];
    private int operatorTop = -1;
    private int operandTop = -1;

    public String booleanOperatorFinder(String input)
    {
        String[] operators = {">", "<", "  ", "  ", "==", "!="};
    	
    	for (String str : operators)
            if (input.contains(str))
                return str;

        return "";
    }
    
    public boolean compare(String input)
    {
    	String operator = booleanOperatorFinder(input);
    	String[] numbers = splitString(input, operator);
    	
    	return comparer(calculate(numbers[0]), calculate(numbers[1]), operator);
    }
    
    private static String[] splitString(String input, String delimiter)
    {
        String[] substrings = new String[2];
        int delimiterIndex = input.indexOf(delimiter);
        
        substrings[0] = input.substring(0, delimiterIndex);
        substrings[1] = input.substring(delimiterIndex + 1);

        return substrings;
    }   

	private static boolean comparer(double a, double b, String operator) {
		switch (operator) {
		case "≥":
			return Double.compare(a, b) >= 0;
		case "≤":
			return Double.compare(a, b) <= 0;
		case ">":
			return Double.compare(a, b) > 0;
		case "<":
			return Double.compare(a, b) < 0;
		case "==":
			return Double.compare(a, b) == 0;
		case "!=":
			return Double.compare(a, b) != 0;
		}
		return true;
	}

    
    public double calculate(String expression)
    {
    	for (int i = 0; i < expression.length(); i++) 
        {
            char c = expression.charAt(i);

            if (c == ' ')
            	continue;
            
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || expression.charAt(i - 1) == '(')))
            {
                StringBuilder sb = new StringBuilder();
                
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.' || (c == '-' && (i == 0 || expression.charAt(i - 1) == '(')))) 
                {   
                	if (c == '-' && (i == 0 || expression.charAt(i - 1) == '(') && expression.charAt(i + 1) == '(' )
                	{
                		pushOperand(0);
                    	pushOperator('-');
                	}
                	else
                		sb.append(expression.charAt(i));
                	
                	i++;
                }
                
                i--;
                
                String number = sb.toString();
                
                if (!(number.equals("")))
                	pushOperand(Double.parseDouble(number));
            } 
            else if (c == '(')
                pushOperator(c);
            else if (c == ')') 
            {
                while (operators[operatorTop] != '(')
                    evaluateTopOperator();
                
                popOperator();
            }
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^')
            {
                while (operatorTop != -1 && precedence(c, operators[operatorTop]) && (operatorTop >= 0 && !(c == '^' && operators[operatorTop] == '^')))
                	evaluateTopOperator();
                pushOperator(c);
            }
        }

        while (operatorTop != -1)
            evaluateTopOperator();
        
        double result = operands[operandTop];
        
        if (Double.isNaN(result))
        	throw new ArithmeticException("Math error: undefined operation.");

        return result;
    }

    private boolean precedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        if (op1 == '^' && (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/'))
            return false;
        return true;
    }

    private void evaluateTopOperator()
    {
        char operator = popOperator();
        double b = popOperand();
        double a = popOperand();

        pushOperand(applyOperator(operator, b, a));
    }

    private void pushOperand(double operand)
    {
        if (operandTop == operands.length - 1)
            expandOperandStack();
        operands[++operandTop] = operand;
    }

    private double popOperand()
    {
        if (operandTop == -1)
            throw new IllegalStateException("Syntax error: the operand stack is empty.");
        return operands[operandTop--];
    }

    private void pushOperator(char operator)
    {
        if (operatorTop == operators.length - 1)
            expandOperatorStack();
        operators[++operatorTop] = operator;
    }

    private char popOperator()
    {
        if (operatorTop == -1)
            throw new IllegalStateException("Syntax error: the operator stack is empty.");
        return operators[operatorTop--];
    }

    private double applyOperator(char operator, double b, double a)
    {
        switch (operator)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new ArithmeticException("Math error: division by zero.");
                return a / b;
            case '^':
                return Math.pow(a, b);
        }
        return 0;
    }

    private void expandOperandStack()
    {
        double[] newArray = new double[operands.length * 2];
        System.arraycopy(operands, 0, newArray, 0, operands.length);
        operands = newArray;
    }

    private void expandOperatorStack()
    {
        char[] newArray = new char[operators.length * 2];
        System.arraycopy(operators, 0, newArray, 0, operators.length);
        operators = newArray;
    }

    public static void main(String[] args)
    {
    	Scanner input = null;
    	PrintWriter output = null;
    	
    	try
    	{
    		input = new Scanner(new FileInputStream("C:\\Users\\apple\\Desktop\\Input.txt"));
    		output = new PrintWriter(new FileOutputStream("C:\\Users\\apple\\Desktop\\Output.txt"));
    		
	    	while(input.hasNextLine())
	    	{
	    		String expression = input.nextLine();
	    			
		    	try
		    	{
		        	ArithmeticCalculator2 calculator = new ArithmeticCalculator2();
		    		if (calculator.booleanOperatorFinder(expression).equals(""))
		    	    {
		    	        double result = calculator.calculate(expression);
		    	        output.print(expression);
		    	        output.println("\tResult: " + result);
		    	    }
		    	    else
		    	    {
		    	    	boolean result = calculator.compare(expression);
		    	    	output.print(expression);
		    	        output.println("\tResult: " + result);
		    	    }
		    	}
		    	catch (ArithmeticException aee)
		        {
		    		output.print(expression);
		    		output.println("\t" + aee.getMessage());
		        } 
	    	}
    	}
    	catch (IOException ioe)
    	{
    		System.out.println("File not found or cannot be created.");
    	}
    	finally
    	{
    		if (input != null)
    			input.close();
    		if (output != null)
    			output.close();
    	}
    }
}