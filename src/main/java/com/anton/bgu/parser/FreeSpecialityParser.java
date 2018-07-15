package com.anton.bgu.parser;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.srplib.contract.Argument;

import com.anton.bgu.model.Range;
import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.RANGES;
import static com.anton.bgu.parser.ParserUtils.asInt;

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

        FreeSpecialityElement specialityElement = new FreeSpecialityElement(specialityNameElement);

        Speciality speciality = new Speciality();
        speciality.setElement(specialityElement.getName());

        speciality.setName(specialityElement.getName().text());

        speciality.setPlanFree(asInt(specialityElement.getPlanFree()));
        speciality.setPlanContract(asInt(specialityElement.getPlanContract()));
        speciality.setPlanPay(asInt(specialityElement.getPlanPay()));

        speciality.setRequestFreeTotal(asInt(specialityElement.getRequestFreeTotal()));
        speciality.setRequestContract(asInt(specialityElement.getRequestContract()));
        speciality.setRequestNoExam(asInt(specialityElement.getRequestNoExam()));
        speciality.setRequestNoConcurs(asInt(specialityElement.getRequestNoConcurs()));

        Map<Range, Integer> requests = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 0; i < RANGES.size(); i++) {
            requests.put(RANGES.get(i), asInt(specialityElement.getRanged(i)));
        }
        speciality.setFreeRequests(requests);

        return Optional.of(speciality);
    }

}
