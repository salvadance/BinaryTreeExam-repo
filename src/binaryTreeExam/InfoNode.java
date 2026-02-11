package binaryTreeExam;

/**
 * Class to get information about a binary tree node and encapsulating the Node class.
 * This was done to not give outside access to the Node class. Because family references can be changed which will break the binary tree
 * Has read only access once constructed. All fields are final once object is constructed with fields.
 * Mimics a Node but only stores values.
 * 
 */
public class InfoNode {
	final private Integer VALUE;
	final private Integer PARENT_VALUE;
	final private Integer LEFT_VALUE;
	final private Integer RIGHT_VALUE;
	final private int HEIGHT;
	
	/**
	 * The constructor for Info Extractor. Can take null values except for height because a null node has a height of -1.
	 * @param value the value of a specific node to be assigned to VALUE field
	 * @param parent the value of a node's parent to be assigned to PARENT_VALUE field
	 * @param left a node's 
	 * @param right
	 * @param height
	 */
	public InfoNode(Integer value, Integer parent, Integer left, Integer right, int height) {
		VALUE = value;
		PARENT_VALUE = parent;
		LEFT_VALUE = left;
		RIGHT_VALUE = right;
		HEIGHT = height;
		
	}

	/**
	 * Default construct for InfoExtractor will have in a binary tree
	*/
	public InfoNode() {
		VALUE = null;
		PARENT_VALUE = null;
		LEFT_VALUE = null;
		RIGHT_VALUE = null;
		HEIGHT = -1;
	}

	// Get its value including null
	public Integer getValue() {
		return VALUE;
	}

	// Gets it parent's value including null
	public Integer getParent() {
		return PARENT_VALUE;
	}

	// Get its left child's value including null
	public Integer getLeftChildValue() {
		return LEFT_VALUE;
	}

	// Get its right child value including null
	public Integer getRightChildValue() {
		return RIGHT_VALUE;
	}

	// Get its subtree's height
	public int getSubtreeHeight() {
		return HEIGHT;
	}

	// Checks if VALUE is null therefore making the node null;
	public boolean isEmpty() {
		return (VALUE == null);
	}

}
