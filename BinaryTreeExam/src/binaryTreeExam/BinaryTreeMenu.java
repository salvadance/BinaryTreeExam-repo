package binaryTreeExam;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Menu-driven interface for interacting with an AVL binary search tree.
 * 
 * Provides functionality to:
 * - Add and remove nodes from the tree
 * - Display the tree in various orientations (normal, flipped, reversed)
 * - Query node family information (parent, children, height)
 * - Initialize the tree with random values
 * - Clear the entire tree
 * 
 * The menu supports up to 15 nodes (2^4 - 1) and maintains tree balance
 * automatically through AVL rebalancing operations.
 */

public class BinaryTreeMenu {
	private BinaryTree tree;
	private TreeDisplayer displayer;
	private boolean isTreeReversed;
	private static final int MAX_NUMBER_OF_VALUES = (1 << 4) - 1;
	private int numOfValues;
	
	/**
	 * Constructs a BinaryTreeMenu with an empty AVL tree and TreeDisplayer.
	 * Initializes the tree as non-reversed with zero values
	 */
	public BinaryTreeMenu() {
		tree = new BinaryTree();
		displayer = new TreeDisplayer(tree);
		isTreeReversed = false;
		numOfValues = 0;
	}
	
	/**
	 * Initializes the tree with 16 random values (range 0-9999)
	 * Displays the original array of values an confirms AVL balance status
	 * Automatically inserts values and rebalances the tree
	 */
	private void initializeTreeWithRandomNumbers() {
		Random randG = new Random();
		int numberOfNodes = (1 << 4) - 1;
		int[] nums = new int[numberOfNodes];
		//int treeHeight;
	
		for (int i = 0; i < numberOfNodes; i++) {
			nums[i] = randG.nextInt(10000);
		}

		System.out.print("Orginal: [");
		for (int num : nums) {
			System.out.print(num + ", ");
		}
		System.out.println("]");

		for (int num : nums) {
			tree.insert(num);
		}
		
		System.out.println("Is tree balanced: " + tree.isAVLBalanced());
	}
	
	/**
	 * Toggles the tree displayer orientation between normal and reversed.
	 * In reversed mode, larger values appear on the left and smaller values on the right.
	 * Displays an error message if the tree is empty
	 */
	private void reverseTree() {
		if (tree.isEmpty()) { // Special case the tree is empty
			System.out.println("\n\n Tree is empty! Cannot reverse.\n\n");
			return;
		}

		isTreeReversed = !isTreeReversed; //Set is tree reversed boolean value
		
		System.out.println("\n\nTree is now reversed\n\n");
	}
	
	/**
	 * Clears all nodes from the tree, resetting it to empty state
	 */
	private void clearTree() {
		tree.clear();
		
		System.out.println("\n\nTree is now empty\n\n");
	}
	
	/**
	 * Prompts the user to add one or more numbers to the tree.
	 * Validates input range (1-9999) and helper methods prevent duplicate values, displays appropiate message is duplicate value
	 * Stops when the maximum number of values (15) is reached.
	 * 
	 * @param scnr the Scanner object for reading input 
	 */
	private void addNumber(Scanner scnr) {
		int numToAdd;
		char quitChar;
		
		do { //do-while loop
			
			//If maximum values reached then display message and break loop
			if (numOfValues >= MAX_NUMBER_OF_VALUES) {
				System.out.println("\nLimit of " + MAX_NUMBER_OF_VALUES + " reached for number of elements in binary tree\n\n");
				break;
			}
			
			//Prompt user to enter a number
			System.out.println("Enter a new key/number between 1 - 9999: ");
			
			try { //try/catch for InputMismatch exception
			
				numToAdd = scnr.nextInt();
				scnr.nextLine();
				
				
				if (numToAdd >= 1 && numToAdd <= 9999) { // Validate if value within range
					if (tree.insert(numToAdd)) { // Call helper method and get its return boolean value if was insertered or duplicate
						++numOfValues;
						System.out.println(numToAdd + " added!");
						System.out.println(numOfValues + " / " + MAX_NUMBER_OF_VALUES + " of values in binary tree");
					}
					else {
						System.out.println(numToAdd + " already exists in the AVL tree");
					}
				} 
				else {
					System.out.println("Invalid Input: Please enter a number between 1 - 9999");
				}
			}
			catch (InputMismatchException excpt) { // Input mismatch exception statements
				System.out.println("Invalid Input: Please enter a number ");
				scnr.nextLine(); // clear buffer
			}
			
			//Prompt user to continue or quit
			System.out.println("Enter any key to continue or 'q' to quit");
				
			quitChar = scnr.next().charAt(0);
			
		
		} while (quitChar != 'q');
	}
	
