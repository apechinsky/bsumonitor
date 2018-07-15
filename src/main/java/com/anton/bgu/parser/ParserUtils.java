package com.anton.bgu.parser;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;
import org.srplib.contract.Argument;

import com.anton.bgu.model.Faculty;
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

    public static void checkFacultyElement(Element element) {
        Argument.checkTrue(element.tagName().equals("td"), "Faculty element should be 'td', Got: '%s'", element.tagName());
        Argument.checkTrue(element.attr("class").equals("fl"), "Faculty should have attribute 'class=fl'");
    }

    /**
     * Создает map с элементами [range, value] на основе списка значений (value)
     */
    public static Map<Range, Integer> mapToRanges(List<Integer> rangedValues) {

        Map<Range, Integer> requests = IntStream.range(0, rangedValues.size()).boxed()
            .collect(Collectors.toMap(RANGES::get, rangedValues::get));

        Map<Range, Integer> sortedRequests = new TreeMap<>(Comparator.reverseOrder());
        sortedRequests.putAll(requests);

        return sortedRequests;
    }

    public static Optional<Faculty> findFaculty(List<Faculty> faculties, Faculty faculty) {
        return faculties.stream()
            .filter(f -> f.equals(faculty))
            .findFirst();
    }

    public static int asInt(Element element) {
        String text = element.text().trim();
        return text.isEmpty() ? 0 : Integer.valueOf(text);
    }
}
