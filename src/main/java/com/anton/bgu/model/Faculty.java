package com.anton.bgu.model;

import java.util.List;
import java.util.Optional;

/**
 * @author Q-APE
 */
public class Faculty {

    private String name;

    private List<Speciality> specialities;

    public Faculty(String name, List<Speciality> specialities) {
        this.name = name;
        this.specialities = specialities;
    }

    public String getName() {
        return name;
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

    public void validate() {
        specialities.forEach(Speciality::validate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Faculty{");
        sb.append("name='").append(name).append('\'');
        sb.append(", specialities=").append(specialities);
        sb.append('}');
        return sb.toString();
    }

    public Optional<Speciality> getSpeciality(String name) {
        return specialities.stream()
            .filter(speciality -> speciality.getName().equals(name))
            .findFirst();
    }

    public boolean isSame(Faculty faculty) {
        return name.equalsIgnoreCase(faculty.getName());
    }
}
