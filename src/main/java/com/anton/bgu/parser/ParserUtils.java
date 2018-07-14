package com.anton.bgu.parser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;

import com.anton.bgu.model.Range;

/**
 * @author Q-APE
 */
public class ParserUtils {

    static final List<Range> RANGES = Range.ranges(400, 10, 29);

    public static List<Integer> getIntList(Element startElement, int count) {
        return Stream.iterate(startElement, Element::nextElementSibling)
            .limit(count)
            .map(element -> element.text().trim().isEmpty() ? "0" : element.text().trim())
            .map(Integer::valueOf)
            .collect(Collectors.toList());
    }

    public static Element skipElements(Element element, int count) {
        return count == 0 ? element : skipElements(element.nextElementSibling(), count - 1);
    }

    public static void log(Object pattern, Object... arguments) {
        System.out.printf(pattern + "%n", arguments);
    }
}
