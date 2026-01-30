package binaryTreeExam;



/**
 * Implementation of a self-balancing AVL (Adelson-Velsky and Landis) binary search tree.
 * 
 * This tree maintains the AVL balance property where the height difference between
 * left and right subtrees of any node is at most 1. Automatic rebalancing occurs
 * through rotations after insertions and deletions to maintain O(log n) search time.</p>
 * 
 * Supported operations:
 * 
 *   Insert - O(log n)
 *   Remove - O(log n)
 *   Search - O(log n)
 *   Balance validation - O(n)
 * 
 * @see Node
 * @see TreeDisplayer
 */

/**
	 * Represents a node in the AVL binary search tree.
	 * Each node contains a key value, references to left and right children,
	 * a reference to its parent, and a height value for AVL balancing.
	 */
	class Node {
		/** The key/value stored in this node (range: 1-9999) */
		public Integer key;
		
		/** References to left child, right child, and parent nodes */
		public Node left, right, parent;
		
		/** Height of this node in the tree (leaf nodes have height 0) */
		public int height;

		/**
		 * Creates a new node with the specified key value.
		 * Initializes all child and parent references to null and height to 0.
		 * 
		 * @param item the key value to store in this node
		 */
		public Node(int item) {
			key = item;
			left = right = parent = null;
			height = 0;
		}
	}

public class BinaryTree {


	private Node root; // The root node
	
	//Constructor
	public BinaryTree() {
		root = null;
	}
	
	private enum Direction {RIGHT, LEFT}
	
	/**
	 * Clears the AVL tree
	 */
	public void clear() {
		root = null;
	}
	
	/**
	 * Indicates if tree is empty
	 * An tree is empty when its root node is null
	 * 
	 * @return true if root is null, (tree is empty), false otherwise
	 */
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * Checks if the tree satisfies AVL balance property
	 * @return true if balanced, false otherwise
	 */
	public boolean isAVLBalanced() {
	    return checkBalance(root);
	}

	/**
	 * Helper method that recursively checks if all nodes satisfy AVL balance property.
	 * Prints diagnostic information for any nodes that violate the balance constraint.
	 * 
	 * @param node the current node to check (and its subtrees)
	 * @return true if this node and all descendants are balanced, false otherwise
	 */
	private boolean checkBalance(Node node) {
	    if (node == null) return true;
	    
	    int balance = getTreeBalance(node);
	    
	    if (Math.abs(balance) > 1) {
	        System.out.println("Imbalance at node " + node.key + 
	                          " with balance factor: " + balance);
	        return false;
	    }
	    
	    return checkBalance(node.left) && checkBalance(node.right);
	}
	
	/**
	 * Entry method to insert into binary tree
	 * First checks if the key is already present using BSTContains
	 * If the key is unique, creates a new node and calls insertNode to insert
	 * it into the tree with proper AVL rebalancing 
	 * 
	 * @param key is the number to be inserted
	 * @return returns if key was successfully inserted
	 */
	public boolean insert(int key) {
		if (BSTContains(key)) { //If BSTContains() returns true then return false, no insert occurs 
			return false;
		}

		Node nodeToAdd = new Node(key);
		insertNode(nodeToAdd); // call insertNode() method
		return true;
	}

	/**
	 * Inserts a node into the AVL tree and rebalances as needed
	 * 
	 * Process:
	 * 1.	Base case: root is null, set the node as the root
	 * 2. 	Iteratively traverses the tree to find the correct insertion position
	 * 	 	by comparing keys (smaller keys go left, larger keys go right)
	 * 3.	Insert the node as a leaf at the found position
	 * 4.	Traverse back up from the inserted node's parent to the root calling treeRebalance
	 * 		on each ancestor to maintain the AVL property (height difference of at most 1 between subtrees)
	 * 
	 * @param node is the node to be inserted
	 */
	private void insertNode(Node node) {
		Node cur; // current node variable
		
		if (root == null) { // Base case: if root is null set node as root
			root = node;
			node.parent = null;
			return;
		}

		cur = root; // start at root

		while (cur != null) { // while loop to insert a node by key value
			if (node.key < cur.key) { //Condition if node's key is lower than current node's key
				if (cur.left == null) { // if current node's left child is null set the parameter node as that child
					cur.left = node;
					node.parent = cur;
					cur = null;
				} else {
					cur = cur.left; // else traverse to current node's left child
				}
			} else { // else parameter node's key is greater than current nodes
				if (cur.right == null) { // If current node's right child is null set it the parameter node as that child
					cur.right = node;
					node.parent = cur;
					cur = null;
				} else
					cur = cur.right; // else traverse to right child of current node
			}
		}

		node = node.parent;

		while (node != null) { // while loop to rebalance tree going down from node to parent to root
			treeRebalance(node);
			node = node.parent;
		}
	}
	
	
	/**
	 * Entry method to remove a node with the specified key from the AVL tree
	 * First searches if the tree has a node with key value using BSTSearch. if found, calls
	 * removeNode to remove it and rebalance the tree.
	 * 
	 * @param key the value of the node to remove from the tree
	 * @return true if the node was successfully removed, false if the key was not 
	 * 			found in the tree
	 */
	public boolean remove(int key) {
		//Assign node with BSTSearch() return
		Node node = BSTSearch(root, key);
		
		return removeNode(node); //Return result of removeNode();
		
		
	}
	
