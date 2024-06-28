import org.indic.indicators.*;
import org.indic.records.Bands;
import org.indic.records.MacdResult;
import org.indic.records.OscillatorResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndicatorTest {

    @Test
    void smaTest() {
        List<Double> closes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        int period = 3;
        OptionalDouble sma = IndicatorUtils.sma(closes, period);
        assertTrue(sma.isPresent());
        assertEquals(4.0, sma.getAsDouble(), 0.001);
    }

    @Test
    void smaNewTest() {
        List<Double> closes = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        int period = 3;
        OptionalDouble sma = IndicatorUtils.sma(closes, period);
        assertTrue(sma.isPresent());
        assertEquals(4.0, sma.getAsDouble(), 0.001);
    }


    @Test
    void smaTestWithLargeData() {
        List<Double> closes = IntStream.rangeClosed(1, 100).asDoubleStream().boxed().toList();
        int period = 50;
        OptionalDouble sma = IndicatorUtils.sma(closes, period);
        assertTrue(sma.isPresent());
        assertEquals(75.5, sma.getAsDouble(), 0.001);
    }

    @Test
    void emaTest() {
        List<Double> closes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        int period = 3;
        List<Double> actualEma = IndicatorUtils.ema(closes, period);
        List<Double> expectedEma = List.of(2.0, 3.0, 4.0);

        assertEquals(expectedEma, actualEma);
    }

    @Test
    void emaTestWithLargeData() {
        List<Double> closes = List.of(22.27, 22.19, 22.08, 22.17, 22.18, 22.13, 22.23, 22.43, 22.24, 22.29, 22.15, 22.39, 22.38, 22.61, 23.36, 24.05, 23.75, 23.83, 23.95, 23.63, 23.82, 23.87, 23.65, 23.19, 23.10, 23.33, 22.68, 23.10, 22.40, 22.17);
        int period = 3;
        List<Double> actualEma = IndicatorUtils.emaWithFirstPriceAsInitial(closes, period);
        List<Double> expectedEma = List.of(22.27,22.23,22.155,22.163,22.171,22.151,22.19,22.31,22.275,22.283,22.216,22.303,22.342,22.476,22.918,23.484,23.617,23.723,23.837,23.733,23.777,23.823,23.737,23.463,23.282,23.306,22.993,23.046,22.723,22.447);
        assertEquals(expectedEma.getLast(), actualEma.getLast(), 0.01);
    }

    @Test
    void smaListTest() {
        List<Double> closes = List.of(1.0,2.0,3.0,4.0,5.0,6.0,7.0);
        int period = 3;
        List<Double> actualSma = IndicatorUtils.smaList(closes, period);
        List<Double> expectedSma = List.of(2.0,3.0,4.0,5.0,6.0);
        assertEquals(expectedSma.getLast(), actualSma.getLast());
    }

    @Test
    void smaListTestWithLargeData() {
        List<Double> closes = List.of(2.0,4.0,6.0,8.0,12.0,14.0,16.0,18.0,20.0,22.0,23.0,21.0,23.4,22.0,21.0,19.0,22.5,22.2,22.0,24.3,21.0,22.1,21.0,24.3,19.0,23.2,24.4,23.4,22.0,19.0,18.4,20.4,21.4,19.9,22.5,22.8,22.0,24.3,24.0,25.1);
        int period = 20;
        List<Double> actualSma = IndicatorUtils.smaList(closes, period);
        List<Double> expectedSma = List.of(17.12,18.07,18.975,19.725,20.54,20.89,21.35,21.77,22.04,22.14,21.99,21.76,21.73,21.63,21.525,21.6,21.79,21.765,21.87,21.97,22.01);
        assertEquals(expectedSma.getLast(), actualSma.getLast(), 0.11);
    }

    @Test
    void atrTest() {
        List<Double> highs = List.of(10.0,11.0,12.0,13.0, 14.0);
        List<Double> lows = List.of(8.0,9.0,10.0,11.0,12.0);
        List<Double> closes = List.of(9.0,10.0,11.0,12.0,13.0);
        int period = 3;
        List<Double> atr = AverageTrueRange.calculateAtr(period, highs, lows, closes);

        assertEquals(2.0, atr.getLast(), 0.01);
    }

    @Test
    void atrTestReal() {
        List<Double> highs = List.of(48.70, 48.72, 48.90, 48.87, 48.82, 48.81, 48.74, 48.70, 48.73, 48.96);
        List<Double> lows = List.of(47.79, 48.14, 48.39, 48.37, 48.24, 48.40, 48.22, 48.39, 48.48, 48.42);
        List<Double> closes = List.of(48.16, 48.61, 48.75, 48.63, 48.74, 48.58, 48.65, 48.72, 48.71, 48.90);
        int period = 3;
        List<Double> atr = AverageTrueRange.calculateAtr(period, highs, lows, closes);

        assertEquals(0.43, atr.getLast(), 0.01);
    }

    @Test
    void standardDeviationTest() {
        List<Double> closes = List.of(48.16, 48.61, 48.75, 48.63, 48.74, 48.58, 48.65, 48.72, 48.71, 48.90);
        int period = 10;
        double dev = IndicatorUtils.standardDeviation(closes, period).orElse(0.0);
        assertEquals(0.183262107376294, dev);
    }

    @Test
    void obvTest() {
        List<Integer> v = List.of(8200, 8100, 8300, 8900, 9200, 13300, 10300, 9900, 10100, 11300, 12600, 10700, 11500);
        List<Double> c = List.of(53.30, 53.32, 53.72, 54.19, 53.92, 54.65, 54.60, 54.21, 54.53, 53.79, 53.66, 53.56, 53.57);
        int period = 13;
        List<Integer> obv = OnBalanceVolume.calculate(c, v);
        assertEquals(4400, obv.getLast());
    }

    @Test
    void macdTest() {
        List<Double> c = List.of(53.30, 53.32, 53.72, 54.19, 53.92, 54.65, 54.60, 54.21, 54.53, 53.79, 53.66, 53.56, 53.57);
        MacdResult res = Macd.calculateResult(4, 8, 6, c);
        List<Double> histogram = res.histogram();
        List<Double> line = res.line();
        List<Double> signal = res.signal();
    }

    @Test
    void rsiTest() {
        List<Double> c = List.of(12.0,11.0,12.0,14.0,18.0,12.0,15.0,13.0,16.0,12.0,11.0,13.0,15.0,14.0,16.0,18.0,22.0,19.0,24.0,17.0,19.0);
        List<Double> result = RelativeStrengthIndex.calculate(3, c);
    }

    @Test
    void bandsTest() {
        List<Double> c = List.of(12.0,11.0,12.0,14.0,18.0,12.0,15.0,13.0,16.0,12.0,11.0,13.0,15.0,14.0,16.0,18.0,22.0,19.0,24.0,17.0,19.0);
        Bands b = BollingerBands.calculate(3, c, 2);
    }

    @Test
    void soTest() {
        List<Double> highs = List.of(127.01,127.62,126.59,127.35,128.17,128.43,127.37,126.42,126.90,126.85,125.65,125.72,127.16,127.72,127.69,128.22,128.27,128.09,128.27,127.74,128.77,129.29,130.06,129.12,129.29,128.47,128.09,128.65,129.14,128.64);
        List<Double> lows = List.of(125.36,126.16,124.93,126.09,126.82,126.48,126.03,124.83,126.39,125.72,124.56,124.57,125.07,126.86,126.63,126.80,126.71,126.80,126.13,125.92,126.99,127.81,128.47,128.06,127.61,127.60,127.00,126.90,127.49,127.40);
        List<Double> closes = List.of(127.29,127.18,128.01,127.11,127.73,127.06,127.33,128.71,127.87,128.58,128.60,127.93,128.11,127.60,127.60,128.69,128.27);
        int period = 14;
        OscillatorResult res = StochasticOscillator.calculate(period, closes, highs, lows);
        List<Double> expected = List.of(70.44,67.61,89.20,65.81,81.75,64.52,74.53,98.58,70.10,73.06,73.42,61.23,60.96,40.39,40.39,66.83,56.73);
        assertEquals(expected.getLast(), res.k().getLast(), 0.1);
    }

}
