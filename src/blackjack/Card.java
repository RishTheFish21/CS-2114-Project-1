package blackjack;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single, immutable playing card with a rank and a suit.
 *
 * @author Rishad
 * @version 1.0
 */
public class Card {

    /** All valid ranks, lowest to highest. Shared with {@link Deck} so the two always match. */
    public static final List<String> RANKS = Collections.unmodifiableList(Arrays.asList(
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"));

    /** All valid suits. */
    public static final List<String> SUITS = Collections.unmodifiableList(Arrays.asList(
            "Hearts", "Diamonds", "Clubs", "Spades"));

    /** The rank of the card, e.g. "2", "10", "Jack" or "Ace". */
    private final String rank;

    /** The suit of the card, e.g. "Hearts" or "Spades". */
    private final String suit;

    /**
     * Creates a new card.
     *
     * @param rank the rank of the card; must be one of {@link #RANKS}
     * @param suit the suit of the card; must be one of {@link #SUITS}
     * @throws IllegalArgumentException if the rank or suit is null or not a standard value
     */
    public Card(String rank, String suit) {
        if (!RANKS.contains(rank)) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
        if (!SUITS.contains(suit)) {
            throw new IllegalArgumentException("Invalid card suit: " + suit);
        }
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
     * @return the base value of the card, from 2 to 11
     */
    public int getBaseValue() {
        switch (rank) {
            case "Ace":
                return 11;
            case "King":
            case "Queen":
            case "Jack":
                return 10;
            default:
                // Safe: the constructor guarantees any other rank is "2" to "10".
                return Integer.parseInt(rank);
        }
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
