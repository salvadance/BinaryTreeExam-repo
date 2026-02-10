package binaryTreeExam;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;



public class TreeDisplayer {
	/** The AVL tree to be displayed */
	private AVLTree tree;
	private static int BLOCK_SPACE_AMOUNT = 4;
	
	/** ArrayList storing node values organized by tree level for display purposes */
	private ArrayList<ArrayList<Integer>> levelValues;
	
	/**
	 * Constructs a TreeDisplayer for visualizing the given AVL tree.
	 * Initializes the levelValues ArrayList for storing nodes organized by tree level.
	 * 
	 * @param tree the AVLTree to be displayed
	 */
	public TreeDisplayer(AVLTree tree) {
		this.tree = tree;
		levelValues = new ArrayList<>();
	}
	
	/**
	 * Uses the traverseAllPreorder method that recursively populates the levelValues ArrayList with node values organized by the tree level.
	 * Performs a pre-order traversal, meaning node is visited first before move to its children. Null nodes are added
	 * as null values to maintain positional structure for visual tree representation.
	 * 
	 * traverseAllPreorder() parameters are a visitor method.
	 * The lambda expression implements the DetailedVisitor's visit() method
	 * 
	 * This visitor approach was implemented to give flexibility on how to structure data from tree to be displayed.
	 * In this case an ArrayList of ArrayList<Integer> type that keeps the binary tree structure by level.
	 * 
	 * Uses visitor design pattern. 
	 */
	private void addValuesToLevelValues() {
		
		if (tree == null || tree.isEmpty()) return; //Return is tree is null or empty
		
		levelValues.clear(); //Clear levelValues for safety
			
		tree.traverseAllPreOrder((value, level, maxLevel) -> { //Visitor implementation
			if(level >= levelValues.size() && level <= maxLevel) levelValues.add(new ArrayList<Integer>());
			
			levelValues.get(level).add(value);
		});
		
	}
	
	/**
	 * Entry method to display AVL tree in flipped orientation, root is on bottom.
	 * Checks if AVL tree is empty first an displays a message stating so.
	 * Calls method to fill the levelValues ArrayList
	 * Based on isReversed and isFLipped values, will call appropriate method to display tree non-reversed, reversed, flipped or not flipped
	 * Finally clears array list.
	 * @param isReversed if true that will call method that displays tree in reverse order bigger values on left and smaller values on right
	 * @param isFlipped if true thatt will call method that displays tree in flipped orientation with root on the bottom
	 */
	public void printTree(boolean isReversed, boolean isFlipped) {
		
		if (tree == null || tree.isEmpty()) {
			System.out.println("Tree is empty\n");
			return;
		}
		
		
		addValuesToLevelValues();
		
		if (!isFlipped) {
			if(!isReversed) printTreeNormal();
			else printTreeNormalReversed();
		}
		else {
			if(!isReversed) printTreeFlipped();
			else printTreeFlippedReversed();
			
		}
		
		levelValues.clear();
	}
	
