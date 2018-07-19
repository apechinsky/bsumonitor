package com.anton.bsu.monitor.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.validator.internal.constraintvalidators.bv.time.past.PastValidatorForLocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Q-APE
 */
public class RequestListPageParserTest {

    @Test
    public void getUpdateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime parse = LocalDateTime.parse("17.07.2018 18:45", formatter);
    }
}