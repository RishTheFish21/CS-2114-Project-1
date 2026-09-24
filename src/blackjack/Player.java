package blackjack;
/**
 *
 * @author Wyatt
 * @version 1.0
 */
public class Player {

    /** The money the player has available to bet. */
    private double balance;

    /** The amount the player has bet on the current round, or 0.0 if none. */
    private double currentBet;

    /**
     * Creates a player with a starting balance.
     *
     * @param initialBalance the amount of money the player starts with
     * @throws IllegalArgumentException if the balance is negative, NaN or infinite
     */
    public Player(double initialBalance) {
        if (!isFinite(initialBalance) || initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance must be a finite, non-negative amount");
        }
        this.balance = roundToCents(initialBalance);
        this.currentBet = 0.0;
    }

    /**
     * Returns the player's available balance.
     *
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the player's current bet.
     *
     * @return the current bet, or 0.0 if no bet is placed
     */
    public double getCurrentBet() {
        return currentBet;
    }

    /**
     * Places a bet and takes it out of the balance. The bet is valid when it is
     * greater than 0, no more than the balance, and no other bet is still on the table.
     *
     * @param amount the amount to bet
     * @return {@code true} if the bet was placed, {@code false} if it was invalid
     */
    public boolean placeBet(double amount) {
        if (!isFinite(amount) || currentBet > 0) {
            return false;
        }
        double bet = roundToCents(amount);
        if (bet <= 0 || bet > balance) {
            return false;
        }
        balance = roundToCents(balance - bet);
        currentBet = bet;
        return true;
    }

    /**
     * Pays out the current bet and resets it to 0.
     *
     * <p>Multipliers: 2.0 for a win, 2.5 for a natural Blackjack (3:2), 1.0 for a push,
     * 0.5 for a surrender and 0.0 for a loss.</p>
     *
     * @param multiplier the amount to multiply the current bet by
     * @throws IllegalArgumentException if the multiplier is negative, NaN or infinite
     */
    public void receivePayout(double multiplier) {
        if (!isFinite(multiplier) || multiplier < 0) {
            throw new IllegalArgumentException("Payout multiplier must be a finite, non-negative number");
        }
        double payout = currentBet * multiplier;
        balance = roundToCents(balance + payout);
        currentBet = 0.0;
    }

    /**
     * Rounds an amount to the nearest cent.
     *
     * @param value the amount to round
     * @return the amount rounded to two decimal places
     */
    private static double roundToCents(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Checks that a number is a real, finite value.
     *
     * @param value the number to check
     * @return {@code true} if the value is neither NaN nor infinite
     */
    private static boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }
}
