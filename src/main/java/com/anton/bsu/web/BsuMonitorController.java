package com.anton.bsu.web;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anton.bsu.monitor.BsuMonitor;
import com.anton.bsu.monitor.model.Faculty;
import com.anton.bsu.monitor.model.FacultyName;
import com.anton.bsu.monitor.model.RequestsModel;
import com.anton.bsu.monitor.view.ModifySourceHtmlView;
import com.anton.bsu.monitor.view.TextModelView;

/**
 * Контроллер запросов монитора.
 */
@Controller
public class BsuMonitorController {

    /**
     * Возвращает оригинальную страницу ({@link BsuMonitor#FREE_URL}) без изменений
     */
    @GetMapping(value = "/bsu")
    @ResponseBody
    public String bgu() {
        Document document = BsuMonitor.loadDocumentFromUrl(BsuMonitor.FREE_URL);
        return document.outerHtml();
    }

    /**
     * Модифицирует страницу ({@link BsuMonitor#FREE_URL}), добавляя расчетные показатели.
     *
     * <ul>
     *     <li>Наименование факультета выделяется красным цветом, если количество заявок превышает план набора</li>
     *     <li>Наименование факультета дополняется общим расчетным проходным баллом факультета.</li>
     *     <li>Наименование специальности выделяется красным цветом, если количество заявок превысило общий план набора.</li>
     *     <li>План приема бюджет выделяется красным, если количество заявок на бюджет превысило план</li>
     *     <li>План приема платное выделяется красным, если количество заявок на платное превысило план</li>
     *     <li>План приема платное дополняется (серым цветом) количеством заявок, поданных на платное </li>
     *     <li>Распределение диапазонам баллов дополняется (серым цветом) количеством заявок на платное</li>
     * </ul>
     */
    @GetMapping(value = "/monitor")
    @ResponseBody
    public String monitor() {
        RequestsModel model = new BsuMonitor().loadModelFromSite();

        return new ModifySourceHtmlView().render(model);
    }

    /**
     * Вывод данных по ФПМИ/Мехмат
     */
    @GetMapping(value = "/fpmi")
    public String fpm(Model model) {
        RequestsModel requestsModel = new BsuMonitor().loadModelFromSite();

        model.addAttribute("model", requestsModel);

        model.addAttribute("fpmi", requestsModel.getFaculty(FacultyName.FPMI));

        model.addAttribute("mexmat", requestsModel.getFaculty(FacultyName.MEXMAT));

        return "fpmi";
    }

    /**
     * Вывод расчетных данных по факультетам и специальностям
     */
    @GetMapping(value = "/stat", produces = "text/plain")
    @ResponseBody
    public String stat() {
        RequestsModel model = new BsuMonitor().loadModelFromSite();

        return new TextModelView().render(model);
    }

    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "test");
        return "test";
    }

}
