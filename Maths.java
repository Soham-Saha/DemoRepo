import java.util.Arrays;

public class Maths {
	static String[] ops = { "+", "-", "*", "/", "^", "λ", "σ", "κ", "τ" };
	// To change operator list, change above array with ascending significance
	// Then:
	// (i) Change deriv() func,simplify() func,advanceSimplify() func
	// (ii) Only one character operators are allowed. Arity-2
	// (iii) Change format() and clearFormat() funcs as required
	// (iv) Change Translator appropriately

	/*
	 * public static void main(String[] args) {
	 * Node.hasBlankChild(createNode(formatTrig(format("sinxlogtan")))); }
	 */
	public static String mainfunc(String inp) {
		inp = inp.replace(" ", "");
		inp = formatTrig(format(inp));
		inp = getString(createNode(inp));
		for (char x : inp.toCharArray()) {
			if (!isAlpha(x + "")) {
				if (!Character.isDigit(x)) {
					if (!Arrays.asList(ops).contains(x + "")) {
						if (!(x == '(' || x == ')' || x == '.')) {
							return "#ERRORCAPTURED#5";
						}
					}
				}
			}
		}
		if (inp.equals("")) {
			return "#ERRORCAPTURED#6";
		}
		inp = inp.replace(" ", "");
		if (brackError(inp)) {
			return "#ERRORCAPTURED#7";
		}
		Node node = createNode(inp);
		if (Node.hasBlankChild(node)) {
			return "#ERRORCAPTURED#8";
		}
		while (!getString(node).equals(getString(simplify(node)))) {
			node = simplify(node);
		}
		inp = getString(simplify(node));
		node = createNode(inp);
		node = simplify(createNode(deriv(node)));
		String x = getString(node);
		while (!x.equals(getString(simplify(node)))) {
			x = getString(simplify(node));
			node = simplify(node);
		}
		AdvanceSimplify s = new AdvanceSimplify();
		while (!x.equals(getString(s.advanceSimplify(node)))) {
			x = getString(s.advanceSimplify(node));
			node = s.advanceSimplify(node);
		}
		return x;
	}

	public static boolean brackError(String inp) {
		int brac = 0;
		for (char c : inp.toCharArray()) {
			if (c == '(') {
				brac++;
			}
			if (c == ')') {
				brac--;
			}
		}
		if (brac != 0) {
			return true;
		}
		return false;
	}

	public static String formatTrig(String inp) {
		/*
		 * If you want to add hyperbolic, remember: nothing else done, follow prev steps
		 * inp=inp.replace("arcsinh", "asinh"); inp=inp.replace("arccosh", "acosh");
		 * inp=inp.replace("arctanh", "atanh"); inp=inp.replace("arcsin", "asin");
		 * inp=inp.replace("arccos", "acos"); inp=inp.replace("arctan", "atan");
		 * 
		 * inp=inp.replace("asinh", "(3)σ"); inp=inp.replace("acosh", "(3)κ");
		 * inp=inp.replace("atanh", "(3)τ"); inp=inp.replace("sinh", "(2)σ");
		 * inp=inp.replace("cosh", "(2)κ"); inp=inp.replace("tanh", "(2)τ");
		 * 
		 * inp=inp.replace("asin", "(1)σ"); inp=inp.replace("acos", "(1)κ");
		 * inp=inp.replace("atan", "(1)τ"); inp=inp.replace("sin", "(0)σ");
		 * inp=inp.replace("cos", "(0)κ"); inp=inp.replace("tan", "(0)τ");
		 */
		inp = inp.replace("arcsin", "asin");
		inp = inp.replace("arccos", "acos");
		inp = inp.replace("arctan", "atan");
		inp = inp.replace("asin", "(1)σ");
		inp = inp.replace("acos", "(1)κ");
		inp = inp.replace("atan", "(1)τ");
		inp = inp.replace("sin", "(0)σ");
		inp = inp.replace("cos", "(0)κ");
		inp = inp.replace("tan", "(0)τ");
		return inp;
	}

