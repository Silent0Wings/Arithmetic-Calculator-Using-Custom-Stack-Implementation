# Arithmetic Calculator Using Custom Stack Implementation
![Example](https://github.com/Silent0Wings/Arithmetic-Calculator-Using-Custom-Stack-Implementation/blob/7d14c3c5580c38e864ff4c39a2a6e82dfc3522c0/Example.png)

## Project Overview

Project Repport .[here](https://github.com/Silent0Wings/Arithmetic-Calculator-Using-Custom-Stack-Implementation/blob/a245e32c84a5fcbff7d99f50fdf6ff75a0d6f817/Github.pdf).

This project implements an arithmetic calculator capable of parsing and evaluating mathematical expressions using a custom stack data structure. The calculator handles various arithmetic operations and parentheses, providing accurate results for complex expressions.

## Features

- **Custom Stack Implementation**: A stack with expandable arrays that supports dynamic resizing.
- **Expression Evaluation**: Converts infix expressions to postfix notation and evaluates them.
- **Operator Support**: Handles arithmetic operators, power functions, and comparison operators.
- **File Handling**: Reads expressions from an input file and writes results to an output file.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java IDE (e.g., IntelliJ IDEA, Eclipse)

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/arithmetic-calculator.git
    cd arithmetic-calculator
    ```

2. **Compile the code:**

    ```bash
    javac Stack.java Calculator.java
    ```

3. **Run the calculator:**

    ```bash
    java Calculator input.txt output.txt
    ```

    - `input.txt` should contain one expression per line.
    - `output.txt` will be generated with the results for each expression.

## Usage

- **Custom Stack**: The `Stack` class provides methods to push, pop, and check if the stack is empty.
- **Calculator**: The `Calculator` class evaluates expressions using the custom stack for both operators and operands.

### Example


## Code Structure

- `Stack.java`: Implementation of the custom stack data structure.
- `Calculator.java`: Contains the logic for parsing and evaluating arithmetic expressions.

## Complexity Analysis

- **Time Complexity**: The stack operations (push and pop) have an amortized time complexity of \(O(1)\). The overall time complexity of evaluating an arithmetic expression is \(O(n)\), where \(n\) is the length of the expression.
- **Space Complexity**: The space complexity is \(O(n)\), where \(n\) is the number of elements in the stack.

## Testing

The calculator has been tested with various expressions to ensure its accuracy and robustness. Test cases include basic arithmetic operations, power functions, and complex nested expressions.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your improvements or fixes.
