package ArithmeticCal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Arth_Cal {

	// all the allowed characters for this algorithm
	private static char[] Binary_charcters = { '^', '*', '/', '+', '-', '>', 'G', '≥', '≤', 'L', '<', '=', '!', '(',
			')', '$' };

	private static Array_Stack<Integer> number_Stack = new Array_Stack<>();
	private static Array_Stack<Character> operation_Stack = new Array_Stack<>();

	static String Step_By_Step_Debug = "";

	// VALUES CALCULATED USING WINDOWS EXPRESSION CALCULATOR (alt + space)
	String[] Main_Operations = { "3+(6*(11+1-4))/8*2 + (6/2)", // 18
			" 14 == 4 - 3 * 2 + 7 + 9", // 1
			"3 ^ 2 + 7 > 5 * 2 + 6", // 0
			"((3 + 4 * (8 - 2)) / 2) + (2^3 - 1) * 2", // 27
			"((6 * 2 + (5^2)) / 3) + (3^2 + (8 / 2))", // 25
			"((6 * (4 + (9 / 3))) / 2) - ((18 / 2) * (4 - 1))", // -6
			"(3 + (5 * (7 - 3))) - ((10 * 2) + (4 - 2))", // 1
			"((9 - (3 * (6 / 2))) + ((5 - 2) * (7 * 3)))", // 63
			"2 ^ (9 - (3 * (6 / 2) ) )", // 1
			"(((2^3) * 5) - (6 * 4) + (9 / 3) + (10^2) - (15 * 2)) + (((7^2) * 3) - (12 / 4) + (6 * 2))", // 245
			"(((4^3) / (2^2)) + ((5 * 3) - (6 + 2))) * (((9 / 3) + (7 * 2)) - ((10^3) * (3^2)))", // -206609
			"(((4^3) / (2^2)) + ((5) - (6 + 2))) * (((9 / 3) + (7 * 2)) - ((3^2)))^2^3", // 218103808
			"(((3^2)))^2^3", // 43046721
			"2^2^2^2", // 65536
			"(((2)^2)^2)^2" // 256
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Example usage
		// singleTest();
		fullTest();
		


	}

	// runs all the available tests does cache to file
	private static void fullTest() {
		// read file
		List<String> expression = fromFile("Input.txt");
		if (expression == null) {
			System.err.println("Failed to read the file");
			return;
		}

		try {

			// for (int j = 0; j < expression.length; j++) {
			for (int j = 0; j < expression.size(); j++) {
				if (j == expression.size() - 1) {
					System.out.println("Expression: " + expression.get(j));
				}
				Step_By_Step_Debug = "";
				number_Stack.clear();
				operation_Stack.clear();
				int element = j;
				System.out.println("Expression: " + expression.get(element));
				Step_By_Step_Debug += ("Expression: " + expression.get(element) + "\n");
				expression.set(element, stringParse(expression.get(element)));
				// localParenthesis(expression);

				// int result = processExpression(handleParentheses(expression.get(element)));
				int result = processExpression((expression.get(element)));

				System.out.println("Result: " + result); // Expected
				Step_By_Step_Debug += ("Result: " + result + "\n");

				int Result = result;
				System.err.println("---------------------- Result : " + Result);
				Step_By_Step_Debug += ("---------------------- Result : " + Result + "\n");
				toFile(Step_By_Step_Debug, "Operation" + (element + 1) + ".txt");
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Caught an exception: " + e.getMessage());
		}
	}

	// ==== Main Logic
	private static String handleParentheses(String expression) {
		// Initialize the processed expression with the original expression
		String processedExpression = expression;

		// Keep track of the balance of parentheses to find the matching pairs
		while (processedExpression.contains("(") && processedExpression.contains(")")) {
			// Find the index of the first innermost '(' from the end (closest to a ')')
			int lastOpenParenthesisIndex = processedExpression.lastIndexOf('(');
			// Find the index of the first occurrence of ')' after the last '('
			int firstCloseParenthesisIndex = processedExpression.indexOf(')', lastOpenParenthesisIndex);
			if (lastOpenParenthesisIndex < firstCloseParenthesisIndex && lastOpenParenthesisIndex != -1) {
				// Extract the sub-expression enclosed within the innermost parentheses
				String innerExpression = processedExpression.substring(lastOpenParenthesisIndex + 1,
						firstCloseParenthesisIndex);
				// Evaluate the sub-expression
				int result = processExpression(innerExpression);
				// Replace the sub-expression (including parentheses) with its evaluated result
				// in the processed expression
				processedExpression = processedExpression.substring(0, lastOpenParenthesisIndex) + result
						+ processedExpression.substring(firstCloseParenthesisIndex + 1);
			}
		}
		// Evaluate the final expression after all parentheses have been handled
		return processExpression(processedExpression) + ""; // Convert final result to String
	}

	/**
	 * Executes mathematical or comparison operations using two stacks: one for
	 * numbers, one for operators.
	 * 
	 * Steps: 1. Removes two numbers from the number stack. 2. Removes one operator
	 * from the symbol stack. 3. Performs the operation based on the operator: - '+'
	 * adds, '-' subtracts, '*' multiplies, '/' divides, '^' exponentiates. - '>'
	 * greater, '<' less, '=' equals, '!' not equals, 'G' greater or equal, 'L' less
	 * or equal. 4. Places the result back on the number stack.
	 * 
	 * Notes: - 'G' is '≥', 'L' is '≤'.
	 */
	private static void performOpration() {
		if (operation_Stack.isEmpty())
			return;
		if (operation_Stack.peek() == '(' || operation_Stack.peek() == ')' || operation_Stack.peek() == '$'
				|| operation_Stack.peek() == ' ')
			return;
		int RHS = number_Stack.pop();
		int LHS = number_Stack.pop();
		char oprator = operation_Stack.pop();

		// (expression)? 1 : 0 || 1 for ture, 0 for false
		switch (oprator) {
		case '+':
			number_Stack.push(LHS + RHS);
			break;
		case '-':
			number_Stack.push(LHS - RHS);
			break;
		case '*':
			number_Stack.push(LHS * RHS);
			break;
		case '/':
			if (RHS != 0) {
				number_Stack.push(LHS / RHS);
			} else {
				throw new IllegalArgumentException("Division by zero!");
			}
			break;
		case '^':
			number_Stack.push((int) Math.pow(LHS, RHS));
			break;
		case '>':
			number_Stack.push(LHS > RHS ? 1 : 0);
			break;
		case '<':
			number_Stack.push(LHS < RHS ? 1 : 0);
			break;
		case '=':
			number_Stack.push(LHS == RHS ? 1 : 0);
			break;
		case '!':
			number_Stack.push(LHS != RHS ? 1 : 0);
			break;
		case 'G':
		case '≥':
			number_Stack.push(LHS >= RHS ? 1 : 0);
			break;
		case 'L':
		case '≤':
			number_Stack.push(LHS <= RHS ? 1 : 0);
			break;
		default:
			throw new IllegalArgumentException("Invalid operator: " + oprator);
		}
		Debug();

	}

	/**
	 * Processes operations in the stack until the precedence of the current
	 * operator is higher than the one on top of the stack or until the stack is
	 * empty.
	 * 
	 * Steps: 1. Checks the current symbol (referenceOperator). 2. Continues if
	 * symbols are left and at least two numbers are in the stack, and the current
	 * symbol's precedence is <= top symbol's precedence. 3. Uses performOperation
	 * to execute each operation.
	 * 
	 * Stops when out of symbols or numbers.
	 * 
	 * @param referenceOperator Operator to compare precedence with those in the
	 *                          stack.
	 */
	private static void operationLoop(char referenceOperator) {
		System.out.println("==============referenceOperator : " + referenceOperator);
		Step_By_Step_Debug += ("==============referenceOperator : " + referenceOperator + "\n");

		/*
		 * if (!operation_Stack.empty() && !number_Stack.empty()) {
		 * System.out.println(referenceOperator + " <= " + operation_Stack.peek());
		 * System.out.println( getOperationPriority(referenceOperator) + " <= " +
		 * getOperationPriority(operation_Stack.peek())); }
		 */

		while ((!operation_Stack.isEmpty() && !number_Stack.isEmpty() && number_Stack.size() > 1)

				&& !(referenceOperator == '^' && operation_Stack.peek() == '^') && (referenceOperator == ')'
						|| getOperationPriority(referenceOperator) >= getOperationPriority(operation_Stack.peek()))) {
			System.out.println("Triggered");

			if (operation_Stack.peek() == '(') {
				if (referenceOperator == ')') {
					// Handle parentheses operation
					operation_Stack.pop(); // Remove the '(' from stack
					break; // Stop if we hit a left parenthesis when not processing a right
				}
				performOpration();
				break; // Stop if we hit a left parenthesis when not processing a right
			}

			if (operation_Stack.peek() == '$') {
				operation_Stack.pop(); // Remove the '(' from stack
			}
			performOpration();
		}
	}

	/**
	 * Determines the precedence of an operator to guide the order of operations.
	 * Precedence levels are defined with higher numerical values indicating higher
	 * precedence.
	 * 
	 * @param operator Character representing the operator whose precedence is to be
	 *                 determined.
	 * @return The precedence level as an integer.
	 * @throws IllegalArgumentException If an unrecognized operator is encountered.
	 */
	private static int getOperationPriority(char operator) {
		switch (operator) {
		case '(':
		case ')':
			return 1;
		case '^':
			return 2;
		case '*':
		case '/':
			return 3;
		case '+':
		case '-':
			return 4;
		case '>':
		case '<':
		case '≥':
		case '≤':
		case 'G': // Representing '≥'
		case 'L': // Representing '≤'
			return 5; // Higher priority than '+', '-'
		case '=': // Representing '=='
		case '!': // Representing '!='
			return 6; // Higher priority than '+', '-'
		case '$': // Special end of input token
			return 7;
		default:
			throw new IllegalArgumentException("Invalid operator: " + operator);
		}
	}

	/**
	 * Process a mathematical or logical expression using two stacks and returns the
	 * result. Supports arithmetic operations, power, and comparisons.
	 * 
	 * @param expression The string expression to evaluate.
	 * @return The integer result of evaluating the expression.
	 * @throws IllegalArgumentException If the expression is empty or contains
	 *                                  invalid characters.
	 */
	public static int processExpression(String expression) {

		if (expression.length() == 0)
			throw new IllegalArgumentException("Empty expression.");
		if (!legalExpression(expression))
			throw new IllegalArgumentException("Invalid operator illegal expression.");
		else {
			System.err.println("Current expression : " + expression);
			String processedExpression = stringParse(expression); // Clean and prepare the expression for processing

			// Initialize stacks for numbers and operations
			number_Stack.clear();
			operation_Stack.clear();

			String tempprocessedExpression = stringParse(expression);
			tempprocessedExpression = tempprocessedExpression.replaceAll("\\s+", "").replaceAll("\\$", "");

			try {
				return Integer.parseInt(tempprocessedExpression);
			} catch (NumberFormatException e) {
				System.out.println("Parsing failed: " + tempprocessedExpression + " is not a valid integer.");
			}

			// Temporary variable to hold each number or operator found in the expression
			StringBuilder token = new StringBuilder();

			for (int i = 0; i < processedExpression.length(); i++) {
				char ch = processedExpression.charAt(i);

				// Check if the current character is part of a number (including negative
				// numbers)
				if (Character.isDigit(ch)
						|| (ch == '-' && (i == 0 || !Character.isDigit(processedExpression.charAt(i - 1)))
								&& (i + 1 < processedExpression.length())
								&& Character.isDigit(processedExpression.charAt(i + 1)))) {
					token.setLength(0); // Clear the StringBuilder
					token.append(ch); // Append the current character

					// Continue to build the number (integer or decimal)
					int j = i + 1;
					while (j < processedExpression.length() && (Character.isDigit(processedExpression.charAt(j))
							|| processedExpression.charAt(j) == '.')) {
						token.append(processedExpression.charAt(j));
						j++;
					}

					// Parse the number and add it to the number stack
					number_Stack.push(Integer.parseInt(token.toString()));
					i = j - 1; // Move the index to the end of the number
				} else if (ch != ' ') { // If not a digit or space, then it should be an operator
					// If it's an operator (excluding parentheses), perform operations based on
					// precedence
					if (ch == '(' || ch == ')' || isOperator(ch)) {
						operationLoop(ch); // Process based on current operator
					}

					// If ch is not a closing parenthesis, push it onto the stack
					if (ch != ')') {
						operation_Stack.push(ch);
					}
				}

				// Debugging output
				Debug();
			}

			// Final operations after parsing all characters
			operationLoop('$'); // Ensure all remaining operations are processed

			// The final result should be the last item in the number stack
			int result = number_Stack.isEmpty() ? 0 : number_Stack.pop();
			return result;
		}
	}

	// ==== File Manipulations
	/**
	 * Writes the input string to a file at the specified path.
	 * 
	 * @param input The input string to be written to the file.
	 * @param Path  The path of the file to write the input string.
	 */
	private static void toFile(String input, String Path) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Path))) {
			// Write the input string to the file
			writer.write(input + "\n");
		} catch (IOException e) {
			// Print the stack trace in case of an IOException
			e.printStackTrace();
		}
	}

	/**
	 * Writes the input string to a file at the specified path.
	 * 
	 * @param input The input string to be written to the file.
	 * @param Path  The path of the file to write the input string.
	 */
	private static List<String> fromFile(String input) {
		List<String> expression = null;
		try {
			// Read all lines from a file into a List of Strings

			// Convert the List to an array of Strings
			expression = Files.readAllLines(Paths.get(input));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return expression;
	}

	// ==== Parsing and manipulation

	private static String handleNestedNegatives(String input) {

		if (containsOnlyCharacterAndDigits(input, '*', '-')) {

		}
		if (numOccurrences(input, '*') == 1) {

		}
		int lastOpenParenthesisIndex = input.lastIndexOf('*');

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '-') {

			}
		}
		return "";
	}

	/**
	 * Checks if the input string contains only the target character and digits.
	 * 
	 * @param input  The input string to be checked.
	 * @param target The target character to be checked for.
	 * @return True if the input string contains only the target character and
	 *         digits, false otherwise.
	 */
	private static boolean containsOnlyCharacterAndDigits(String input, char target) {
		// Check if the first character is the target character
		if (input.charAt(0) == target) {
			// Iterate through the input string starting from index 1
			for (int i = 1; i < input.length(); i++) {
				// Check if the character at index i is a digit
				if (Character.isDigit(input.charAt(i))) {
					// Continue iterating if it is a digit
					continue;
				} else {
					// Return false if a non-digit character is found
					if (input.charAt(i) == target)
						continue;

					return false;
				}
			}
			// Return true if all characters are the target character or digits
			return true;
		} else {
			// Return false if the first character is not the target character
			return false;
		}
	}

	/**
	 * Checks if the input string contains only the target character and digits.
	 * 
	 * @param input   The input string to be checked.
	 * @param target  The target character to be checked for.
	 * @param target1 The target character to be checked for.
	 * @return True if the input string contains only the target character and
	 *         digits, false otherwise.
	 */
	private static boolean containsOnlyCharacterAndDigits(String input, char target, char target1) {
		// Check if the first character is the target character
		if (input.charAt(0) == target) {
			// Iterate through the input string starting from index 1
			for (int i = 1; i < input.length(); i++) {
				// Check if the character at index i is a digit
				if (Character.isDigit(input.charAt(i))) {
					// Continue iterating if it is a digit
					continue;
				} else {
					// Return false if a non-digit character is found
					if (input.charAt(i) == target || input.charAt(i) == target1)
						continue;

					return false;
				}
			}
			// Return true if all characters are the target character or digits
			return true;
		} else {
			// Return false if the first character is not the target character
			return false;
		}
	}

	/**
	 * Checks if the occurrences of two different target characters in the input
	 * string are uneven.
	 * 
	 * @param input   The input string to be checked.
	 * @param target  The first target character to be counted.
	 * @param target1 The second target character to be counted.
	 * @return True if the occurrences of the two target characters are uneven,
	 *         false otherwise.
	 */
	private static boolean OccurrencesUnEven(String input, char target, char target1) {
		// Count the occurrences of the first target character
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == target) {
				count++;
			}
		}

		// Count the occurrences of the second target character
		int count1 = 0;
		for (int j = 0; j < input.length(); j++) {
			if (input.charAt(j) == target) {
				count1++;
			}
		}
		// Return true if the counts of the two characters are not equal
		return count1 != count;
	}

	/**
	 * Counts the occurrences of 2 target character in the input string .
	 * 
	 * @param input   The input string to be checked.
	 * @param target  The target character to be counted.
	 * @param target1 The second target character to be counted.
	 * @return The number of occurrences of the target character in the input
	 *         string.
	 */
	private static int numOccurrences(String input, char target, char target1) {
		// Count the occurrences of the target character
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == target) {
				count++;
			}
		}

		// Count the occurrences of the second target character
		int count1 = 0;
		for (int j = 0; j < input.length(); j++) {
			if (input.charAt(j) == target) {
				count1++;
			}
		}
		// Return the count of occurrences of the target character
		return count;
	}

	/**
	 * Counts the occurrences of 1 target character in the input string .
	 * 
	 * @param input  The input string to be checked.
	 * @param target The target character to be counted.
	 * @return The number of occurrences of the target character in the input
	 *         string.
	 */
	private static int numOccurrences(String input, char target) {
		// Count the occurrences of the target character
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == target) {
				count++;
			}
		}
		// Return the count of occurrences of the target character
		return count;
	}

	/**
	 * Checks if an expression only contains allowed characters or digits. Iterates
	 * through the expression and verifies each character against a list of allowed
	 * characters (Binary_characters).
	 * 
	 * Skips spaces, checks if each character is in the allowed list or is a digit.
	 * Ignores non-alphabet characters.
	 *
	 * @param expression String to be checked.
	 * @return true if all characters are legal, false if any illegal character is
	 *         found.
	 */
	private static boolean legalExpression(String expression) {
		// Iterate through each character of the expression.

		if (OccurrencesUnEven(expression, '(', ')')) {
			throw new IllegalArgumentException("Uneven Parenthesis.");
		}

		for (char character : expression.toCharArray()) {
			// Skip space characters.
			if (character == ' ')
				continue;

			// Flag to check if the current character exists in the predefined list or is a
			// digit.
			boolean exist = false;

			// Check if the current character is in the list of allowed characters or is a
			// digit.
			for (char allowedCharacter : Binary_charcters) {
				// System.out.println(allowedCharacter + " " + character);
				if (allowedCharacter == character || Character.isDigit(character) || !Character.isLetter(character)) {
					exist = true; // Set exist to true if character is legal.
					break; // Break out of the loop if a legal character is found.
				}
			}

			// If after checking all valid characters 'exist' is still false, the character
			// is illegal.
			if (!exist) {
				System.err.print("error!");
				// Return false because the expression contains an illegal character.
				return false;
			}
		}

		// Return true if all characters in the expression are legal.
		return true;
	}

	/**
	 * Prepares a string expression for parsing by removing whitespace and replacing
	 * complex operators with single-character equivalents.
	 * 
	 * Transformations: - Removes whitespace and other whitespace characters. -
	 * Replaces multi-character logical and comparison operators with
	 * single-character equivalents. - Replaces Unicode characters for greater than
	 * or equal to and less than or equal to with single-character equivalents. -
	 * Adds parentheses to the beginning and end of the expression.
	 *
	 * @param expression The string expression to be processed.
	 * @return The processed string expression, ready for evaluation.
	 */
	private static String stringParse(String expression) {
		// Remove all spaces and other whitespace characters from the expression
		// "\\s+" is a regular expression that matches any sequence of one or more
		// whitespace characters.
		String processedExpression = expression.replaceAll("\\s+", "");
		// Replace multi-character logical and comparison operators with
		// single-character equivalents
		processedExpression = processedExpression.replace(">=", "G") // Replace ">=" with "G"
				.replace("<=", "L") // Replace "<=" with "L"
				.replace("==", "=") // Replace "==" with "="
				.replace("!=", "!"); // Replace "!=" with "!"
		processedExpression = processedExpression.replace("≥", "G") // Replace ">=" with "G"
				.replace("≤", "L"); // Replace "<=" with "L"
		processedExpression = '(' + processedExpression + ')' + '$';

		processedExpression = processedExpression.replace("--", "");
		// Add parenthesis to the beginning and end of the expression to handle the
		// first and last operations'
		// Return the processed expression which is now ready for evaluation.
		return processedExpression;
	}

	private static Boolean isOperator(char temp) {
		Boolean val = false;

		for (char Ope : Binary_charcters) {
			if (temp == Ope) {
				val = true;
			}
		}

		return val;
	}

	/**
	 * Debugging function to display the contents of the stacks and update the debug
	 * tracker .
	 */
	public static void Debug() {
		System.out.println("_____________");
		System.out.println("number  : " + number_Stack.toString());
		System.out.println("operator: " + operation_Stack.toString());
		Step_By_Step_Debug += ("_____________" + "\n");
		Step_By_Step_Debug += ("number  : " + number_Stack.toString() + "\n");
		Step_By_Step_Debug += ("operator: " + operation_Stack.toString() + "\n");

	}

}
