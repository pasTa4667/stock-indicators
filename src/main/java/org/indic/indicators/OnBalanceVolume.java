package org.indic.indicators;

import java.util.ArrayList;
import java.util.List;

public class OnBalanceVolume {


    /**
     * Calculates the On-Balance-Volume for the given closes and volume, returns period values.
     * closes.get(i) should match volumes.get(i)
     * @param closes - Closes with oldest (index 0)
     * @param volumes - Volumes with oldest (index 0)
     * @return List with obv values
     */
    public static List<Double> calculate(int period, List<Double> closes, List<Integer> volumes) {
        if(closes.size() <= period) return new ArrayList<>();

        List<Double> obv = new ArrayList<>();
        obv.add(0.0);

        for(int i = closes.size() - period; i < closes.size(); i++) {
            double diff = closes.get(i) - (i <= 0 ? 0.0 : closes.get(i - 1));
            if(diff > 0) {
                obv.add(obv.get(obv.size() - 1) + volumes.get(i));
            } else if(diff < 0) {
                obv.add(obv.get(obv.size() - 1) - volumes.get(i));
            } else {
                obv.add(obv.get(obv.size() - 1));
            }
        }

        return obv;
    }

    /**
     * Calculates the On-Balance-Volume for the given closes and volumes.
     * closes.get(i) should match volumes.get(i)
     * @param closes - Closes with oldest (index 0)
     * @param volumes - Volumes with oldest (index 0)
     * @return List with obv values
     */
    public static List<Integer> calculate(List<Double> closes, List<Integer> volumes) {
        List<Integer> obv = new ArrayList<>();
        obv.add(volumes.get(0));

        for(int i = 1; i < closes.size(); i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            if(diff > 0) {
                obv.add(obv.get(obv.size() - 1) + volumes.get(i));
            } else if(diff < 0) {
                obv.add(obv.get(obv.size() - 1) - volumes.get(i));
            } else {
                obv.add(obv.get(obv.size() - 1));
            }
        }

        return obv;
    }

}
