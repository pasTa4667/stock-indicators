package org.indic.indicators;

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
    public static Bands calculate(int period, List<Double> prices, int width) {
        double deviation = IndicatorUtils.standardDeviation(prices, period).orElse(0.0);

        List<Double> middle = IndicatorUtils.smaList(prices, period);
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

}


