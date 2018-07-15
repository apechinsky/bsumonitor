package com.anton.bsu.monitor.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.anton.bsu.monitor.model.Faculty;
import com.anton.bsu.monitor.model.RequestsModel;
import com.anton.bsu.monitor.model.Speciality;
import static com.anton.bsu.monitor.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class RequestListPageParser {

    /**
     * Селектор элемента (td) с именем факультета. С этих строк начинается парсинг данных факультетов.
     */
    private static final String FACULTY_CSS_SELECTOR = "td.fl";

    /**
     * Индекс первой строки с данными (имя специальности + заявки) начиная со строки с именем факультата.
     */
    private static final int DATA_ROW_INDEX = 5;

    public RequestsModel parse(Document free, Document pay) {
        List<Faculty> freeFaculties = parseDocument(free, new FreeSpecialityParser());
        List<Faculty> payFaculties = parseDocument(pay, new PaySpecialityParser());

        List<Faculty> faculties = merge(freeFaculties, payFaculties);

        return new RequestsModel(free, faculties);
    }

    public List<Faculty> parseDocument(Document document, SpecialityParser specialityParser) {
        Elements elements = document.select(FACULTY_CSS_SELECTOR);

        return StreamSupport.stream(elements.spliterator(), false)
            .map(element -> parseFaculty(element, specialityParser))
            .collect(Collectors.toList());
    }

    public Faculty parseFaculty(Element element, SpecialityParser specialityParser) {
        ParserUtils.checkFacultyElement(element);

        List<Speciality> specialities = new ArrayList<>();

        for (int index = DATA_ROW_INDEX; ; index++) {
            Element specialityRow = skipElements(element.parent(), index);

            if (specialityRow == null) {
                break;
            }

            Optional<Speciality> speciality = specialityParser.parse(specialityRow);

            if (!speciality.isPresent()) {
                break;
            }

            specialities.add(speciality.get());
        }

        return new Faculty(element.text(), specialities, element);
    }


    private List<Faculty> merge(List<Faculty> freeFaculties, List<Faculty> payFaculties) {
        ArrayList<Faculty> result = new ArrayList<>();

        for (Faculty freeFaculty : freeFaculties) {
            Optional<Faculty> payFaculty = ParserUtils.findFaculty(payFaculties, freeFaculty);

            if (payFaculty.isPresent()) {
                result.add(merge(freeFaculty, payFaculty.get()));
            }
            else {
                ParserUtils.log("Не найден факультет '%s' в платном плане.", freeFaculty.getName());
            }

        }

        return result;
    }

    private Faculty merge(Faculty freeFaculty, Faculty payFaculty) {

        for (Speciality freeSpeciality : freeFaculty.getSpecialities()) {

            Optional<Speciality> paySpeciality = payFaculty.getSpeciality(freeSpeciality.getName());

            if (paySpeciality.isPresent()) {
                freeSpeciality.setPayRequestDistribution(paySpeciality.get().getPayRequestDistribution());
                freeSpeciality.setRequestPay(paySpeciality.get().getRequestPay());
            }
            else {
                ParserUtils.log("%s. Не найдена специальность '%s' в платном плане.", freeFaculty.getName(), freeSpeciality.getName());
            }
        }

        return freeFaculty;
    }

}
