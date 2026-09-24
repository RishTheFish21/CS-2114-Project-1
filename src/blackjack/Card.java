/**
 * Represents a single playing card with a rank and a suit.
 *
 * @author Syed Rishad
 * @version 0.1
 */
public class Card {

    /** The rank of the card, e.g. "2", "10", "Jack" or "Ace". */
    private final String rank;

    /** The suit of the card, e.g. "Hearts" or "Spades". */
    private final String suit;

    /**
     * Creates a new card.
     *
     * @param rank the rank of the card ("2" through "10", "Jack", "Queen", "King" or "Ace")
     * @param suit the suit of the card ("Hearts", "Diamonds", "Clubs" or "Spades")
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
     * Returns a readable description of the card, e.g. "Ace of Spades".
     *
     * @return the card as a string
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
