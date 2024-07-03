package org.indic.indicators;

import org.indic.enums.Trend;

import java.util.ArrayList;
import java.util.List;

public class OnBalanceVolume {


    /**
     * Calculates the On-Balance-Volume for the given closes and volume, returns period values.
     * closes.get(i) should match volumes.get(i)
     * @param closes - Closes with oldest (index 0)
     * @param volumes - Volumes with oldest (index 0)
     * @return List with obv values
     */
    public static List<Integer> calculate(int period, List<Double> closes, List<Integer> volumes) {
        if(closes.size() <= period) return new ArrayList<>();

        List<Integer> obv = new ArrayList<>();
        obv.add(0);

        for(int i = closes.size() - period; i < closes.size(); i++) {
            double diff = closes.get(i) - (i <= 0 ? 0.0 : closes.get(i - 1));
            if(diff > 0) {
                obv.add(obv.get(obv.size() - 1) + volumes.get(i));
            } else if(diff < 0) {
                obv.add(obv.get(obv.size() - 1) - volumes.get(i));
            } else {
                obv.add(obv.get(obv.size() - 1));
            }
        }

        return obv;
    }

    /**
     * Calculates the On-Balance-Volume for the given closes and volumes.
     * closes.get(i) should match volumes.get(i)
     * @param closes - Closes with oldest (index 0)
     * @param volumes - Volumes with oldest (index 0)
     * @return List with obv values
     */
    public static List<Integer> calculate(List<Double> closes, List<Integer> volumes) {
        List<Integer> obv = new ArrayList<>();
        obv.add(volumes.get(0));

        for(int i = 1; i < closes.size(); i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            if(diff > 0) {
                obv.add(obv.get(obv.size() - 1) + volumes.get(i));
            } else if(diff < 0) {
                obv.add(obv.get(obv.size() - 1) - volumes.get(i));
            } else {
                obv.add(obv.get(obv.size() - 1));
            }
        }

        return obv;
    }

    /**
     * Compares obv values to check for a bullish or bearish trend.
     * @param obvValues
     * @return A Trend (Bullish, Bearish, None)
     */
    public static Trend generateTrendSimple(List<Integer> obvValues) {
        if (obvValues == null || obvValues.size() < 3) {
            throw new IllegalArgumentException("OBV values list must contain at least 3 values.");
        }

        int size = obvValues.size();
        int recent = obvValues.get(size - 1);
        int prev = obvValues.get(size - 2);
        int prevPrev = obvValues.get(size - 3);

        // Check for upward trend (Buy Signal)
        if (recent > prev && prev > prevPrev) {
            return Trend.BULLISH;
        }

        // Check for downward trend (Sell Signal)
        if (recent < prev && prev < prevPrev) {
            return Trend.BEARISH;
        }

        // Otherwise, hold
        return Trend.NONE;
    }

    /**
     * Compares the ema of obv values to check for a bullish or bearish trend.
     * @param obvValues
     * @return A Trend (Bullish, Bearish, None)
     */
    public static Trend generateTrendWithEma(List<Integer> obvValues, int shortPeriod, int longPeriod) {
        if (obvValues == null || obvValues.size() < longPeriod) {
            throw new IllegalArgumentException("OBV values list must contain at least as many values as the longest period.");
        }

        List<Double> shortEma = IndicatorUtils.ema(obvValues, shortPeriod);
        List<Double> longEma = IndicatorUtils.ema(obvValues, longPeriod);

        double shortEmaRecent = shortEma.get(shortEma.size() - 1);
        double longEmaRecent = longEma.get(longEma.size() - 1);
        double shortEmaPrev = shortEma.get(shortEma.size() - 2);
        double longEmaPrev = longEma.get(longEma.size() - 2);

        // Buy signal when short-term EMA crosses above long-term EMA
        if (shortEmaPrev <= longEmaPrev && shortEmaRecent > longEmaRecent) {
            return Trend.BULLISH;
        }

        // Sell signal when short-term EMA crosses below long-term EMA
        if (shortEmaPrev >= longEmaPrev && shortEmaRecent < longEmaRecent) {
            return Trend.BEARISH;
        }

        // Hold signal when there's no clear crossover
        return Trend.NONE;
    }

}

