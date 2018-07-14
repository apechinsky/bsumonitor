package com.anton.bgu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.getIntList;
import static com.anton.bgu.parser.ParserUtils.mapToRanges;
import static com.anton.bgu.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class PayRequestListPageParser {

    /**
     * Селектор элемента (td) с именем факультета. С этих строк начинается парсинг данных факультетов.
     */
    private static final String FACULTY_CSS_SELECTOR = "td.fl";

    /**
     * Селектор элемента (td) с именем специальности. После этого элемента идут данные по плану приема и заявкам.
     */
    private static final String SPECIALITY_CSS_SELECTOR = "td.vl";

    /**
     * Индекс первой строки с данными (имя специальности + заявки) начиная со строки с именем факультата.
     */
    private static final int DATA_ROW_INDEX = 5;

    public List<Faculty> parse(Document document) {
        Elements elements = document.select(FACULTY_CSS_SELECTOR);

        return StreamSupport.stream(elements.spliterator(), false)
            .map(this::parseFaculty)
            .collect(Collectors.toList());
    }

    public Faculty parseFaculty(Element element) {
        ParserUtils.checkFacultyElement(element);

        List<Speciality> specialities = new ArrayList<>();

        for (int index = DATA_ROW_INDEX; ; index++) {
            Element specialityRow = skipElements(element.parent(), index);

            Speciality speciality = tryParseSpeciality(specialityRow);

            if (speciality == null) {
                break;
            }

            specialities.add(speciality);
        }

        return new Faculty(element.text(), specialities);
    }

    public Speciality tryParseSpeciality(Element specialityRow) {

        if (specialityRow == null) {
            return null;
        }

        Element specialityNameElement = specialityRow.selectFirst(SPECIALITY_CSS_SELECTOR);

        if (specialityNameElement == null) {
            return null;
        }

        Speciality speciality = new Speciality();
        speciality.setName(specialityNameElement.text());
        List<Integer> values = getIntList(skipElements(specialityNameElement, 1), 33);

        speciality.setPlanFree(0);
        speciality.setPlanContract(0);
        speciality.setPlanPay(values.get(0));

        speciality.setRequestPayTotal(values.get(1));
        speciality.setRequestContract(0);
        speciality.setRequestNoExam(values.get(2));
        speciality.setRequestNoConcurs(values.get(3));

        speciality.setPayRequests(mapToRanges(values.subList(4, 33)));

        return speciality;
    }

}
