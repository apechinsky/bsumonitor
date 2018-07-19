package com.anton.bsu.monitor.model;

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


    private int requestPay;


    private int requestFree;

    private int requestContract;

    private int requestNoExam;

    private int requestNoConcurs;

    private RequestsDistribution freeRequestDistribution = new RequestsDistribution();

    private RequestsDistribution payRequestDistribution = new RequestsDistribution();

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

    public int getRequestFree() {
        return requestFree;
    }

    public Speciality setRequestFree(int requestFree) {
        this.requestFree = requestFree;
        return this;
    }

    public int getRequestPay() {
        return requestPay;
    }

    public void setRequestPay(int requestPay) {
        this.requestPay = requestPay;
    }

    public int getRequestsBeyondPassFree() {
        return Math.max(0, getRequestFree() - getPlanFree());
    }

    public int getRequestsBeyondPassPay() {
        return Math.max(0, getRequestPay() - getPlanPay());
    }

    public int getRequestsTotal() {
        return getRequestFree() + getRequestPay();
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

    public RequestsDistribution getFreeRequestDistribution() {
        return freeRequestDistribution;
    }

    public Speciality setFreeRequestDistribution(RequestsDistribution distribution) {
        this.freeRequestDistribution = distribution;
        return this;
    }

    public RequestsDistribution getPayRequestDistribution() {
        return payRequestDistribution;
    }

    public void setPayRequestDistribution(RequestsDistribution distribution) {
        this.payRequestDistribution = distribution;
    }

    public Range getFreePass() {
        return getFreeRequestDistribution().getPassRange(getPlanFree(), getPrivilegedRequests());
    }

    public Range getPayPass() {
        return getPayRequestDistribution().getPassRange(getPlanPay(), 0);
    }

    private int getRangedFreeRequests() {
        return freeRequestDistribution.getRequestsCount();
    }

    private int getRangedPayRequests() {
        return payRequestDistribution.getRequestsCount();
    }

    public int getPrivilegedRequests() {
        return requestContract + requestNoExam + requestNoConcurs;
    }

    public void validate(ValidationErrors errors) {

        if (getRequestFree() != getPrivilegedRequests() + getRangedFreeRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Бюджет. '%s'\n " +
                "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                "количеством поданных заявок (%d).", getName(), getRequestFree(), getPrivilegedRequests() + getRangedFreeRequests())));
        }

        if (getRequestPay() != getRangedPayRequests()) {
            errors.add(new DefaultValidationError(String.format(
                "Платное. '%s'\n " +
                "Общее количество поданных заявок (Всего/requestsTotal: %d) не совпадает с расчетным " +
                "количеством поданных заявок (%d).", getName(), getRequestPay(), getRangedPayRequests())));
        }

    }

    public int getRequestCountBelowFree(Range range) {
        return freeRequestDistribution.getRequestCountBelow(range);
    }
    public int getRequestCountBelowPay(Range range) {
        return payRequestDistribution.getRequestCountBelow(range);
    }

    /**
     * Возвращает количество заявок ниже указанного диапазона.
     *
     * <p>Расчет ведется по бюджетным и платным заявкам.</p>
     *
     * @param range диапазон
     */
    public int getRequestCountBelow(Range range) {
        return getRequestCountBelowFree(range) + getRequestCountBelowPay(range);
    }

    public int getRequestCountBelow311() {
        return getRequestCountBelow(new Range(320, 311));
    }

    public int getRequestCountBelow311Free() {
        return getRequestCountBelowFree(new Range(320, 311));
    }

    public int getRequestCountBelow311Pay() {
        return getRequestCountBelowPay(new Range(320, 311));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Speciality{");
        sb.append("name='").append(name).append('\'');
        sb.append(", planFree=").append(planFree);
        sb.append(", planContract=").append(planContract);
        sb.append(", planPayee=").append(planPay);
        sb.append(", requestFree=").append(requestFree);
        sb.append(", requestPay=").append(requestPay);
        sb.append(", requestContract=").append(requestContract);
        sb.append(", requestNoExam=").append(requestNoExam);
        sb.append(", requestNoConcurs=").append(requestNoConcurs);
        sb.append(", requests=").append(freeRequestDistribution);
        sb.append('}');
        return sb.toString();
    }

}