	public static String clearTrig(String inp) {
		inp = inp.replace("(0)σ", "sin");
		inp = inp.replace("0σ", "sin");
		inp = inp.replace("(1)σ", "asin");
		inp = inp.replace("1σ", "asin");
		inp = inp.replace("(0)κ", "cos");
		inp = inp.replace("0κ", "cos");
		inp = inp.replace("(1)κ", "acos");
		inp = inp.replace("1κ", "acos");
		inp = inp.replace("(0)τ", "tan");
		inp = inp.replace("0τ", "tan");
		inp = inp.replace("(1)τ", "atan");
		inp = inp.replace("1τ", "atan");
		return inp;
	}

	public static String format(String inp) {
		inp = inp.replace("log", "λ");
		return inp;
	}

	public static String clearFormat(String inp) {
		inp = inp.replace("λ", "log");
		return inp;
	}

	public static Node simplify(Node nodeinp) {
		Node node = nodeinp;
		if (node.children.length == 0) {
			return node;
		}
		Node newnode = node;
		Node child1 = node.children[0];
		Node child2 = node.children[1];
		// Simplification shortcuts
		if (node.name.equals("*")) {
			if (child1.name.equals("0") || child2.name.equals("0")) {
				return new Node("0", 0);
			}
			if (child1.name.equals("1")) {
				return simplify(child2);
			}
			if (child2.name.equals("1")) {
				return simplify(child1);
			}
		}
		if (node.name.equals("/")) {
			if (child1.name.equals("0")) {
				return new Node("0", 0);
			}
			if (child2.name.equals("1")) {
				return simplify(child1);
			}
			if (Node.isEqual(child1, child2)) {
				return new Node("1", 0);
			}
		}
		if (node.name.equals("+")) {
			if (child1.name.equals("0")) {
				return simplify(child2);
			}
			if (child2.name.equals("0")) {
				return simplify(child1);
			}
		}
		if (node.name.equals("-")) {
			if (child2.name.equals("0")) {
				return simplify(child1);
			}
			if (Node.isEqual(child1, child2)) {
				return new Node("0", 0);
			}
		}
		if (node.name.equals("^")) {
			if (child2.name.equals("1")) {
				return simplify(child1);
			}
			if (child2.name.equals("0")) {
				return new Node("1", 0);
			}
			if (child1.name.equals("1")) {
				return new Node("1", 0);
			}
			if (child1.name.equals("0") && !child2.name.equals("0")) {
				return new Node("0", 0);
			}
			if (child1.name.equals("0") && child2.name.equals("0")) {
				return new Node("1", 0);
			}
		}
		if (node.name.equals("λ")) {
			if (child2.name.equals("1")) {
				return new Node("0", 0);
			}
			if (Node.isEqual(child1, child2)) {
				return new Node("1", 0);
			}
		}
		if (node.name.equals("σ")) {
			if (child1.name.equals("0")) {
				if (child2.name.equals("0")) {
					return new Node("0", 0);
				}
				/*
				 * if (getString(child2).equals("(pi)/(6)")) { return createNode("1/2"); } if
				 * (getString(child2).equals("(pi)/(4)")) { return createNode("1/(2)^(1/2)"); }
				 * if (getString(child2).equals("(pi)/(3)")) { return createNode("(3^(1/2))/2");
				 * } if (getString(child2).equals("(pi)/(2)")) { return createNode("1"); }
				 */
			}
			if (child1.name.equals("1")) {
				if (child2.name.equals("0")) {
					return new Node("0", 0);
				}
			}
		}
		if (node.name.equals("κ")) {
			if (child1.name.equals("0")) {
				if (child2.name.equals("0")) {
					return new Node("1", 0);
				}
				/*
				 * if (getString(child2).equals("(pi)/(6)")) { return createNode("(3^(1/2))/2");
				 * } if (getString(child2).equals("(pi)/(4)")) { return
				 * createNode("1/(2)^(1/2)"); } if (getString(child2).equals("(pi)/(3)")) {
				 * return createNode("1/2"); } if (getString(child2).equals("(pi)/(2)")) {
				 * return createNode("0"); }
				 */
			}
			if (child1.name.equals("1")) {
				if (child2.name.equals("1")) {
					return new Node("0", 0);
				}
			}
		}
		if (node.name.equals("τ")) {
			if (child1.name.equals("0")) {
				if (child2.name.equals("0")) {
					return new Node("0", 0);
				}
				if (getString(child2).equals("(pi)/(6)")) {
					return createNode("(3^(1/2))/3");
				}
				if (getString(child2).equals("(pi)/(4)")) {
					return createNode("1");
				}
				if (getString(child2).equals("(pi)/(3)")) {
					return createNode("(3^(1/2))");
				}
			}
			if (child1.name.equals("1")) {
				if (child2.name.equals("0")) {
					return new Node("0", 0);
				}
			}
		}
		// Shortcuts E.O.F
		boolean doubleParsable = true;
		try {
			Double.parseDouble(child1.name);
			Double.parseDouble(child2.name);
		} catch (NumberFormatException e) {
			doubleParsable = false;
		}
		if (child1.children.length == 0 && child2.children.length == 0 && doubleParsable) {
			if (isAlpha(child1.name) || isAlpha(child2.name)) {
				return node;
			}
			double one = Double.parseDouble(child1.name);
			double two = Double.parseDouble(child2.name);
			if (node.name.equals("-")) {
				double val = one - two;
				return new Node(val + "", 0);
			}
			if (node.name.equals("+")) {
				double val = one + two;
				return new Node(val + "", 0);
			}
			if (node.name.equals("*")) {
				double val = one * two;
				return new Node(val + "", 0);
			}
			if (node.name.equals("/")) {
				Math.IEEEremainder(two, one);
				if (one % two == 0) {
					double val = one / two;
					return new Node(val + "", 0);
				} else if (one % 1 != 0 || two % 1 != 0) {
					if (Math.IEEEremainder(two, one) == 0) {
						double val = one / two;
						return new Node(val + "", 0);
					}
				} else {
					return node;
				}
			}
			if (node.name.equals("^")) {
				if (two % 1 != 0) {
					return node;
				}
				double val = Math.pow(one, two);
				return new Node(val + "", 0);
			}
			return node;
		} else {
			newnode.children[0] = simplify(child1);
			newnode.children[1] = simplify(child2);
			return newnode;
		}
	}

