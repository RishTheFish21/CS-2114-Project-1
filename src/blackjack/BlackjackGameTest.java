package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import student.TestCase;

/**
 * Tests the BlackjackGame class.
 *
 * <p>Each test builds a StackedDeck so the cards are known
 * instead of shuffled, and hands the game a Scanner full of
 * answers instead of the keyboard. Cards are dealt player,
 * dealer, player, dealer.</p>
 *
 * @author Rishad, Claude
 * @version 2026.09.24
 */
public class BlackjackGameTest extends TestCase
{
    private StackedDeck deck;


    /**
     * Sets up each test method with an empty deck to stack.
     */
    public void setUp()
    {
        deck = new StackedDeck();
    }


    /**
     * Tests that standing on a higher total than the dealer pays
     * twice the bet.
     */
    public void testPlayerWins()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\ns\n"));
        game.playRound();

        assertEquals(110.00, game.getPlayerBalance(), 0.001);
        assertEquals(0.00, game.getCurrentBet(), 0.001);
    }


    /**
     * Tests that standing on a lower total than the dealer loses
     * the bet.
     */
    public void testPlayerLoses()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("7", "Spades");
        deck.add("9", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\ns\n"));
        game.playRound();

        assertEquals(90.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that matching the dealer's total returns the bet.
     */
    public void testPush()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("9", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\ns\n"));
        game.playRound();

        assertEquals(100.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that a natural Blackjack pays 3:2 and ends the round
     * without asking the player for an action.
     */
    public void testNaturalBlackjack()
    {
        deck.add("Ace", "Spades");
        deck.add("9", "Clubs");
        deck.add("King", "Hearts");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\n"));
        game.playRound();

        assertEquals(115.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that hitting past 21 loses the bet.
     */
    public void testBust()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("7", "Diamonds");
        deck.add("King", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\nh\n"));
        game.playRound();

        assertEquals(90.00, game.getPlayerBalance(), 0.001);
        assertEquals(3, game.getPlayerHand().getCards().size());
    }


    /**
     * Tests that surrendering refunds half of the bet.
     */
    public void testSurrender()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("6", "Spades");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(
            deck, new Scanner("10\nsurrender\n"));
        game.playRound();

        assertEquals(95.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that the dealer keeps drawing until reaching 17.
     */
    public void testDealerDrawsToSeventeen()
    {
        deck.add("10", "Hearts");
        deck.add("5", "Clubs");
        deck.add("9", "Spades");
        deck.add("6", "Diamonds");
        deck.add("4", "Diamonds");
        deck.add("3", "Clubs");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\ns\n"));
        game.playRound();

        assertEquals(4, game.getDealerHand().getCards().size());
        assertEquals(18, game.getDealerHand().calculateTotal());
        assertEquals(110.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that the player wins when the dealer busts.
     */
    public void testDealerBusts()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("2", "Spades");
        deck.add("6", "Diamonds");
        deck.add("King", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("10\ns\n"));
        game.playRound();

        assertTrue(game.getDealerHand().isBust());
        assertEquals(110.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that a bet that is not a number, and a bet bigger
     * than the balance, are both rejected before a good bet is
     * taken.
     */
    public void testBadBetInput()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(
            deck, new Scanner("abc\n500\n10\ns\n"));
        game.playRound();

        assertEquals(110.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that an action the game does not know is rejected and
     * the player is asked again.
     */
    public void testBadActionInput()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(
            deck, new Scanner("10\nfold\ns\n"));
        game.playRound();

        assertEquals(110.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that startGame() plays a hand and stops when the
     * player answers no.
     */
    public void testStartGame()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("9", "Spades");
        deck.add("7", "Diamonds");

        BlackjackGame game = new BlackjackGame(
            deck, new Scanner("10\ns\nn\n"));
        game.startGame();

        assertEquals(110.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * Tests that losing the whole balance ends the game without
     * asking whether to play again.
     */
    public void testStartGameOutOfMoney()
    {
        deck.add("10", "Hearts");
        deck.add("10", "Clubs");
        deck.add("7", "Spades");
        deck.add("9", "Diamonds");

        BlackjackGame game = new BlackjackGame(deck, new Scanner("100\ns\n"));
        game.startGame();

        assertEquals(0.00, game.getPlayerBalance(), 0.001);
    }


    /**
     * A deck that deals the cards it is given, in order, instead
     * of shuffling. Used so that each test knows exactly what the
     * player and the dealer are holding.
     *
     * @author CS-2114-Project-1
     * @version 2026.09.24
     */
    private static class StackedDeck extends Deck
    {
        private List<Card> stack = new ArrayList<Card>();

        /**
         * Puts one more card on the bottom of the stack.
         *
         * @param rank the rank of the card
         * @param suit the suit of the card
         */
        public void add(String rank, String suit)
        {
            stack.add(new Card(rank, suit));
        }

        /**
         * Deals the next card off the top of the stack.
         *
         * @return the next card
         */
        public Card drawCard()
        {
            return stack.remove(0);
        }

        /**
         * Returns how many cards are left in the stack.
         *
         * @return the number of cards still to deal
         */
        public int remainingCards()
        {
            return stack.size();
        }
    }
}