	/**
	 * Method that prints tree in normal orientation, root on top, and with smaller values on left and bigger.
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the AVL tree is set up to take values up to 4 digits, a single space is four white-spaces. 
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. 
	 * The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * 
	 * Uses string build even though the number of nodes is limited, will help if limitation is increased. 
	 * Great for not calling print as often which can add up/
	 * 		For example:
	 * 				At tree level 9 is 2^9 which is 512 elements
	 * 				But the tree will have 2^(h+1)-1 nodes including null
	 * 				2(9+1)-1 = 1023 calls
	 * 
	 * During each iteration of each tree level:
	 * 			- The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			- Calculating padding for both types
	 * 			- Adding left padding to string builder
	 * 			- Inner for loop for each element at current tree level 
	 * 				- Add a either a value or "XXXX" if null along with between nodes padding to string builder.
	 * 			- Print string builder
	 * 			- At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 * 
	 */
	private void printTreeNormal() { 
		if (tree == null || tree.isEmpty());
		
		int treeHeight;
		int levelNums; // number of possible values in each level
		int betweenNodesAmount; // Value for number of spaces between each node
		int leftPaddingAmount; // value for spaces on the left of left most node
		final int BLOCK_SPACE_AMOUNT = 4;
		String betweenPadding;
		String leftPadding;
		final String SPACE = " ".repeat(BLOCK_SPACE_AMOUNT); // A space - 4 character spaces

		treeHeight = tree.getTreeHeight();
		betweenNodesAmount = 0; // initial value is zero because first number is the root
		levelNums = 1; 

		
		levelNums = 1 << treeHeight; //Calculate total possible number of values in current level

		leftPaddingAmount = (levelNums * 2 - 1) / 2; // Calculate left spacing
		
		betweenNodesAmount = 1; // initial value of between node spaces is 1
		

		// Iterate for each level starting at the root
		for (ArrayList<Integer> rowArray : levelValues) {
			
			//Create StringBuilder for row or level
			StringBuilder rowString = new StringBuilder();
			
			//Assign padding strings
			leftPadding = SPACE.repeat(leftPaddingAmount);
			betweenPadding = SPACE.repeat(betweenNodesAmount);
			
			rowString.append(leftPadding);
			
			//System.out.println("levelNums: " + levelNums);
			for (Integer numToAdd : rowArray) {

				// If value is obtained format to print to fill 4 spaces
				if (numToAdd != null) {
					formatNumber(rowString, numToAdd);
				} 
				//If null then display XXXX to fill 4 spaces
				else {
					rowString.append("XXXX");
				}

				//Output the spaces between each node as many times as the value of betweenNodeSpaces
				rowString.append(betweenPadding);

			}
			rowString.append("\n\n"); //Adds rows below to visually look like a tree
			
			System.out.println(rowString.toString()); //Prints row
			
			betweenNodesAmount = leftPaddingAmount; // spaces between nodes new value is left spacing value
			leftPaddingAmount /= 2; // left spacing value is halved
			
			
		}

		System.out.println("\n");
	}
	
	/**
	 * Method that prints tree in normal orientation, root on top, and with bigger values on left and smaller.
	 * This is the reversed display of printTreeNormal(). The layout and spacing calculations are identical
	 * to printTreeNormal(), but the nodes at each level are printed in reverse order (right to left)
	 * to display larger values on the left and smaller values on the right.
	 * IMPORTANT: A "space" is actually 4 character spaces
	 * 
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree.
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the AVL tree is set up to take values up to 4 digits, a single space is four white-spaces. 
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. 
	 * The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * 
	 * Uses string build even though the number of nodes is limited, will help if limitation is increased. 
	 * Great for not calling print as often which can add up/
	 * 		For example:
	 * 				At tree level 9 is 2^9 which is 512 elements
	 * 				But the tree will have 2^(h+1)-1 nodes including null
	 * 				2(9+1)-1 = 1023 calls
	 * 
	 * During each iteration of each tree level:
	 * 			- The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			- Calculating padding for both types
	 * 			- Adding left padding to string builder
	 * 			- Inner for loop for each element at current tree level 
	 * 				- Add a either a value or "XXXX" if null along with between nodes padding to string builder.
	 * 			- Print string builder
	 * 			- At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 */
	private void printTreeNormalReversed() {
		
		int treeHeight;
		int levelNums; // number of possible values in each level
		int betweenNodesAmount; // Value for number of spaces between each node
		int leftPaddingAmount; // value for spaces on the left of left most node
		final int BLOCK_SPACE_AMOUNT = 4;
		String betweenPadding;
		String leftPadding;
		final String SPACE = " ".repeat(BLOCK_SPACE_AMOUNT); // A space - 4 character spaces
		Integer numToAdd;

		treeHeight = tree.getTreeHeight();
		betweenNodesAmount = 0;
		levelNums = 1;
		
		levelNums = 1 << treeHeight; //Calculate the total possible number of values per level

		leftPaddingAmount = (levelNums * 2 - 1) / 2; //Calculate initial left spaces
		
		betweenNodesAmount = 1; //initial spaces between nodes value is 1
		
		
		//Iterate through each level of the tree
		for (ArrayList<Integer> rowArray : levelValues) {
			
			StringBuilder rowString = new StringBuilder();
			
			leftPadding = SPACE.repeat(leftPaddingAmount);
			betweenPadding = SPACE.repeat(betweenNodesAmount);
			
			rowString.append(leftPadding);

			// iterate backward from tree
			for (int j = rowArray.size() - 1; j >= 0; j--) {

				numToAdd = rowArray.get(j); // Set get value 

				//If has value then append it format formatted
				if (numToAdd != null) {
					formatNumber(rowString, numToAdd);
				} 
				// append out XXXX for null values
				else {
					rowString.append("XXXX");
				}

				//append 
				rowString.append(betweenPadding);
			}
			
			rowString.append("\n\n");
			
			//print row
			System.out.println(rowString.toString());
			
			betweenNodesAmount = leftPaddingAmount; // Spaces between nodes new value is set to value of left spacing
			leftPaddingAmount /= 2; // left spacing new value is its value divided by 2
			
			
		}


	}

	
	/**
	 * Entry method to display AVL tree in normal orientation, root is on top.
	 * Checks if AVL tree is empty first an displays a message stating so.
	 * Calls method to fill the levelValues ArrayList
	 * Based on isReversed value call appropriate method to display tree non-reversed or reversed
	 * Finally clears array list.
	 * @param isReversed if true that will call method that displays tree in reverse order bigger values on left and smaller values on right
	 
	public void printTreeNormalOrientation(boolean isReversed) {
		
		if (tree == null || tree.isEmpty()) { // Base case tree is empty
			System.out.println("Tree is empty\n");
			return;
		}
		
		
		addValuesToLevelValues(); // builds levelValues arraylist
		
		if (!isReversed) printTreeNormal(); // Call method to print tree normally 
		else printTreeNormalReversed(); // else print tree reversed 
		levelValues.clear();
	}
	*/

