package com.anton.bsu.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class BsuMonitorRestController {

    @RequestMapping("/rest")
    public String index() {
        return "not implemented";
    }

}
