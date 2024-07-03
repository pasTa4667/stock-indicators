package org.indic.indicators;

import org.indic.enums.Trend;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AverageTrueRange {

    /**
     * Calculates the true range.
     * @param high High of a certain day or period
     * @param low Low of a certain day or period
     * @param yes_close The close previous to that period
     * @return The true range
     */
    public static double calculateTR(double high, double low, double yes_close) {
        return Math.max(Math.max(high - low, Math.abs(high - yes_close)), Math.abs(low - yes_close));
    }

    /**
     * Calculates the Average True Range.
     * Starts from 0 to closes.size(), the first (0) value is just
     * high[0] - low[0], since closes(-1) does not exist.
     * @param period
     * @param closes
     * @param highs
     * @param lows
     * @return List of Average True Range values
     */
    public static List<Double> calculateAtr(int period, List<Double> closes, List<Double> highs, List<Double> lows) {
        if (closes.size() < period) return new ArrayList<>();
        List<Double> trValues = IntStream.range(0, closes.size())
                .mapToDouble((i) -> i > 0 ? calculateTR(highs.get(i), lows.get(i), closes.get(i - 1)) : highs.get(i) - lows.get(0))
                .boxed()
                .toList();

        double initialATR = trValues.stream().limit(period).reduce(0.0, Double::sum) / period;

        List<Double> atrValues = new ArrayList<>();
        atrValues.add(initialATR);

        for (int i = period; i < trValues.size(); i++) {
            double previousATR = atrValues.get(atrValues.size() - 1);
            double currentTR = trValues.get(i);
            double currentATR = ((previousATR * (period - 1)) + currentTR) / period;
            atrValues.add(currentATR);
        }

        return atrValues;
    }


    /**
     * Generates a Trend based on whether the last atr is above or below the given thresholds.
     * @param atr Average True Range value(s)
     * @param upperThreshold Upper Threshold to compare
     * @param lowerThreshold Lower Threshold to compare
     * @return Trend
     */
    public static Trend generateSignalSimple(List<Double> atr, double upperThreshold, double lowerThreshold) {
        if(atr.size() == 0) {
            throw new IllegalArgumentException("Atr must have at least one value");
        }

        double lastAtr = atr.get(atr.size() - 1);

        if(lastAtr > upperThreshold) {
            return Trend.BULLISH;
        } else if(lastAtr < lowerThreshold) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }

    /**
     * Generates a Trend based on the last atr, close, high and low.
     * @param atr Average True Range value(s)
     * @param atrMultiplier A multiplier, often 2
     * @param lastClose Latest close
     * @param lastHigh Latest high
     * @param lastLow Latest low
     * @return Trend
     */
    public static Trend generateBreakoutSignal(List<Double> atr, double atrMultiplier, double lastClose, double lastHigh, double lastLow) {
        if(atr.size() == 0) {
            throw new IllegalArgumentException("Atr must have at least one value");
        }

        double lastAtr = atr.get(atr.size() - 1);

        double buyLevel = lastLow + lastAtr * atrMultiplier;
        double sellLevel = lastHigh + lastAtr * atrMultiplier;

        if(lastClose > buyLevel) {
            return Trend.BULLISH;
        } else if(lastClose < sellLevel) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }

}
