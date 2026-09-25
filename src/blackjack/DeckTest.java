package blackjack;

import student.TestCase;

/**
 * Tests the Deck class.
 *
 * @author Rishad, Claude
 * @version 2026.09.24
 */
public class DeckTest extends TestCase
{
    private Deck deck;


    /**
     * Sets up each test method with a fresh Deck object.
     */
    public void setUp()
    {
        deck = new Deck();
    }


    /**
     * Tests that a new deck holds all 52 cards in order.
     */
    public void testConstructor()
    {
        assertEquals(52, deck.remainingCards());
        assertEquals("2 of Hearts", deck.drawCard().toString());
    }


    /**
     * Tests that drawing a card takes it out of the deck.
     */
    public void testDrawCard()
    {
        assertNotNull(deck.drawCard());
        assertEquals(51, deck.remainingCards());
    }


    /**
     * Tests that drawing from an empty deck rebuilds it instead
     * of failing.
     */
    public void testDrawCardFromEmptyDeck()
    {
        for (int i = 0; i < 52; i++)
        {
            deck.drawCard();
        }
        assertEquals(0, deck.remainingCards());

        assertNotNull(deck.drawCard());
        assertEquals(51, deck.remainingCards());
    }


    /**
     * Tests that shuffling does not add or lose any cards.
     */
    public void testShuffle()
    {
        deck.shuffle();
        assertEquals(52, deck.remainingCards());
        assertNotNull(deck.drawCard());
    }
}