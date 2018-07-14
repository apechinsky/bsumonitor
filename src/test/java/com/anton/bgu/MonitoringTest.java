package com.anton.bgu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.anton.bgu.model.Faculty;
import com.anton.bgu.model.RequestsModel;
import com.anton.bgu.parser.FreeRequestListPageParser;
import com.anton.bgu.parser.PayRequestListPageParser;
import com.anton.bgu.parser.RequestListPageParser;
import com.anton.bgu.view.TextModelView;

/**
 * @author Q-APE
 */
public class MonitoringTest {

    private static final String FREE_URL = "https://abit.bsu.by/formk1?id=1";
    private static final String PAY_URL = "https://abit.bsu.by/formk1?id=7";

    @Test
    public void modify() throws Exception {
        Document free = loadDocument("/free.html");
        Elements fl = free.select("td.fl");
        fl.attr("class", "aaaa bbbb cccc");

        Files.write(Paths.get("modified.html"), free.outerHtml().getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void all() throws Exception {
//        Document free = loadDocument("/free.html");
//        Document pay = loadDocument("/pay.html");
        
        Document free = loadDocument(new URL(FREE_URL));
        Document pay = loadDocument(new URL(PAY_URL));

        RequestsModel requestsModel = new RequestListPageParser().parse(free, pay);

        String render = new TextModelView().render(requestsModel);

        System.out.println(render);
    }

    @Test
    public void free() throws Exception {
        Document document = loadDocument("/free.html");

        List<Faculty> faculties = new FreeRequestListPageParser().parse(document);

        new TextModelView().render(new RequestsModel(faculties));
    }

    @Test
    public void pay() throws Exception {
        Document document = loadDocument("/pay.html");

        List<Faculty> faculties = new PayRequestListPageParser().parse(document);

        new TextModelView().render(new RequestsModel(faculties));
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
