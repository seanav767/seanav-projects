import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

public class CalculatorGui extends JFrame {

	private static final long serialVersionUID = 1L;

	Stack<Double> numStack = new Stack<>();
	Stack<Character> ops = new Stack<Character>();
	Stack<Double> reverseNumStack = new Stack<Double>();
	Stack<Character> reverseOps = new Stack<Character>();

	boolean previousEqualButtonClicked = false;

	public static void main(String[] args) {

		new CalculatorGui();
	}

	JButton btnAdd, btnSubtract, btnDivide, btnMultiply, btnClear, btnEquals;
	JButton numBtn[];
	JTextField output;
	String current;

	public CalculatorGui() {
		super("Seanav's calculator");

		JPanel mainPanel = new JPanel();

		// creating the sub panels
		JPanel row1 = new JPanel(new GridLayout(1, 4));
		JPanel row2 = new JPanel(new GridLayout(1, 4));
		JPanel row3 = new JPanel(new GridLayout(1, 4));
		JPanel row4 = new JPanel(new GridLayout(1, 4));

		// Initialising components (all the buttons on the calculator)

		output = new JTextField(0);
		btnAdd = new JButton("+");
		btnSubtract = new JButton("-");
		btnDivide = new JButton("÷");
		btnMultiply = new JButton("X");
		btnClear = new JButton("AC");
		btnEquals = new JButton("=");

		// Instantiating my action listeners

		NumHandler numHandler = new NumHandler();
		ArithmeticHandler arithmeticHandler = new ArithmeticHandler();
		ProcessOtherBtn processOtherBtn = new ProcessOtherBtn();

		// Styling and adding action listeners
		numBtn = new JButton[11];
		for (int count = 0; count < numBtn.length - 1; count++) {
			numBtn[count] = new JButton(String.valueOf(count));
			numBtn[count].addActionListener(numHandler);
		}

		// Initialising my calculators operands
		current = "";
		// previous = "";

		// Setting font colour
		btnAdd.setForeground(Color.BLUE);
		btnSubtract.setForeground(Color.BLUE);
		btnDivide.setForeground(Color.BLUE);
		btnMultiply.setForeground(Color.BLUE);
		btnClear.setForeground(Color.YELLOW);
		btnEquals.setForeground(Color.RED);

		// Setting font and making it Bold

		btnAdd.setFont(new Font("Arial", Font.BOLD, 22));
		btnSubtract.setFont(new Font("Arial", Font.BOLD, 22));
		btnDivide.setFont(new Font("Arial", Font.BOLD, 22));
		btnMultiply.setFont(new Font("Arial", Font.BOLD, 22));
		btnClear.setFont(new Font("Arial", Font.BOLD, 20));
		btnEquals.setFont(new Font("Arial", Font.BOLD, 22));

		// changing the size of the button
		btnSubtract.setPreferredSize(new Dimension(50, 50));
		btnDivide.setPreferredSize(new Dimension(50, 50));
		btnMultiply.setPreferredSize(new Dimension(50, 50));
		btnClear.setPreferredSize(new Dimension(50, 50));
		btnEquals.setPreferredSize(new Dimension(50, 50));
		btnAdd.setPreferredSize(new Dimension(50, 50));
		btnClear.setPreferredSize(new Dimension(50, 50));
		numBtn[0].setPreferredSize(new Dimension(50, 50));
		numBtn[1].setPreferredSize(new Dimension(50, 50));
		numBtn[2].setPreferredSize(new Dimension(50, 50));
		numBtn[3].setPreferredSize(new Dimension(50, 50));
		numBtn[4].setPreferredSize(new Dimension(50, 50));
		numBtn[5].setPreferredSize(new Dimension(50, 50));
		numBtn[6].setPreferredSize(new Dimension(50, 50));
		numBtn[7].setPreferredSize(new Dimension(50, 50));
		numBtn[8].setPreferredSize(new Dimension(50, 50));
		numBtn[9].setPreferredSize(new Dimension(50, 50));

		// Adding action listeners to arithmetic buttons

		btnDivide.addActionListener(arithmeticHandler);
		btnMultiply.addActionListener(arithmeticHandler);
		btnAdd.addActionListener(arithmeticHandler);
		btnSubtract.addActionListener(arithmeticHandler);

		// Adding action listeners to other buttons
		btnClear.addActionListener(processOtherBtn);
		btnEquals.addActionListener(processOtherBtn);

		// Styling the output display for the calculator
		output.setMaximumSize(new Dimension(500, 45));
		output.setFont(new Font("Arial", Font.BOLD, 28));
		output.setDisabledTextColor(new Color(0, 0, 0));
		output.setMargin(new Insets(0, 5, 0, 0));
		output.setText("0");

		// adding components to row 1
		row1.add(numBtn[7]);
		row1.add(numBtn[8]);
		row1.add(numBtn[9]);
		row1.add(btnDivide);

		// adding components to row 2
		row2.add(numBtn[4]);
		row2.add(numBtn[5]);
		row2.add(numBtn[6]);
		row2.add(btnMultiply);

		// adding components to row 3
		row3.add(numBtn[1]);
		row3.add(numBtn[2]);
		row3.add(numBtn[3]);
		row3.add(btnSubtract);

		// adding components to row 4
		row4.add(btnClear);
		row4.add(numBtn[0]);
		row4.add(btnEquals);
		row4.add(btnAdd);

		/*
		 * Setting layout for main panel adding respective rows in accordance using
		 * PAGE_AXIS which arranges components inside the panel createRigidArea makes a
		 * space between the buttons and display
		 */

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(output);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 4)));
		mainPanel.add(row1);
		mainPanel.add(row2);
		mainPanel.add(row3);
		mainPanel.add(row4);

		/*
		 * need to set visibility to true or it will be invisible making the shape of
		 * the calculator using setSize() method from JFrame adding main panel and
		 * functionality with the close button
		 */

		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(300, 300);

	}

	public void allClear() {
		current = "";
		// previous = "";
		// operator = null;
		output.setText(current);
	}

	public void updateDisplay() {
		output.setText(decimalDecider(current));
	}

	public void appendToDisplay(String num) {
		current += num;
	}

	public String decimalDecider(String s) {

		String str = s;

		// check whether . is there

		if (s.contains(".")) {

			String[] parts = s.split(Pattern.quote("."));

			String integerSeg = parts[0];
			String decimalSeg = parts[1];

			if (decimalSeg.equals("0")) {
				str = integerSeg;
			}

		}

		return str;
	}

	public void calculator() {

		String str = current;

		//System.out.println("From calculator  : Current =" + str);

		String result = "";

		try {

			double correctResult = evaluate(str);

			result = Double.toString(correctResult);

			//System.out.println("Result inside calculator : " + result);

		}

		catch (IllegalArgumentException e) {

			result = "Error";

		}

		current = decimalDecider(String.valueOf(result));
	}

	/*
	 * numHandler class is responsible for handling events with all number buttons
	 * on my calculator
	 * 
	 * @param ActionEvent used by the event package in java.awt.event.ActionEvent;
	 */

	private class NumHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (previousEqualButtonClicked) {
				allClear();
				previousEqualButtonClicked = false;
			}
			JButton selectedBtn = (JButton) e.getSource();
			for (JButton btn : numBtn) {
				if (selectedBtn == btn) {
					appendToDisplay(btn.getText());
					updateDisplay();
				}
			}

		}

	}

	/*
	 * In this method i compare each operator button on the calculator to the one
	 * clicked so i know which operator is selected When a button is selected we
	 * pass the text to chooseOperator()
	 * 
	 */

	private class ArithmeticHandler implements ActionListener {
				
		@Override
		public void actionPerformed(ActionEvent e) {
			previousEqualButtonClicked = false;
			JButton selectedBtn = (JButton) e.getSource();
			if (selectedBtn == btnMultiply) {
				appendToDisplay(btnMultiply.getText());
			} else if (selectedBtn == btnAdd) {
				appendToDisplay(btnAdd.getText());
			} else if (selectedBtn == btnSubtract) {
				appendToDisplay(btnSubtract.getText());
			} else if (selectedBtn == btnDivide) {
				appendToDisplay(btnDivide.getText());
			}

			updateDisplay();
		}
	}

	/*
	 * This method is supposed to handle the other buttons such as the equals button
	 * and clear button when they are clicked on the methods clear() or calculate()
	 * are called.
	 */

	private class ProcessOtherBtn implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			previousEqualButtonClicked = false;
			JButton selectedBtn = (JButton) e.getSource();
			if (selectedBtn == btnClear) {
				allClear();
			} else if (selectedBtn == btnEquals) {
				calculator();
				previousEqualButtonClicked = true;
			}
			updateDisplay();

		}
	}

	public double applyOp(char op, double b, double a) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case 'X':
			return a * b;
		case '÷':
			if (b == 0)
				throw new IllegalArgumentException("Cannot divide by zero");
			return a / b;
		}
		return 0;
	}

	public String fixedString(String junkString) {

		ArrayList<Character> al = new ArrayList<Character>();

		// First Check if the last character of the string is an operator
		// if it is need to get rid of that

		String correctString = "";

		int lengthOfIncomingString = junkString.length();

		// System.out.println("Length of String is : " + lengthOfIncomingString );

		// System.out.println(junkString.charAt(lengthOfIncomingString - 1));

		int k = lengthOfIncomingString;

		while (k > 0) {

			if (junkString.charAt(k - 1) == '÷' || junkString.charAt(k - 1) == 'X' || junkString.charAt(k - 1) == '+'
					|| junkString.charAt(k - 1) == '-') {
				junkString = junkString.substring(0, k - 1);

				k--;

			}

			else {
				break;
			}

		}

		// System.out.println("Junk String now with its end fix is : " + junkString);

		// more fix on the string

		// it cannot start with an operator; only exception is -

		Character ch = junkString.charAt(0);
		if (ch == '-') {
			junkString = "0" + junkString;
		}

		// System.out.println("Junk String with beginning fix is: " + junkString);

		// Now will put the character elements in arraylist; if two operators come next
		// to each other
		// first one will be ignored (left to right traverse)

		Character ch1;

		for (int i = 0; i < junkString.length(); i++) {

			ch1 = junkString.charAt(i);

			al.add(ch1);

		}

		for (int j = 0; j < al.size(); j++) {

			if (j + 1 < al.size()) {
				if ((al.get(j) == '÷' || al.get(j) == 'X' || al.get(j) == '+' || al.get(j) == '-')
						&& (al.get(j + 1) == '÷' || al.get(j + 1) == 'X' || al.get(j + 1) == '+'
								|| al.get(j + 1) == '-')) {
					al.remove(j);
				}
			}

		}

		String almostFixedString = "";

		for (int j = 0; j < al.size(); j++) {

			// System.out.println(al.get(j));

			almostFixedString = almostFixedString + al.get(j);

		}

		// System.out.println("Junk String now after operator fix is : " +
		// almostFixedString);

		// only thing remain is - after operator if the immediate number is 0 - need to
		// remove that

		for (int p = 0; p < al.size(); p++) {

			if ((p + 2) < al.size()) {

				if ((al.get(p) == '÷' || al.get(p) == 'X' || al.get(p) == '+' || al.get(p) == '-')
						&& (al.get(p + 1) == '0')) {
					al.remove(p + 1);
				}

			}

		}

		for (int j = 0; j < al.size(); j++) {

			// System.out.println(al.get(j));

			correctString = correctString + al.get(j);

		}

		// System.out.println("Inside fixedString - Correct String is : " +
		// correctString);

		return correctString;
	}

	public void dividePriority(String expression) {
		
		//System.out.println("String came to divide : " + expression);

		char[] tokens = expression.toCharArray();

		for (int i = 0; i < tokens.length; i++) {

			if (tokens[i] >= '0' && tokens[i] <= '9') {
				StringBuffer sbuf = new StringBuffer();
				// there may be more than one digits in a number
				while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
					sbuf.append(tokens[i]);
					if ((i + 1) < tokens.length && tokens[i + 1] >= '0' && tokens[i + 1] <= '9') {
						i++;
					} else {
						break;
					}
				}
				numStack.push(Double.parseDouble(sbuf.toString()));
			} else {
				// it means we got operator here
				if (ops.isEmpty()) {
					ops.push(tokens[i]);
					continue;
				}

				else {
					char op1 = ops.peek();
					if (op1 == '÷') {
						double num1, num2;
						num1 = numStack.pop();
						num2 = numStack.pop();
						//double ans = num2 / num1;
						try {
							
							double ans = applyOp(op1, num1, num2);
							numStack.push(ans);
							ops.pop();
							ops.push(tokens[i]);
							
						}
						catch (IllegalArgumentException ie) {
							
							throw new IllegalArgumentException("Cannot divide by zero");
							
						}
						//double ans = applyOp(op1, num1, num2);
						//numStack.push(ans);
						//ops.pop();
						//ops.push(tokens[i]);

					} else {
						ops.push(tokens[i]);
					}
				}

			}
		}

		// only the top could be a / because that was the last operator
		// we need to clear it
		if (ops.size() > 0) {
			//System.out.println("Size of ops stack is : " + ops.size());
			//System.out.println("ops stack peek is : " + ops.peek());
			//System.out.println("Size of NumStack is: " + numStack.size());
			if (ops.peek() == '÷') {
				ops.pop();
				double num1, num2;
				num1 = numStack.pop();
				//System.out.println("Number 1 is : " + num1);
				num2 = numStack.pop();
				//System.out.println("Number 2 is : " + num2);
				//double ans = num2 / num1;
				try {
					
					double ans = applyOp('÷', num1, num2);
					numStack.push(ans);
					
				}
				
				catch (IllegalArgumentException ie) {
					throw new IllegalArgumentException("Cannot divide by zero");
				}
				//double ans = applyOp('÷', num1, num2);
				//numStack.push(ans);
			}

		}

	}

	public void multiplyPriority(Stack<Double> containsNumber, Stack<Character> containsCharacter) {

		Double number1;
		Double number2;
		Double answer;

		number1 = containsNumber.pop();

		while (!containsCharacter.isEmpty()) {

			if (numStack.isEmpty()) {
				numStack.push(number1);

			} else {
				Character ch1 = containsCharacter.pop();

				if (ch1 == 'X') {

					number1 = numStack.pop();

					number2 = containsNumber.pop();

					//answer = number1 * number2;
					answer = applyOp(ch1, number1, number2);

					numStack.push(answer);
				} else {
					ops.push(ch1);
					number2 = containsNumber.pop();
					numStack.push(number2);

				}

			}

		}

	}

	public void addPriority(Stack<Double> containsNumber, Stack<Character> containsCharacter) {

		Double number1;
		Double number2;
		Double answer;

		number1 = containsNumber.pop();

		while (!containsCharacter.isEmpty()) {

			if (numStack.isEmpty()) {
				numStack.push(number1);

			} else {
				Character ch1 = containsCharacter.pop();

				if (ch1 == '+') {

					number1 = numStack.pop();

					number2 = containsNumber.pop();

					//answer = number1 + number2;
					answer = applyOp(ch1, number1, number2);

					numStack.push(answer);
				} else {
					ops.push(ch1);
					number2 = containsNumber.pop();
					numStack.push(number2);

				}

			}

		}

	}

	public void subtractPriority(Stack<Double> containsNumber, Stack<Character> containsCharacter) {

		Double number1;
		Double number2;
		Double answer;

		number1 = containsNumber.pop();

		while (!containsCharacter.isEmpty()) {

			if (numStack.isEmpty()) {
				numStack.push(number1);

			} else {
				Character ch1 = containsCharacter.pop();

				if (ch1 == '-') {

					number1 = numStack.pop();

					number2 = containsNumber.pop();

					//answer = number1 - number2;
					answer = applyOp(ch1, number2, number1);

					numStack.push(answer);
				} else {
					ops.push(ch1);
					number2 = containsNumber.pop();
					numStack.push(number2);

				}

			}

		}

	}

	public void reversingStacks(Stack<Double> num, Stack<Character> ch) {

		while (!num.isEmpty()) {

			Double tempDouble = num.pop();
			reverseNumStack.push(tempDouble);
		}

		while (!ch.isEmpty()) {
			Character tempChar = ch.pop();
			reverseOps.push(tempChar);
		}

	}

	public Double evaluate(String str) {

		String s = "";

		//System.out.println("Inside evaluate str is : " + str);

		Double answer = 0.0;

		s = fixedString(str);

		//System.out.println("After fixing string : " + s);
		try {
			dividePriority(s);
		}
		catch (IllegalArgumentException ie) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		//dividePriority(s);
		reversingStacks(numStack, ops);
		if (reverseNumStack.size() > 1 && reverseOps.size() > 0) {
			// System.out.println("Here 1");
			multiplyPriority(reverseNumStack, reverseOps);
			reversingStacks(numStack, ops);
			if (reverseNumStack.size() > 1 && reverseOps.size() > 0) {
				// System.out.println("Here 2");
				addPriority(reverseNumStack, reverseOps);
				reversingStacks(numStack, ops);
				if (reverseNumStack.size() > 1 && reverseOps.size() > 0) {
					// System.out.println("Here 3");
					subtractPriority(reverseNumStack, reverseOps);
				}
			}
		}

		if (numStack.size() == 1) {
			answer = numStack.pop();
		}

		if (reverseNumStack.size() == 1) {
			answer = reverseNumStack.pop();
		}

		return answer;

	}

}