	/**
	 * Method that prints tree in flipped orientation, root on bottom, and with smaller values on left and bigger.
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the AVL tree is set up to take values up to 4 digits, a single space is four white-spaces.    
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * The leftSpacePadding and betweenNodeSpaces values grow larger as the tree is printed from bottom to top.
	 * leftSpacePadding starts at 0 and betweenNodeSpaces starts at 1.
	 * betweenNodeSpaces increases by doubling its value and adding one each iteration.
	 * 
	 * XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    5695    6314    8739    9805    
     *
     *     XXXX            XXXX            2708            XXXX            3755            4735            5934            9520            
     *             
     *             0961                            3268                            4623                            7337                            
     *                              
     *                             2609                                                            5472                                                            
     *                                                              
     *                                                             3605 
	 * Uses string build even though the number of nodes is limited, will help if limitation is increased. 
	 * Great for not calling print as often which can add up/
	 * 		For example:
	 * 				At tree level 9 is 2^9 which is 512 elements
	 * 				But the tree will have 2^(h+1)-1 nodes including null
	 * 				2(9+1)-1 = 1023 calls
	 * 
	 * During each iteration of each tree level:
	 * 			- The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			- Calculating padding for both types
	 * 			- Adding left padding to string builder
	 * 			- Inner for loop for each element at current tree level 
	 * 				- Add a either a value or "XXXX" if null along with between nodes padding to string builder.
	 * 			- Print string builder
	 * 			- At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 */
	private void printTreeFlipped() {
		//Special case tree is empty
		if (tree == null || tree.isEmpty()) return;
		
		int betweenNodesAmount; // Value for number of spaces between each node
		int leftPaddingAmount; // value for spaces on the left of left most node
		String betweenPadding;
		String leftPadding;
		final String SPACE = " ".repeat(BLOCK_SPACE_AMOUNT); // A space is 4 character spaces
		ArrayList<Integer> rowArray;
			
		

		leftPaddingAmount = 0; //Calculate initial left spaces
		
		betweenNodesAmount = 1; //initial spaces between nodes value is 1
		
		
		// while loop condition to make sure current level does not 
		for (int i = levelValues.size() - 1; i >= 0; --i ) { // while loop to print tree;
			
			StringBuilder rowString = new StringBuilder();
			
			leftPadding = SPACE.repeat(leftPaddingAmount);
			betweenPadding = SPACE.repeat(betweenNodesAmount);
			
			rowString.append(leftPadding);
			
			rowArray = levelValues.get(i);
			
			//Iterate the level
			for (Integer numToAdd : rowArray) { // for loop to print a row of the AVL tree
				
				// format and append depending if non-null or not 
				if (numToAdd != null) { // If-else to print if value is null or not
					formatNumber(rowString, numToAdd);
				} else {
					rowString.append("XXXX");
				}

				rowString.append(betweenPadding); // put spaces between nodes in string
				
			}
			
			rowString.append("\n\n");
			System.out.println(rowString); //print row
			
			betweenNodesAmount = (betweenNodesAmount * 2) + 1; // recalculate spaces between nodes value
			leftPaddingAmount = (leftPaddingAmount * 2) + 1; // recalculate spaces on left of leftmost node


		}
		
		
	}
	
	
	/** 
	 * Method that prints tree in flipped orientation, root on bottom, and with bigger values on left and smaller.
	 * This is the reversed display of printTreeFlipped(). The layout and spacing calculations are identical
	 * to printTreeFlipped(), but the nodes at each level are printed in reverse order (right to left)
	 * to display larger values on the left and smaller values on the right.
	 * 
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree.
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the AVL tree is set up to take values up to 4 digits, a single space is four white-spaces.    
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * The leftSpacePadding and betweenNodeSpaces values grow larger as the tree is printed from bottom to top.
	 * leftSpacePadding starts at 0 and betweenNodeSpaces starts at 1.
	 * betweenNodeSpaces increases by doubling its value and adding one each iteration.
	 * 
	 * Uses string build even though the number of nodes is limited, will help if limitation is increased. 
	 * Great for not calling print as often which can add up/
	 * 		For example:
	 * 				At tree level 9 is 2^9 which is 512 elements
	 * 				But the tree will have 2^(h+1)-1 nodes including null
	 * 				2(9+1)-1 = 1023 calls
	 * 
	 * During each iteration of each tree level:
	 * 			- The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			- Calculating padding for both types
	 * 			- Adding left padding to string builder
	 * 			- Inner for loop for each element at current tree level 
	 * 				- Add a either a value or "XXXX" if null along with between nodes padding to string builder.
	 * 			- Print string builder
	 * 			- At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 */
	private void printTreeFlippedReversed() {
		if (tree.isEmpty()) return;	
		
		int betweenNodesAmount; // Value for number of spaces between each node
		int leftPaddingAmount; // value for spaces on the left of left most node
		String betweenPadding;
		String leftPadding;
		final String SPACE = " ".repeat(BLOCK_SPACE_AMOUNT); // A space - 4 character spaces
		Integer numToAdd;
		ArrayList<Integer> rowArray;
		

		leftPaddingAmount = 0; //Calculate initial left spaces
		
		betweenNodesAmount = 1; //initial spaces between nodes value is 1
		
		// for loop to iterate array in reverse
		for (int i = levelValues.size() - 1; i >= 0; --i ) { // while loop to print tree;
			
			//Initialize String Builder
			StringBuilder rowString = new StringBuilder();
			
			//Set the types of padding
			leftPadding = SPACE.repeat(leftPaddingAmount);
			betweenPadding = SPACE.repeat(betweenNodesAmount);
			
			//Add left padding to sting
			rowString.append(leftPadding);
			
			rowArray = levelValues.get(i);
			
			//Iterate by level in reverse
			for (int j= rowArray.size() - 1; j >= 0; --j) { // for loop to create string of a row of the AVL tree
				
				numToAdd = rowArray.get(j); // get value at index

				//Determine what to append to row depending if numToAdd is null or not
				if (numToAdd != null) { // If-else to print if value is null or not
					formatNumber(rowString, numToAdd);
				} 
				else {
					rowString.append("XXXX");
				}

				//Add spaces between nodes
				rowString.append(betweenPadding);
				
			}
			
			rowString.append("\n\n");
			System.out.println(rowString.toString()); // print row
			
			betweenNodesAmount = (betweenNodesAmount * 2) + 1; // recalculate the value for spaces between nodes
			leftPaddingAmount = (leftPaddingAmount * 2) + 1; // recalculate the value for left spacing 


		}

	}
	