	/**
	 * Prompts the user to remove a node from the tree by key value
	 * Validates input range (1-9999) and confirms nodes existence before removal.
	 * Updtaes the node count and displays remaining values.
	 * Handles InputMismatchException gracefully and exits if tree becomes empty
	 * 
	 * @param scnr the Scanner object for reading user input
	 */
	private void removeNumber(Scanner scnr) {
		int numberToRemove;
		char quitChar = 'a';
		
		do {
			
			try {

				//Checks if tree is empty
				if (!tree.isEmpty()) {
					
					//Prompt user to enter key/node to remove
					System.out.println(numOfValues + " / " + MAX_NUMBER_OF_VALUES + " of values in binary tree");
					System.out.println("Enter a key to remove: ");
					numberToRemove = scnr.nextInt();
					scnr.nextLine();
					
					//Check is user input is within range 
					if (numberToRemove >= 1 && numberToRemove <= 9999) {

						//Check if number exists if so key is removed and displays message, otherwise another message is displayed
						if (tree.remove(numberToRemove)) {
							System.out.println("key " + numberToRemove + " successfully removed!");
							--numOfValues;
							System.out.println(numOfValues + " / " + MAX_NUMBER_OF_VALUES + " of values in binary tree\n\n");
							
						}
						else {
							//Key does not exist message
							System.out.println("Key " + numberToRemove + " does not exist\n\n");
						}
					}
					else {
						//Not within range messsage
						System.out.println("Not in valid range 1-9999, so does not exist in tree\n\n");
					}
					
					System.out.println("Enter any key to continue removing or 'q' to exit: ");
					quitChar = scnr.nextLine().charAt(0);
				}
				
				else { // Empty tree statements
					System.out.println("Tree is empty now exiting");
					break;
				}
			}
			catch (InputMismatchException excpt) {
				System.out.println("Invalid input: Please enter a number");
				scnr.next();
			}
			
			
		} while (quitChar != 'q');
	}
	
	/**
	 * Main menu loop that continuously displays menu options and processed user selections.
	 * Continues until the user enters 'q' to quit.
	 * Re-displays the menu after each operation 
	 * 
	 * @param scnr the Scanner object for reading user input.
	 */
	public void menuSelection(Scanner scnr) {
		char userSelection = ' ';
		printMenu(); // print menu options
		
		do {
			
			handleSelection(userSelection, scnr); // Call helper method that handles user selection
			
			//Print menu options after each successful selection
			if (userSelection == 'a' || userSelection == 'f' || userSelection == 'r' || userSelection == 'i' || userSelection == 'p'
					|| userSelection == 's' || userSelection == 'l' || userSelection == 'd' || userSelection == 'c') {
					printMenu();
			}
			
			
			//Prompt user for a selection
			System.out.println("Enter a selection: ");
			userSelection = scnr.next().charAt(0);
			System.out.println();
			
			
			
		} while(userSelection != 'q');
	}
	
	/**
	 * Processes the user's menu selection and calls the appropriate method/
	 * Valid selections:
	 * - 'a': Add a number
     * - 'f': Get family information for a node
     * - 'r': Reverse tree display orientation
     * - 'l': Print flipped tree view
     * - 'i': Initialize tree with random values
     * - 'p': Print normal tree view
     * - 's': Print tree statistics (in-order, reversed, original order)
     * - 'd': Delete/remove a node
     * - 'c': Clear entire tree
	 * 
	 * @param selection the character entered by the user
	 * @param scnr the Scanner object for reading user input.
	 */
	private void handleSelection(char selection, Scanner scnr) {
		switch(selection) {
			case 'a' -> addNumber(scnr);
			case 'f' -> displayer.menuGetFamily(scnr);
			case 'r' -> reverseTree();
			case 'l' -> displayer.printFlipped(isTreeReversed);
			case 'i' -> initializeTreeWithRandomNumbers();
			case 'p' -> displayer.printTreeNormalOrientation(isTreeReversed);
			case 's' -> displayer.printTreeOrders();
			case 'd' -> removeNumber(scnr);
			case 'c' -> clearTree();
		}
	}
	
	/**
	 * Displays the menu options to the user
	 * Shows all available operations with their corresponding character codes.
	 */
	private void printMenu() {
		System.out.println("Binary Tree Menu");
		System.out.println("'a' -- add a number");
		System.out.println("'f' -- get family info");
		System.out.println("'r' -- reverse binary tree");
		System.out.println("'l' -- print tree flipped");
		System.out.println("'i' -- initialize tree with random values");
		System.out.println("'p' -- print binary tree");
		System.out.println("'s' -- print binary tree stats");
		System.out.println("'d' -- delete a node/key");
		System.out.println("'c' -- clear tree");
		System.out.println("'q' -- quit");
	}

	/**
	 * Entry point for the binary tree application.
	 * Creates a menu instance and starts the interactive menu loop
	 * 
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		BinaryTreeMenu menu = new BinaryTreeMenu();
		menu.menuSelection(scnr);
		
	}
}
