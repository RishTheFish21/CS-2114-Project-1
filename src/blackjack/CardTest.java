package blackjack;

import student.TestCase;

/**
 * Tests the Card class.
 *
 * @author Rishad, Claude
 * @version 2026.09.24
 */
public class CardTest extends TestCase
{
    private Card card;


    /**
     * Sets up each test method with a fresh Card object.
     */
    public void setUp()
    {
        card = new Card("Ace", "Spades");
    }


    /**
     * Tests that the constructor stores the rank and the suit.
     */
    public void testConstructor()
    {
        assertEquals("Ace", card.getRank());
        assertEquals("Spades", card.getSuit());
    }


    /**
     * Tests getBaseValue() for a number card, a face card, and an
     * Ace.
     */
    public void testGetBaseValue()
    {
        assertEquals(11, card.getBaseValue());
        assertEquals(7, new Card("7", "Hearts").getBaseValue());
        assertEquals(10, new Card("King", "Hearts").getBaseValue());
    }


    /**
     * Tests that a rank that is not in a real deck is worth 2.
     */
    public void testGetBaseValueBadRank()
    {
        assertEquals(2, new Card("Joker", "Hearts").getBaseValue());
    }


    /**
     * Tests that getShortRank() shortens a face card but leaves a
     * number card alone.
     */
    public void testGetShortRank()
    {
        assertEquals("A", card.getShortRank());
        assertEquals("K", new Card("King", "Hearts").getShortRank());
        assertEquals("10", new Card("10", "Hearts").getShortRank());
    }


    /**
     * Tests getSuitSymbol() for a real suit and for a suit that
     * does not exist, which is drawn as a spade.
     */
    public void testGetSuitSymbol()
    {
        assertEquals("\u2660", card.getSuitSymbol());
        assertEquals("\u2665", new Card("2", "Hearts").getSuitSymbol());
        assertEquals("\u2660", new Card("2", "Stars").getSuitSymbol());
    }


    /**
     * Tests that a card is drawn 7 lines tall with the rank in
     * the corners.
     */
    public void testToAsciiArt()
    {
        String[] art = card.toAsciiArt();
        assertEquals(7, art.length);
        assertEquals("|A        |", art[1]);
    }


    /**
     * Tests that a card describes itself as "rank of suit".
     */
    public void testToString()
    {
        assertEquals("Ace of Spades", card.toString());
    }
}