package blackjack;
/**
 *
 * @author Rishad
 * @version 1.0
 */
public class Player {

    private double balance;
    private double currentBet;

    /**
     * Creates a player with a starting balance.
     *
     * @param initialBalance the amount of money the player starts with
     */
    public Player(double initialBalance) {
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
     * @return the current bet
     */
    public double getCurrentBet() {
        return currentBet;
    }

    /**
     * Places a bet and takes it out of the balance. 
     * @param amount the amount to bet
     * @return true if the bet was placed, false if it was invalid
     */
    public boolean placeBet(double amount) {
        if (currentBet > 0) {
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
     * @param multiplier the amount to multiply the current bet by
     */
    public void receivePayout(double multiplier) {
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

    
}
