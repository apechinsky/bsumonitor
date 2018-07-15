package com.anton.bgu.monitor.view;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.anton.bgu.monitor.model.Faculty;
import com.anton.bgu.monitor.model.Range;
import com.anton.bgu.monitor.model.RequestsModel;
import com.anton.bgu.monitor.model.Speciality;
import com.anton.bgu.monitor.parser.FreeSpecialityElement;

/**
 * @author Q-APE
 */
public class ModifySourceHtmlView implements ModelView {

    @Override
    public String render(RequestsModel model) {

        for (Faculty faculty : model.getFaculties()) {
            processFaculty(faculty);
        }

        Document document = model.getDocument();
        setEncoding(document, "utf-8");

        return model.getDocument().outerHtml();
    }

    private void processFaculty(Faculty faculty) {
        facultyHighlightTitleIfRequestsExceedsPlan(faculty);

        facultyAddPassPointsToTitle(faculty);

        for (Speciality speciality : faculty.getSpecialities()) {
            processSpeciality(speciality);
        }
    }

    private void facultyAddPassPointsToTitle(Faculty faculty) {
        faculty.getElement().text(String.format("%s, Проходный баллы: %s / %s",
            faculty.getElement().text(), faculty.getFreePass(), faculty.getPayPass()));
    }

    private void facultyHighlightTitleIfRequestsExceedsPlan(Faculty faculty) {
        if (faculty.getRequestFree() > faculty.getPlanFree()) {
            faculty.getElement().attributes().put("style", "color: red; font-size: 2em");
        }
    }

    private void processSpeciality(Speciality speciality) {
        FreeSpecialityElement element = new FreeSpecialityElement(speciality.getElement());

        element.getName().text(String.format("%s, Проходные: %s / %s",
            element.getName().text(), speciality.getFreePass(), speciality.getPayPass()));

        if (speciality.getRequestsTotal() >= speciality.getPlanTotal()) {
            setColor(element.getName(), "red");
        }

        if (speciality.getRequestFree() >= speciality.getPlanFree()) {
            setColor(element.getPlanFree(), "red");
        }

        if (speciality.getRequestPay() > speciality.getPlanPay()) {
            setColor(element.getPlanPay(), "red");
        }

        Element payTotal = new Element("span")
            .text(String.valueOf(speciality.getRequestPay()))
            .attr("style", "color: gray; font-size: 0.8em");
        element.getPlanPay().appendChild(payTotal);

        int index = 0;
        for (Map.Entry<Range, Integer> entry : speciality.getPayRequestDistribution().entrySet()) {
            if (entry.getValue() > 0) {
                Element ranged = element.getRanged(index);

                Element free = new Element("span")
                    .text(entry.getValue().toString())
                    .attr("style", "color: gray; font-size: 0.8em");

                ranged.appendChild(free);
            }
            index++;
        }


    }

    private void setColor(Element element, String color) {
        element.attributes().put("style", String.format("color: %s", color));
    }

    private void setEncoding(Document document, String encoding) {
        Element meta = document.selectFirst("meta");
        meta.attributes().put("content", String.format("text/html; charset=%s", encoding));
    }
}
