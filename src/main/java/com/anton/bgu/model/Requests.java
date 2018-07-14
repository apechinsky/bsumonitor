package com.anton.bgu.model;

import java.util.List;
import java.util.Optional;

/**
 * @author Q-APE
 */
public class Requests {

    private List<Faculty> faculties;

    public Requests(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public Optional<Faculty> getFacultyByName(String name) {
        return faculties.stream()
            .filter(faculty -> faculty.getName().regionMatches(true, 0, name, 0, name.length()))
            .findFirst();
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void validate() {
        faculties.forEach(Faculty::validate);
    }
}
