## Stock Indicator Library for Java

Currently, offers implementations for:
+ Average True Range
+ Bollinger Bands
+ Moving Average Convergence/Divergence
+ On Balance Volume
+ Relative Strength Index
+ Stochastic Oscillator
+ Simple Moving Average 
+ Exponential Moving Average

The prices/closes should be passed in ascending order meaning, the first value (index 0) should be
the earliest price and the last value should be the most recent price.

For most Indicators with a period, we calculate the initial value from
0 to period and start calculating the Indicator from period onward, as it is
usually done.

**Examples**

```Java
//Exponential-Moving-Average
List<Double> closes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
int period = 3;
List<Double> ema = IndicatorUtils.ema(closes, period);

//On-Balance-Volume
List<Integer> volumes = List.of(8200, 8100, 8300, 8900, 9200, 13300, 10300, 9900, 10100, 11300, 12600, 10700, 11500);
List<Double> closes = List.of(53.30, 53.32, 53.72, 54.19, 53.92, 54.65, 54.60, 54.21, 54.53, 53.79, 53.66, 53.56, 53.57);
List<Integer> obv = OnBalanceVolume.calculate(closes, volumes);

//Moving-Average-Convergence/Divergence
List<Double> closes = List.of(53.30, 53.32, 53.72, 54.19, 53.92, 54.65, 54.60, 54.21, 54.53, 53.79, 53.66, 53.56, 53.57, ...);
int shortPeriod = 12;
int longPeriod = 26;
int signalPeriod = 9;       
MacdResult res = Macd.calculateResult(shortPeriod, longPeriod, signalPeriod, closes);
List<Double> histogram = res.histogram();
List<Double> line = res.line();
List<Double> signal = res.signal();
```

**Disclaimer**

There exist different implementation of these indicators and values may differ from other 
calculators. For example, some may use the first price/close as initial value of the ema 
others will use the sma to calculate the ema. Therefore, indicators like Macd using the ema
may have slightly different values depending on your interpretation. Please read the comments
of the methods, where the implementation is described.