	public static boolean isAlpha(String s) {
		return s != null && s.matches("^[a-zA-Z]*$");
	}

	private static String deriv(Node node) {
		String inp = node.name;
		if (!(hasOperator(inp)) && isAlpha(inp)) {
			if (inp.contains((String) DisplayFrame.lowervar.getSelectedItem())) {
				return 1 + "";
			} else {
				return 0 + "";
			}
		} else if (!(hasOperator(inp))) {
			return 0 + "";
		}
		if (node.name.equals("+")) {
			return "(" + deriv(node.children[0]) + ")+(" + deriv(node.children[1]) + ")";
		}
		if (node.name.equals("-")) {
			return "(" + deriv(node.children[0]) + ")-(" + deriv(node.children[1]) + ")";
		}
		Node f = node.children[0];
		Node g = node.children[1];
		if (node.name.equals("*")) {
			return "(" + getString(g) + ")*(" + deriv(f) + ")+(" + getString(f) + ")*(" + deriv(g) + ")";
		}
		if (node.name.equals("/")) {
			String out1 = "(" + deriv(f) + ")/(" + getString(g) + ")";
			String out2 = "(" + getString(f) + ")*(" + deriv(g) + ")/((" + getString(g) + ")^2)";
			return "(" + out1 + ")-(" + out2 + ")";
		}
		if (node.name.equals("^")) {
			return "((" + getString(f) + ")^(" + getString(g) + "))*((e)λ(" + getString(f) + ")*(" + deriv(g) + ")+("
					+ deriv(f) + ")*(" + getString(g) + ")/(" + getString(f) + "))";
		}
		if (node.name.equals("λ")) {
			String out = "(((" + deriv(g) + ")/(" + getString(g) + "))-(((" + getString(f) + ")λ(" + getString(g)
					+ "))*(" + deriv(f) + "))/(" + getString(f) + "))/((e)λ(" + getString(f) + "))";
			return "(" + out + ")";
		}
		if (node.name.equals("σ")) {
			if (f.name.equals("0")) {
				String out = "(0κ(" + getString(g) + "))*(" + deriv(g) + ")";
				return "(" + out + ")";
			}
			if (f.name.equals("1")) {
				String out = "(" + deriv(g) + ")/((1-(" + getString(g) + ")^2)^(1/2))";
				return "(" + out + ")";
			}
		}
		if (node.name.equals("κ")) {
			if (f.name.equals("0")) {
				String out = "0-(0σ(" + getString(g) + "))*(" + deriv(g) + ")";
				return "(" + out + ")";
			}
			if (f.name.equals("1")) {
				String out = "0-(" + deriv(g) + ")/((1-(" + getString(g) + ")^2)^(1/2))";
				return "(" + out + ")";
			}
		}
		if (node.name.equals("τ")) {
			if (f.name.equals("0")) {
				// String out="(1+(0τ("+getString(g)+"))^2)*("+deriv(g)+")";
				String out = "(" + deriv(g) + ")/((0κ(" + getString(g) + "))^2)";
				return "(" + out + ")";
			}
			if (f.name.equals("1")) {
				String out = "(" + deriv(g) + ")/(1+(" + getString(g) + ")^2)";
				return "(" + out + ")";
			}
		}
		return "";
	}

