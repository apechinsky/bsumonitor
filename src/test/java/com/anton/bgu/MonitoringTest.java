package com.anton.bgu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.Requests;
import com.anton.bgu.parser.FreeRequestListPageParser;
import com.anton.bgu.parser.PayRequestListPageParser;
import com.anton.bgu.parser.RequestListPageParser;

/**
 * @author Q-APE
 */
public class MonitoringTest {

    private static final String FREE_URL = "https://abit.bsu.by/formk1?id=1";
    private static final String PAY_URL = "https://abit.bsu.by/formk1?id=7";

    @Test
    public void all() throws Exception {
        Document free = loadDocument("/free.html");
        Document pay = loadDocument("/pay.html");
        
//        Document free = loadDocument(new URL(FREE_URL));
//        Document pay = loadDocument(new URL(PAY_URL));

        Requests requests = new RequestListPageParser().parse(free, pay);

        printFaculties(requests.getFaculties());
    }

    @Test
    public void free() throws Exception {
        Document document = loadDocument("/free.html");

        List<Faculty> faculties = new FreeRequestListPageParser().parse(document);

        printFaculties(faculties);
    }

    @Test
    public void pay() throws Exception {
        Document document = loadDocument("/pay.html");

        List<Faculty> faculties = new PayRequestListPageParser().parse(document);

        printFaculties(faculties);
    }

    private void getFacultyTest() {
        //        Optional<Faculty> faculty = requests.getFacultyByName("Факультет прикладной математики и информатики");
//
//        if (faculty.isPresent()) {
//            printFaculty(faculty.get());
//
//            log("================================\n");
//        }
    }

    private void printFaculties(List<Faculty> faculties) {
        for (Faculty faculty : faculties) {

            printFaculty(faculty);

            log("================================\n");
        }
    }

    private void printFaculty(Faculty faculty) {
        log("%s - %d / %d", faculty.getName(), faculty.getPlanTotal(), faculty.getRequestTotal());
        
//        faculty.validate();

        faculty.getSpecialities().forEach(speciality -> {
            log("    План [%3d/%3d], Заявок [%3d/%3d], Баллы %s / %s - %s",
                speciality.getPlanFree(), speciality.getPlanPay(),
                speciality.getRequestFreeTotal(), speciality.getRequestPayTotal(),
                speciality.getFreePass(), speciality.getPayPass(),
                speciality.getName()
            );
        });
    }


    private void log(Object pattern, Object... arguments) {
        System.out.printf(pattern + "%n", arguments);
    }


    private Document loadDocument(String resource) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resource)) {
            return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), "https://abit.bsu.by/formk1?id=1");
        }
    }

    private Document loadDocument(URL url) throws IOException {
        return Jsoup.parse(url, 60000);
    }

}
