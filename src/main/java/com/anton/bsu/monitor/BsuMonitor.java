package com.anton.bsu.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.anton.bsu.monitor.model.RequestsModel;
import com.anton.bsu.monitor.parser.RequestListPageParser;

/**
 * @author Q-APE
 */
public class
BsuMonitor {

    private static final String BASE_URL = "https://abit.bsu.by/formk1";

    public static final String FREE_URL = "https://abit.bsu.by/formk1?id=1";

    public static final String PAY_URL = "https://abit.bsu.by/formk1?id=7";


    public RequestsModel loadModelFromSite() {
        Document free = loadDocumentFromUrl(FREE_URL);
        Document pay = loadDocumentFromUrl(PAY_URL);

        RequestsModel requestsModel = new RequestListPageParser().parse(free, pay);

        requestsModel.validate();

        return requestsModel;
    }

    public static Document loadDocumentFromUrl(String url) {
        try {
            return Jsoup.parse(new URL(url), 60000);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Document loadDocumentFromResource(String resource) {
        try {
            try (InputStream inputStream = BsuMonitor.class.getResourceAsStream(resource)) {
                return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), BASE_URL);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
