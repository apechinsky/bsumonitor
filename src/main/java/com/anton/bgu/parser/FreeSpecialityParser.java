package com.anton.bgu.parser;

import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.srplib.contract.Argument;

import com.anton.bgu.model.RequestsDistribution;
import com.anton.bgu.model.Speciality;
import static com.anton.bgu.parser.ParserUtils.RANGES;
import static com.anton.bgu.parser.ParserUtils.asInt;
import static com.anton.bgu.parser.ParserUtils.getIntList;
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

        FreeSpecialityElement specialityElement = new FreeSpecialityElement(specialityNameElement);

        Speciality speciality = new Speciality();
        speciality.setElement(specialityElement.getName());

        speciality.setName(specialityElement.getName().text());

        speciality.setPlanFree(asInt(specialityElement.getPlanFree()));
        speciality.setPlanContract(asInt(specialityElement.getPlanContract()));
        speciality.setPlanPay(asInt(specialityElement.getPlanPay()));

        speciality.setRequestFree(asInt(specialityElement.getRequestFreeTotal()));
        speciality.setRequestContract(asInt(specialityElement.getRequestContract()));
        speciality.setRequestNoExam(asInt(specialityElement.getRequestNoExam()));
        speciality.setRequestNoConcurs(asInt(specialityElement.getRequestNoConcurs()));

        // элемент соответствует диапазону баллов [400, 391]
        Element firstRangedValueElement = skipElements(specialityElement.getRequestNoConcurs(), 1);
        List<Integer> rangedRequests = getIntList(firstRangedValueElement, RANGES.size());

        speciality.setFreeRequestDistribution(RequestsDistribution.fromRequestList(rangedRequests));

        return Optional.of(speciality);
    }

}
