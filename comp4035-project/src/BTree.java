
public class BTree {
	int order = 2;	// max number of node is order*2
	IndexNode root;
	int height = 0;	// height start from 0

	class IndexNode {
		int[] entries = new int[order * 2 + 1];		// store searching key
		IndexNode[] indexPointer = new IndexNode[entries.length];	// pointer for next level index, only use when it's not leaf node
		LeafNode[] leafPointer = new LeafNode[entries.length];	// pointer for locating leaf node, only use when it's leaf node
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

	class LeafNode {
		LeafNode next;	// pointer for next leaf node array
		LeafNode previous;	// pointer for previous leaf node array
		Node[] entries = new Node[order * 2 + 1]; // array to store nodes
	}

	public void Insert(int key) {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
