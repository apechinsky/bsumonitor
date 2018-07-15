package com.anton.bgu.web;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anton.bgu.monitor.BguMonitor;
import com.anton.bgu.monitor.model.RequestsModel;
import com.anton.bgu.monitor.view.ModifySourceHtmlView;
import com.anton.bgu.monitor.view.TextModelView;

@Controller
public class BguMonitorController {

    @GetMapping(value = "/bgu")
    @ResponseBody
    public String bgu() {
        Document document = loadDocument("https://abit.bsu.by/formk1?id=1");
        return document.outerHtml();
    }

    @GetMapping(value = "/monitor")
    @ResponseBody
    public String monitor() {
        RequestsModel model = new BguMonitor().loadModelFromSite();

        return new ModifySourceHtmlView().render(model);
    }

    @GetMapping(value = "/stat", produces = "text/plain")
    @ResponseBody
    public String stat() {
        RequestsModel model = new BguMonitor().loadModelFromSite();
        return new TextModelView().render(model);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    private Document loadDocument(String url) {
        try {
            return Jsoup.parse(new URL(url), 60000);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
