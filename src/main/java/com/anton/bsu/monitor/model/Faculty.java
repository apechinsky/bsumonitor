package com.anton.bsu.monitor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Speciality> getSpecialitiesByPopularity() {
        Comparator<Speciality> comparing =
            Comparator.comparing(speciality -> (double) speciality.getRequestFree() / speciality.getPlanFree());

        return specialities.stream()
            .sorted(comparing.reversed())
            .collect(Collectors.toList());
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
            .mapToInt(Speciality::getRequestFree)
            .sum();
    }

    public int getRequestsBeyondPassFree() {
        return Math.max(0, getRequestFree() - getPlanFree());
    }

    public int getRequestPay() {
        return specialities.stream()
            .mapToInt(Speciality::getRequestPay)
            .sum();
    }

    public int getRequestsBeyondPassPay() {
        return Math.max(0, getRequestPay() - getPlanPay());
    }

    public RequestsDistribution getFreeRequestDistribution() {
        RequestsDistribution distribution = new RequestsDistribution();
        specialities.forEach(speciality -> distribution.add(speciality.getFreeRequestDistribution()));
        return distribution;
    }

    public RequestsDistribution getPayRequestDistribution() {
        RequestsDistribution distribution = new RequestsDistribution();
        specialities.forEach(speciality -> distribution.add(speciality.getPayRequestDistribution()));
        return distribution;
    }

    public int getPrivilegedRequests() {
        return specialities.stream().mapToInt(Speciality::getPrivilegedRequests).sum();
    }

    public Range getFreePass() {
        return getFreeRequestDistribution().getPassRange(getPlanFree(), getPrivilegedRequests());
    }

    public Range getPayPass() {
        return getPayRequestDistribution().getPassRange(getPlanPay(), 0);
    }

    /**
     * Возвращает количество заявок ниже указанного диапазона.
     *
     * <p>Расчет ведется по бюджетным и платным заявкам.</p>
     *
     * @param range диапазон
     */
    public int getRequestCountBelowFree(Range range) {
        return specialities.stream()
            .mapToInt(speciality -> speciality.getRequestCountBelowFree(range))
            .sum();
    }
    public int getRequestCountBelowPay(Range range) {
        return specialities.stream()
            .mapToInt(speciality -> speciality.getRequestCountBelowPay(range))
            .sum();
    }

    public int getRequestCountBelow311Free() {
        return getRequestCountBelowFree(new Range(320, 311));
    }

    public int getRequestCountBelow311Pay() {
        return getRequestCountBelowPay(new Range(320, 311));
    }

    public void validate() {
        ValidationErrors errors = new ValidationErrors();

        specialities.forEach(speciality -> speciality.validate(errors));

        if (getRequestFree() != getFreeRequestDistribution().getRequestsCount() + getPrivilegedRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Бюджет. '%s'\n " +
                    "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                    "количеством поданных заявок (%d).",
                getName(), getRequestFree(), getFreeRequestDistribution().getRequestsCount() + getPrivilegedRequests())));

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
