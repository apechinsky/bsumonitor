package com.anton.bgu.parser;

import java.util.Optional;

import org.jsoup.nodes.Element;

import com.anton.bgu.model.Speciality;

/**
 * Парсер специальности
 *
 * @author Q-APE
 */
public interface SpecialityParser {

    /**
     * Селектор элемента (td) с именем специальности. После этого элемента идут данные по плану приема и заявкам.
     */
    String SPECIALITY_CSS_SELECTOR = "td.vl";



    Optional<Speciality> parse(Element element);

}