	/**
	 * Menu method to get family information of a node based on user input key.
	 * Prompts user to enter a key and retrieves the corresponding node's family information
	 * (parent, left child, right child, height) until the user decides to quit by entering 'q'.
	 * Handles invalid inputs gracefully by catching InputMismatchException.
	 * 
	 * @param scnr the Scanner object for reading user input
	 */
	public void menuGetFamily(Scanner scnr) {
		int key;
		int userChar = 'a';
		
		if (tree == null || tree.isEmpty()) {
			System.out.println("Tree is empty");
			return;
		}

		do {
			try {
				
				//Prompt user for a number
				System.out.println("Enter key to get info: ");
				key = scnr.nextInt();
				scnr.nextLine();
	
				//Check is user number is within range
				if (key >= 1 && key <= 9999) {
					printFamily(key);
				}
				else {
					System.out.println("Out of range 1-9999;");
				}

				//Prompt user if wish to continue
				System.out.println("Enter any key to continue or 'q' to quit");
				userChar = scnr.nextLine().charAt(0);
			}
			catch (InputMismatchException excpt) { //Catch input mismatch exception and display message
				System.out.println("Invalid input: Please enter a number");
			}
			
		} while (userChar != 'q');

	}
	
	/** Private helper method to print the family information of a node given its key.
	 * Retrieves the node from the AVL tree and prints its parent, left child,
	 * right child, and height. If the node does not exist, an appropriate message is displayed.
	 * 
	 * @param key the key of the node whose family information is to be printed
	 */
	private void printFamily(int key) {

		InfoExtractor info = tree.search(key);
	
		if (info != null && !info.isEmpty()) {
			
			//Display family info
			System.out.print("[" + info.getValue() + "] :: ");
	
			// Parent
			System.out.print("Parent: ");
			if (info.getParent() != null)
				System.out.print(info.getParent() + " || ");
			else
				System.out.print("NULL || ");
	
			// Left child
			System.out.print("Left: ");
			if (info.getLeftChildValue() != null)
				System.out.print(info.getLeftChildValue() + " || ");
			else
				System.out.print("NULL || ");
	
			// Right child
			System.out.print("Right: ");
			if (info.getRightChildValue() != null)
				System.out.print(info.getRightChildValue() + " || ");
			else
				System.out.print("NULL || ");
	
			// Height
			System.out.print("Height: " + info.getSubtreeHeight());
	
			System.out.print("\n\n");
	
		}
		//Node does not exist message
		else {
			System.out.println("Node with key [" + key + "] does not exist\n\n");
		}
	}
	
