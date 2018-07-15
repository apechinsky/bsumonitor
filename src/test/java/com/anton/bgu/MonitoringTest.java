package com.anton.bgu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.anton.bgu.model.RequestsModel;
import com.anton.bgu.parser.RequestListPageParser;
import com.anton.bgu.view.ModifySourceHtmlView;
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
        Element fl = free.selectFirst("td.fl");
        fl.attributes().put("class", "aaaa") ;
        fl.attributes().put("class", "bbbb") ;

        writeToFile(free, "modified.html");
    }

    private Path writeToFile(Document free, String file) throws IOException {
        return writeToFile(free.outerHtml(), file);
    }

    private Path writeToFile(String document, String file) throws IOException {
        return Files.write(Paths.get(file), document.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void all() throws Exception {
//        Document free = loadDocument("/free.html");
//        Document pay = loadDocument("/pay.html");
        
        Document free = loadDocument(new URL(FREE_URL));
        Document pay = loadDocument(new URL(PAY_URL));

        RequestsModel requestsModel = new RequestListPageParser().parse(free, pay);

        requestsModel.validate();

        String render = new TextModelView().render(requestsModel);
        System.out.println(render);

        String render2 = new ModifySourceHtmlView().render(requestsModel);
        writeToFile(render2, "modified.html");

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
