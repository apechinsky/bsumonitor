package com.anton.bgu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.anton.bgu.monitor.BguMonitor;
import com.anton.bgu.monitor.model.RequestsModel;
import com.anton.bgu.monitor.view.ModifySourceHtmlView;
import com.anton.bgu.monitor.view.TextModelView;


/**
 * @author Q-APE
 */
public class MonitoringTest {


    @Test
    public void modify() throws Exception {
        Document free = BguMonitor.loadDocumentFromResource("/free.html");
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

        RequestsModel requestsModel = new BguMonitor().loadModelFromSite();

        String render = new TextModelView().render(requestsModel);
        System.out.println(render);

        String render2 = new ModifySourceHtmlView().render(requestsModel);
        writeToFile(render2, "modified.html");
    }

}
