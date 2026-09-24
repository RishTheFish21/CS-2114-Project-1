package blackjack;
/**
 * Represents a single playing card with a rank and a suit.
 *
 * @author Syed Rishad
 * @version 0.1
 */
public class Card {

    private final String rank;

    private final String suit;

    /**
     * Creates a new card.
     *
     * @param rank the rank of the card 
     * @param suit the suit of the card 
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the rank of this card.
     *
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns the suit of this card.
     *
     * @return the suit
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Returns the Blackjack value of this card before any Ace adjustment.
     * Number cards are worth their number, face cards are worth 10 and an Ace is worth 11.
     *
     * @return the base value of the card
     */
    public int getBaseValue() {
        if (rank.equals("Ace")) {
            return 11;
        }
        if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            return 10;
        }
        return Integer.parseInt(rank);
    }

    /**
     * Returns a readable description of the card
     *
     * @return the card as a string
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
