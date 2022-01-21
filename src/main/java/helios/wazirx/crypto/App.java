package helios.wazirx.crypto;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 *
 */

public class App {

    static void applyCal(double usdtCurr) {
        // 0.5092
        final float bankTransferFee = 23.6F;
        final float takerFeePercent = 0.2F / 100.0F;
        final int investment = 40000;
        final float exProfit = 125;

        double netInvestment = (investment - bankTransferFee) - (investment - bankTransferFee) * takerFeePercent;
        double netDeduction = investment - netInvestment;
        double usdtTotal = netInvestment / usdtCurr;

        final double calExProfit = exProfit + netDeduction;

        double profitPercent1 = exProfit / netInvestment;
        double profitPercent2 = calExProfit / netInvestment;

        double sellPrice1 = usdtCurr + (usdtCurr * profitPercent1);
        double netProfit1 = netInvestment * profitPercent1 - netDeduction;
        double netSum1 = investment + netInvestment * profitPercent1 - netDeduction;

        double sellPrice2 = usdtCurr + (usdtCurr * profitPercent2);
        double netProfit2 = netInvestment * profitPercent2 - netDeduction;
        double netSum2 = investment + netInvestment * profitPercent2 - netDeduction;

        System.out.println();
        System.out.printf("net investment = %.2f\n", netInvestment);
        System.out.println();
        System.out.printf("net deduction = %.2f\n", netDeduction);
        System.out.println();
        System.out.printf("curr price = %.2f\n", usdtCurr);
        System.out.printf("total usdt = %.2f\n", usdtTotal);
        System.out.println();
        System.out.printf("[1]sell Price = %.2f\n", sellPrice1);
        System.out.printf("[1]net profit = %.2f\n", netProfit1);
        System.out.printf("[1]net sum = %.2f\n", netSum1);
        System.out.println();
        System.out.printf("[2]sell Price = %.2f\n", sellPrice2);
        System.out.printf("[2]net profit = %.2f\n", netProfit2);
        System.out.printf("[2]net sum = %.2f\n", netSum2);
        System.out.println();
    }

    static void loop(final Price price) throws IOException {

        class ToDo extends TimerTask {
            double prevPrice = 0.0;

            public ToDo() {
            }

            public ToDo(double prevPrice) {
                this.prevPrice = prevPrice;
            }

            @Override
            public void run() {
                double currAskPrice = 0.0;
                try {
                    currAskPrice = price.getCurrAskPrice();
                    if (currAskPrice != prevPrice) {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        prevPrice = currAskPrice;
                        applyCal(currAskPrice);
                    }
                    Timer nextRound = new Timer();
                    TimerTask nextToDo = new ToDo(prevPrice);
                    nextRound.schedule(nextToDo, 350);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        Timer nextRound = new Timer();
        TimerTask nextToDo = new ToDo();
        nextRound.schedule(nextToDo, 300);
    }

    public static void main(String[] args) {
        try {
            Price price = new Price(AccessPointLoopkup.getTICKER(), MarketLookup.getUSDTINR());
            // double currAskPrice = price.getCurrAskPrice();

            // System.out.println(currAskPrice);
            // applyCal(currAskPrice);
            loop(price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
