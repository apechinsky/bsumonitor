package com.anton.bgu.model;

import java.util.Map;

import org.jsoup.nodes.Element;
import org.srplib.validation.DefaultValidationError;
import org.srplib.validation.ValidationErrors;

/**
 * Специальность.
 *
 * @author Q-APE
 */
public class Speciality {

    private String name;

    private Element element;

    /**
     * План приема на бюджет
     */
    private int planFree;

    /**
     * Целевики-контрактники. Вроде как входит в {@link #planFree}
     */
    private int planContract;

    /**
     * План приема на платное
     */
    private int planPay;


    private int requestPayTotal;


    private int requestFreeTotal;

    private int requestContract;

    private int requestNoExam;

    private int requestNoConcurs;

    private RequestsDistribution freeRequests = new RequestsDistribution();

    private RequestsDistribution payRequests = new RequestsDistribution();

    public String getName() {
        return name;
    }

    public Speciality setName(String name) {
        this.name = name;
        return this;
    }

    public Element getElement() {
        return element;
    }

    public Speciality setElement(Element element) {
        this.element = element;
        return this;
    }

    public int getPlanFree() {
        return planFree;
    }

    public Speciality setPlanFree(int planFree) {
        this.planFree = planFree;
        return this;
    }

    public int getPlanContract() {
        return planContract;
    }

    public Speciality setPlanContract(int planContract) {
        this.planContract = planContract;
        return this;
    }

    public int getPlanPay() {
        return planPay;
    }

    public Speciality setPlanPay(int planPay) {
        this.planPay = planPay;
        return this;
    }

    public int getPlanTotal() {
        return getPlanFree() + getPlanPay();
    }

    public int getRequestFreeTotal() {
        return requestFreeTotal;
    }

    public Speciality setRequestFreeTotal(int requestFreeTotal) {
        this.requestFreeTotal = requestFreeTotal;
        return this;
    }

    public int getRequestPayTotal() {
        return requestPayTotal;
    }

    public void setRequestPayTotal(int requestPayTotal) {
        this.requestPayTotal = requestPayTotal;
    }

    public int getRequestsTotal() {
        return getRequestFreeTotal() + getRequestPayTotal();
    }

    public int getRequestContract() {
        return requestContract;
    }

    public Speciality setRequestContract(int requestContract) {
        this.requestContract = requestContract;
        return this;
    }

    public int getRequestNoExam() {
        return requestNoExam;
    }

    public Speciality setRequestNoExam(int requestNoExam) {
        this.requestNoExam = requestNoExam;
        return this;
    }

    public int getRequestNoConcurs() {
        return requestNoConcurs;
    }

    public Speciality setRequestNoConcurs(int requestNoConcurs) {
        this.requestNoConcurs = requestNoConcurs;
        return this;
    }

    public RequestsDistribution getFreeRequests() {
        return freeRequests;
    }

    public Speciality setFreeRequests(RequestsDistribution freeRequests) {
        this.freeRequests = freeRequests;
        return this;
    }

    public RequestsDistribution getPayRequests() {
        return payRequests;
    }

    public void setPayRequests(RequestsDistribution payRequests) {
        this.payRequests = payRequests;
    }

    public Range getFreePass() {
        Integer sum = getPrivilegedRequests();

        for (Map.Entry<Range, Integer> entry : freeRequests.entrySet()) {
            sum += entry.getValue();
            if (sum >= planFree) {
                return entry.getKey();
            }
        }
        return Range.zero();
    }

    public Range getPayPass() {
        Integer sum = 0;

        for (Map.Entry<Range, Integer> entry : payRequests.entrySet()) {
            sum += entry.getValue();
            if (sum >= planPay) {
                return entry.getKey();
            }
        }
        return Range.zero();
    }

    private int getRangedFreeRequests() {
        return freeRequests.getRequestsCount();
    }

    private int getRangedPayRequests() {
        return payRequests.getRequestsCount();
    }

    private int getPrivilegedRequests() {
        return requestContract + requestNoExam + requestNoConcurs;
    }

    public void validate(ValidationErrors errors) {

        if (getRequestFreeTotal() != getPrivilegedRequests() + getRangedFreeRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Бюджет. '%s'\n " +
                "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                "количеством поданных заявок (%d).", getName(), getRequestFreeTotal(), getPrivilegedRequests() + getRangedFreeRequests())));
        }

        if (getRequestPayTotal() != getPrivilegedRequests() + getRangedPayRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Платное. '%s'\n " +
                "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                "количеством поданных заявок (%d).", getName(), getRequestPayTotal(), getPrivilegedRequests() + getRangedPayRequests())));
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Speciality{");
        sb.append("name='").append(name).append('\'');
        sb.append(", planFree=").append(planFree);
        sb.append(", planContract=").append(planContract);
        sb.append(", planPayee=").append(planPay);
        sb.append(", requestFreeTotal=").append(requestFreeTotal);
        sb.append(", requestPayTotal=").append(requestPayTotal);
        sb.append(", requestContract=").append(requestContract);
        sb.append(", requestNoExam=").append(requestNoExam);
        sb.append(", requestNoConcurs=").append(requestNoConcurs);
        sb.append(", requests=").append(freeRequests);
        sb.append('}');
        return sb.toString();
    }

}
