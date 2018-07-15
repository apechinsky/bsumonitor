package com.anton.bsu.monitor.parser;

import org.jsoup.nodes.Element;

import static com.anton.bsu.monitor.parser.ParserUtils.skipElements;

/**
 * @author Q-APE
 */
public class FreeSpecialityElement {

    private static final int DATA_COLUMNS_COUNT = 36;

    private Element element;

    public FreeSpecialityElement(Element element) {
        this.element = element;
    }

    public Element getName() {
        return element;
    }

    public Element getPlanFree() {
        return skipElements(element, 1);
    }

    public Element getPlanContract() {
        return skipElements(element, 2);
    }

    public Element getPlanPay() {
        return skipElements(element, 3);
    }

    public Element getRequestFreeTotal() {
        return skipElements(element, 4);
    }

    public Element getRequestContract() {
        return skipElements(element, 5);
    }

    public Element getRequestNoExam() {
        return skipElements(element, 6);
    }

    public Element getRequestNoConcurs() {
        return skipElements(element, 7);
    }

    public Element getRanged(int index) {
        return skipElements(element, 8 + index);
    }
}
