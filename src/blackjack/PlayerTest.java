package blackjack;

import student.TestCase;

/**
 * Tests the Player class.
 *
 * @author Rishad, Claude
 * @version 2026.09.24
 */
public class PlayerTest extends TestCase
{
    private Player player;


    /**
     * Sets up each test method with a fresh Player object.
     */
    public void setUp()
    {
        player = new Player(100.00);
    }


    /**
     * Tests that a new player starts with the given balance and
     * no bet.
     */
    public void testConstructor()
    {
        assertEquals(100.00, player.getBalance(), 0.001);
        assertEquals(0.00, player.getCurrentBet(), 0.001);
    }


    /**
     * Tests that a valid bet is taken out of the balance.
     */
    public void testPlaceBet()
    {
        assertTrue(player.placeBet(25.00));
        assertEquals(75.00, player.getBalance(), 0.001);
        assertEquals(25.00, player.getCurrentBet(), 0.001);
    }


    /**
     * Tests that a bet of zero, a negative bet, and a bet bigger
     * than the balance are all rejected.
     */
    public void testPlaceBetBadAmounts()
    {
        assertFalse(player.placeBet(0.00));
        assertFalse(player.placeBet(-20.00));
        assertFalse(player.placeBet(100.01));

        assertEquals(100.00, player.getBalance(), 0.001);
        assertEquals(0.00, player.getCurrentBet(), 0.001);
    }


    /**
     * Tests that a second bet cannot be placed while one is still
     * on the table.
     */
    public void testPlaceBetTwice()
    {
        assertTrue(player.placeBet(10.00));
        assertFalse(player.placeBet(10.00));
        assertEquals(90.00, player.getBalance(), 0.001);
    }


    /**
     * Tests the payout for a win, which is twice the bet, and for
     * a loss, which is nothing.
     */
    public void testReceivePayout()
    {
        player.placeBet(10.00);
        player.receivePayout(2.0);
        assertEquals(110.00, player.getBalance(), 0.001);
        assertEquals(0.00, player.getCurrentBet(), 0.001);

        player.placeBet(10.00);
        player.receivePayout(0.0);
        assertEquals(100.00, player.getBalance(), 0.001);
    }


    /**
     * Tests the payout for a Blackjack, which pays 3:2, and for a
     * surrender, which returns half the bet.
     */
    public void testReceivePayoutBlackjackAndSurrender()
    {
        player.placeBet(10.00);
        player.receivePayout(2.5);
        assertEquals(115.00, player.getBalance(), 0.001);

        player.placeBet(10.00);
        player.receivePayout(0.5);
        assertEquals(110.00, player.getBalance(), 0.001);
    }


    /**
     * Tests that a payout with no bet on the table changes
     * nothing.
     */
    public void testReceivePayoutWithNoBet()
    {
        player.receivePayout(2.0);
        assertEquals(100.00, player.getBalance(), 0.001);
        assertEquals(0.00, player.getCurrentBet(), 0.001);
    }
}