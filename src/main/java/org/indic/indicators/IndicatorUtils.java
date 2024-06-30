package org.indic.indicators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class IndicatorUtils {

    /**
     * Calculates the sma, but takes a range of:
     * prices.size() - period to prices.size()
     * instead of period to prices.size().
     * Also does not take the initial values before
     * into account.
     * @param prices
     * @param period
     * @return the sma as OptionalDouble
     */
    public static <T extends Number> OptionalDouble smaWithPeriodValues(List<T> prices, int period) {
        if(prices.size() < period) return OptionalDouble.empty();
        return IntStream.range(prices.size() - period, prices.size())
                .mapToDouble((i) -> prices.get(i).doubleValue())
                .average();
    }

    /**
     * Calculates the sma the standard way:
     * takes 0 to period values as initial and
     * proceeds to calculate the sma with the
     * remaining values.
     * @param prices
     * @param period
     * @return The sma as OptionalDouble
     */
    public static <T extends Number> OptionalDouble sma(List<T> prices, int period) {
        if(prices.size() < period) return OptionalDouble.empty();
        return IntStream.rangeClosed(0, prices.size() - period)
                .mapToDouble(i -> prices.subList(i, i + period).stream()
                        .mapToDouble(Number::doubleValue).average().orElse(0.0))
                .average();
    }

    /**
     * Calculates the sma values the standard way, see {@link #sma(List, int) sma}.
     * @param prices
     * @param period
     * @return The sma values as list
     */
    public static <T extends Number> List<Double> smaList(List<T> prices, int period) {
        if (prices.size() < period) return Collections.emptyList();
        return IntStream.rangeClosed(0, prices.size() - period)
                .mapToDouble(i -> prices.subList(i, i + period).stream().mapToDouble(Number::doubleValue).average().orElse(0.0))
                .boxed()
                .toList();
    }

    /**
     * Calculates the ema values the standard way, with the sma of prices
     * 0 to period as initial ema value and 2 / period + 1 as multiplier.
     * @param prices
     * @param period
     * @return The ema values as list
     */
    public static <T extends Number> List<Double> ema(List<T> prices, int period) {
        if(prices.size() < period) return new ArrayList<>();

        List<Double> emaValues = new ArrayList<>();
        double ema = sma(prices.subList(0, period), period).orElse(0.0);
        emaValues.add(ema);
        double multiplier = 2.0 / (period + 1);

        for(int i = period; i < prices.size(); i++) {
            ema = (prices.get(i).doubleValue() - ema) * multiplier + ema;
            emaValues.add(ema);
        }

        return emaValues;
    }

    /**
     * Calculates the ema values the standard way, with prices[0] as initial ema
     * value and 2 / period + 1 as multiplier.
     * @param prices
     * @param period
     * @return The ema values as list
     */
    public static <T extends Number> List<Double> emaWithFirstPriceAsInitial(List<T> prices, int period) {
        if(prices.size() < period) return new ArrayList<>();

        List<Double> emaValues = new ArrayList<>();
        double ema = prices.get(0).doubleValue();
        emaValues.add(ema);
        double multiplier = 2.0 / (period + 1);

        for(int i = period; i < prices.size(); i++) {
            ema = (prices.get(i).doubleValue() - ema) * multiplier + ema;
            emaValues.add(ema);
        }

        return emaValues;
    }

    /**
     * Calculates the standard deviation.
     * @param prices
     * @param period
     * @return The standard deviation as OptionalDouble
     */
    public static OptionalDouble standardDeviation(List<Double> prices, int period) {
        if (prices.size() < period) return OptionalDouble.empty();

        List<Double> subPrices = prices.subList(prices.size() - period, prices.size());
        double mean = subPrices.stream().mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double variance = subPrices.stream()
                .mapToDouble(price -> Math.pow(price - mean, 2))
                .average()
                .orElse(0.0);

        return OptionalDouble.of(Math.sqrt(variance));
    }

}
