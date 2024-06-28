package org.indic.records;

import java.util.List;

public record MacdResult(List<Double> line, List<Double> signal, List<Double> histogram) {
}
