package binaryTreeExam;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class TreeDisplayer {
	/** The binary tree to be displayed */
	private BinaryTree tree;
	
	/** ArrayList storing node values organized by tree level for display purposes */
	private ArrayList<ArrayList<Integer>> levelValues;
	
	/**
	 * Constructs a TreeDisplayer for visualizing the given binary tree.
	 * Initializes the levelValues ArrayList for storing nodes organized by tree level.
	 * 
	 * @param tree the BinaryTree to be displayed
	 */
	public TreeDisplayer(BinaryTree tree) {
		this.tree = tree;
		levelValues = new ArrayList<>();
	}
	
	/**
	 * Recursively populates the levelValues ArrayList with node values organized by the tree level
	 * Performs a post-order traversal, adding children before parents. Null nodes are added
	 * as null values to maintain positional structure for visual tree representation.
	 * 
	 * The method ensures complete binary tree structure by recursively adding null children 
	 * for null nodes up to maxHeight, which preserves the spacing needed for display.
	 * @param height the current height/level in the tree (0 for root)
	 * @param node the current node being traversed or processed (can be null)
	 * @param maxHeight the maximum height of the tree to process for helping process null nodes and no more past max height for spacing
	 */
	private void addValuesToLevelValues(int height, Node node, int maxHeight) {
		
		if (levelValues.size() <= height && height <= maxHeight) levelValues.add(new ArrayList<Integer>()); //Add a level to levelValues ArrayList if height is bigger than current index, but less than maxHeight
			
			if (node == null) { // Base case if node is null
				Integer numToAdd = null;
	
				if (height <= maxHeight) { // Checks is height is less than or equal to maxHeight 
					levelValues.get(height).add(numToAdd);
					
					addValuesToLevelValues(height + 1, null, maxHeight); // makes sure no go gaps in top height leaves level for treeLevels
					addValuesToLevelValues(height + 1, null, maxHeight); // Basically counts the null child of a null
				} 
				
				return; //return after performing needed operations for null node, whether or not the inner if statement was executed
			}
	
			addValuesToLevelValues(height + 1, node.left, maxHeight); // Move to left node
			addValuesToLevelValues(height + 1, node.right, maxHeight); // Move to right node
	
			levelValues.get(height).add(node.key); // insert value on height for the index of arraylist for a level arraylist and add at the end of list of that.
		
	}
	
	/**
	 * Entry method to display binary tree in normal orientation, root is on top.
	 * Checks if binary tree is empty first an displays a message stating so.
	 * Calls method to fill the levelValues ArrayList
	 * Based on isReversed value call appropriate method to display tree non-reversed or reversed
	 * Finally clears array list.
	 * 
	 * @param isReversed if true that will call method that displays tree in reverse order bigger values on left and smaller values on right
	 */
	public void printTreeNormalOrientation(boolean isReversed) {
		
		if (tree.isEmpty()) { // Base case tree is empty
			System.out.println("Tree is empty\n");
			return;
		}
		
		Node root = tree.getRoot();
		
		addValuesToLevelValues(0, root, tree.getTreeHeight(root)); // builds levelValues arraylist
		
		if (!isReversed) printTreeNormal(); // Call method to print tree normally 
		else printTreeNormalReversed(); // else print tree reversed 
		levelValues.clear();
	}
	
	/**
	 * Method that prints tree in normal orientation, root on top, and with smaller values on left and bigger.
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the binary tree is set up to take values up to 4 digits, a single space is four white-spaces. 
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * During each iteration of each tree level:
	 * 			-The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			-Inner for loop for each element at current tree level 
	 * 				-Display either a value or "XXXX" if null.
	 * 				-Then perform the operation for spacing between the nodes using a for loop iterating value of betweenNodeSpaces.
	 * 			-At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 * 
	 */
	private void printTreeNormal() {
		int treeHeight;
		int levelNums; // number of possible values in each level
		int betweenNodeSpaces; // Value for number of spaces between each node
		int leftSpacePadding; // value for spaces on the left of left most node
		final String SPACE_STRING = "    "; // A space - 4 character spaces
		Integer numToAdd;

		treeHeight = tree.getTreeHeight(tree.getRoot());
		betweenNodeSpaces = 0; // initial value is zero because first number is the root
		levelNums = 1; 

		
		levelNums = 1 << treeHeight; //Calculate total possible number of values in current level

		leftSpacePadding = (levelNums * 2 - 1) / 2; // Calculate left spacing
		
		betweenNodeSpaces = 1; // initial value of between node spaces is 1
		

		// Iterate by each level starting at the root
		for (int i = 0; i < levelValues.size(); i++) {

			// Put a number of space stings by the number of leftSpacePadding value
			for (int j = 0; j < leftSpacePadding; j++) {
				System.out.print(SPACE_STRING);
			}
			
			//System.out.println("levelNums: " + levelNums);
			for (int j = 0; j < levelValues.get(i).size(); j++) {
				//Get null or a value
				numToAdd = levelValues.get(i).get(j);

				// If value is obtained format to print to fill 4 spaces
				if (numToAdd != null) {
					System.out.printf("%04d", numToAdd.intValue());
				} 
				//If null then display XXXX to fill 4 spaces
				else {
					System.out.print("XXXX");
				}

				//Output the spaces between each node as many times as the value of betweenNodeSpaces
				for (int k = 0; k < betweenNodeSpaces; k++) {
					System.out.print(SPACE_STRING);
				}

			}
			System.out.println("\n\n");
			
			betweenNodeSpaces = leftSpacePadding; // spaces between nodes new value is left spacing value
			leftSpacePadding /= 2; // left spacing value is halved
			
			
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
	 * Since the binary tree is set up to take values up to 4 digits, a single space is four white-spaces. 
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * During each iteration of each tree level:
	 * 			-The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			-Inner for loop for each element at current tree level (in reverse order)
	 * 				-Display either a value or "XXXX" if null.
	 * 				-Then perform the operation for spacing between the nodes using a for loop iterating value of betweenNodeSpaces.
	 * 			-At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 */
	public void printTreeNormalReversed() {

		int treeHeight;
		int levelNums; // Number of possible values in each current level
		int betweenNodeSpaces; // Value for number of spaces between each node
		int leftSpacePadding; // value for spaces on the left of left most node
		final String SPACE_STRING = "    "; // A space - 4 character spaces
		Integer numToAdd;

		treeHeight = tree.getTreeHeight(tree.getRoot());
		betweenNodeSpaces = 0;
		levelNums = 1;
		
		levelNums = 1 << treeHeight; //Calculate the total possible number of values per level

		leftSpacePadding = (levelNums * 2 - 1) / 2; //Calculate initial left spaces
		
		betweenNodeSpaces = 1; //initial spaces between nodes value is 1
		

		//Iterate through each level of the tree
		for (int i = 0; i < levelValues.size(); i++) {

			//Adding left spacing
			for (int j = 0; j < leftSpacePadding; j++) {
				System.out.print(SPACE_STRING);
			}

			//Loop to add values or XXXX and spacing in between

			//Iterate throught the level (root = index/level 0)
			for (int j = levelValues.get(i).size() - 1; j >= 0; j--) {

				numToAdd = levelValues.get(i).get(j); // Set get value 

				//If has value then print format it
				if (numToAdd != null) {
					System.out.printf("%04d", numToAdd.intValue());
				} 
				// Print out XXXX for null values
				else {
					System.out.print("XXXX");
				}

				//Print out the number of a group of 4 spaces as many times of the value of betweenNodeSpaces
				for (int k = 0; k < betweenNodeSpaces; k++) {
					System.out.print(SPACE_STRING);
				}

			}
			System.out.println("\n\n");
			
			betweenNodeSpaces = leftSpacePadding; // Spaces between nodes new value is set to value of left spacing
			leftSpacePadding /= 2; // left spacing new value is its value divided by 2
			
			
		}

		System.out.println("\n");

	}
	
	/**
	 * Entry method to display binary tree in flipped orientation, root is on bottom.
	 * Checks if binary tree is empty first an displays a message stating so.
	 * Calls method to fill the levelValues ArrayList
	 * Based on isReversed value call appropriate method to display tree non-reversed or reversed
	 * Finally clears array list.
	 * 
	 * @param isReversed if true that will call method that displays tree in reverse order bigger values on left and smaller values on right
	 */
	public void printFlipped(boolean isReversed) {
		
		if (tree.isEmpty()) {
			System.out.println("Tree is empty\n");
			return;
		}
		
		Node root = tree.getRoot();
		
		addValuesToLevelValues(0, root, tree.getTreeHeight(root));
		
		if (!isReversed) printTreeFlipped();
		else printTreeFlippedReversed();
		
		levelValues.clear();
	}

	/**
	 * Method that prints tree in flipped orientation, root on bottom, and with smaller values on left and bigger.
	 * leftSpacePadding is the amount of white space on the left of the leftmost value in the tree
	 * It is calculated by determining the levelNums which is the most possible number of nodes at the bottom most level.
	 * Then multiplying levelNums by 2 and subtracting by one and dividing all by 2. That initial value is for spacing on the left for the root node.
	 * Since the binary tree is set up to take values up to 4 digits, a single space is four white-spaces.    
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * The leftSpacePadding and betweenNodeSpaces values grow larger as the tree is printed from bottom to top.
	 * leftSpacePadding starts at 0 and betweenNodeSpaces starts at 1.
	 * betweenNodeSpaces increases by doubling its value and adding one each iteration.
	 * 
	 * XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    XXXX    5695    6314    8739    9805    
           XXXX            XXXX            2708            XXXX            3755            4735            5934            9520            
                   0961                            3268                            4623                            7337                            
                                   2609                                                            5472                                                            
                                                           3605 
	 * During each iteration of each tree level:
	 * 			-The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			-Inner for loop for each element at current tree level 
	 * 				-Display either a value or "XXXX" if null.
	 * 				-Then perform the operation for spacing between the nodes using a for loop iterating value of betweenNodeSpaces.
	 * 			-At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 * 
	 */
	private void printTreeFlipped() {
		//Special case tree is empty
		if (tree.isEmpty()) return;
		int betweenNodesSpaces; // number of spaces between each node
		int leftSpacePadding; // left spaces of each left most node
		final String SPACE_STRING = "    "; // A space to add is a actually a group of 4 character spaces
		Integer numToAdd;
		
		betweenNodesSpaces = 1;
		leftSpacePadding = 0; 
		
		
			
		
		// while loop condition to make sure current level does not 
		for (int i = levelValues.size() - 1; i >= 0; --i ) { // while loop to print tree;
																										
			
			for (int j = 0; j < leftSpacePadding; j++) { // for loop to print padding
				System.out.print(SPACE_STRING);
			}

		
			for (int j= 0; j < levelValues.get(i).size(); j++) { // for loop to print a row of the binary tree
				
				numToAdd = levelValues.get(i).get(j); // get value at index

				if (numToAdd != null) { // If-else to print if value is null or not
					System.out.printf("%04d", numToAdd.intValue());
				} else {
					System.out.print("XXXX");
				}

				for (int k = 0; k < betweenNodesSpaces; k++) {
					System.out.print(SPACE_STRING);
				}
				
			}
			
			
			
			betweenNodesSpaces = (betweenNodesSpaces * 2) + 1; // recalculate spaces between nodes value
			leftSpacePadding = (leftSpacePadding * 2) + 1; // recalculate spaces on left of leftmost node


			System.out.println("\n\n");

		}
		

		System.out.println("\n\n\n");
		
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
	 * Since the binary tree is set up to take values up to 4 digits, a single space is four white-spaces.    
	 * So the output area is divided into a grid 4x1 blocks, 4 spaces wide by 1 space tall. The betweenNodeSpaces is initialized to an arbitrary value of zero.
	 * The leftSpacePadding and betweenNodeSpaces values grow larger as the tree is printed from bottom to top.
	 * leftSpacePadding starts at 0 and betweenNodeSpaces starts at 1.
	 * betweenNodeSpaces increases by doubling its value and adding one each iteration.
	 * 
	 * During each iteration of each tree level:
	 * 			-The operation to display the spacing on the left of current tree level is performed via for loop.
	 * 			-Inner for loop for each element at current tree level (in reverse order)
	 * 				-Display either a value or "XXXX" if null.
	 * 				-Then perform the operation for spacing between the nodes using a for loop iterating value of betweenNodeSpaces.
	 * 			-At the end of each outer iteration betweenNodeSpaces is set to leftSpacePadding's value, which leftSpacePadding value
	 * 			is divided by 2.
	 */
	private void printTreeFlippedReversed() {
		if (tree.isEmpty()) return;
		int betweenNodesSpaces;
		int leftSpacePadding;
		final String SPACE_STRING = "    ";
		Integer numToAdd;
		
		
		betweenNodesSpaces = 1;
		leftSpacePadding = 0;
		
		// while loop condition to make sure current level does not 
		for (int i = levelValues.size() - 1; i >= 0; --i ) { // while loop to print tree;
																										
			
			for (int j = 0; j < leftSpacePadding; j++) { // for loop to print padding
				System.out.print(SPACE_STRING);
			}
			
		
			for (int j= levelValues.get(i).size() - 1; j >= 0; --j) { // for loop to print a row of the binary tree
				
				numToAdd = levelValues.get(i).get(j); // get value at index

				if (numToAdd != null) { // If-else to print if value is null or not
					System.out.printf("%04d", numToAdd.intValue());
				} else {
					System.out.print("XXXX");
				}

				//Add spaces between nodes
				for (int k = 0; k < betweenNodesSpaces; k++) {
					System.out.print(SPACE_STRING);
				}
				
			}
			
			

			betweenNodesSpaces = (betweenNodesSpaces * 2) + 1; // recalculate the value for spaces between nodes
			leftSpacePadding = (leftSpacePadding * 2) + 1; // recalculate the value for left spacing 


			System.out.println("\n\n");

		}

		System.out.println("\n\n\n");
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
		
		if (tree.isEmpty()) {
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
	 * Retrieves the node from the binary tree and prints its parent, left child,
	 * right child, and height. If the node does not exist, an appropriate message is displayed.
	 * 
	 * @param key the key of the node whose family information is to be printed
	 */
	private void printFamily(int key) {
		Node currNode;

		//Call BSTSearch and set its Node return value to currNode
		currNode = tree.BSTSearch(tree.getRoot(), key);

		//If currNode not null then node exists 
		if (currNode != null) {
			
			//Display family info

			System.out.print("[" + key + "] :: ");

			// Parent
			System.out.print("Parent: ");
			if (currNode.parent != null)
				System.out.print(currNode.parent.key + " || ");
			else
				System.out.print("NULL || ");

			// Left child
			System.out.print("Left: ");
			if (currNode.left != null)
				System.out.print(currNode.left.key + " || ");
			else
				System.out.print("NULL || ");

			// Right child
			System.out.print("Right: ");
			if (currNode.right != null)
				System.out.print(currNode.right.key + " || ");
			else
				System.out.print("NULL || ");

			// Height
			System.out.print("Height: " + currNode.height);

			System.out.print("\n\n");

		} 
		//Node does not exist message
		else {
			System.out.println("Node with key [" + key + "] does not exist\n\n");
		}
	}
	
	/** Public method to print the binary tree's data in various orders:
	 * In-order, reversed order, and original insertion order.
	 * If the tree is empty, it prints "Empty Tree".
	 * It first clears the levelValues list, populates it with the tree's values,
	 * and then calls helper methods to print the values in the specified orders.
	 */
	public void printTreeOrders() {
		//Special case tree is empty
		if (tree.isEmpty()) {
			System.out.println("Empty Tree");
		}
		
		levelValues.clear(); //Clear arraylist values
		
		addValuesToLevelValues(0, tree.getRoot(), tree.getTreeHeight(tree.getRoot()));
		
		//Print in ascending order
		System.out.print("In Order [");
		inOrderRec(tree.getRoot());
		System.out.print("]\n");

		//Print in descending order
		System.out.print("Reversed [");
		reverseRec(tree.getRoot());
		System.out.print("]\n");
		
		//Print in original order from root going down a level from left to right
		System.out.print("Original order [");
		printArrayAsIs();
		System.out.println("]\n");
		System.out.println("\n\n");
		levelValues.clear(); // clear arraylist
	}
	
	
	/**
	 * The private method of printing binary tree's data in reverse order
	 * @param node the starting or subtree node
	 */
	private void inOrderRec(Node root) {
		if (root != null) {
			inOrderRec(root.left);
			System.out.print(root.key + ", ");
			inOrderRec(root.right);
		}
	}

	
	/**
	 * The private method of printing binary tree's data in reverse order
	 * @param node the starting or subtree node
	 */
	private void reverseRec(Node node) {

		if (node != null) {

			reverseRec(node.right);
			System.out.print(node.key + ", ");
			reverseRec(node.left);

		}

	}
	
	/**
	 * Private method to print the levelValues Array as it is stored in ascending order
	 * Prints from level 0 or the root and left to right
	 */
	private void printArrayAsIs() {
		Integer num;
		for (int i = 0; i < levelValues.size(); i++) {
			for (int j = 0; j < levelValues.get(i).size(); j++) {
			num = levelValues.get(i).get(j);
			if (num != null)
				System.out.print(num + ", ");
			}

		}
		
	}
	
}
