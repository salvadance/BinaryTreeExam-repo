package binaryTreeExam;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of a self-balancing AVL (Adelson-Velsky and Landis) tree.
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

	

public class AVLTree {

	/**
	 * Represents a node in the AVL tree.
	 * Each node contains a key value, references to left and right children,
	 * a reference to its parent, and a height value for AVL balancing.
	 */
	private class Node {
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
	
	
	private Node root; // The root node
	private int numberOfValues; // Size of tree aka how many nodes with values does it have
	

	public AVLTree() {
		root = null;
		numberOfValues = 0;
	}
	
	// Enum for direction of rotation
	private enum Direction {Right, Left}

/* **************************************************************************************************************
 * AVL Tree operations insert and remove 
 * 
 * *************************************************************************************************************
*/
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

		node = node.parent; // Point the current node to it's parent node

		while (node != null) { // while loop to rebalance tree going down from node to parent to root
			treeRebalance(node);
			node = node.parent;
		}
	}

	
	/** 
	 * Method that takes a node as a parameter and removes based on 4 cases
	 * Base case: Node node is null returns false.
	 * Case 1: Node is an non-root internal node with 2 children, find successor node based on AVL tree implementation if larger numbers are on left or right
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

	
	/**
	 * Rebalances an AVL tree node to maintain the balance property
	 * 
	 * First updates the node's height, then checks its balance factor.
	 * Prints the rotation and if subtree is balanced after rotation.
	 * Calls the 
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
		
		if (!checkIsBalanced(node)) {
			String rotationStr = "("; //String for rotation directions
			
			System.out.print("Rotating tree ");
			
			if (treeBalance == -2) { // Right subtree is too tall, needs left rotation
				if (getTreeBalance(node.right) == 1) {
					// Double rotation case.
					treeRotateRight(node.right);
					rotationStr += Direction.Right.toString();
				}
				treeRotateLeft(node);
				rotationStr += Direction.Left.toString();
			}
	
			else if (treeBalance == 2) { // Left subtree is too tall, needs right rotation
				// Double rotation case. Left-Right
				if (getTreeBalance(node.left) == -1) {
					treeRotateLeft(node.left);
					rotationStr += Direction.Left.toString();
				}
	
				treeRotateRight(node);
				rotationStr += Direction.Right.toString();
			}
			
			System.out.println(rotationStr + ") -> Subtree balanced now?: " + checkIsBalanced(node));
		}

	}

	/**
	 * Method that updates the subtree given a node as the root of the subtree
	 * @param node is the root of the subtree which height will be updated (root of subtree)
	 */
	private void treeUpdateHeight(Node node) {
		int leftHeight;
		int rightHeight; 
		
		if(node == null) return;
		
		leftHeight = rightHeight = -1;
		
		if (node.left != null)
			leftHeight = node.left.height;
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

		treeSetChild(node.left, Direction.Right, node);
		treeSetChild(node, Direction.Left, leftRightChild);
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

		treeSetChild(node.right, Direction.Left, node);
		treeSetChild(node, Direction.Right, rightLeftChild);
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
		if (whichChild == null) return false; // Return false if string argument does not match


		if (whichChild == Direction.Left)
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
			return treeSetChild(parent, Direction.Left, childToAdd);
		else if (parent.right == currentChild)
			return treeSetChild(parent, Direction.Right, childToAdd);
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
	 * Iterative searches the AVL search tree to determine if key exists
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
	
	/** Method to get a node with a specific key value
	 * @param node represents the current node
	 * @param key is the value to be found.
	 * @return node after BSTSearch method or null if current node is null
	 */
	private Node BSTSearch(Node node, int key) {
		
		if (node == null) return null;

		if (node.key == key) return node;
		else if (key < node.key) return BSTSearch(node.left, key);
		else return BSTSearch(node.right, key);
	}
	
	/**
	 * Helper method that recursively checks if subtree satisfy AVL balance property.
	 * Prints diagnostic information for any nodes that violate the balance constraint.
	 * 
	 * @param node the current node to check (and its subtrees)
	 * @return true if this node and all descendants are balanced, false otherwise
	 */
	private boolean checkIsBalanced(Node node) {
	    if (node == null) return true;
	    
	    int balance = getTreeBalance(node);
	    
	    if (Math.abs(balance) > 1) {
	    	String nodeStr;
	    	String nodeHeightStr;
	    	
	        System.out.print("Imbalance at node " + node.key + 
	                          " with balance factor: " + balance + " || ");
	        
	        if (node.left == null) {
	        	nodeStr = "null";
	        	nodeHeightStr = "-1";
	        }
	        else {
	        	nodeStr = "" + node.left.key;
	        	nodeHeightStr = "" + node.left.height;
	        }
	        
	        System.out.print("left child : " + nodeStr + " left child height: " + nodeHeightStr + " || ");
	        
	        if (node.right == null) {
	        	nodeStr = "null";
	        	nodeHeightStr = "-1";
	        }
	        else {
	        	nodeStr = "" + node.right.key;
	        	nodeHeightStr = "" + node.right.height;
	        }
	        
	        System.out.println("right child : " + nodeStr + " right child height: " + nodeHeightStr);
	        return false;
	    }
	    return true;
	}
	
	
/* ================================================================================================================================
 * 											*Adding tree orders to a data structure*
 * 
 * Since encapsulation is kept by making the Node class private these methods with retrieving different orders. 
 * '
 * There is also a visitor implementation for a method to be called at each node and null node within tree height. 
 * It gives more flexibility on how to implement the display binary tree nodes implementation.
 * 
 * There are 4 type of traversal here in order, reverse order, preorder and post order
 * ================================================================================================================================
 */
	


	/**
	 * Method that searches for a node's information given a key. It utilizes binary search specifically the Binary Search Tree algorithm for searching 
	 * for a value. The algorithm searches until a matching key is found or a null node is reached.
	 * 
	 * 	 * Visit: Pass the matching node's key, parent's key, left child's key, right child's key, and its subtree's height
	 * 
	 * Logic:
	 *  1. Base Case: null is reached extractor is called passing null Integers for the node's and family's node key and -1 for the height
	 * 	2. Base Case: If key equals node's key then extractor is called passing node's information.
	 * 	3. If key is less than node's key go left
	 *  4. Else go to right child
	 *  5. Steps 3 and four until a matching key is found or a null node is reached
	 *  
	 *  If null is reached extractor is called passing null Integers for the node's and family's node key and -1 for the height
	 *  If node with matching key is found extractor is called passing null Integers for the node's and family's node key and -1 for the height
	 *  	-Logic is built in to handle cases when a node has family members that are null
	 * 
	 * 
	 * @param node
	 * @param key
	 * @param extractor
	 */
	private InfoNode BSTInfoSearch(Node node, int key) {
		
		if (node == null) { // Base Case: null reached
			return new InfoNode();
		}

		if (node.key == key) { // Base Case: Node found with match key
			Integer parent = node.parent != null ? node.parent.key : null;
			Integer left = node.left != null ? node.left.key : null;
			Integer right = node.right != null ? node.right.key : null;;
			return new InfoNode(node.key, parent, left, right, node.height);
		}
		else if (key < node.key) return BSTInfoSearch(node.left, key); // user's key less than node's key move to left child
		else return BSTInfoSearch(node.right, key); // else move right
		
	}
	
	/**
	 * The method to traverse in order or ascending order
	 * 
	 * Adds values to a queue
	 * 
	 * 1. Recusively goes to left-most node of the subtree if left child if left child not null.
	 * 2. Then goes up one node, visits it or performs an action
	 * 3. Then goes to the right child if not null, 
	 * 4. Then the process repeats until the right most node of the tree is reached.
	 * @param node the current node traversing
	 * @param list the queue to add values to
	 */
	private void inOrder(Node node, Queue<Integer> list) {
		
		if (node == null) return; // return if node is null
		
		inOrder(node.left, list); // Go to left child
		list.add(node.key); // add to queue
		inOrder(node.right, list); // Go to right child
		
	}
	
	/**
	 * The method to traverse in order or descending order 
	 * 
	 * Adds to a queue parameter
	 * 
	 * 1. Recusively goes to right-most node of the subtree if left child if right child not null.
	 * 2. Then goes up one node, visits it or performs an action
	 * 3. Then goes to the left child if not null
	 * 4. Then the process repeats until the right most node of the tree is reached.
	 * @param node the current node in traversal
	 * @param list the Queue in which to add values to
	 */
	private void reverseOrder(Node node, Queue<Integer> list) {
		
		if (node == null) return; // return if node is null

		reverseOrder(node.right, list); // Go to right child
		list.add(node.key); // add to queue
		reverseOrder(node.left, list); // Go to right child



	}

	/**
	 * The method to traverse in post order traversal. Visits the left-most node, then its sibling to parent then goes right.
	 * Adds to a queue parameter
	 * 
	 * 1. First recusively goes to left child until it finds the deepest and left-most leaf node and performs an action, 
	 * 		- Can be a left or right child of a parent node. (The following would)
	 * 2. Then checks if the node has a sibling
	 * 3. If no right sibling then performs action at its parent node.
	 * 4. Then the process repeats until the right most node of the tree is reached.
	 * @param node the current node in traversal
	 * @param list a queue to add node values to
	 */
	private void postOrder(Node node, Queue<Integer> list) {
		if (node == null) return; // return if node is null

		postOrder(node.left, list); // Go to left child
		postOrder(node.right, list); // Go to right child
		list.add(node.key); // add to queue
	}


	/**
	 * The method to traverse in pre order traversal. Visit, meaning it performs an action at the root node first of a non-empty tree
	 * 
	 * Adds to a queue parameter
	 * 
	 * 1. First visits the root node and performs an action on node value.
	 * 2. Then traverses to 
	 * 			- the left child if non-null and performs an action
	 * 			- If no left child then moves right and performs an action
	 * 			- it node has no children, moves up to its parent node (no action is preformed since parent was visited before) 
	 * 3. The process repeats until the last node is reached which would be the right most and bottom most node of the tree. 
	 * @param node the current node during traversal
	 * @param list the Queue to add node values to
	 */
	private void preOrder(Node node, Queue<Integer> list) {
		if (node == null) return; // return if node is null

		list.add(node.key); // add value to queue
		preOrder(node.left, list); // Go to left child
		preOrder(node.right, list); // Go to right child
	}
	
	/**
	 * The preorder traversal implementation method that uses recursion. Visit, meaning it performs an action 
	 * at the root node first of a non-empty tree
	 * 
	 * Visit: pass the nodes value and height.
	 * 
	 * 1. First visits the root node and performs an action on node value.
	 * 2. Then traverses to: 
	 * 			- the left child if non-null and performs an action
	 * 			- If no left child then moves right and performs an action
	 * 			- it node has no children, moves up to its parent node (no action is preformed since parent was visited before) 
	 * 3. The process repeats until the last node is reached which would be the right most and bottom most node of the tree. 
	 * @param node the current node during traversal
	 * @param height the node's height relative to the root
	 * @param maxLevel the height of the whole tree
	 * @param visitor the NodeVisitor that will be called with each node's value
	 */
	private void preOrder(Node node, int level, int maxLevel, DetailedVisitor visitor) {
		
		if (node == null) { //Base Case
			if (level <= maxLevel) { // Logic to account for gaps inside the tree
				
				Integer refToAdd = null;
				
				visitor.visit(refToAdd, level, maxLevel); //visit
				
				//Accounts for two children
				preOrder(null, level + 1, maxLevel, visitor);
				preOrder(null, level + 1, maxLevel, visitor);
			}
			return; // regardless if null node if it is beyond the tree height or not it still returns
		}
		
		visitor.visit(node.key, level, maxLevel); // visit
		preOrder(node.left, level + 1, maxLevel, visitor); // go to left child
		preOrder(node.right, level + 1, maxLevel, visitor); // got to right child
				
	}
	
/* **********************************************************************************************************
 *                        Public Methods
 * Methods used to insert, remove, search and get info on the AVL tree
 * 
 * **********************************************************************************************************
 */
	
	
	/**
	 * Entry method to insert into AVL tree
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
		numberOfValues++;
		return true;
	}
	
	/**
	 * Entry method to remove a node with the specified key from the AVL tree
	 * First searches if the tree has a node with key value using BSTSearch. Calls boolean method
	 * removeNode to remove it which handles removal and rebalance. If the method called returns true,
	 * the number of nodes count is decremented by 1 and this method returns true.
	 * 
	 * @param key the value of the node to remove from the tree
	 * @return true if the node was successfully removed, false if the key was not 
	 * 			found in the tree
	 */
	public boolean remove(int key) {
		//Assign node with BSTSearch() return
		Node node = BSTSearch(root, key);
		
		if (removeNode(node)) { // If node successfully removed statements then returns true
			numberOfValues--; //then number of nodes count is reduced 
			return true;	
		} 
		
		return false; // returns false is no node was removed because it didn't exist in the tree
	}
	
	/**
	 * Entry method to search for a key. 
	 * @param key the key to find to compare against nodes in the binary tree
	 * @param extractor extracts the InfoExtractor that will be called when node is found, 
	 * 		  used to extract node information (key, parent key, left child key, right child key, and height of its subtree)
	 */
	public InfoNode search(int key) {

		return BSTInfoSearch(root, key);

	}

	
	/**
	 * Returns height of a node parameter 
	 * 
	 * @param node the root of the subtree whose height is to be calculated
	 * @return returns tree height from node's class field  of height, or -1 if the node is null
	 */
	public int getTreeHeight() {

		if (root == null) {
			return -1;
		}

		return root.height;
	}
	

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
		
	    return checkIsBalanced(root);
	}
	
	
	/**
	 * Entry method for getting an in order queue
	 */
	public Queue<Integer> inOrderQueue() {
		Queue<Integer> list = new LinkedList<>();
		
		if (root == null) return list;
		
		inOrder(root, list);
		
		return list;
	}
	
