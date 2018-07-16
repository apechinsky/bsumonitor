package com.anton.bsu.monitor.model;

/**
 * @author Q-APE
 */
public enum FacultyName {

    FPMI("Факультет прикладной математики и информатики"),

    MEXMAT("Механико-математический факультет"),;

    String name;

    FacultyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