	/** 
	 * Method that takes a node as a parameter and removes based on 4 cases
	 * Base case: Node node is null returns false.
	 * Case 1: Node is an non-root internal node with 2 children, find successor node based on binary tree implementation if larger numbers are on left or right
	 * 			or smaller numbers left or right.
	 * Case 2: The node to be removed is the root
	 * Case 3: Node is internal node with left child only
	 * Case 4: Node is internal node with right child only
	 * Iteratively call treeRebalance() starting at the removed node's parent and moving up to that node's parent and up to the root
	 * @param node to be removed
	 * @return if node has been removed or node is null.
	 */
	private boolean removeNode(Node node) {
		
		if (node == null) return false;
		
		//Create variables
		Node parent; // parent node
		Node succNode; // successor node
		
		//Parent needed for re-balancing
		parent = node.parent;
		
		// Case 1: Internal node with 2 children
		if (node.left != null && node.right != null) {
			//Find successor
			succNode = node.right;
			while (succNode.left != null) {
				succNode = succNode.left;
			}
		
		
			//Copy the key from the successor
			node.key = succNode.key;
			
			//Recursively remove successor node
			removeNode(succNode);
			
			//Nothing left to do since the recursive call will have rebalanced
			return true;
		}
		
		//Case 2: Root node (with 1 or 0 children)
		else if (node == root) {
			
			if (node.left != null) root = node.left;
			else root = node.right;
			
			if (root != null) {
				root.parent = null;
			}
			return true;
		}
		
		//Case 3; Internal with left child only
		else if (node.left != null) {
			//Call Method to remove node and replace parent's child
			treeReplaceChild(parent, node, node.left);
		}
		//Case 4: Internal with right child OR leaf
		else {
			//Call method to remove node and replace 
			treeReplaceChild(parent, node, node.right);
		}
		
		//node is gone. Anything that was below node that has persisted is already correctly
		// balanced, but ancestors of node may need re-balancing
		
		node = parent; // Set node to parent 
		
		while (node != null) { //Iteratively re-balance tree.
			treeRebalance(node);
			node = node.parent;
		}
		
		
		return true;
	}

	/** Method to get a node with a specific key value
	 * @param node represents the current node
	 * @param key is the value to be found.
	 * @return node after BSTSearch method or null if current node is null
	 */
	public Node BSTSearch(Node node, int key) {

		if (node == null) // Base Case: current node is null, returns null 
			return null;

		if (node.key == key) // Base case node's key == key being searched, returns that node
			return node;
		else if (key < node.key) // If key < node's key then recursively call BSTSearch with node's left child as argument
			return BSTSearch(node.left, key);
		else
			return BSTSearch(node.right, key); // Else recursively call with node's right child as argument

	}
	
	/**
	 * Rebalances an AVL tree node to maintain the balance property
	 * 
	 * First updates the node's height, then checks its balance factor.
	 * Performs rotations based on the balance factor:
	 * 		- Balance factor -2 (right-heavy): Perform left rotation
	 * 		  with optional right rotation on right child first (double roation)
	 * 		- Balance factor +2 (left-heavy): Perform right rotation,
	 * 		  with optional left rotation on left child first (double rotation)
	 * 		- Balance facotr -1, 0, +1: No rotation needed
	 * @param node the current node/subtree root to rebalance
	 */
	private void treeRebalance(Node node) {
		//First calls treeUpdateHeight
		treeUpdateHeight(node);
		
		int treeBalance = getTreeBalance(node);
		
		
		if (treeBalance == -2) { // Right subtree is too tall, needs left rotation
			if (getTreeBalance(node.right) == 1) {
				// Double rotation case.
				treeRotateRight(node.right);
			}
			treeRotateLeft(node);
		}

		else if (treeBalance == 2) { // Left subtree is too tall, needs right rotation
			// Double rotation case. Left-Right
			if (getTreeBalance(node.left) == -1) {
				treeRotateLeft(node.left);
			}

			treeRotateRight(node);
		}

	}

	/**
	 * Method that updates the subtree given a node as the root of the subtree
	 * 
	 * @param node is the root of the subtree which height will be updated (root of subtree)
	 */
	private void treeUpdateHeight(Node node) {
		int leftHeight, rightHeight;
		leftHeight = -1;
		if (node.left != null)
			leftHeight = node.left.height;
		rightHeight = -1;
		if (node.right != null)
			rightHeight = node.right.height;
		node.height = Math.max(leftHeight, rightHeight) + 1;
	}

