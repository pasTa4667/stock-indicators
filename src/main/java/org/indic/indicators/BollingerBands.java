package org.indic.indicators;

import org.indic.enums.Trend;
import org.indic.records.Bands;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class BollingerBands extends IndicatorUtils {

    /**
     * Calculates the Bollinger Bands. <br></br>
     * Middle band is calculated with {@link #smaList(List, int) smaList}
     * Upper and lower are calculated with the
     * {@link #standardDeviation(List, int) standardDeviation} and
     * the width (usually 2).
     * @param period Period where to start calculating from
     * @param prices List of Stock Prices
     * @param width The width, usually 2
     * @return The middle, lower and upper Bands
     */
    public static Bands calculateWithSma(int period, List<Double> prices, int width) {
        double deviation = IndicatorUtils.standardDeviation(prices, period).orElse(0.0);

        List<Double> middle = IndicatorUtils.smaList(prices, period);
        List<Double> upper = middle.stream().mapToDouble((value) -> value + width * deviation).boxed().toList();
        List<Double> lower = middle.stream().mapToDouble((value) -> value - width * deviation).boxed().toList();

        return new Bands(lower, middle, upper);
    }

    /**
     * Calculates the Bollinger Bands. <br></br>
     * Middle band is calculated with {@link #ema(List, int) ema}
     * Upper and lower are calculated with the
     * {@link #standardDeviation(List, int) standardDeviation} and
     * the width (usually 2).
     * @param period Period where to start calculating from
     * @param prices List of Stock Prices
     * @param width The width, usually 2
     * @return The middle, lower and upper Bands
     */
    public static Bands calculateWithEma(int period, List<Double> prices, int width) {
        double deviation = IndicatorUtils.standardDeviation(prices, period).orElse(0.0);

        List<Double> middle = IndicatorUtils.ema(prices, period);
        List<Double> upper = middle.stream().mapToDouble((value) -> value + width * deviation).boxed().toList();
        List<Double> lower = middle.stream().mapToDouble((value) -> value - width * deviation).boxed().toList();

        return new Bands(lower, middle, upper);
    }

    /**
     * Calculates the bandwidth of the provided bollinger bands. <br></br>
     * Bands must have the same size.
     * @param bands
     * @return The bandwidth as Optional<List>
     */
    public static Optional<List<Double>> bandwidth(Bands bands) {
        if(bands.middle().size() != bands.lower().size() || bands.lower().size() != bands.upper().size()) {
            return Optional.empty();
        }

        return Optional.of(IntStream.range(0, bands.middle().size())
                .mapToDouble((i) -> (bands.upper().get(i) - bands.lower().get(i)) / bands.middle().get(i))
                .boxed()
                .toList());
    }

    /**
     * Calculates a signal based on the last 2 values of the prices and bands.
     * If a price crosses over the lower band signals an upward trend.
     * If a price falls below the upper band signals a downward trend.
     * @param bands Bands with at least 2 values
     * @param prices At least the last 2 prices
     * @return A Trend signal
     */
    public static Trend generateSignalSimple(Bands bands, List<Double> prices) {
        if(bands.middle().size() < 2 || prices.size() < 2) {
            throw new IllegalArgumentException("Need Bands and prices of at least two days.");
        }

        double prevLower = bands.lower().get(bands.lower().size() - 2);
        double lower = bands.lower().get(bands.lower().size() - 1);

        double prevUpper = bands.upper().get(bands.upper().size() - 2);
        double upper = bands.upper().get(bands.upper().size() - 1);

        double prevPrice = prices.get(prices.size() - 2);
        double price = prices.get(prices.size() - 1);

        if(prevPrice < prevLower && price > lower) {
            return Trend.BULLISH;
        } else if(prevPrice > prevUpper && price < upper) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }
}


