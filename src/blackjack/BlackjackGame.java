package blackjack;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * Runs a game of Blackjack in the terminal.
 *
 * <p>This class controls the game loop, each round, input validation and everything
 * printed to the console.</p>
 *
 * @author Syed Rishad
 * @version 1.1
 */
public class BlackjackGame {


    private static final double INITIAL_BALANCE = 100.00;
    private static final int BLACKJACK = 21;
    private static final int DEALER_STAND_THRESHOLD = 17;
    private static final String DIVIDER = "========================================";

    private static final double MULTIPLIER_WIN = 2.0;
    private static final double MULTIPLIER_BLACKJACK = 2.5;
    private static final double MULTIPLIER_PUSH = 1.0;
    private static final double MULTIPLIER_SURRENDER = 0.5;
    private static final double MULTIPLIER_LOSS = 0.0;

    private Deck deck;
    private Player player;
    private Hand playerHand;
    private Hand dealerHand;
    private Scanner scanner;

    /**
     * Sets up a new game with a $100.00 balance, empty hands and
     * a shuffled deck.
     */
    public BlackjackGame() {
        scanner = new Scanner(System.in);
        player = new Player(INITIAL_BALANCE);
        playerHand = new Hand();
        dealerHand = new Hand();
        deck = new Deck();
        deck.shuffle();
    }

    /**
     * Starts the program
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
    }

    /**
     * Runs rounds until the player runs out of money or chooses to stop.
     */
    public void startGame() {
        printBanner();

        while (player.getBalance() > 0) {
            playRound();

            if (player.getBalance() <= 0) {
                System.out.println(DIVIDER);
                System.out.println("You're out of money. GAME OVER.");
                System.out.println(DIVIDER);
                break;
            }

            if (!promptPlayAgain()) {
                break;
            }
        }

        System.out.println(DIVIDER);
        System.out.println("Final balance: " + formatMoney(player.getBalance()));
        System.out.println("Thanks for playing!");
        System.out.println(DIVIDER);
    }

    /**
     * Plays one round: betting, the initial deal, natural Blackjack checks,
     * the player's turn, the dealer's turn and the final result.
     */
    public void playRound() {
        playerHand.clear();
        dealerHand.clear();

        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("NEW ROUND  |  Balance: " + formatMoney(player.getBalance()));
        System.out.println(DIVIDER);

        promptValidBet();

        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        printSection("INITIAL DEAL");
        displayTable(false);

        if (playerHand.isNaturalBlackjack() && dealerHand.isNaturalBlackjack()) {
            printSection("RESULT");
            displayTable(true);
            System.out.println("Both hit Natural Blackjack! Tie.");
            settleBet(MULTIPLIER_PUSH);
            return;
        } else if (playerHand.isNaturalBlackjack()) {
            printSection("RESULT");
            displayTable(true);
            System.out.println("Natural Blackjack! Paid 3:2!");
            settleBet(MULTIPLIER_BLACKJACK);
            return;
        }

        if (!playPlayerTurn()) {
            return;
        }

        playDealerTurn();
        evaluateOutcome();
    }

    /**
     * Runs the player's turn, letting them hit, stand or surrender.
     * Hitting to exactly 21 stands automatically.
     *
     * @return true if the player stood and the dealer must play
     *         false if the round already ended with a bust or surrender
     */
    private boolean playPlayerTurn() {
        printSection("YOUR TURN");

        while (true) {
            String action = promptValidAction();

            if (action.equals("hit")) {
                Card card = deck.drawCard();
                playerHand.addCard(card);
                System.out.println();
                System.out.println("You drew: " + card);
                displayTable(false);

                if (playerHand.isBust()) {
                    printSection("RESULT");
                    System.out.println("Bust! You went over 21.");
                    settleBet(MULTIPLIER_LOSS);
                    return false;
                }
                if (playerHand.calculateTotal() == BLACKJACK) {
                    System.out.println("You have 21! Automatically standing.");
                    return true;
                }
            } else if (action.equals("surrender")) {
                printSection("RESULT");
                System.out.println("Surrendered hand. Half bet refunded.");
                settleBet(MULTIPLIER_SURRENDER);
                return false;
            } else if (action.equals("stand")) {
                System.out.println("You stand on " + playerHand.calculateTotal() + ".");
                return true;
    
            }
        }
    }

    /**
     * Reveals the dealer's hidden card, then draws until the dealer's total is at least 17.
     */
    private void playDealerTurn() {
        printSection("DEALER'S TURN");
        System.out.println("Dealer reveals hidden card: " + dealerHand.getCards().get(1));
        displayTable(true);

        while (dealerHand.calculateTotal() < DEALER_STAND_THRESHOLD) {
            Card card = deck.drawCard();
            dealerHand.addCard(card);
            System.out.println();
            System.out.println("Dealer draws: " + card);
            displayTable(true);
        }

        if (!dealerHand.isBust()) {
            System.out.println("Dealer stands on " + dealerHand.calculateTotal() + ".");
        }
    }

