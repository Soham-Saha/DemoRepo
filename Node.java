public class Node {
	Node[] children;
	String name;

	public Node(String name, int num) {
		this.children = new Node[num];
		this.name = name;
	}

	static void printAll(Node node, int tab) {
		String blank = "";
		for (int i = 0; i < tab; i++) {
			blank += "	";
		}
		if (node != null) {
			System.out.println(blank + "\\" + node.name + "\\");
			for (int i = 0; i < node.children.length; i++) {
				printAll(node.children[i], tab + 1);
			}
		}
	}

	public static boolean isEqual(Node node1, Node node2) {
		if (!node1.name.equals(node2.name)) {
			return false;
		}
		if (node1.children.length != node2.children.length) {
			return false;
		}
		for (int i = 0; i < node1.children.length; i++) {
			if (!isEqual(node1.children[i], node2.children[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasBlankChild(Node node) {
		for (Node x : node.children) {
			if (x.name.equals("")) {
				return true;
			} else {
				if (hasBlankChild(x)) {
					return true;
				}
			}
		}
		return false;
	}

}
