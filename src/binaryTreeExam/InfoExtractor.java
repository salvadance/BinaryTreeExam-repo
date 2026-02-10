package binaryTreeExam;

/**
 * Class to get information about a binary tree node and encapsulating the node class.
 * This was done to not give outside access to the Node class. Because family references can be changed which will break the binary tree
 * Has read only access once constructed. All fields are final once object is constructed with fields.
 */
public class InfoExtractor {
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
	public InfoExtractor(Integer value, Integer parent, Integer left, Integer right, int height) {
		VALUE = value;
		PARENT_VALUE = parent;
		LEFT_VALUE = left;
		RIGHT_VALUE = right;
		HEIGHT = height;
		
	}
	
	public Integer getValue() {
		return VALUE;
	}
	
	public Integer getParent() {
		return PARENT_VALUE;
	}
	
	public Integer getLeftChildValue() {
		return LEFT_VALUE;
	}
	
	public Integer getRightChildValue() {
		return RIGHT_VALUE;
	}
	
	public int getSubtreeHeight() {
		return HEIGHT;
	}
	
	public boolean isEmpty() {
		return (VALUE == null);
	}

}
