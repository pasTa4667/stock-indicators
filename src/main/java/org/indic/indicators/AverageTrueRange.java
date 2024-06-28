package org.indic.indicators;

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

}
