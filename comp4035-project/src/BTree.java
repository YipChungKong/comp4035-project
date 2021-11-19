
public class BTree {
	int order = 2;
	IndexNode root;
	int height = 0;

	class IndexNode {
		int[] entries = new int[order * 2 + 1];
		IndexNode[] indexPointer = new IndexNode[entries.length];
		LeafNode[] leafPointer = new LeafNode[entries.length];
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
		LeafNode next;
		LeafNode previous;
		Node[] entries = new Node[order * 2 + 1];
	}

	public void Insert(int key) {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
