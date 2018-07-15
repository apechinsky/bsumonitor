package com.anton.bgu.view;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.Range;
import com.anton.bgu.model.RequestsModel;
import com.anton.bgu.model.Speciality;
import com.anton.bgu.parser.FreeSpecialityElement;

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
        Element element = faculty.getElement();

        if (faculty.getRequestTotal() > faculty.getPlanTotal()) {
            element.attributes().put("style", "color: red; font-size: 2em");
        }

//        element.text(String.format("%s - %s %s", element.text(), faculty.getFreePass(), faculty.getPayPass()));


        for (Speciality speciality : faculty.getSpecialities()) {
            processSpeciality(speciality);
        }
    }

    private void processSpeciality(Speciality speciality) {
        FreeSpecialityElement element = new FreeSpecialityElement(speciality.getElement());

        if (speciality.getRequestsTotal() >= speciality.getPlanTotal()) {
            setColor(element.getName(), "red");
        }

        if (speciality.getRequestFreeTotal() >= speciality.getPlanFree()) {
            setColor(element.getPlanFree(), "red");
        }

        if (speciality.getRequestPayTotal() > speciality.getPlanPay()) {
            setColor(element.getPlanPay(), "red");
        }

        Element payTotal = new Element("span")
            .text(String.valueOf(speciality.getRequestPayTotal()))
            .attr("style", "color: gray; font-size: 0.8em");
        element.getPlanPay().appendChild(payTotal);

        int index = 0;
        for (Map.Entry<Range, Integer> entry : speciality.getPayRequests().entrySet()) {
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
