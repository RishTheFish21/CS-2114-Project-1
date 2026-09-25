package blackjack;

import student.TestCase;

/**
 * Tests the Hand class.
 *
 * @author Rishad, Claude
 * @version 2026.09.24
 */
public class HandTest extends TestCase
{
    private Hand hand;


    /**
     * Sets up each test method with a fresh, empty Hand object.
     */
    public void setUp()
    {
        hand = new Hand();
    }


    /**
     * Tests that a new hand holds no cards and is worth nothing.
     */
    public void testConstructor()
    {
        assertEquals(0, hand.getCards().size());
        assertEquals(0, hand.calculateTotal());
    }


    /**
     * Tests addCard() and calculateTotal() with normal cards.
     */
    public void testCalculateTotal()
    {
        hand.addCard(new Card("7", "Hearts"));
        hand.addCard(new Card("King", "Spades"));

        assertEquals(2, hand.getCards().size());
        assertEquals(17, hand.calculateTotal());
    }


    /**
     * Tests that an Ace is worth 11 when it fits, but drops to 1
     * when counting it as 11 would bust the hand.
     */
    public void testCalculateTotalWithAce()
    {
        hand.addCard(new Card("Ace", "Hearts"));
        hand.addCard(new Card("6", "Spades"));
        assertEquals(17, hand.calculateTotal());

        hand.addCard(new Card("9", "Clubs"));
        assertEquals(16, hand.calculateTotal());
    }


    /**
     * Tests that 21 on two cards is a natural Blackjack but 21 on
     * three cards is not.
     */
    public void testIsNaturalBlackjack()
    {
        hand.addCard(new Card("Ace", "Spades"));
        hand.addCard(new Card("King", "Hearts"));
        assertTrue(hand.isNaturalBlackjack());

        hand.addCard(new Card("2", "Clubs"));
        assertFalse(hand.isNaturalBlackjack());
    }


    /**
     * Tests that 21 is safe but 22 busts.
     */
    public void testIsBust()
    {
        hand.addCard(new Card("King", "Spades"));
        hand.addCard(new Card("Ace", "Hearts"));
        assertEquals(21, hand.calculateTotal());
        assertFalse(hand.isBust());

        hand.clear();
        hand.addCard(new Card("King", "Spades"));
        hand.addCard(new Card("Queen", "Hearts"));
        hand.addCard(new Card("2", "Clubs"));
        assertEquals(22, hand.calculateTotal());
        assertTrue(hand.isBust());
    }


    /**
     * Tests that clear() empties the hand for the next round.
     */
    public void testClear()
    {
        hand.addCard(new Card("King", "Spades"));
        hand.clear();

        assertEquals(0, hand.getCards().size());
        assertEquals(0, hand.calculateTotal());
    }


    /**
     * Tests that a hand lists its cards separated by commas.
     */
    public void testToString()
    {
        assertEquals("", hand.toString());

        hand.addCard(new Card("Ace", "Spades"));
        hand.addCard(new Card("10", "Hearts"));
        assertEquals("Ace of Spades, 10 of Hearts", hand.toString());
    }
}