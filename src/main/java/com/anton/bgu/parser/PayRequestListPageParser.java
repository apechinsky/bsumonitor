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

/**
 * @author Q-APE
 */
public class PayRequestListPageParser {

    public List<Faculty> parse(Document document) {
        Elements elements = document.select("td.fl");

        return StreamSupport.stream(elements.spliterator(), false)
            .map(this::parseFaculty)
            .collect(Collectors.toList());
    }

    public Faculty parseFaculty(Element facultyElement) {

        List<Speciality> specialities = new ArrayList<>();

        for (int index = 5; ; index++) {
            Element specialityRow = ParserUtils.skipElements(facultyElement.parent(), index);

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
        List<Integer> values = ParserUtils.getIntList(ParserUtils.skipElements(specialityNameElement, 1), 33);

        speciality.setPlanFree(0);
        speciality.setPlanContract(0);
        speciality.setPlanPay(values.get(0));

        speciality.setRequestPayTotal(values.get(1));
        speciality.setRequestContract(0);
        speciality.setRequestNoExam(values.get(2));
        speciality.setRequestNoConcurs(values.get(3));

        List<Integer> rangesValues = values.subList(4, 33);

        Map<Range, Integer> requests = IntStream.range(0, 29).boxed()
            .collect(Collectors.toMap(ParserUtils.RANGES::get, rangesValues::get));
        Map<Range, Integer> sortedRequests = new TreeMap<>(Comparator.reverseOrder());
        sortedRequests.putAll(requests);

        speciality.setPayRequests(sortedRequests);

        return speciality;
    }

}
