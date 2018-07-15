package com.anton.bgu.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.srplib.contract.Assert;
import org.srplib.validation.DefaultValidationError;
import org.srplib.validation.ValidationErrors;

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

    public int getPlanFree() {
        return specialities.stream()
            .mapToInt(Speciality::getPlanFree)
            .sum();
    }

    public int getPlanPay() {
        return specialities.stream()
            .mapToInt(Speciality::getPlanPay)
            .sum();
    }

    public int getRequestFree() {
        return specialities.stream()
            .mapToInt(Speciality::getRequestFreeTotal)
            .sum();
    }

    public int getRequestPay() {
        return specialities.stream()
            .mapToInt(Speciality::getRequestPayTotal)
            .sum();
    }

    public RequestsDistribution getFreeRequests() {
        RequestsDistribution distribution = new RequestsDistribution();
        specialities.forEach(speciality -> distribution.add(speciality.getFreeRequests()));
        return distribution;
    }

    public RequestsDistribution getPayRequests() {
        RequestsDistribution distribution = new RequestsDistribution();
        specialities.forEach(speciality -> distribution.add(speciality.getPayRequests()));
        return distribution;
    }

    public int getPrivilegedRequests() {
        return specialities.stream().mapToInt(Speciality::getPrivilegedRequests).sum();
    }

    public Range getFreePass() {
        return getFreeRequests().getPassRange(getPlanFree(), getPrivilegedRequests());
    }

    public Range getPayPass() {
        return getFreeRequests().getPassRange(getPlanPay(), 0);
    }

    public void validate() {
        ValidationErrors errors = new ValidationErrors();

        specialities.forEach(speciality -> speciality.validate(errors));

        if (getRequestFree() != getFreeRequests().getRequestsCount() + getPrivilegedRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Бюджет. '%s'\n " +
                    "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                    "количеством поданных заявок (%d).",
                getName(), getRequestFree(), getFreeRequests().getRequestsCount() + getPrivilegedRequests())));

        }

        if (errors.hasErrors()) {
            System.out.println(errors.toString("\n"));
//            throw new ValidationException(errors.toString("\n"), null, errors);
        }
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
