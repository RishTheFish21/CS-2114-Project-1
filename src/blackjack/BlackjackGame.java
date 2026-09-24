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

    private static final String DIVIDER = "========================================";

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
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
    }

    /**
     * Runs rounds until the player runs out of money or chooses to stop.
     */
    public void startGame() {
        System.out.println(DIVIDER);
        System.out.println("        WELCOME TO BLACKJACK");
        System.out.println(DIVIDER);

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
        System.out.println("Final balance: " + formatMoney(player.getBalance()));
        System.out.println("Thanks for playing!");
    }

    /**
     * Plays one round: betting, dealing, natural Blackjack checks, the player's turn,
     * the dealer's turn and the result.
     */
    public void playRound() {
        playerHand.clear();
        dealerHand.clear();

        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("NEW ROUND | Balance: " + formatMoney(player.getBalance()));
        System.out.println(DIVIDER);
        promptValidBet();

        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        System.out.println(DIVIDER);
        showHands(false);

        if (playerHand.isNaturalBlackjack() && dealerHand.isNaturalBlackjack()) {
            showHands(true);
            System.out.println("Both hit Natural Blackjack! Push/Tie.");
            player.receivePayout(1.0);
            return;
        } else if (playerHand.isNaturalBlackjack()) {
            System.out.println("Natural Blackjack! Paid 3:2!");
            player.receivePayout(2.5);
            return;
        }

        System.out.println(DIVIDER);
        boolean playerTurnOver = false;
        while (!playerTurnOver) {
            String action = promptValidAction();
            if (action.equals("hit")) {
                Card card = deck.drawCard();
                playerHand.addCard(card);
                System.out.println("You drew: " + card);
                showHands(false);
                if (playerHand.isBust()) {
                    System.out.println("Bust! You went over 21.");
                    player.receivePayout(0.0);
                    return;
                }
                if (playerHand.calculateTotal() == 21) {
                    System.out.println("You have 21! Standing automatically.");
                    playerTurnOver = true;
                }
            } else if (action.equals("surrender")) {
                System.out.println("Surrendered hand. Half bet refunded.");
                player.receivePayout(0.5);
                return;
            } else {
                playerTurnOver = true;
            }
        }

        System.out.println(DIVIDER);
        System.out.println("Dealer reveals: " + dealerHand.getCards().get(1));
        showHands(true);
        while (dealerHand.calculateTotal() < 17) {
            Card card = deck.drawCard();
            dealerHand.addCard(card);
            System.out.println("Dealer draws: " + card);
            showHands(true);
        }


        System.out.println(DIVIDER);
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
        System.out.println("Balance: " + formatMoney(player.getBalance()));
    }

    /**
     * Prints the balance, the bet and both hands.
     *
     */
    private void showHands(boolean showDealerCard) {
        System.out.println("Balance: " + formatMoney(player.getBalance())
                + " | Bet: " + formatMoney(player.getCurrentBet()));
        System.out.println("Your hand: " + playerHand + " (Total: " + playerHand.calculateTotal() + ")");
        if (showDealerCard) {
            System.out.println("Dealer hand: " + dealerHand + " (Total: " + dealerHand.calculateTotal() + ")");
        } else {
            System.out.println("Dealer hand: " + dealerHand.getCards().get(0) + ", [Hidden Card]");
        }
    }

    /**
     * Asks for a bet until a valid one is entered, then places it.
     * Reads a whole line each time so bad input never gets stuck in the scanner.
     *
     * @return the amount that was bet
     */
    private double promptValidBet() {
        while (true) {
            System.out.print("Enter your bet: ");
            String input = scanner.nextLine().trim();
            double amount;
            try {
                amount = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[Error] '" + input + "' is not a number. Try again.");
                continue;
            }

            if (amount <= 0) {
                System.out.println("[Error] Bet must be more than $0.00.");
            } else if (amount > player.getBalance()) {
                System.out.println("[Error] You only have " + formatMoney(player.getBalance()) + ".");
            } else if (player.placeBet(amount)) {
                return amount;
            }
        }
    }

    /**
     * Asks the player to hit, stand or surrender until they enter a valid choice.
     * Accepts "h" for hit and "s" for stand.
     *
     * @return "hit", "stand" or "surrender"
     */
    private String promptValidAction() {
        while (true) {
            System.out.print("Hit (h), Stand (s) or Surrender? ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("hit") || input.equals("h")) {
                return "hit";
            } else if (input.equals("stand") || input.equals("s")) {
                return "stand";
            } else if (input.equals("surrender")) {
                return "surrender";
            }
            System.out.println("[Error] Invalid choice. Type hit, stand or surrender.");
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
            }
            System.out.println("[Error] Please enter Y or N.");
        }
    }

    /**
     * Formats an amount of money with a dollar sign and two decimal places.
     *
     * @param amount the amount to format
     * @return the formatted amount
     */
    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }
}
