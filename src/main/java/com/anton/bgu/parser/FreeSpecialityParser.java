package com.anton.bgu.parser;

import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.srplib.contract.Argument;

import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.getIntList;
import static com.anton.bgu.parser.ParserUtils.mapToRanges;
import static com.anton.bgu.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class FreeSpecialityParser implements SpecialityParser {

    private static final int DATA_COLUMNS_COUNT = 36;

    @Override
    public Optional<Speciality> parse(Element element) {

        Argument.checkNotNull(element, "element must not be null!");

        Element specialityNameElement = element.selectFirst(SPECIALITY_CSS_SELECTOR);

        if (specialityNameElement == null) {
            return Optional.empty();
        }

        Speciality speciality = new Speciality();
        speciality.setElement(element);

        speciality.setName(specialityNameElement.text());

        List<Integer> values = getIntList(skipElements(specialityNameElement, 1), DATA_COLUMNS_COUNT);

        speciality.setPlanFree(values.get(0));
        speciality.setPlanContract(values.get(1));
        speciality.setPlanPay(values.get(2));

        speciality.setRequestFreeTotal(values.get(3));
        speciality.setRequestContract(values.get(4));
        speciality.setRequestNoExam(values.get(5));
        speciality.setRequestNoConcurs(values.get(6));

        speciality.setFreeRequests(mapToRanges(values.subList(7, DATA_COLUMNS_COUNT)));

        return Optional.of(speciality);
    }
}
