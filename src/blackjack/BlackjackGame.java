package blackjack;
/**
 * Entry point for the Blackjack game.
 * 
 * @author Rishad Aniket
 * @version 0.2
 */
public class BlackjackGame {

    /**
     * Places some test bets and checks the payouts.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Player player = new Player(100.0);
        System.out.println("Starting balance: " + player.getBalance());

        System.out.println("Bet 0 accepted? " + player.placeBet(0) + " (expected false)");
        System.out.println("Bet 500 accepted? " + player.placeBet(500) + " (expected false)");

        player.placeBet(10);
        player.receivePayout(2.0);
        System.out.println("After winning a $10 bet: " + player.getBalance() + " (expected 110.0)");

        player.placeBet(10);
        player.receivePayout(2.5);
        System.out.println("After a $10 natural: " + player.getBalance() + " (expected 125.0)");

        player.placeBet(10);
        player.receivePayout(0.5);
        System.out.println("After surrendering $10: " + player.getBalance() + " (expected 120.0)");

        player.placeBet(20);
        player.receivePayout(0.0);
        System.out.println("After losing $20: " + player.getBalance() + " (expected 100.0)");
    }
}
