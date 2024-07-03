package org.indic.indicators;

import org.indic.enums.Trend;
import org.indic.records.MacdResult;

import java.util.ArrayList;
import java.util.List;

public class Macd extends IndicatorUtils {

    /**
     * Calculates the moving average convergence/divergence Line.
     * If shortEma.size() > longEma.size(), fills up the
     * longEma with the sma to match shortEma size. So
     * the returned line has only valid values.
     * @param shortPeriod
     * @param longPeriod
     * @param prices
     * @return The Macd Line
     */
    public static List<Double> calculateLine(int shortPeriod, int longPeriod, List<Double> prices) {
        List<Double> shortEma = IndicatorUtils.ema(prices, shortPeriod);
        List<Double> longEma = IndicatorUtils.ema(prices, longPeriod);

        // fill missing values
        double longSma = IndicatorUtils.sma(prices, longPeriod).orElse(longEma.getFirst());
        while(shortEma.size() > longEma.size()) {
            longEma.add(0, longSma);
        }

        List<Double> macdLine = new ArrayList<>();
        for(int i = 0; i < shortEma.size(); i++) {
            macdLine.add(shortEma.get(i) - longEma.get(i));
        }

        return macdLine;
    }

    /**
     * Calculates the moving average convergence/divergence Signal Line.
     * If signalLine.size() < macdLine.size(), fills up the
     * signalLine with the sma to match macdLine size.
     * @param shortPeriod
     * @param longPeriod
     * @param signalPeriod
     * @param prices
     * @return The Signal Line
     */
    public static List<Double> calculateSignal(int shortPeriod, int longPeriod, int signalPeriod, List<Double> prices) {
        List<Double> macdLine = calculateLine(shortPeriod, longPeriod, prices);
        List<Double> signalLine = IndicatorUtils.ema(macdLine, signalPeriod);

        double signalSma = IndicatorUtils.sma(macdLine, signalPeriod).orElse(signalLine.getFirst());
        while(signalLine.size() < macdLine.size()) {
            signalLine.add(0, signalSma);
        }

        return signalLine;
    }

    /**
     * Calculates the moving average convergence/divergence histogram.
     * @param shortPeriod
     * @param longPeriod
     * @param signalPeriod
     * @param prices
     * @return The Macd Histogram
     */
    public static List<Double> calculateHistogram(int shortPeriod, int longPeriod, int signalPeriod, List<Double> prices) {
        List<Double> macdLine = calculateLine(shortPeriod, longPeriod, prices);
        List<Double> signalLine = IndicatorUtils.ema(macdLine, signalPeriod);

        double signalSma = IndicatorUtils.sma(macdLine, signalPeriod).orElse(signalLine.getFirst());
        while(signalLine.size() < macdLine.size()) {
            signalLine.add(0, signalSma);
        }

        List<Double> histogram = new ArrayList<>();

        for(int i = 0; i < macdLine.size(); i++) {
            histogram.add(macdLine.get(i) - signalLine.get(i));
        }

        return histogram;
    }

    /**
     * Calculates the moving average convergence/divergence histogram.<br></br>
     * See {@link #calculateLine(int, int, List) calculateLine} and
     * {@link #calculateSignal(int, int, int, List) calculateSignal} for
     * implementation details.
     * @param shortPeriod
     * @param longPeriod
     * @param signalPeriod
     * @param prices
     * @return The Macd Line, Signal and Histogram as MacdResult.
     */
    public static MacdResult calculateResult(int shortPeriod, int longPeriod, int signalPeriod, List<Double> prices) {
        List<Double> macdLine = calculateLine(shortPeriod, longPeriod, prices);
        List<Double> signalLine = IndicatorUtils.ema(macdLine, signalPeriod);
        List<Double> histogram = new ArrayList<>();

        double signalSma = IndicatorUtils.sma(macdLine, signalPeriod).orElse(signalLine.getFirst());
        while(signalLine.size() < macdLine.size()) {
            signalLine.add(0, signalSma);
        }

        for(int i = 0; i < macdLine.size(); i++) {
            histogram.add(macdLine.get(i) - signalLine.get(i));
        }

        return new MacdResult(macdLine, signalLine, histogram);
    }

    /**
     * Generates a signal based on the last to macd and signal values.
     * @param macdLine  Macd line values
     * @param signalLine Signal line values
     * @return  A Trend
     */
    public static Trend generateSignalSimple(List<Double> macdLine, List<Double> signalLine) {
        if(macdLine.size() < 2 || signalLine.size() < 2) {
            throw new IllegalArgumentException("Macd Line and Signal must have at least 2 values");
        }

        double curMacd = macdLine.get(macdLine.size() - 1);
        double prevMacd = macdLine.get(macdLine.size() - 2);
        double curSignal = signalLine.get(signalLine.size() - 1);
        double prevSignal = signalLine.get(signalLine.size() - 1);

        if(prevMacd < prevSignal && curMacd > curSignal) {
            return Trend.BULLISH;
        } else if(prevMacd > prevSignal && curMacd < curSignal) {
            return Trend.BEARISH;
        }

        return Trend.NONE;
    }

    /**
     * Generates a signal based on the last to macd and signal values.
     * @param result The Macd Results at least 2 values for line and signal
     * @return  A Trend
     */
    public static Trend generateSignalSimple(MacdResult result) {
        return generateSignalSimple(result.line(), result.signal());
    }
}

