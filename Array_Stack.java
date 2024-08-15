package ArithmeticCal;

/**
 * Represents a generic last-in-first-out (LIFO) stack. The Stack is implemented
 * using an array to handle its elements, that is resized to be doubled each
 * time it goes beyond the limit allowing it to grow as needed.
 * 
 * Space Complexity: O(n), where n is the number of elements in the stack.
 * 
 * @param <T> The type of elements held in this stack.
 */
public class Array_Stack<T> {
	private Object[] stackArray; // Internal array to hold stack elements
	private int size; // Number of elements in the stack
	private int DefaultCapacity = 100; // Default starting size of the array in case an empty array is needed
	/**
	 * Default empty constructor. Initializes the stack with default capacity. Space
	 * Complexity: O(1) for fixed-size initial array. Time Complexity: O(1), as it
	 * initializes with constant time operations.
	 */
	public Array_Stack() {
		this.stackArray = new Object[DefaultCapacity];
		this.size = 0;
	}
	/**
	 * Constructs a stack with specified initial capacity. Space Complexity: O(n),
	 * where n is initialCapacity. Time Complexity: O(1), as it only involves
	 * initializing an array of given capacity.
	 * 
	 * @param initialCapacity The initial size of the stack.
	 */
	public Array_Stack(int initialCapacity) {
		this.stackArray = new Object[initialCapacity];
		this.size = 0;
	}
	/**
	 * Adds an item to the top of the stack. Time Complexity: Amortized O(1), since
	 * resizing happens infrequently. Space Complexity: O(1) for adding a single
	 * element, but can trigger resizing to O(n) if capacity exceeded.
	 * 
	 * @param item The item to add to the stack.
	 */
	public void push(T item) {
		ensureCapacity();
		stackArray[size++] = item;
	}
	/**
	 * Removes and returns the top item of the stack. Time Complexity: O(1), as it
	 * removes the top element. Space Complexity: O(1), no additional space used.
	 * 
	 * @return The top item of the stack, or null if the stack is empty.
	 */
	public T pop() {
		if (size == 0)
			return null;
		T item = (T) stackArray[--size];
		stackArray[size] = null;
		return item;
	}
	/**
	 * Looks at the top item of the stack without removing it. Time Complexity:
	 * O(1), directly accesses the top element. Space Complexity: O(1), does not use
	 * any additional space.
	 * 
	 * @return The top item of the stack, or null if the stack is empty.
	 */
	public T peek() {
		if (size == 0)
			return null;
		return (T) stackArray[size - 1];
	}
	/**
	 * Returns the number of items in the stack. Time Complexity: O(1), just
	 * returning the size. Space Complexity: O(1), no extra space used.
	 * 
	 * @return The size of the stack.
	 */
	public int size() {
		return size;
	}
	/**
	 * Checks whether the stack is empty. Time Complexity: O(1), checks only the
	 * size attribute. Space Complexity: O(1), no extra space used.
	 * 
	 * @return True if the stack is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	/**
	 * Clears all items from the stack. Time Complexity: O(n), where n is the number
	 * of elements in the stack. Space Complexity: O(1), as it doesn't allocate any
	 * new structures.
	 */
	public void clear() {
		for (int i = 0; i < size; i++)
			stackArray[i] = null;
		size = 0;
	}
	/**
	 * Ensures there is enough capacity to add new elements to the stack. Time
	 * Complexity: Amortized O(1), as resizing is rare but can be O(n) when resizing
	 * happens. Space Complexity: O(n), as a new array of double size is created
	 * when needed.
	 */
	private void ensureCapacity() {
		if (size >= stackArray.length) {
			Object[] newStackArray = new Object[stackArray.length * 2];
			System.arraycopy(stackArray, 0, newStackArray, 0, size);
			stackArray = newStackArray;
		}
	}
	/**
	 * Converts the stack to a String representation. Time Complexity: O(n), where n
	 * is the number of elements in the stack. Space Complexity: O(n), as it
	 * constructs a string representing all elements.
	 * 
	 * @return A String representation of the stack.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size; i++) {
			sb.append(stackArray[i].toString());
			if (i < size - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * Prints the stack to the console (for demonstration purposes). Time
	 * Complexity: O(n), iterates through each element of the stack. Space
	 * Complexity: O(1), uses constant extra space regardless of stack size.
	 */
	public void printStack() {
		if (isEmpty()) {
			System.out.println("[ ]"); // Print an empty stack
			return;
		}
		for (int i = 0; i < size(); i++) {
			if (i == 0)
				System.out.print("[ " + stackArray[i]); // Print the first element with opening bracket
			else if (i == size() - 1)
				System.out.print(" | " + stackArray[i] + " ]"); // Print the last element with closing bracket
			else
				System.out.print(" | " + stackArray[i]); // Print intermediate elements with a separator
		}
	}
}