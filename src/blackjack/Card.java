package blackjack;
import java.util.List;

/**
 * Represents a single, immutable playing card with a rank and a suit.
 * @author Wyatt Courson
 * @version 1.1
 */
public class Card {

    public static final String[] RANKS = {
        "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"
    };
    public static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private static final int CARD_HEIGHT = 7;

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
     *
     * @return the base value of the card, from 2 to 11
     */
    public int getBaseValue() {
        if (rank.equals("Ace")) {
            return 11;
        } else if (rank.equals("King") || rank.equals("Queen") || rank.equals("Jack")
                || rank.equals("10")) {
            return 10;
        } else if (rank.equals("9")) {
            return 9;
        } else if (rank.equals("8")) {
            return 8;
        } else if (rank.equals("7")) {
            return 7;
        } else if (rank.equals("6")) {
            return 6;
        } else if (rank.equals("5")) {
            return 5;
        } else if (rank.equals("4")) {
            return 4;
        } else if (rank.equals("3")) {
            return 3;
        } else {
            return 2;
        }
    }

    /**
     * Returns the short rank printed in the corners of the ASCII art:
     * "A", "K", "Q", "J", or the number for number cards.
     *
     * @return the short rank, one or two characters long
     */
    public String getShortRank() {
        switch (rank) {
            case "Ace":
                return "A";
            case "King":
                return "K";
            case "Queen":
                return "Q";
            case "Jack":
                return "J";
            default:
                return rank;
        }
    }

    /**
     * Returns the symbol for this card's suit.
     *
     * @return the suit symbol: a heart, diamond, club or spade
     */
    public String getSuitSymbol() {
        if (suit.equals("Hearts")) {
            return "\u2665";   // heart
        } else if (suit.equals("Diamonds")) {
            return "\u2666";   // diamond
        } else if (suit.equals("Clubs")) {
            return "\u2663";   // club
        } else {
            return "\u2660";   // spade
        }
    }

    /**
     * Draws this card as text art. The short rank appears in the top-left and
     * bottom-right corners and the suit symbol is in the middle.
     *
     * @return the lines of the card, one array element per line
     */
    public String[] toAsciiArt() {
        String rank = getShortRank();
        String suitSymbol = getSuitSymbol();

        String gap = "        ";   // 8 spaces
        if (rank.equals("10")) {
            gap = "       ";       // 7 spaces
        }

        return new String[] {
            ".---------.",
            "|" + rank + gap + "|",
            "|         |",
            "|    " + suitSymbol + "    |",
            "|         |",
            "|" + gap + rank + "|",
            "'---------'"
        };
    }

    /**
     * Draws the back of a card
     *
     * @return the lines of the card back
     */
    public static String[] getHiddenAsciiArt() {
        return new String[] {
            ".---------.",
            "|/\\/\\/\\/\\/|",
            "|\\/\\/\\/\\/\\|",
            "|/\\/\\/\\/\\/|",
            "|\\/\\/\\/\\/\\|",
            "|/\\/\\/\\/\\/|",
            "'---------'"
        };
    }

    /**
     * Joins several cards' ASCII art so they print side by side.
     *
     * @param cardArts the art for each card
     * @return the cards side by side as one multi-line string
     */
    public static String renderSideBySide(List<String[]> cardArts) {
        String result = "";
        for (int line = 0; line < CARD_HEIGHT; line++) {
            if (line > 0) {
                result += "\n";
            }
            for (String[] art : cardArts) {
                result += art[line] + " ";
            }
        }
        return result;
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