/**
 * Entry point for the Blackjack game.
 *
 * @author Syed Rishad
 * @version 0.1
 */
public class BlackjackGame {

    /**
     * Runs a quick test of the {@link Deck} and {@link Card} classes.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();

        System.out.println("Drawing 5 cards from a shuffled deck:");
        for (int i = 0; i < 5; i++) {
            Card card = deck.drawCard();
            System.out.println(card + " (value " + card.getBaseValue() + ")");
        }
        System.out.println("Cards left: " + deck.remainingCards());
    }
}
