package binaryTreeExam;

@FunctionalInterface
public interface DetailedVisitor {
	/**
	 * The visitor method to be implemented
	 * @param value take's the value of the current node
	 * @param level takes level the node is at. For Example: root is at level zero
	 */
	void visit(Integer value, int level, int maxLevel);
}
