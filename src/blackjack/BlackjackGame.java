package blackjack;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Runs a game of Blackjack in the terminal.
 *
 * @author Rishad
 * @version 1.0
 */
public class BlackjackGame {

    private static final double INITIAL_BALANCE = 100.00;

    private static final int BLACKJACK = 21;

    private static final int DEALER_STAND_THRESHOLD = 17;

    private static final String DIVIDER = "========================================";

    /**
     * Pattern for a valid bet. Accepts plain decimal amounts with at most two decimal places: "25", "12.5", "12.50", ".75".
     * Rejects "fifty", "$20", "20abc", "-5", "1e3", "NaN", "Infinity" and "20d"
     * (the last three would otherwise slip through Double.parseDouble).
     */
    private static final String BET_PATTERN = "\\d+(\\.\\d{1,2})?|\\.\\d{1,2}";

    private static final double MULTIPLIER_WIN = 2.0;

    /** Payout multiplier for a natural Blackjack (bet returned plus 1.5x). */
    private static final double MULTIPLIER_BLACKJACK = 2.5;

    /** Payout multiplier for a push (bet returned). */
    private static final double MULTIPLIER_PUSH = 1.0;

    /** Payout multiplier for a surrender (half the bet returned). */
    private static final double MULTIPLIER_SURRENDER = 0.5;

    /** Payout multiplier for a loss (nothing returned). */
    private static final double MULTIPLIER_LOSS = 0.0;

    /** The deck used to deal cards. */
    private Deck deck;

    /** The player and their money. */
    private Player player;

    /** The player's cards for the current round. */
    private Hand playerHand;

    /** The dealer's cards for the current round. */
    private Hand dealerHand;

    /** Reads input from the keyboard. */
    private Scanner scanner;

