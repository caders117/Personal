package blackjackGUI;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class UITest {
	public static void main(String[] args){
		BlackjackUI ui = new BlackjackUI();
		JFrame frame = new JFrame("Blackjack");
		frame.add(ui, BorderLayout.CENTER);
		
		frame.setSize(1000, 1000);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ui.start();
	}
}