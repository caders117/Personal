package ch06;
import java.util.*;
// **********************************************************************
// DeckOfCardsTest.java
//
// Tester program to shuffle and deal a deck of Card objects
// **********************************************************************

import blackjackGUI.Card;
import blackjackGUI.DeckOfCards;

public class Blackjack
{
    public static void main (String[] args)
    {
        Scanner scan = new Scanner(System.in);
        String playerAction;
        boolean comp;
        int playerTotal, compTotal;
        DeckOfCards masterDeck;
        ArrayList<Card> playerDeck, compDeck;
        
    	System.out.print("Would you like to play BlackJack? (Y/N): ");
    	playerAction = scan.next();
    	
    	while(playerAction.equalsIgnoreCase("y")){    		
	        masterDeck = new DeckOfCards();
	        playerTotal = 0;
	        compTotal = 0;
	        comp = true;
	        
	        masterDeck.shuffle(); // put Card objects in random order
	        playerDeck = new ArrayList<>();
	        compDeck = new ArrayList<>();
	        
	        for(int i = 0; i < 2; i++){
	            playerDeck.add(masterDeck.dealCard());
	            System.out.println(playerDeck.get(playerDeck.size() - 1).getValue());
	            playerTotal += playerDeck.get(playerDeck.size() - 1).getValue();
	            compDeck.add(masterDeck.dealCard());
	            compTotal += compDeck.get(compDeck.size() - 1).getValue();
	        }
	        
	        System.out.println("Your cards are: " + printArrayList(playerDeck) + "\tTotal: " + playerTotal);
	        
	        if(playerTotal == 21)
	            System.out.println("BlackJack!");
	        
	        System.out.print("(H)it or (S)tand: ");
	        playerAction = scan.next();
	        
	        while(playerAction.equalsIgnoreCase("H")){
	            if(playerAction.equalsIgnoreCase("H")){
	                playerDeck.add(masterDeck.dealCard());
	                playerTotal += playerDeck.get(playerDeck.size() - 1).getValue();
	                System.out.println("Your cards are: " + printArrayList(playerDeck) + "\tTotal: " + playerTotal);
	                
	                if(playerTotal > 21){
	                    System.out.println("Busted");
	                    comp = false;
	                    break;
	                }
	            }
	            System.out.print("(H)it or (S)tand: ");
	            playerAction = scan.next();
	        }
	        
	        if(comp){
		        System.out.println("\nDealer's Cards are: " + printArrayList(compDeck) + "\tTotal: " + compTotal);
		        
		        while(compTotal < 17){
		        	compDeck.add(masterDeck.dealCard());
		            compTotal += compDeck.get(compDeck.size() - 1).getValue();
		            System.out.println("Dealer hits.");
		            System.out.println("Dealer's Cards are: " + printArrayList(compDeck) + "\tTotal: " + compTotal);
	
			        if(compTotal > 21){
			        	System.out.println("Dealer busted.  You win!");
			        	comp = false;
			        	break;
			        }
		        }
		        
		        if(comp){
		        	System.out.println("Dealer stands.");
			        if(compTotal > playerTotal){
			        	System.out.println("Computer wins!");
			        } else if(playerTotal > compTotal){
			        	System.out.println("You win!");
			        } else {
			        	System.out.println("Draw!");
			        }
		        }
	        }
	        
	        System.out.print("\n\n\nWould you like to play again? (Y/N): ");
	        playerAction = scan.next();
    	}
    }
    
    public static String printArrayList(ArrayList<Card> a){
    	String ret = "";
    	for(int i = 0; i < a.size(); i++){
    		ret += a.get(i) + (i != a.size() - 1 ? ", " : "");
    	}
    	return ret;
    }
}