    /**
     * Sets up a new game with a $100.00 balance, empty hands and a shuffled deck.
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
     * Starts the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
    }

    /**
     * Runs rounds until the player runs out of money, chooses to stop, or input ends.
     * If standard input is closed (Ctrl+D, or the end of a piped file), the game
     * ends cleanly and prints the final balance instead of crashing.
     */
    public void startGame() {
        printBanner();

        try {
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
        } catch (NoSuchElementException e) {
            // Standard input was closed (Ctrl+D / Ctrl+Z or end of a piped file).
            System.out.println();
            System.out.println("[System] Input stream closed. Ending game.");
            if (player.getCurrentBet() > 0) {
                System.out.println("[System] Unresolved bet of " + formatMoney(player.getCurrentBet())
                        + " was forfeited.");
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
        // 1. Reset hands
        playerHand.clear();
        dealerHand.clear();

        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("NEW ROUND  |  Balance: " + formatMoney(player.getBalance()));
        System.out.println(DIVIDER);

        // 2. Bet phase (the bet is placed on the Player inside the prompt loop)
        promptValidBet();

        // 3. Initial deal: player, dealer, player, dealer
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        // 4. Display state with the dealer's hole card hidden
        printSection("INITIAL DEAL");
        displayTable(false);

        // 5. Natural blackjack checks
        if (playerHand.isNaturalBlackjack() && dealerHand.isNaturalBlackjack()) {
            printSection("RESULT");
            displayTable(true);
            System.out.println("Both hit Natural Blackjack! Push/Tie.");
            settleBet(MULTIPLIER_PUSH);
            return;
        } else if (playerHand.isNaturalBlackjack()) {
            printSection("RESULT");
            displayTable(true);
            System.out.println("Natural Blackjack! Paid 3:2!");
            settleBet(MULTIPLIER_BLACKJACK);
            return;
        }

        // 6. Player turn (returns false if the round already ended by bust or surrender)
        if (!playPlayerTurn()) {
            return;
        }

        // 7. Dealer turn
        playDealerTurn();

        // 8. Evaluation
        evaluateOutcome();
    }

    /**
     * Runs the player's turn, letting them hit, stand or surrender.
     * Hitting to exactly 21 stands automatically.
     *
     * @return {@code true} if the player stood and the dealer must play;
     *         {@code false} if the round already ended with a bust or surrender
     */
    private boolean playPlayerTurn() {
        printSection("YOUR TURN");

        while (true) {
            String action = promptValidAction();

            switch (action) {
                case "hit": {
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
                    break;
                }
                case "surrender":
                    printSection("RESULT");
                    System.out.println("Surrendered hand. Half bet refunded.");
                    settleBet(MULTIPLIER_SURRENDER);
                    return false;

                case "stand":
                    System.out.println("You stand on " + playerHand.calculateTotal() + ".");
                    return true;

                default:
                    // Unreachable: promptValidAction only returns canonical actions.
                    throw new IllegalStateException("Unexpected action: " + action);
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
     * Pays out the current bet and prints the bet, the amount returned and the net result.
     *
     * @param multiplier the payout multiplier passed to {@link Player#receivePayout(double)}
     */
    private void settleBet(double multiplier) {
        double balanceBefore = player.getBalance();
        double bet = player.getCurrentBet();

        player.receivePayout(multiplier);

        double returned = player.getBalance() - balanceBefore;
        double net = returned - bet;

        System.out.println("Bet: " + formatMoney(bet)
                + "  |  Returned: " + formatMoney(returned)
                + "  |  Net: " + formatSignedMoney(net));
        System.out.println("Balance: " + formatMoney(player.getBalance()));
    }

    /**
     * Asks for a bet until a valid one is entered, then places it.
     *
     * <p>Each read uses {@code nextLine()}, so bad input never stays in the scanner.
     * Input must be a plain amount with at most two decimal places; entries such as
     * "fifty", "$20", "20abc", "NaN" and "20d" are rejected.</p>
     *
     * @return the amount that was bet
     * @throws java.util.NoSuchElementException if standard input is closed
     */
    private double promptValidBet() {
        while (true) {
            System.out.print("Enter your bet (available " + formatMoney(player.getBalance()) + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("[Error] Please enter a bet amount.");
                continue;
            }
            if (!input.matches(BET_PATTERN)) {
                System.out.println("[Error] '" + input + "' is not a valid amount. "
                        + "Enter a number with up to two decimals, e.g. 25 or 12.50 (no $ sign).");
                continue;
            }

            double amount;
            try {
                amount = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                // Defensive: the regex should already have excluded anything unparseable.
                System.out.println("[Error] '" + input + "' could not be read as a number.");
                continue;
            }

            if (amount <= 0) {
                System.out.println("[Error] Bet must be greater than $0.00.");
                continue;
            }
            if (amount > player.getBalance()) {
                System.out.println("[Error] Bet of " + formatMoney(amount)
                        + " exceeds your balance of " + formatMoney(player.getBalance()) + ".");
                continue;
            }

            if (player.placeBet(amount)) {
                System.out.println("Bet placed: " + formatMoney(player.getCurrentBet())
                        + "  |  Remaining balance: " + formatMoney(player.getBalance()));
                return player.getCurrentBet();
            }
            System.out.println("[Error] Bet could not be placed. Please try again.");
        }
    }

    /**
     * Asks the player to hit, stand or surrender until they enter a valid choice.
     * Input is trimmed and case-insensitive; "h" and "s" are accepted as shortcuts.
     *
     * @return "hit", "stand" or "surrender"
     * @throws java.util.NoSuchElementException if standard input is closed
     */
    private String promptValidAction() {
        while (true) {
            System.out.print("Choose an action - [H]it, [S]tand, or Surrender: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "hit":
                case "h":
                    return "hit";
                case "stand":
                case "s":
                    return "stand";
                case "surrender":
                    return "surrender";
                default:
                    System.out.println("[Error] Invalid action '" + input
                            + "'. Type 'hit' (h), 'stand' (s), or 'surrender'.");
            }
        }
    }

    /**
     * Asks whether to play another hand until the player answers yes or no.
     *
     * @return {@code true} to keep playing, {@code false} to quit
     * @throws java.util.NoSuchElementException if standard input is closed
     */
    private boolean promptPlayAgain() {
        while (true) {
            System.out.print("Play another hand? (Y/N): ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
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
     * Prints the balance, the active bet and both hands with their totals.
     *
     * @param revealDealer {@code true} to show the dealer's full hand;
     *                     {@code false} to show only the up card and "[Hidden Card]"
     */
    private void displayTable(boolean revealDealer) {
        System.out.println("Balance: " + formatMoney(player.getBalance())
                + "  |  Active bet: " + formatMoney(player.getCurrentBet()));
        System.out.println("Your hand:   " + playerHand
                + "  (Total: " + playerHand.calculateTotal() + ")");

        if (revealDealer) {
            System.out.println("Dealer hand: " + dealerHand
                    + "  (Total: " + dealerHand.calculateTotal() + ")");
        } else {
            Card upCard = dealerHand.getCards().get(0);
            System.out.println("Dealer hand: " + upCard + ", [Hidden Card]"
                    + "  (Showing: " + upCard.getBaseValue() + ")");
        }
    }

    /**
     * Formats an amount as dollars with two decimal places, e.g. "$1,234.50".
     * {@code Locale.US} keeps the format the same on every system.
     *
     * @param amount the amount to format
     * @return the formatted amount
     */
    private static String formatMoney(double amount) {
        return String.format(Locale.US, "$%,.2f", amount);
    }

    /**
     * Formats an amount with a leading "+" or "-", e.g. "+$10.00" or "-$5.00".
     *
     * @param amount the amount to format
     * @return the formatted amount, or "$0.00" if it rounds to zero
     */
    private static String formatSignedMoney(double amount) {
        if (amount > 0.004) {
            return "+" + formatMoney(amount);
        } else if (amount < -0.004) {
            return "-" + formatMoney(-amount);
        }
        return formatMoney(0.0);
    }
}
