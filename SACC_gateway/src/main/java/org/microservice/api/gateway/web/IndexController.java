package org.microservice.api.gateway.web;

import org.apache.dubbo.config.annotation.Reference;
import org.microservice.pub.commons.service.ReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class IndexController {

    @Reference(version = "1.0.0")
    private ReaderService readerService;

    @GetMapping("/")
    public String index() throws IOException {
        return "index";
    }
}
