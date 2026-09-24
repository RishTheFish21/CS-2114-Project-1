package blackjack;
/**
 * Entry point for the Blackjack game.
 * 
 * @author Rishad Aniket
 * @version 0.2
 */
public class BlackjackGame {

    /**
     * Deals a test hand to a player and a dealer and checks some Ace totals.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        System.out.println("Player: " + playerHand + " (Total: " + playerHand.calculateTotal() + ")");
        System.out.println("Dealer: " + dealerHand + " (Total: " + dealerHand.calculateTotal() + ")");

       
        Hand aceTest = new Hand();
        aceTest.addCard(new Card("Ace", "Hearts"));
        aceTest.addCard(new Card("Ace", "Spades"));
        aceTest.addCard(new Card("9", "Clubs"));
        System.out.println("A + A + 9 = " + aceTest.calculateTotal() + " (expected 21)");

        Hand naturalTest = new Hand();
        naturalTest.addCard(new Card("Ace", "Hearts"));
        naturalTest.addCard(new Card("King", "Hearts"));
        System.out.println("A + K natural? " + naturalTest.isNaturalBlackjack() + " (expected true)");

        
        for (int i = 0; i < 60; i++) {
            deck.drawCard();
        }
        System.out.println("Cards left after 60 more draws: " + deck.remainingCards());
    }
}
