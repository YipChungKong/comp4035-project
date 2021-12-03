
public class BTree {
	int order = 2; // max number of node is order*2
	Nodes root = new Nodes(); // It should be LeafNode as root is the leaf at the first 5 keys
	Nodes start = root; // first leaf node array
	int height = 0; // height start from 0

	class Nodes {
		Integer[] entries = new Integer[order * 2 + 1]; // store searching key, first entry uses to store number of key
														// in the
		// array
		Nodes[] indexPointer = new Nodes[entries.length]; // pointer for next level index, only use when it's
															// not leaf node
		Nodes leafSidling; // pointer for locating leaf node, only use when it's
							// leaf node , It's only point to next leaf node
		boolean isLeafNode = true;

		Nodes() {
			this.entries[0] = 0;
			this.isLeafNode = true;
		}
	}

	class Node {
		int key;
		int rid;

		Node(int key, int rid) {
			this.key = key;
			this.rid = rid;
		}

		Node(int key) {
			this.key = key;
			this.rid = 0;
		}
	}

	public void Insert(int key) {
		Nodes currentNode = root;
		while (currentNode.isLeafNode == false) { // Find the nodes should store the key first, scan from root until the
													// Nodes.isLeafNode = true
			for (int i = 1; i <= 4; i++) {
				if (currentNode.entries[i] == null) {
					currentNode = currentNode.indexPointer[i - 1];
					break;
				} else {
					if (currentNode.entries[i] > key) {
						currentNode = currentNode.indexPointer[i - 1];
						break;
					}
				}
			}
		}
		if (currentNode.entries[0] == 4) {
			// full
			CopyUp(currentNode, key);
		} else {
			for (int i = 1; i <= 4; i++) {
				if (currentNode.entries[i] == null) {
					currentNode.entries[i] = key; // Insert complete add entries[0] count
					currentNode.entries[0]++;
					return;
				} else {
					if (currentNode.entries[i] > key) {
						int moveEntriesIndex = currentNode.entries[0]; // Move from last one
						while (moveEntriesIndex >= i) {
							currentNode.entries[moveEntriesIndex + 1] = currentNode.entries[moveEntriesIndex];
							moveEntriesIndex--;
						}
						currentNode.entries[i] = key;
						currentNode.entries[0]++;
						return;
					}
				}
			}
		}
	}

	public void PushUp() {
	};

	public void CopyUp(Nodes targetNodes, int key) {
		// check if sibling
		if (targetNodes == root) {
			// It is root nodes, find the copy up key first, we new a int array to store 5
			// key and sort it
			Integer[] overFlowKeys = new Integer[5];
			for (int i = 0; i < 4; i++) {
				// Put all the key of targetNodes to the overFlow one first
				overFlowKeys[i] = targetNodes.entries[i + 1];
			}
			// compare Key and overFlow one by one
			if (overFlowKeys[3] < key) {
				overFlowKeys[4] = key;
			} else {
				int sortPointer = 0;
				while (overFlowKeys[sortPointer] < key) {
					sortPointer++;
				}
				int moveEntriesIndex = 3; // Move from last one
				while (moveEntriesIndex >= sortPointer) {
					overFlowKeys[moveEntriesIndex + 1] = overFlowKeys[moveEntriesIndex];
					moveEntriesIndex--;
				}
				overFlowKeys[sortPointer] = key;
			}
			// new two leafNode, and change root to indexNode
			targetNodes.isLeafNode = false;
			Nodes leftLeafNode = new Nodes();
			Nodes rightLeafNode = new Nodes();
			leftLeafNode.entries[0] = 2;
			rightLeafNode.entries[0] = 3;
			leftLeafNode.entries[1] = overFlowKeys[0];
			leftLeafNode.entries[2] = overFlowKeys[1];
			leftLeafNode.leafSidling = rightLeafNode;
			rightLeafNode.entries[1] = overFlowKeys[2];
			rightLeafNode.entries[2] = overFlowKeys[3];
			rightLeafNode.entries[3] = overFlowKeys[4];
			targetNodes.entries[0] = 1;
			targetNodes.entries[1] = overFlowKeys[2];
			targetNodes.entries[2] = null;
			targetNodes.entries[3] = null;
			targetNodes.entries[4] = null;
			targetNodes.indexPointer[0] = leftLeafNode;
			targetNodes.indexPointer[1] = rightLeafNode;
			start = leftLeafNode;
		} else {
			System.out.println("Full but not root");
		}
	};

	public void PrintAllLeafNode() {
		Nodes currentNodes = start;
		while (currentNodes != null) {
			for (int i = 1; i <= 4; i++) {
				System.out.print(currentNodes.entries[i]);
				System.out.print(", ");
			}
			currentNodes = currentNodes.leafSidling;
		}
		System.out.println("");
		System.out.println("End");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTree bTree = new BTree();
		bTree.Insert(8);
		bTree.Insert(6);
		bTree.Insert(5);
		bTree.Insert(1);
		bTree.Insert(2);
		bTree.Insert(10);
		bTree.Insert(4);
		bTree.Insert(3);
		bTree.PrintAllLeafNode();
	}

}
