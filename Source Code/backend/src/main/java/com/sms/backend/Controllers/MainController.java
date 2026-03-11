package com.sms.backend.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @RequestMapping("test")
    public String checkAPI()
    {
        return "Its Working...";
    }
}
