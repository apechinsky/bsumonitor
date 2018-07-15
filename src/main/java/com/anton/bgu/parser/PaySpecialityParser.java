package com.anton.bgu.parser;

import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.srplib.contract.Argument;

import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.getIntList;
import static com.anton.bgu.model.RequestsDistribution.fromRequestList;
import static com.anton.bgu.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class PaySpecialityParser implements SpecialityParser {

    private static final int DATA_COLUMN_COUNT = 33;

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

        List<Integer> values = getIntList(skipElements(specialityNameElement, 1), DATA_COLUMN_COUNT);

        speciality.setPlanFree(0);
        speciality.setPlanContract(0);
        speciality.setPlanPay(values.get(0));

        speciality.setRequestPayTotal(values.get(1));
        speciality.setRequestContract(0);
        speciality.setRequestNoExam(values.get(2));
        speciality.setRequestNoConcurs(values.get(3));

        speciality.setPayRequests(fromRequestList(values.subList(4, DATA_COLUMN_COUNT)));

        return Optional.of(speciality);
    }
}