	/**
	 * Method to rotate subtree right given a node as the pivot
	 * 
	 * The left child becomes the new root of the subtree, the original node
	 * becomes the right child of its former left child, and the former left
	 * child's right subtree becomes the left subtree of the original node
	 * 
	 * @param node is the pivot point
	 */
	private void treeRotateRight(Node node) {

		Node leftRightChild = node.left.right;

		if (node.parent != null)
			treeReplaceChild(node.parent, node, node.left);
		else { // node is root
			root = node.left;
			root.parent = null;
		}

		treeSetChild(node.left, Direction.RIGHT, node);
		treeSetChild(node, Direction.LEFT, leftRightChild);
	}

	/**
	 * Method to rotate tree left given a node as the pivot 
	 * 
	 * The right child becomes the new root of the subtree, the original node
	 * becomes the left child of its former right child, and the former right
	 * child's left subtree becomes the right subtree of the original node
	 * 
	 * @param node is the pivot point of the rotation
	 */
	private void treeRotateLeft(Node node) {
		Node rightLeftChild = node.right.left;

		if (node.parent != null)
			treeReplaceChild(node.parent, node, node.right);
		else { // node is root
			root = node.right;
			root.parent = null;
		}

		treeSetChild(node.right, Direction.LEFT, node);
		treeSetChild(node, Direction.RIGHT, rightLeftChild);
	}

	/**
	 * Method that sets node as either left or right child of a parent node
	 * Updates the child's parent pointer and recalculates the parent's height
	 * @param parent the parent node
	 * @param whichChild DIrection enum "left" or "right" to specify which child to set
	 * @param child the node to set as the child
	 * @return true that child was set or false if whichChild is invalid
	 */
	private boolean treeSetChild(Node parent, Direction whichChild, Node child) {
		if (!(whichChild == Direction.LEFT) && !(whichChild == whichChild.RIGHT)) return false; // Return false if string argument does not match


		if (whichChild == Direction.LEFT)
			parent.left = child;
		else
			parent.right = child;

		if (child != null) // If child node not null update its parent node field
			child.parent = parent;

		treeUpdateHeight(parent); // Update parent node's height

		return true;
	}

	/**
	 * Replaces a parent's child node with a new child
	 * Determines whether currentChild is the left or right child of parent
	 * then replaces it with childToAdd in the same position using the treeSetChild() method
	 * 
	 * @param parent the parent node whose child will be replaced
	 * @param currentChild the existing child node to be replaced
	 * @param childToAdd the new child node to replace currentChild
	 * @return true if the child was successfully replaced, false if currentChild is not a child of parent
	 */
	private boolean treeReplaceChild(Node parent, Node currentChild, Node childToAdd) {

		if (parent.left == currentChild)
			return treeSetChild(parent, Direction.LEFT, childToAdd);
		else if (parent.right == currentChild)
			return treeSetChild(parent, Direction.RIGHT, childToAdd);
		return false;
	}

	/**
	 * Calculates the balance factor of a node in the AVL tree.
	 * The balance factor is the difference between the left subtree height
	 * and the right subtree height (left height - right height)
	 * 
	 * A balance factor of:
	 * - 0 indicates perfectly balanced
	 * - +1 value indicates left-heavy (left subtree is taller)
	 * - -1 value indicates right-heavy (right subtree is taller)
	 * - Values of -2 or 2 indicates the tree needs rebalancing
	 * 
	 * @param node the node whose balance factor is to be calculated
	 * @return the balance factor (left height - right height)
	 */
	private int getTreeBalance(Node node) {
		int leftHeight, rightHeight;
		leftHeight = rightHeight = -1;

		if (node.left != null)
			leftHeight = node.left.height;
		if (node.right != null)
			rightHeight = node.right.height;

		return leftHeight - rightHeight;
	}

	/**
	 * Iterative searches the binary search tree to determine if key exists
	 * Traverses the tree by comparing the search key with each node's key,
	 * moving left for smaller values and right for larger values
	 * 
	 * @param key the value to search for in the tree
	 * @return true if key exists in the tree, false otherwise
	 */
	private boolean BSTContains(int key) {
		Node currNode = root; // start at the root

		while (currNode != null) {
			if (currNode.key == key) {
				return true;
			} else if (key < currNode.key) {
				currNode = currNode.left;
			} else {
				currNode = currNode.right;
			}
		}

		return false;
	}
	
	/**
	 * Recursively calculates the height of a subtree given a node as its root.
	 * 
	 * @param node the root of the subtree whose height is to be calculated
	 * @return returns tree height, or -1 if the node is null
	 */
	public int getTreeHeight(Node node) {

		if (node == null) {
			return -1;
		}

		return 1 + Math.max(getTreeHeight(node.left), getTreeHeight(node.right));

	}
	
	/**
	 * Method that returns the root
	 * @return the root
	 */
	public Node getRoot() {
		return root;
	}

	
}
