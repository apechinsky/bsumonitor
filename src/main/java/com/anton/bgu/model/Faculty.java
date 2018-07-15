package com.anton.bgu.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.srplib.contract.Assert;

/**
 * Факультет.
 *
 * @author Q-APE
 */
public class Faculty {

    private String name;

    private Element element;

    private List<Speciality> specialities;

    public Faculty(String name, List<Speciality> specialities, Element element) {
        this.name = name;
        this.specialities = specialities;
        this.element = element;
    }

    public Faculty(String name, List<Speciality> specialities) {
        this(name, specialities, null);
    }

    public String getName() {
        return name;
    }

    public Element getElement() {
        Assert.checkNotNull(element, "element was not provided!");
        return element;
    }

    public List<Speciality> getSpecialities() {
        return specialities;
    }

    public int getPlanTotal() {
        return specialities.stream()
            .mapToInt(Speciality::getPlanFree)
            .sum();
    }

    public int getRequestTotal() {
        return specialities.stream()
            .mapToInt(Speciality::getRequestFreeTotal)
            .sum();
    }

    public Map<Range, Integer> getFreeRequests() {
//        specialities.forEach(speciality -> {
//            speciality
//        });
//        return freeRequests;
        return null;
    }

    public Range getFreePass() {
        return Range.zero();
    }

    public Range getPayPass() {
        return Range.zero();
    }

    public void validate() {
        specialities.forEach(Speciality::validate);
    }

    public Optional<Speciality> getSpeciality(String name) {
        return specialities.stream()
            .filter(speciality -> speciality.getName().equals(name))
            .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Faculty faculty = (Faculty) o;
        return Objects.equals(name, faculty.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Faculty{");
        sb.append("name='").append(name).append('\'');
        sb.append(", specialities=").append(specialities);
        sb.append('}');
        return sb.toString();
    }

}
