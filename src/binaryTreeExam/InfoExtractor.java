package binaryTreeExam;

@FunctionalInterface
public interface InfoExtractor {
	
	void extract(Integer value, Integer parent, Integer left, Integer right, int height);

}
