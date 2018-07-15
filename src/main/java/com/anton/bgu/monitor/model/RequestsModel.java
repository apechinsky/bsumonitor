package com.anton.bgu.monitor.model;

import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.srplib.contract.Assert;

/**
 * @author Q-APE
 */
public class RequestsModel {

    private Document document;

    private List<Faculty> faculties;

    public RequestsModel(Document document, List<Faculty> faculties) {
        this.document = document;
        this.faculties = faculties;
    }

    public RequestsModel(List<Faculty> faculties) {
        this(null, faculties);
    }

    public Optional<Faculty> getFacultyByName(String name) {
        return faculties.stream()
            .filter(faculty -> faculty.getName().regionMatches(true, 0, name, 0, name.length()))
            .findFirst();
    }

    public Document getDocument() {
        Assert.checkNotNull(document, "Document was not provided!");
        return document;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void validate() {
        faculties.forEach(Faculty::validate);
    }
}
