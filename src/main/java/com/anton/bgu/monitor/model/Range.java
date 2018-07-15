package com.anton.bgu.monitor.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Q-APE
 */
public class Range implements Comparable<Range> {

    private int min;

    private int max;

    public Range(int max, int min) {
        this.min = min;
        this.max = max;
    }

    public static Range zero() {
        return new Range(0, 0);
    }

    public static List<Range> ranges(int max, int count, int groups) {
        return IntStream.range(0, groups)
            .mapToObj(val -> {
                int rangeMax = max - (val * count);
                int rangeMin = val < groups - 1 ? rangeMax - count + 1 : 0;
                return new Range(rangeMax, rangeMin);
            })
            .collect(Collectors.toList());
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range range = (Range) o;
        return min == range.min && max == range.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return String.format("[%3d - %3d]", max, min);
    }

    @Override
    public int compareTo(Range that) {
        return Integer.compare(this.max, that.max);
    }
}
