package org.indic.indicators;

import org.indic.enums.Trend;
import org.indic.records.OscillatorResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StochasticOscillator {

    /**
     * Calculates the Stochastic Oscillator as follows:
     * fills closes until closes.size() == highs.size(), then calculates
     * k[i] = 100 * ((closes[i] - low) / (high - low))
     * starting from period and takes the
     * high and low from the past i - period days
     * @param period - period to start calculating the SO from
     * @param closes - list of closes
     * @param highs - list of highs, should be same size as lows
     * @param lows - list of lows, should be same size as highs
     * @return k = list of SO values, d = average of k, d_3 = three period average of k
     */
    public static OscillatorResult calculate(int period, int periodD, List<Double> closes, List<Double> highs, List<Double> lows) {
        if (closes.size() < period) {
            throw new IllegalArgumentException("Closes size has to be larger than period");
        }

        List<Double> fullCloses = new ArrayList<>();

        while(fullCloses.size() < highs.size() - closes.size()) {
            fullCloses.addFirst(0.0);
        }

        fullCloses.addAll(closes);

        List<Double> k = IntStream.range(period - 1, fullCloses.size())
                .mapToDouble(i -> {
                    double low = lows.subList(i - period + 1, i + 1).stream().min(Double::compare).orElse(Double.NaN);
                    double high = highs.subList(i - period + 1, i + 1).stream().max(Double::compare).orElse(Double.NaN);
                    return 100 * ((fullCloses.get(i) - low) / (high - low));
                })
                .boxed()
                .toList();

        List<Double> d = IndicatorUtils.smaList(k, periodD);
        return new OscillatorResult(k, d);
    }

    /**
     * Generates a Trend based on the 2 last values
     * @param results %K and %D, at least 2 values
     * @return A Trend
     */
    public static Trend generateSignalSimple(OscillatorResult results) {
        if(results.d().size() < 2 || results.k().size() < 2) {
            throw new IllegalArgumentException("%D and %K must have at least 2 values");
        }

        double curK = results.k().get(results.k().size() - 1);
        double prevK = results.k().get(results.k().size() - 2);
        double curD = results.d().get(results.d().size() - 1);
        double prevD = results.d().get(results.d().size() - 2);

        if(prevK < prevD && curK > curD) {
            return Trend.BULLISH;
        } else if(prevK > prevD && curK < curD) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }
}

