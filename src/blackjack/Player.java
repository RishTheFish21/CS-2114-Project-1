package blackjack;
/**
 * Represents the player's money and the bet they currently have on the table.
 *
 * @author aniket
 * @version 0.3
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
        this.balance = initialBalance;
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
     * Places a bet. The bet must be greater than 0 and no more than the balance.
     * If it is valid, it is taken out of the balance.
     *
     * @param amount the amount to bet
     * @return true if the bet was placed, false if it was invalid
     */
    public boolean placeBet(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            currentBet = amount;
            return true;
        }
        return false;
    }

    /**
     * Pays out the current bet and resets it to 0.
     *
     *
     * @param multiplier the amount to multiply the current bet by
     */
    public void receivePayout(double multiplier) {
        double payout = currentBet * multiplier;
        balance += payout;
        currentBet = 0.0;
    }
}