	public static String getString(Node node) {
		String inp = node.name;
		if (!(hasOperator(inp))) {
			return inp;
		}
		String str = "";
		if (node.children.length == 0) {
			return inp;
		}
		str = "(" + getString(node.children[0]) + ")" + node.name + "(" + getString(node.children[1]) + ")";
		if (node.name.equals("-") && node.children[0].name.equals("")) {
			str = "(0)-(" + getString(node.children[1]) + ")";
		}
		return str;
	}

	public static Node createNode(String inp) {
		double brac = 0;
		main: {
			for (;;) {
				if (inp.startsWith("(")) {
					brac++;
					for (int i = 1; i < inp.length(); i++) {
						if (inp.charAt(i) == ')') {
							brac--;
						}
						if (inp.charAt(i) == '(') {
							brac++;
						}
						if (brac == 0 && i != inp.length() - 1) {
							break main;
						}
					}
					inp = inp.substring(1, inp.length() - 1);
				} else {
					break;
				}
			}
		}
		Node root = new Node("", 2);
		if (!(hasOperator(inp))) {
			return new Node(inp, 0);
		}
		brac = 0;
		loop: {
			for (String op : ops) {
				for (int i = 0; i < inp.length(); i++) {
					char x = inp.charAt(i);
					if (x == '(') {
						brac++;
					} else if (x == ')') {
						brac--;
					} else if (brac == 0) {
						if (op.equals(x + "")) {
							root.name = x + "";
							inp.substring(i + 1, inp.length());
							/*
							 * if (inp.substring(0, i).equals("") || inp.substring(i+1,
							 * inp.length()).equals("")) { return null; }
							 */
							root.children[0] = createNode(inp.substring(0, i));
							root.children[1] = createNode(inp.substring(i + 1, inp.length()));
							break loop;
						}
					}
				}
			}
		}
		return root;
	}

	private static boolean hasOperator(String inp) {
		boolean bool = false;
		for (String op : ops) {
			bool = bool || inp.contains(op);
		}
		return bool;
	}

}