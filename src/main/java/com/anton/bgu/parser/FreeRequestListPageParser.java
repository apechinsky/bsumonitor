package com.anton.bgu.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.Range;
import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.RANGES;
import static com.anton.bgu.parser.ParserUtils.getIntList;
import static com.anton.bgu.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class FreeRequestListPageParser {

    public List<Faculty> parse(Document document) {
        Elements elements = document.select("td.fl");

        return StreamSupport.stream(elements.spliterator(), false)
            .map(this::parseFaculty)
            .collect(Collectors.toList());
    }

    public Faculty parseFaculty(Element facultyElement) {
//        Element rangesElement = skipElements(facultyElement.parent(), 4);

        List<Speciality> specialities = new ArrayList<>();

        for (int index = 5; ; index++) {
            Element specialityRow = skipElements(facultyElement.parent(), index);

            Speciality speciality = tryParseSpeciality(specialityRow);

            if (speciality == null) {
                break;
            }

            specialities.add(speciality);
        }

        return new Faculty(facultyElement.text(), specialities);
    }

    public Speciality tryParseSpeciality(Element specialityRow) {

        if (specialityRow == null) {
            return null;
        }

        Element specialityNameElement = specialityRow.selectFirst("td.vl");

        if (specialityNameElement == null) {
            return null;
        }

        Speciality speciality = new Speciality();
        speciality.setName(specialityNameElement.text());
        List<Integer> values = getIntList(skipElements(specialityNameElement, 1), 36);

        speciality.setPlanFree(values.get(0));
        speciality.setPlanContract(values.get(1));
        speciality.setPlanPay(values.get(2));

        speciality.setRequestFreeTotal(values.get(3));
        speciality.setRequestContract(values.get(4));
        speciality.setRequestNoExam(values.get(5));
        speciality.setRequestNoConcurs(values.get(6));

        List<Integer> rangesValues = values.subList(7, 36);

        Map<Range, Integer> requests = IntStream.range(0, 29).boxed()
            .collect(Collectors.toMap(RANGES::get, rangesValues::get));
        Map<Range, Integer> sortedRequests = new TreeMap<>(Comparator.reverseOrder());
        sortedRequests.putAll(requests);

        speciality.setFreeRequests(sortedRequests);

        return speciality;
    }

}
