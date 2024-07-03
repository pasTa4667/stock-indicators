package org.indic.indicators;

import org.indic.enums.Trend;

import java.util.ArrayList;
import java.util.List;

public class RelativeStrengthIndex {

    //TODO: Improve the method performance
    /**
     * Calculates the Relative Strength Index.
     * Calculates the average loss and gain for the
     * first period values as initial values. If on a
     * specific day was a gain, adds 0 to the loss average and
     * the other way around. After the initial period
     * calculates the averages, rs and rsi = 100 - (100 / (1 + rs)).
     * Calculation starts from closes[1] since we do closes[i] - closes[i -1].
     * @param period The index where to start calculating rsi values
     * @param closes The closes as List
     * @return The RS Indexes as List.
     */
    public static List<Double> calculate(int period, List<Double> closes) {
        List<Double> rsi = new ArrayList<>();

        double avgGains;
        double avgLosses;

        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = 1; i < period; i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            if (diff > 0) {
                gains.add(diff);
                losses.add(0.0);
            } else {
                losses.add(-diff);
                gains.add(0.0);
            }
        }

        double rs;

        // Calculate RSI for the rest of the values
        for (int i = period; i < closes.size(); i++) {
            double diff = closes.get(i) - closes.get(i - 1);

            if (diff > 0) {
                gains.add(diff);
                losses.add(0.0);
            } else {
                losses.add(-diff);
                gains.add(0.0);
            }
            avgGains = gains.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgLosses = losses.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            rs = avgGains / avgLosses;
            rsi.add(100 - (100 / (1 + rs)));
        }

        return rsi;
    }

    /**
     * Generates a signal based on the last period values.
     * @param rsi Values, at least period + 1
     * @param period The amount of rsi values to look at
     * @return Trend signal
     */
    public static Trend generateSignal(List<Double> rsi, int period) {
        if(rsi.get(rsi.size() - 1) == 100.0) return Trend.BEARISH; // Overbought
        if(rsi.get(rsi.size() - 1) == 0.0) return Trend.BULLISH; // Oversold

        if (rsi.size() < period + 1) {
            throw new IllegalArgumentException("RSI must have at least period + 1 values.");
        }

        boolean oversold = true;
        boolean overbought = true;

        // Check RSI values over the specified period
        for (int i = rsi.size() - period - 1; i < rsi.size(); i++) {
            if (rsi.get(i) >= 30) {
                oversold = false;
            }
            if (rsi.get(i) <= 70) {
                overbought = false;
            }
        }

        double latestRsi = rsi.get(rsi.size() - 1);

        // Confirm the latest RSI value for the signal
        if (oversold && latestRsi > 30) {
            return Trend.BULLISH;
        } else if (overbought && latestRsi < 70) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }

}