    /**
     * Compares the final totals, prints the result and pays out the bet.
     */
    private void evaluateOutcome() {
        int playerTotal = playerHand.calculateTotal();
        int dealerTotal = dealerHand.calculateTotal();

        printSection("RESULT");
        System.out.println("Your total: " + playerTotal + "  |  Dealer total: " + dealerTotal);

        if (dealerHand.isBust()) {
            System.out.println("Dealer busts! You win!");
            settleBet(MULTIPLIER_WIN);
        } else if (playerTotal > dealerTotal) {
            System.out.println("You win!");
            settleBet(MULTIPLIER_WIN);
        } else if (playerTotal < dealerTotal) {
            System.out.println("Dealer wins.");
            settleBet(MULTIPLIER_LOSS);
        } else {
            System.out.println("Push (Tie)!");
            settleBet(MULTIPLIER_PUSH);
        }
    }

    /**
     * Pays out the current bet and prints the bet, the amount returned and the new balance.
     *
     * @param multiplier the payout multiplier
     */
    private void settleBet(double multiplier) {
        double balanceBefore = player.getBalance();
        double bet = player.getCurrentBet();

        player.receivePayout(multiplier);

        double returned = player.getBalance() - balanceBefore;
        System.out.println("Bet: " + formatMoney(bet) + "  |  Returned: " + formatMoney(returned));
        System.out.println("Balance: " + formatMoney(player.getBalance()));
    }

    /**
     * Asks for a bet until a valid one is entered, then places it.
     * Anything that is not a number is rejected,
     * rejects bets that are $0.00 or less or more than the balance.
     *
     * @return the amount that was bet
     */
    private double promptValidBet() {
        while (true) {
            System.out.print("Enter your bet (available " + formatMoney(player.getBalance()) + "): ");
            String input = scanner.nextLine().trim();

            double amount;
            try {
                amount = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[Error] '" + input + "' is not a number. Try again.");
                continue;
            }

            if (player.placeBet(amount)) {
                System.out.println("Bet placed: " + formatMoney(player.getCurrentBet())
                        + "  |  Remaining balance: " + formatMoney(player.getBalance()));
                return player.getCurrentBet();
            }
            System.out.println("[Error] Bet must be more than $0.00 and no more than "
                    + formatMoney(player.getBalance()) + ".");
        }
    }

    /**
     * Asks the player to hit, stand or surrender until they enter a valid choice.
     * Input is trimmed and case-insensitive
     *
     * @return "hit", "stand" or "surrender"
     */
    private String promptValidAction() {
        while (true) {
            System.out.print("Choose an action - [H]it, [S]tand, or Surrender: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("hit") || input.equals("h")) {
                return "hit";
            } else if (input.equals("stand") || input.equals("s")) {
                return "stand";
            } else if (input.equals("surrender")) {
                return "surrender";
            } else {
                System.out.println("[Error] Invalid action '" + input
                        + "'. Type 'hit' (h), 'stand' (s), or 'surrender'.");
            }
        }
    }
    /**
     * Asks whether to play another hand until the player answers yes or no.
     *
     * @return true to keep playing, false to quit
     */
    private boolean promptPlayAgain() {
        while (true) {
            System.out.print("Play another hand? (Y/N): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("[Error] Please enter Y (yes) or N (no).");
            }
        }
    }

    /**
     * Prints the welcome banner and the table rules.
     */
    private void printBanner() {
        System.out.println(DIVIDER);
        System.out.println("          WELCOME TO BLACKJACK");
        System.out.println(DIVIDER);
        System.out.println("Starting balance: " + formatMoney(player.getBalance()));
        System.out.println("Blackjack pays 3:2  |  Dealer stands on all 17s");
        System.out.println("Actions: hit (h), stand (s), surrender");
    }

    /**
     * Prints a section heading between two divider lines.
     *
     * @param title the heading text
     */
    private void printSection(String title) {
        System.out.println(DIVIDER);
        System.out.println(title);
        System.out.println(DIVIDER);
    }

    /**
     * Prints the balance, the active bet and both hands as ASCII art with their totals.
     *
     * @param revealDealer true to show the dealer's full hand
     *                     false to show only the up card and a face-down card
     */
    private void displayTable(boolean revealDealer) {
        System.out.println("Balance: " + formatMoney(player.getBalance())
                + "  |  Active bet: " + formatMoney(player.getCurrentBet()));
        System.out.println();

        if (revealDealer) {
            System.out.println("Dealer's hand  (Total: " + dealerHand.calculateTotal() + ")");
            System.out.println(dealerHand.toAsciiArt());
        } else {
            Card upCard = dealerHand.getCards().get(0);
            System.out.println("Dealer's hand  (Showing: " + upCard.getBaseValue() + ")");
            System.out.println(Card.renderSideBySide(
                    Arrays.asList(upCard.toAsciiArt(), Card.getHiddenAsciiArt())));
        }

        System.out.println("Your hand  (Total: " + playerHand.calculateTotal() + ")");
        System.out.println(playerHand.toAsciiArt());
        System.out.println();
    }

    /**
     * Formats an amount as dollars with two decimal place
     *
     * @param amount the amount to format
     * @return the formatted amount
     */
    private static String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }
}