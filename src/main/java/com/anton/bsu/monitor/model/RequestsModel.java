package com.anton.bsu.monitor.model;

import java.time.LocalDateTime;
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

    private LocalDateTime updateFree;

    private LocalDateTime updatePay;

    public RequestsModel(LocalDateTime updateFree, LocalDateTime updatePay, Document document,
        List<Faculty> faculties) {

        this.updateFree = updateFree;
        this.updatePay = updatePay;
        this.document = document;
        this.faculties = faculties;
    }

    public LocalDateTime getUpdateFree() {
        return updateFree;
    }

    public LocalDateTime getUpdatePay() {
        return updatePay;
    }

    public Optional<Faculty> getFacultyByName(String name) {
        return faculties.stream()
            .filter(faculty -> faculty.getName().regionMatches(true, 0, name, 0, name.length()))
            .findFirst();
    }

    public Faculty getFaculty(FacultyName name) {
        return getFacultyByName(name.getName())
            .orElseThrow(() -> Assert.failure("Faculty '%s' not found", name));
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