	/**
	 * Entry method to get a queue in reverse order
	 */
	public Queue<Integer> reverseOrderQueue() {
		Queue<Integer> list = new LinkedList<>();
		
		if (root == null) return list;

		reverseOrder(root, list);
		
		return list;
	}

	/**
	 * Entry method for getting a queue in post order
	 */
	public Queue<Integer> postOrderQueue() {
		Queue<Integer> list = new LinkedList<>();
		
		if (root == null) return list;

		postOrder(root, list);
		
		return list;
	}

	/**
	 * Entry method get a queue of the tree in preorder
	 * 
	 */
	public Queue<Integer> preOrderQueue() {
		Queue<Integer> list = new LinkedList<>();
		
		if (root == null) return list;

		preOrder(root, list);
		
		return list;
	}
	
	/**
	 * Entry method to traverse the tree but also include null nodes to help with adding to an array so nothing is skipped
	 * Helps with logic as some implementations of displaying a tree will result in wrong node placement.
	 * This is due even if this an AVL tree gaps still appear inside the tree, but the tree balance will still be +1,-1,and 0
	 * @param visitor the DetailedVisitor that will be called with each node's value
	 */
	public void traverseAllPreOrder(DetailedVisitor visitor) {
		
		if (root == null) return;
		
		preOrder(root, 0, getTreeHeight(), visitor);
	}


	/** Method to get the numberOfValues of the tree meaning the number of nodes with values
	 * @return returns the value of the value in the numberOfValues field, aka the number of nodes.
	 */
	public int size() {
		return numberOfValues;
	}
	
	/**
	 * Entry method to get the balance factor of the tree.
	 * Returns the result of of getTreeBalance();
	 * @return
	 */
	public int balanceFactor() {
		return getTreeBalance(root);
		
	}
	
}
