package blackjack;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Entry point for the Blackjack game.
 * 
 * @author Rishad Aniket
 * @version 0.2
 */
public class BlackjackGame {

    private Deck deck;

    private Player player;

    private Hand playerHand;

    private Hand dealerHand;

    private Scanner scanner;

    /**
     * Sets up a new game with a $100.00 balance and a shuffled deck.
     */
    public BlackjackGame() {
        scanner = new Scanner(System.in);
        player = new Player(100.00);
        playerHand = new Hand();
        dealerHand = new Hand();
        deck = new Deck();
        deck.shuffle();
    }

    /**
     * Starts the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
    }

    /**
     * Runs rounds until the player runs out of money or chooses to stop.
     */
    public void startGame() {
        System.out.println("=== Welcome to Blackjack! ===");

        while (player.getBalance() > 0) {
            playRound();

            if (player.getBalance() <= 0) {
                System.out.println("You're out of money. Game over!");
                break;
            }

            System.out.print("Play another hand? (Y/N): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("n") || answer.equals("no")) {
                break;
            }
        }
        System.out.println("Final balance: $" + player.getBalance());
    }

    /**
     * Plays one round: betting, dealing, the player's turn, the dealer's turn and the result.
     */
    public void playRound() {
        playerHand.clear();
        dealerHand.clear();

        System.out.println();
        System.out.println("Balance: $" + player.getBalance());
        promptValidBet();

        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        System.out.println("Your hand: " + playerHand + " (Total: " + playerHand.calculateTotal() + ")");
        System.out.println("Dealer shows: " + dealerHand.getCards().get(0) + ", [Hidden Card]");

        // Player's turn
        while (true) {
            String action = promptValidAction();
            if (action.equals("hit")) {
                Card card = deck.drawCard();
                playerHand.addCard(card);
                System.out.println("You drew: " + card);
                System.out.println("Your hand: " + playerHand + " (Total: " + playerHand.calculateTotal() + ")");
                if (playerHand.isBust()) {
                    System.out.println("Bust! You went over 21.");
                    player.receivePayout(0.0);
                    return;
                }
            } else {
                break;
            }
        }

        // Dealer's turn
        System.out.println("Dealer's hand: " + dealerHand + " (Total: " + dealerHand.calculateTotal() + ")");
        while (dealerHand.calculateTotal() < 17) {
            Card card = deck.drawCard();
            dealerHand.addCard(card);
            System.out.println("Dealer draws: " + card + " (Total: " + dealerHand.calculateTotal() + ")");
        }

        // Result
        int playerTotal = playerHand.calculateTotal();
        int dealerTotal = dealerHand.calculateTotal();
        if (dealerHand.isBust()) {
            System.out.println("Dealer busts! You win!");
            player.receivePayout(2.0);
        } else if (playerTotal > dealerTotal) {
            System.out.println("You win!");
            player.receivePayout(2.0);
        } else if (playerTotal < dealerTotal) {
            System.out.println("Dealer wins.");
            player.receivePayout(0.0);
        } else {
            System.out.println("Push (Tie)!");
            player.receivePayout(1.0);
        }
    }

    /**
     * Asks for a bet until a valid one is entered, then places it.
     *
     * @return the amount that was bet
     */
    private double promptValidBet() {
        while (true) {
            System.out.print("Enter your bet: ");
            try {
                double amount = scanner.nextDouble();
                scanner.nextLine(); // clear the rest of the line
                if (player.placeBet(amount)) {
                    return amount;
                }
                System.out.println("Bet must be more than 0 and no more than your balance.");
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                scanner.nextLine(); // clear the bad input
            }
        }
    }

    /**
     * Asks the player to hit or stand until they enter a valid choice.
     *
     * @return "hit" or "stand"
     */
    private String promptValidAction() {
        while (true) {
            System.out.print("Hit or stand? ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("hit") || input.equals("stand")) {
                return input;
            }
            System.out.println("Invalid choice. Type 'hit' or 'stand'.");
        }
    }
}

