public class AdvanceSimplify {

	public Node advanceSimplify(Node node) {
		Node simp = node;
		/*if (simp.children.length == 0) {
			return simp;
		}
		Node child1 = simp.children[0];
		Node child2 = simp.children[1];
		if (simp.name.equals("*")) {
			if (child1.name.equals("/")) {
				Node divsimp = new Node("/", 2);
				divsimp.children[1] = child1.children[1];
				divsimp.children[0] = new Node("*", 2);
				divsimp.children[0].children[0] = child2;
				divsimp.children[0].children[1] = child1.children[0];
				simp = divsimp;
			} else if (child2.name.equals("/")) {
				Node divsimp = new Node("/", 2);
				divsimp.children[1] = child2.children[1];
				divsimp.children[0] = new Node("*", 2);
				divsimp.children[0].children[0] = child1;
				divsimp.children[0].children[1] = child2.children[0];
				simp = divsimp;
			}
		}
		simp = repeatSimplify(simp);
		simp.children[0] = advanceSimplify(simp.children[0]);
		simp.children[1] = advanceSimplify(simp.children[1]);*/
		return simp;
	}

	/*private Node repeatSimplify(Node node) {
		Node newnode = node;
		while (Maths.simplify(newnode) != newnode) {
			newnode = Maths.simplify(newnode);
		}
		return newnode;
	}*/

}
