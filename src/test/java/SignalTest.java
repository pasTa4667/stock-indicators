
import org.indic.enums.Trend;
import org.indic.indicators.*;
import org.indic.records.Bands;
import org.indic.records.MacdResult;
import org.indic.records.OscillatorResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SignalTest {

    private final List<Double> mockDataOneCloses = List.of(4.23,4.23,4.25,4.17,3.98,3.92,4.32,4.12,4.11,4.07,4.07,3.99,3.93,4.03,3.99,4.2,4.2,4.24,4.22,4.13,4.15,4.26,4.21,5.25,5.27,5.29,5.23,5.2,5.08,5.06,5.23,5.72,5.46,5.32,5.27,5.28,5.25,5.07,4.99,5.15,5.04,5.14,5.34,5.42);
    private final List<Integer> mockDataOneVolume = List.of(1321400,1141700,624900,735600,1472900,1647000,2908400,1796600,1097800,1371700,1516600,860500,811500,1384900,1016000,1181500,1279000,1352900,1273900,699400,910700,1810400,1306600,5903500,3467900,1415400,1413600,1770900,1311700,989000,1418900,2839900,2733900,945500,823500,1248800,1567000,1868200,1148500,1154800,2139600,1474200,1435800,1210800);

    private final List<Double> mockDataTwoCloses = List.of(21.049999,19.780001,18.120001,18.49,18.040001,18.18,18.58,18.309999,20.450001,19.200001,18.25,18.25,17.0,18.01,17.440001,18.67,20.27,19.75,19.25,19.719999,21.23,21.299999,21.959999,22.809999,21.375,20.6,22.045,21.700001,21.99,20.120001,19.93,20.35,21.139999,20.51,20.690001,20.84,20.4,19.73,18.940001,18.799999,18.66,18.209999,17.9,18.870001);
    private final List<Integer> mockDataTwoVolume = List.of(291100,310500,464500,414000,246000,241900,176400,201700,591400,546700,448400,281200,628000,502200,521800,479700,503700,304500,278600,179200,317400,431600,410000,293900,482400,427900,316000,273500,139400,156200,151200,113400,188000,240900,193800,193700,197600,243300,250400,167000,298200,390300,268100,626800);
    private final List<Double> mockDataTwoHigh = List.of(21.9,20.879999,19.780001,19.41,18.65,18.24,18.764999,18.655001,20.889999,20.18,19.120001,18.6,18.379999,18.129999,18.4,19.200001,20.719999,20.66,19.76,20.0,21.48,21.82,22.200001,23.16,23.955,21.58,22.51,22.24,22.1,22.289,20.74,20.92,21.41,21.34,20.84,21.790001,21.219999,21.281,19.924999,19.35,19.27,18.5,18.68,18.938999);
    private final List<Double> mockDataTwoLow = List.of(20.754999,19.57,17.809999,18.25,17.900999,17.860001,17.959999,17.9,18.120001,19.049999,17.83,17.969999,16.700001,16.549999,17.309999,17.5,18.674999,19.620001,18.709999,18.865,19.809999,20.879999,20.49,21.459999,21.07,20.450001,20.370001,21.41,21.620001,20.02,19.799999,19.75,20.379999,20.25,20.26,20.440001,19.68,19.66,18.570999,18.41,18.08,17.91,17.65,17.790001);

    @Test
    void obvUpSignalTest() {
        List<Integer> obv = OnBalanceVolume.calculate(mockDataOneCloses, mockDataOneVolume);
        Trend trend = OnBalanceVolume.generateTrendSimple(obv);
        Trend trendWithEma = OnBalanceVolume.generateTrendWithEma(obv, 12, 24);
        assertEquals(Trend.NONE, trendWithEma);
        assertEquals(Trend.BULLISH, trend);
    }

    @Test
    void obvDownSignalTest() {
        List<Integer> obv = OnBalanceVolume.calculate(mockDataTwoCloses, mockDataTwoVolume);
        Trend trend = OnBalanceVolume.generateTrendSimple(obv);
        Trend trendWithEma = OnBalanceVolume.generateTrendWithEma(obv, 12, 24);
        assertEquals(Trend.NONE, trendWithEma);
        assertEquals(Trend.NONE, trend);
    }

    @Test
    void rsiSignalTest() {
        int period = 14;
        List<Double> rsi = RelativeStrengthIndex.calculate(period, mockDataTwoCloses);
        System.out.println(rsi);
        Trend trend = RelativeStrengthIndex.generateSignal(rsi, 1);
        assertEquals(Trend.NONE, trend);
    }

    @Test
    void bbSignalTest() {
        int period = 14;
        Bands bands = BollingerBands.calculateWithSma(period, mockDataOneCloses, 2);
        Trend trend = BollingerBands.generateSignalSimple(bands, mockDataOneCloses);
        assertEquals(Trend.NONE, trend);
    }

    @Test
    void atrSignalTest() {
        int period = 14;
        List<Double> atr = AverageTrueRange.calculateAtr(period, mockDataTwoCloses, mockDataTwoHigh, mockDataTwoLow);
        Trend trend = AverageTrueRange.generateBreakoutSignal(atr, 2.0, mockDataOneCloses.getLast(), mockDataTwoHigh.getLast(), mockDataTwoLow.getLast());
        assertEquals(Trend.BEARISH, trend);
    }

    @Test
    void soSignalTest() {
        int period = 14;
        int periodD = 3;
        OscillatorResult res = StochasticOscillator.calculate(period, periodD, mockDataTwoCloses, mockDataTwoHigh, mockDataTwoLow);
        Trend trend = StochasticOscillator.generateSignalSimple(res);
        assertEquals(Trend.BULLISH, trend);
    }

    @Test
    void macdSignalTest() {
        int shortPeriod = 12;
        int longPeriod = 24;
        int signalPeriod = 8;
        MacdResult res = Macd.calculateResult(shortPeriod, longPeriod, signalPeriod, mockDataOneCloses);
        Trend trend = Macd.generateSignalSimple(res);
        assertEquals(Trend.NONE, trend);
    }



}
