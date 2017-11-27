package blackjackGUI;

import java.util.Arrays;

// **********************************************************************
// Card.java
//
// Represents a Card with a face and a suit.
// **********************************************************************
public class Card
{
    private String face;
    private String suit;
    private int value;
    private static String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    
    public Card (String cardFace, String cardSuit, int cardVal)
    {
        face = cardFace;
        suit = cardSuit;
        value = cardVal;
    }

    public String toString(){

        return face + " of " + suit;
    }
    
    public String getFace(){
        return face;
    }
 
    public int getValue(){
        return value;
    }
    
    public int getFaceAsInt(){
    	return Arrays.asList(faces).indexOf(face);
    }
    
    public int getSuit(){
    	if(suit.equalsIgnoreCase("hearts"))
    		return 0;
    	if(suit.equalsIgnoreCase("diamonds"))
    		return 1;
    	if(suit.equalsIgnoreCase("clubs"))
    		return 2;
    	if(suit.equalsIgnoreCase("spades"))
    		return 3;
    	else
    		return 0;
    }

}