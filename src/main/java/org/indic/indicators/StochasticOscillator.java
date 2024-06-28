package org.indic.indicators;

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
    public static OscillatorResult calculate(int period, List<Double> closes, List<Double> highs, List<Double> lows) {
        if (closes.size() < period) {
            return new OscillatorResult(new ArrayList<>(), 0.0, 0.0);
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

        double d = k.stream().reduce(0.0, Double::sum) / k.size();
        double d_3 = k.subList(k.size() - 3, k.size()).stream().reduce(0.0, Double::sum) / 3;

        return new OscillatorResult(k, d, d_3);
    }
}

