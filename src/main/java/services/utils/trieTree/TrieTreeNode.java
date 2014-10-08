package services.utils.trieTree;

public class TrieTreeNode {
	public TrieTreeNode(Integer nodeSize) {
		children = new TrieTreeNode[nodeSize];
		stopable = false;
		acceptableNumberRange = new int[2];
		acceptableNumberRange[0] = -1;
		acceptableNumberRange[1] = -1;
	}

	public TrieTreeNode[] children;
	public boolean stopable;
	public int[] acceptableNumberRange;

	public void addNumberLength(int numberLength) {
		if (acceptableNumberRange[0] == -1) {
			acceptableNumberRange[0] = numberLength;
			acceptableNumberRange[1] = numberLength;
		} else {
			if (numberLength < acceptableNumberRange[0])
				acceptableNumberRange[0] = numberLength;
			else if (numberLength > acceptableNumberRange[1])
				acceptableNumberRange[1] = numberLength;
		}
	}

	public boolean isNumberLengthInRange(int numberLength) {
		if (acceptableNumberRange[0] == -1)
			return false;
		else if (acceptableNumberRange[0] <= numberLength && //
				numberLength <= acceptableNumberRange[1])
			return true;
		return false;
	}
}