	/** Public method to print the AVL tree's data in various orders:
	 * In-order, reversed order, and original insertion order.
	 * If the tree is empty, it prints "Empty Tree".
	 * It first clears the levelValues list, populates it with the tree's values,
	 * and then calls helper methods to print the values in the specified orders.
	 */
	public void printTreeOrders() {
		
		//Special case tree is empty
		if (tree == null || tree.isEmpty()) {
			System.out.println("Empty Tree");
			return;
		}
		
		
		
		//Print in ascending order
		System.out.print("In Order: ");
		System.out.println(tree.inOrderQueue().toString() + "\n");

		//Print in descending order
		System.out.print("Reversed :");
		System.out.println(tree.reverseOrderQueue().toString() + "\n");
		
		//Print in original order from root going down a level from left to right
		System.out.print("Pre order: ");
		System.out.println(tree.preOrderQueue().toString() + "\n");
		
		//Print in original order from root going down a level from left to right
		System.out.print("Post order: ");
		System.out.println(tree.postOrderQueue().toString() + "\n");
		
		System.out.println("Tree Height " + tree.getTreeHeight());
		System.out.println("Is tree balanced? " + tree.isAVLBalanced());
		System.out.println("Balance Factor: " + tree.balanceFactor());
		
		System.out.println("\n\n");
	}
	
	
	
	/**
	 * Appends the given number to sb as a zero-padded 4-digit field.
	 * For example, 7 becomes 0007 and  42} becomes 0042.
	 *
	 * @param sb the target buffer to append to
	 * @param number the number to format (assumes 0..9999 for 4-digit padding)
	 */
	private void formatNumber(StringBuilder sb, int number) {
		
		if (number < 10) sb.append("000");
		else if (number < 100) sb.append("00");
		else if (number < 1000) sb.append("0");
		sb.append(number);
	}
	
}
