package com.anton.bgu.monitor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.srplib.contract.Argument;
import org.srplib.contract.Assert;

import static com.anton.bgu.monitor.parser.ParserUtils.RANGES;


/**
 * @author Q-APE
 */
public class RequestsDistribution {

    private Map<Range, Integer> requests = new TreeMap<>(Comparator.reverseOrder());

    /**
     * Распределеяет список баллов по диапазонам Range.
     *
     * <p>Нулевой элемент должен соответствовать диапазону [400, 391], следующий [390, 381] и т.п.</p>
     */
    public static RequestsDistribution fromRequestList(List<Integer> rangedPoints) {

        Argument.checkTrue(rangedPoints.size() == RANGES.size(),
            "RangedPoints size (%d) doesn't match RANGES size (%d).", rangedPoints.size(), RANGES.size());

        RequestsDistribution distribution = new RequestsDistribution();

        for (int index = 0; index < rangedPoints.size(); index++) {
            distribution.set(RANGES.get(index), rangedPoints.get(index));
        }

        return distribution;
    }

    public Iterable<? extends Map.Entry<Range, Integer>> entrySet() {
        return requests.entrySet();
    }


    public void set(Range range, int value) {
        Assert.checkFalse(requests.containsKey(range), "Can't set %s to %d. Range already has value %d.",
            range, value, requests.get(range));

        requests.put(range, value);
    }

    public void add(Range range, int value) {
        if (requests.containsKey(range)) {
            requests.put(range, requests.get(range) + value);
        }
        else {
            requests.put(range, value);
        }
    }

    public void add(RequestsDistribution distribution) {
        for (Map.Entry<Range, Integer> entry : distribution.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public int getRequestsCount() {
        return requests.values().stream()
            .mapToInt(i -> i)
            .sum();
    }

    @Override
    public String toString() {
        return requests.toString();
    }

    public Range getPassRange(int planCount, int privilegedCount) {

        int sum = privilegedCount;

        for (Map.Entry<Range, Integer> entry : requests.entrySet()) {
            sum += entry.getValue();
            if (sum >= planCount) {
                return entry.getKey();
            }
        }

        return Range.zero();

    }
}
