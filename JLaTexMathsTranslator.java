public class JLaTexMathsTranslator {

	public static String translate(String inp) {
		inp = Maths.formatTrig(Maths.format(inp));
		String convtext = "";
		Node node = Maths.createNode(inp);
		String str1 = "";
		String str2 = "";
		if (node.children.length > 0) {
			str1 = translate(Maths.getString(node.children[0]));
			str2 = translate(Maths.getString(node.children[1]));
			if (node.children[0].children.length != 0) {
				str1 = "\\left(" + str1 + "\\right)";
			}
			if (node.children[1].children.length != 0) {
				str2 = "\\left(" + str2 + "\\right)";
			}
		}
		switch (node.name) {
		case "+":
			convtext = "{{" + str1 + "}+{" + str2 + "}}";
			break;
		case "-":
			convtext = "{{" + str1 + "}-{" + str2 + "}}";
			break;
		case "*":
			convtext = "{{\\left(" + translate(Maths.getString(node.children[0])) + "\\right)}{\\left("
					+ translate(Maths.getString(node.children[1])) + "\\right)}}";
			break;
		case "/":
			convtext = "{\\frac {" + translate(Maths.getString(node.children[0])) + "}{"
					+ translate(Maths.getString(node.children[1])) + "}}";
			break;
		case "^":
			convtext = "{{" + str1 + "}^{" + str2 + "}}";
			// If want to remove sqrt symbol and replace with just exponential symbol,
			// remove next if block
			if (node.children[1].name.equals("/")) {
				if (node.children[1].children[0].name.equals("1")) {
					if (node.children[1].children[1].name.equals("2")) {
						convtext = "\\sqrt{" + translate(Maths.getString(node.children[0])) + "}";
					} else {
						convtext = "\\sqrt[" + translate(Maths.getString(node.children[1].children[1])) + "]{"
								+ translate(Maths.getString(node.children[0])) + "}";
					}
				}
			}
			break;
		case "λ":
			if (removeSecondBracket(str1.replaceAll(" ", "")).equalsIgnoreCase("e")) {
				convtext = "{\\ln{" + str2 + "}}";
			} else {
				convtext = "{\\log_{" + str1 + "}{" + str2 + "}}";
			}
			break;
		case "σ":
			if (node.children[0].name.equals("0")) {
				convtext = "{\\sin\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			if (node.children[0].name.equals("1")) {
				convtext = "{\\arcsin\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			break;
		case "κ":
			if (node.children[0].name.equals("0")) {
				convtext = "{\\cos\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			if (node.children[0].name.equals("1")) {
				convtext = "{\\arccos\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			break;
		case "τ":
			if (node.children[0].name.equals("0")) {
				convtext = "{\\tan\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			if (node.children[0].name.equals("1")) {
				convtext = "{\\arctan\\left({" + translate(Maths.getString(node.children[1])) + "}\\right)}";
			}
			break;
		default:
			convtext = "{" + node.name + "}";
			if (node.name.equalsIgnoreCase("pi")) {
				convtext = "{\\pi}";
			}
			break;
		}
		return convtext;
	}

	private static String removeSecondBracket(String str) {
		return str.replaceAll("\\{", "").replaceAll("\\}", "");
	}

}