package blackjackGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BlackjackUI extends JPanel implements ComponentListener, ActionListener, ChangeListener{
	private BlackjackEngine game;
	private JPanel comp;
	private JPanel player;
	private JLabel[][] cardLbls;
	private JLabel cardBack;
	private int cardWidth, cardHeight;
	private JPanel compCards, playerCards;
	private JPanel statusPnl;
	private JLabel status;
	private JPanel playerBtns;
	private JButton hit, stand;
	private JPanel header;
	private JButton reset;
	private boolean compGoing;
	private JPanel score;
	private JLabel scoreLbl;
	private JPanel playerHeader;
	private JSlider fontSize;
	
	private final BufferedImage[][] CARD_SPRITES = readCardSheet();
	private final BufferedImage CARD_BACK_IMG = readCardBack();
	private final Object RESET_OPTIONS[] = {"Reset", "Reset all data", "Cancel"};
	private final int COMP_WAIT_TIME = 1500;
	private final Color BACKGROUND = new Color(0, 204, 0);
	
	private void newRound(){
		game.init();
		hit.setEnabled(true);
		stand.setEnabled(true);
		status.setText("Your turn.");
		compGoing = false;
		updateCardPanels();
		start();
	}
	
	public void start(){
		checkBlackJack();
	}
	
	public void init(){
		setDefaultSize(43);
		
		game = new BlackjackEngine();
		cardBack = new JLabel(new ImageIcon(CARD_BACK_IMG));
		compCards = new JPanel(new GridLayout(1, 0));  
		playerCards = new JPanel(new GridLayout(1, 0));
		playerBtns = new JPanel();       
		cardLbls = convertSpritesToJLabels(CARD_SPRITES);
		comp = new JPanel();
		player = new JPanel();
		hit = new JButton("(H) Hit");
		stand = new JButton("(S) Htand");
		statusPnl = new JPanel();
		status = new JLabel();
		statusPnl.add(status, SwingConstants.CENTER);
		header = new JPanel();
		header.setLayout(new BorderLayout());
		reset = new JButton("(R) Reset");
		score = new JPanel();
		scoreLbl = new JLabel("You            0 - 0            Dealer");
		playerHeader = new JPanel();
		fontSize = new JSlider(JSlider.HORIZONTAL, 0, 50, 24);
		fontSize.addChangeListener(this);
		fontSize.setMajorTickSpacing(25);
		fontSize.setMinorTickSpacing(5);
		fontSize.setPaintTicks(true);
		fontSize.setPaintLabels(true);
	}
	
	public BlackjackUI(){
		
		init();
		
		setBackground(BACKGROUND);
		
		setLayout(new GridLayout(2, 0));
		
		add(comp);
		add(player);
		
		comp.setOpaque(false);	//set panel to transparent
		player.setOpaque(false);
		
		compCards.setOpaque(false);
		compCards.addComponentListener(this);
		playerCards.setOpaque(false);
		playerCards.addComponentListener(this);
		
		score.add(scoreLbl);

		comp.setLayout(new BorderLayout());
		comp.add(header, BorderLayout.NORTH);
		header.add(score, BorderLayout.NORTH);
		JLabel dealer = new JLabel("Dealer", SwingConstants.LEFT);
		dealer.setBorder(new EmptyBorder(0, 10, 0, 10));
		dealer.setOpaque(true);										// JLabel default is opaque(false) -- transparent, so you can't see background color
		dealer.setBackground(BACKGROUND);
		header.add(dealer, BorderLayout.WEST);
		header.add(reset, BorderLayout.EAST);
		comp.add(compCards, BorderLayout.CENTER);
		comp.add(statusPnl, BorderLayout.SOUTH);
		for(Card i : game.getComp()){
			compCards.add(cardLbls[i.getSuit()][i.getFaceAsInt()]);
		}		
		
		player.setLayout(new BorderLayout());
		player.add(playerHeader, BorderLayout.NORTH);
		playerHeader.setLayout(new BorderLayout());
		
		
		
		//playerHeader.add(fontSize, BorderLayout.EAST);
		
		
		
		
		JLabel you = new JLabel("You", SwingConstants.LEFT);
		you.setBorder(new EmptyBorder(0, 25, 0, 25));
		you.setOpaque(true);
		you.setBackground(BACKGROUND);
		playerHeader.add(you, BorderLayout.WEST);
		player.add(playerCards, BorderLayout.CENTER);
		for(Card i : game.getPlayer()){
			playerCards.add(cardLbls[i.getSuit()][i.getFaceAsInt()]);
		}
		
		System.out.println(printArrayList(game.getComp()) + "\t\t" + printArrayList(game.getPlayer()));
		System.out.println(game.getCompTotal() + "\t\t" + game.getPlayerTotal());
		
		player.add(playerBtns, BorderLayout.SOUTH);
		
		hit.setPreferredSize(stand.getPreferredSize());
		
		// Hit button actionlistener and keybinding
		hit.setActionCommand("hit");
		hit.addActionListener(this);
		
		hit.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "Hit");
		hit.getActionMap().put("Hit", buttonPressed);
		
		// Stand button actionListene and Keybinding
		stand.setActionCommand("stand");
		stand.addActionListener(this);
		
		stand.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Stand");
		stand.getActionMap().put("Stand", buttonPressed);
		
		
		playerBtns.add(hit);
		playerBtns.add(stand);
		
		reset.setActionCommand("Reset");
		reset.addActionListener(this);
		
		reset.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "Reset");
		reset.getActionMap().put("Reset", buttonPressed);
		
		
	}
	
	AbstractAction buttonPressed = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(e.getActionCommand().equals("h"))
        		hit.doClick();
        	else if(e.getActionCommand().equals("s"))
        		stand.doClick();
        	else if(e.getActionCommand().equals("r"))
        		reset.doClick();
        }
    };
	
	private void checkBlackJack(){
		
		if(game.getPlayerTotal() != game.getCompTotal()){
			if(game.getPlayerTotal() == 21) {
				game.incrementPlayerScore(2);
				scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
				if(JOptionPane.showConfirmDialog(
						this, 
						"You got Blackjack!  Play Again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
					newRound();
				}
			} else if(game.getCompTotal() == 21) {
				compGoing = true;
				game.incrementCompScore(2);
				scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
				updateCardPanels();
				if(JOptionPane.showConfirmDialog(
						this, 
						"Computer got Blackjack!  Play Again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
					newRound();
				}
			}
		} else if (game.getPlayerTotal() == 21 && game.getPlayerTotal() == 21){
			compGoing = true;
			updateCardPanels();
			if(JOptionPane.showConfirmDialog(
					this, 
					"Push!  Play Again?", 
					"Result", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
				newRound();
			}
		}
	}
	
	public String printArrayList(ArrayList<Card> a){
    	String ret = "";
    	for(int i = 0; i < a.size(); i++){
    		ret += a.get(i) + (i != a.size() - 1 ? ", " : "");
    	}
    	return ret;
    }
	
	public JLabel[][] convertSpritesToJLabels(BufferedImage[][] b){
		JLabel[][] jl = new JLabel[b.length][b[0].length];
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[i].length; j++){
				jl[i][j] = new JLabel(new ImageIcon(b[i][j]));
			}
		}
		return jl;
	}
	
	
	public JLabel[][] scaleCards(BufferedImage[][] b, double percent){
		JLabel[][] jl = new JLabel[b.length][b[0].length];
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[i].length; j++){
				jl[i][j] = new JLabel(new ImageIcon(CARD_SPRITES[i][j].getScaledInstance((int)(cardWidth * percent), (int)(cardHeight * percent), Image.SCALE_SMOOTH)));
			}
		}
		return jl;
	}
	
	private BufferedImage readCardBack(){
		try {
			BufferedImage spriteSheet = ImageIO.read(this.getClass().getResource("/resources/Deck_Spritesheet.gif"));
			
			int width = spriteSheet.getWidth();
			int height = spriteSheet.getHeight();
			int spriteWidth = width / 13;
			int spriteHeight = height / 5;
			
			return spriteSheet.getSubimage(5 * spriteWidth, 4 * spriteHeight, spriteWidth, spriteHeight);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	// See ReadSpriteSheet.java in Tools project or Github for more info
	public BufferedImage[][] readCardSheet(){
		try {
			BufferedImage spriteSheet = ImageIO.read(this.getClass().getResource("/resources/Deck_Spritesheet.gif"));
			BufferedImage[][] sprites = new BufferedImage[4][13];
			
			int width = spriteSheet.getWidth();
			int height = spriteSheet.getHeight();
			int spriteWidth = width / 13;
			int spriteHeight = height / 5;
			
			cardWidth = spriteWidth;
			cardHeight = spriteHeight;
			
			for(int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++){
					sprites[i][j] = spriteSheet.getSubimage(
							j * spriteWidth, 
							i * spriteHeight, 
							spriteWidth, 
							spriteHeight
					);
				}
			}
			
			return sprites;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Deals with hiDPI swing scaling problems
	 * Taken from http://stackoverflow.com/questions/26877517/java-swing-on-high-dpi-screen
	 */
	private void setDefaultSize(int size) {
	
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);
		
		for (Object key : keys) {
			
			if (key != null && key.toString().toLowerCase().contains("font")) {
				
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
				font = font.deriveFont((float)size);
				UIManager.put(key, font);
				}
			}
		}
	}
	
	public void updateCardPanels(){
		playerCards.removeAll();
		compCards.removeAll();
		for(Card i : game.getPlayer()){
			playerCards.add(cardLbls[i.getSuit()][i.getFaceAsInt()]);
		}
		if(compGoing){
			for(Card i : game.getComp()){
				compCards.add(cardLbls[i.getSuit()][i.getFaceAsInt()]);
			}
		} else {
			compCards.add(cardLbls[game.getComp().get(0).getSuit()][game.getComp().get(0).getFaceAsInt()]);		// First card in Dealer's hand
			compCards.add(cardBack);																			// Image of card back
		}
		
		playerCards.repaint();
		compCards.repaint();
		playerCards.validate();
		this.validate();
		scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
		System.out.println("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
		System.out.println("Player: Total = " + game.getPlayerTotal() + "\t\tDeck: " + printArrayList(game.getPlayer()));
		System.out.println("Dealer: Total = " + game.getCompTotal() + "\t\tDeck: " + printArrayList(game.getComp()));
		System.out.println();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		double newHeight = e.getComponent().getHeight() / 2.0;
		double newWidth = e.getComponent().getWidth() / 6.0;
		double scaleToWidth = newWidth / cardWidth;
		double scaleToHeight = newHeight / cardHeight;
		double scalePercent = scaleToHeight < scaleToWidth ? scaleToHeight : scaleToWidth;
		
		// Scale cards to the lease scale so they don't overlap at all
		cardLbls = scaleCards(CARD_SPRITES, scalePercent);
		cardBack = new JLabel(new ImageIcon(CARD_BACK_IMG.getScaledInstance((int)(cardWidth * scalePercent), (int)(cardHeight * scalePercent), Image.SCALE_SMOOTH)));
		
		
		// Replace original panel cards with resized cards
		updateCardPanels();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Reset")){
			
			int action = JOptionPane.showOptionDialog(
					this, 
					"Are you sure?", 
					"Reset", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.WARNING_MESSAGE, 
					null, 
					RESET_OPTIONS, 
					null
				);
			if(action == JOptionPane.YES_OPTION)
				newRound();
			else if (action == JOptionPane.NO_OPTION){
				game = new BlackjackEngine();
				newRound();
			}
					
				
		} else if(e.getActionCommand().equals("hit")) {
			game.playerHit();
			status.setText("You hit.");
			updateCardPanels();
			if(game.getPlayerTotal() > 21){
				hit.setEnabled(false);
				stand.setEnabled(false);
				game.incrementCompScore(1);
				scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
				status.setText("You Busted.");
				if(JOptionPane.showConfirmDialog(
						this, 
						"You Busted.  Play again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
					setDefaultSize(50);
					newRound();
				}	
			}
			
		} else if (e.getActionCommand().equals("stand")){
			hit.setEnabled(false);
			stand.setEnabled(false);
			status.setText("You Stand.");
			compGoing = true;
			updateCardPanels();
			Timer t = new Timer(COMP_WAIT_TIME, new ActionListener(){
	            @Override
	            public void actionPerformed(ActionEvent e) {

	            	if(game.getCompTotal() < 17){
	            		game.compHit();
	            		status.setText("Dealer hits.");
	            		updateCardPanels();
	            		if(game.checkCompBust()) {
	            			((Timer) e.getSource()).stop();
	            			onTimerEnd();
	            		}
	            	} else {
	            		status.setText("Dealer Stands.");
	            		((Timer) e.getSource()).stop();
	            		onTimerEnd();
	            	}
	            }
	        });
			
			t.setInitialDelay(COMP_WAIT_TIME);
			t.start();
		}
	}
	
	public void onTimerEnd(){
		if(game.checkCompBust()){
			game.incrementPlayerScore(1);
			scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
			status.setText("Dealer Busted.");
			if(JOptionPane.showConfirmDialog(
					this, 
					"Dealer Busted.  Play again?", 
					"Result", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
				newRound();
			}	
		} else {
			if(game.getPlayerTotal() > game.getCompTotal()) {
				game.incrementPlayerScore(1);
				scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
				status.setText("You win!");
				if(JOptionPane.showConfirmDialog(
						this, 
						"You win!  Play Again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
					newRound();
				}
			} else if (game.getCompTotal() > game.getPlayerTotal()){
				game.incrementCompScore(1);
				scoreLbl.setText("You            " + game.getPlayerScore() + " - " + game.getCompScore() + "            Dealer");
				status.setText("Dealer wins.");
				if(JOptionPane.showConfirmDialog(
						this, 
						"Dealer wins.  Play again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
					newRound();
				}
			} else {
				status.setText("Draw!");
				if(JOptionPane.showConfirmDialog(
						this, 
						"Push!  Play again?", 
						"Result", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
					newRound();
				}
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		setDefaultSize(((JSlider)e.getSource()).getValue());
		revalidate();
		System.out.println("Slider adjusted to: " + ((JSlider)e.getSource()).getValue());
	}
}
