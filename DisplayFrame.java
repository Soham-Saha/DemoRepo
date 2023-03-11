import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class DisplayFrame {
	static JTextField input;
	static JScrollPane inputdisplay;
	static JScrollPane outputdisplay;
	static JButton nextderiv;
	static JButton prevderiv;
	static int derivnumber;
	static String resulttext;
	static String previnput;
	static JComboBox<String> lowervar;

	public static void main(String[] args) throws IOException {
		DisplayFrame dispfrm = new DisplayFrame();
		final JFrame frm = new JFrame("DerivCalc");
		frm.setSize(500 + 15, 200);
		frm.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frm.getSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frm.getSize().height / 2);
		// relocate(frm);
		frm.setLayout(null);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setFocusable(true);
		frm.setIconImage(Toolkit.getDefaultToolkit().getImage(dispfrm.getClass().getResource("/Icon.png")));
		previnput = "";
		derivnumber = 1;
		input = new JTextField("arcsin(a/x)");
		input.setLocation(5, 5);
		input.setSize(490, 20);
		frm.add(input);
		String[] choice = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z" };
		lowervar = new JComboBox<String>(choice);
		lowervar.setSelectedIndex(23);
		// lowervar.setLocation(input.getX()+input.getWidth()+5,input.getY());
		// lowervar.setSize(490-input.getWidth()-5,input.getHeight());
		// frm.add(lowervar);
		inputdisplay = new JScrollPane();
		outputdisplay = new JScrollPane();
		nextderiv = new JButton("Next Derivative");
		nextderiv.setSize(120, 20);
		nextderiv.setLocation(frm.getWidth() - 17 - 5 - nextderiv.getWidth(),
				outputdisplay.getY() + 5 + outputdisplay.getHeight());
		nextderiv.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loop: {
					if (derivnumber == 4) {
						JOptionPane.showMessageDialog(frm, "5th and higher derivatives are not supported",
								"DerivCalc: HigherDerivativeError", JOptionPane.WARNING_MESSAGE);
						break loop;
					}
					derivnumber++;
					modifyJFrame(new KeyEvent(input, 402, 0, 0, KeyEvent.VK_ENTER, ' '), frm);
				}
			}
		});
		prevderiv = new JButton("Previous Derivative");
		prevderiv.setSize(150, 20);
		prevderiv.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loop: {
					if (derivnumber == 1) {
						JOptionPane.showMessageDialog(frm, "No previous derivatives present",
								"DerivCalc: PreviousDerivativeError", JOptionPane.WARNING_MESSAGE);
						break loop;
					}
					derivnumber--;
					modifyJFrame(new KeyEvent(input, 402, 0, 0, KeyEvent.VK_ENTER, ' '), frm);
				}
			}
		});
		input.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent keypress) {
				modifyJFrame(keypress, frm);
			}
		});
		modifyJFrame(new KeyEvent(input, 402, 0, 0, KeyEvent.VK_ENTER, ' '), frm);
		frm.setVisible(true);
		input.grabFocus();
		input.selectAll();
	}

	/*
	 * private static void relocate(JFrame frm) {
	 * frm.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frm.
	 * getSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-frm.
	 * getSize().height/2); }
	 */

	protected static void modifyJFrame(KeyEvent keypress, JFrame frm) {
		if (!input.getText().replace(" ", "").equals(previnput)) {
			derivnumber = 1;
		}
		// f6 for copy output to clipboard
		if (keypress.getKeyCode() == KeyEvent.VK_F6) {
			StringSelection stringSelection = new StringSelection(resulttext);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
		// f5 to show output in text format
		if (keypress.getKeyCode() == KeyEvent.VK_F5) {
			showOutput(frm);
		}
		// f1 for help
		if (keypress.getKeyCode() == KeyEvent.VK_F1) {
			showHelp(frm);
		}
		// esc for end
		if (keypress.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		frm.remove(nextderiv);
		frm.remove(prevderiv);
		frm.remove(inputdisplay);
		frm.remove(outputdisplay);
		String dataFromUser = input.getText().replace(" ", "").toLowerCase();
		inputdisplay = display("", interpret(dataFromUser), 490, 5, 30);
		inputdisplay.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Input"),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		String out = dataFromUser;
		previnput = dataFromUser;
		for (int i = 0; i < derivnumber; i++) {
			out = Maths.mainfunc(out);
		}
		outputdisplay = display(out, interpret(dataFromUser), 490, 5,
				inputdisplay.getY() + 5 + inputdisplay.getHeight());
		outputdisplay.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Result"),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		nextderiv.setLocation(nextderiv.getX(), outputdisplay.getY() + 5 + outputdisplay.getHeight());
		prevderiv.setLocation(nextderiv.getX() - 5 - prevderiv.getWidth(), nextderiv.getY());
		refresh(frm);

	}

	private static void showOutput(JFrame frm) {
		JTextArea text = new JTextArea(5, 20);
		text.setLineWrap(true);
		text.setText("Output:\n" + resulttext);
		text.setEnabled(true);
		text.setEditable(false);
		text.setBackground(frm.getContentPane().getBackground());
		JScrollPane scpane = new JScrollPane(text);
		scpane.setLocation(text.getLocation());
		scpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JOptionPane.showMessageDialog(frm, scpane, "DerivCalc: OUTPUT", JOptionPane.INFORMATION_MESSAGE);

	}

	protected static void showHelp(JFrame frm) {
		String message = "DerivCalc, made by Soham Saha, is a derivative calculator (as the name suggests),\n"
				+ "which can help in calulating derivatives.\n"
				+ "Standard arithmetical operators can be derivated here over dx.\n"
				+ "Variable names containing x are taken to be x-based values and are derivated as x.\n"
				+ "Hyperbolic trigonometrical operators are not supported yet.\n"
				+ "Use logarithm: log a to base b in (a)log(b) format.\n" + "Use multiplication with the * symbol.\n"
				+ "For example: write elog(x^n-a/x) or (e^(i*x)-e^(-i*x))/2i";
		JTextArea text = new JTextArea(5, 20);
		text.setText(message);
		text.setEnabled(true);
		text.setEditable(false);
		text.setBackground(frm.getContentPane().getBackground());
		JOptionPane.showMessageDialog(frm, text, "DerivCalc: HELP", JOptionPane.INFORMATION_MESSAGE);

	}

	protected static String interpret(String inp) {
		inp = inp.replace(" ", "");
		inp = Maths.formatTrig(Maths.format(inp));
		inp = Maths.getString(Maths.createNode(inp));
		for (char x : inp.toCharArray()) {
			if (!Maths.isAlpha(x + "")) {
				if (!Character.isDigit(x)) {
					if (!Arrays.asList(Maths.ops).contains(x + "")) {
						if (!(x == '(' || x == ')' || x == '.')) {
							return "#ERRORCAPTURED#1";
						}
					}
				}
			}
		}
		if (inp.equals("")) {
			return "#ERRORCAPTURED#3";
		}
		inp = inp.replace(" ", "");
		if (Maths.brackError(inp)) {
			return "#ERRORCAPTURED#2";
		}
		Node node = Maths.createNode(inp);
		if (Node.hasBlankChild(node)) {
			return "#ERRORCAPTURED#4";
		}
		while (!Maths.getString(node).equals(Maths.getString(Maths.simplify(node)))) {
			node = Maths.simplify(node);
		}
		inp = Maths.getString(Maths.simplify(node));
		return Maths.clearTrig(Maths.clearFormat(inp));
	}

	protected static void refresh(JFrame frm) {
		frm.add(input);
		frm.add(inputdisplay);
		frm.add(outputdisplay);
		frm.add(nextderiv);
		frm.add(prevderiv);
		frm.setSize(frm.getWidth(), 25 + 39 + input.getHeight() + inputdisplay.getHeight() + outputdisplay.getHeight()
				+ nextderiv.getHeight());
		// relocate(frm);
		input.grabFocus();
		frm.invalidate();
		frm.validate();
		frm.repaint();
	}

	public static JScrollPane display(String result, String inp, int width, int x, int y) {
		resulttext = "";
		String output = "";
		if (!(result.startsWith("#ERRORCAPTURED#") || inp.startsWith("#ERRORCAPTURED#"))) {
			resulttext = Maths.clearTrig(Maths.clearFormat(result));
			String primesymbol = "";
			if (derivnumber > 3) {
				primesymbol = "^{(" + derivnumber + ")}";
			} else {
				for (int i = 0; i < derivnumber; i++) {
					primesymbol += "'";
				}
			}
			if (result.equals("")) {
				output = "\\left[" + JLaTexMathsTranslator.translate(Maths.formatTrig(Maths.format(inp))) + "\\right]"
						+ primesymbol;
			} else {
				output = /*
							 * "\\left["+JLaTexMathsTranslator.translate(Maths.formatTrig(Maths.format(inp))
							 * )+"\\right]'="+
							 */
						JLaTexMathsTranslator.translate(Maths.formatTrig(Maths.format(result)));
			}
		}
		// To debug error open comment below
		/*
		 * else{ if (result.startsWith("#ERRORCAPTURED#")) { System.out.println(result);
		 * } if (inp.startsWith("#ERRORCAPTURED#")) { System.out.println(inp); }
		 * System.out.println("----"); }
		 */
		TeXFormula teXFormula = new TeXFormula("{\\tiny{" + output + "}}");
		TeXIcon icon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
		final BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		icon.paintIcon(new JLabel(), image.getGraphics(), icon.getIconWidth(), icon.getIconHeight());
		JLabel lbl = new JLabel();
		lbl.setSize(width, image.getHeight() + 50);
		if (lbl.getHeight() > 200) {
			lbl.setSize(width, 200);
		}
		JScrollPane scpane = new JScrollPane(lbl);
		scpane.setLocation(x, y);
		scpane.setSize(width, lbl.getHeight());
		scpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		lbl.setIcon(icon);
		return scpane;
	}

}
