package blackjackGUI;

import java.util.ArrayList;

public class BlackjackEngine {
	private DeckOfCards master;
	private ArrayList<Card> playerDeck, compDeck;
	private int playerTotal, compTotal;
	private int playerScore, compScore;
	
	public BlackjackEngine(){
		playerScore = 0;
		compScore = 0;
		init();
	}
	
	public BlackjackEngine(int ps, int cs){
		playerScore = ps;
		compScore = ps;
	}
	
	public void init(){
		master = new DeckOfCards();
		playerDeck = new ArrayList<>();
		compDeck = new ArrayList<>();
		master.shuffle();
		
		for(int i = 0; i < 2; i++){	
			playerDeck.add(master.dealCard());
			compDeck.add(master.dealCard());
		}
		updateTotals();
	}
	
	public void updateTotals(){
		int tempTotal = 0;
		for(Card i : playerDeck)
			tempTotal += i.getValue();
		if(playerTotal < tempTotal)
			playerTotal += tempTotal - playerTotal;
		
		tempTotal = 0;
		for(Card i : compDeck)
			tempTotal += i.getValue();
		if(compTotal < tempTotal)
			compTotal += tempTotal - compTotal;

	}
	
	public int getPlayerScore(){
		return playerScore;
	}
	
	public void setPlayerScore(int ps){
		playerScore = ps;
	}
	
	public void incrementPlayerScore(int i){
		playerScore += i;
	}
	
	public int getCompScore(){
		return compScore;
	}
	
	public void setCompScore(int cs){
		compScore = cs;
	}
	
	public void incrementCompScore(int i){
		compScore += i;
	}
	
	public void playerHit(){
		playerDeck.add(master.dealCard());
		updateTotals();
	}
	
	public void compHit(){
		compDeck.add(master.dealCard());
		updateTotals();
		System.out.println("Dealer hits");
	}
	
	public boolean checkPlayerBust(){
		if(playerTotal > 21)
			return true;
		return false;
	}
	
	public boolean checkCompBust(){
		if(compTotal > 21)
			return true;
		return false;
	}
	
	public ArrayList<Card> getPlayer(){
		return playerDeck;
	}
	
	public ArrayList<Card> getComp(){
		return compDeck;
	}
	
	public int getPlayerTotal(){
		int pt = 0;
		int aces = 0;
		for(Card x : playerDeck){
			pt += x.getValue();
			if(x.getValue() == 1){
				aces ++;
			}
		}
		if(pt + 10 <= 21 && aces > 0){
			pt += 10;
		}
		return pt;
	}
	
	public int getCompTotal(){
		int ct = 0;
		int aces = 0;
		for(Card x : compDeck){
			ct += x.getValue();
			if(x.getValue() == 1){
				aces ++;
			}
		}
		if(ct + 10 <= 21 && aces > 0){
			ct += 10;
		}
		return ct;
	}
	
	public String results(){
		if(compTotal > playerTotal)
        	return "Computer wins!";
        else if(playerTotal > compTotal)
        	return "You win!";
        else
        	return "Draw!";
	}
}
