package io.github._13shoot.normieprogression.visibility;

public class VisibilityService {

    public static int calculateVisibility(int daysAlive, double totalMoneyEarned) {
        double moneyScore = 0;
        if (totalMoneyEarned > 0) {
            moneyScore = Math.log10(totalMoneyEarned + 1) * 5;
        }
        return (int) Math.floor(daysAlive + moneyScore);
    }
}
